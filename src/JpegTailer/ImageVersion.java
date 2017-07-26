package JpegTailer;

import static AppendTailer.java.readData;
import java.applet.Applet;
import java.awt.Button;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;
public class ImageVersion extends Applet implements ActionListener{
   
    BufferedImage img,img2=null;
    byte[] data;
    int EOI,sTail,lock=0;
    byte[] index;
    int[] RGB;
    byte[] description;
    List<Integer> indexStart = new ArrayList();
    List<Integer> tailerStart = new ArrayList();
    List<Integer> RGBStart = new ArrayList();
    List<Integer> descriptionStart = new ArrayList();
    int Yquant[]={16,11,10,16,24,40,51,61,12,12,14,19,26,58,60,55,14,13,16,24,40,57,69,56,14,17,22,29,51,87,80,62,18,22,37,56,68,109,103,77,24,35,55,64,81,104,113,92,49,64,78,87,103,121,120,101,72,92,95,98,112,100,103,99};
    int Cquant[] = {17,18,24,47,99,99,99,99,18,21,26,66,99,99,99,99,24,26,56,99,99,99,99,99,47,67,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99};   
    int[] jpegNaturalOrder = { 0, 1, 8, 16, 9, 2, 3, 10, 17, 24, 32, 25, 18, 11, 4, 5,
      12, 19, 26, 33, 40, 48, 41, 34, 27, 20, 13, 6, 7, 14, 21, 28, 35, 42, 49, 56, 57, 50, 43, 36,
      29, 22, 15, 23, 30, 37, 44, 51, 58, 59, 52, 45, 38, 31, 39, 46, 53, 60, 61, 54, 47, 55, 62,
      63 };
    public static byte[] readData(File file) throws Exception 
    {
        ByteArrayOutputStream ous = null;
	InputStream ios = null;
	try 
        {
            byte[] buffer = new byte[1514400];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ((read = ios.read(buffer)) != -1) 
            {
		ous.write(buffer, 0, read);
            }
        }catch(Exception e){System.out.println(e);}
	return ous.toByteArray();
    } 
    public void drawImage(Graphics g)
    {
        InputStream in = new ByteArrayInputStream(data);
	try
        {
            img = ImageIO.read(in);
	}
        catch(Exception e){System.out.println(e);}
	g.drawImage(img,0,0,null);
    }
   
    public int getTailerButtons(byte[] data)
    {
        int version=1;
        while(EOI!=sTail)
        {
            
            version++;
            int tLength = data[EOI+5] << 24 | (data[EOI+6] & 0xFF) << 16 | (data[EOI+7] & 0xFF) << 8 | (data[EOI+8] & 0xFF);
            indexStart.add(EOI+9);
            System.out.println("Index start: "+(EOI+9));
            for(int i=(EOI+9);i<(EOI+tLength);i++)
            {
                if(data[i]==(char)'C'&&data[i+1]==(char)'D')
                {
                    RGBStart.add(i);
                    System.out.println("RGB start: "+(i));
                }
                else if(data[i]==(char)'D'&&data[i+1]==(char)'S')
                {
                    descriptionStart.add(i);
                    System.out.println("Description start: "+(i));
                }
            }
            EOI = EOI + tLength;
            tailerStart.add(EOI);
            System.out.println("tailer start: "+(EOI));
        }
        
        return version;
    }
    public void addButtons(int nTailer)
    {
        Button button[] = new Button[nTailer];
            if(nTailer!=0)
            {
                for(int i=0;i<nTailer;i++)
                {
                    button[i] = new Button();
                    button[i].setLabel("version "+i);
                    button[i].setLocation(600, 50+(i*10));
                    button[i].addActionListener(this);
                    this.add(button[i]);
		}
            }
    }
    
     public static float[][] iforwardDCTExtreme(int input[][]) 
     {
        int N=8;
        float output[][] = new float[N][N];
        int v=0, u, x, y;
        for (x = 0; x < 8; x++) {
            for (y = 0; y < 8; y++) {
               for (u = 0; u < 8; u++) {
                 for (v = 0; v < 8; v++) {
              //input[x][y] -=128; 
                    output[x][y] += input[u][v]
                    * Math.cos(((double) (2 * x + 1) * (double) u * Math.PI) / 16)
                    * Math.cos(((double) (2 * y + 1) * (double) v * Math.PI) / 16)*((u == 0) ? (1.0 / Math.sqrt(2)) : (double) 1.0)
                    * ((v == 0) ? (1.0 / Math.sqrt(2)) : (double) 1.0);
                    }
                }
                output[x][y] *= (0.25) ;
             }
            }
        return output;
     }
    
    public int[] decompressMatrix(byte[] Matrix,int grp)
    {
        //System.out.println("DecompressMatrix");
        int i,j;
        byte[] serializeData = new byte[64];
        int[] quant = new int[64];
        if(grp==1)
        {
           quant = Yquant;
        }
        else if(grp==2||grp==3)
        {
            quant = Cquant;
        }
        for(i=0;i<Matrix.length;i++)
        {
            serializeData[jpegNaturalOrder[i]] = Matrix[i];
            //System.out.print(" "+serializeData[i]);
        }
        //System.out.println();
        int compressedData[][] = new int[8][8];
        int quantize[][] = new int[8][8];
        int k=0;
        for(i=0;i<8;i++)
        {
            for(j=0;j<8;j++)
            {
                compressedData[i][j] = serializeData[k];
                quantize[i][j]=(Math.round(compressedData[i][j]*quant[k]));
                k++;
                //System.out.print(" "+compressedData[i][j]);
            }
            //System.out.println();
        }
        float DCTArray[][] = iforwardDCTExtreme(quantize);
        int finalData[][] = new int[8][8];
        int finalDatalist[] = new int[64];
        k=0;
        for(i=0;i<8;i++)
        {
            for(j=0;j<8;j++)
            {
               finalData[i][j] = (Math.round(DCTArray[i][j])+128);
               //System.out.print(" "+finalData[i][j]);
               finalDatalist[k] = finalData[i][j];k++;
            }
             //System.out.println();
            
        }
       
        return finalDatalist;
    }
    public int[] decompressMatrices(List<Byte> compressedMatrices)
    {
        //System.out.println("DecompressMatrices");
        byte[] Matrix = new byte[64];
        int ord=0;
        int grp = 1;
        int i;
        int finalData[] = new int[192];
        int k=0;
        for(i=0;i<compressedMatrices.size();i+=2)
        {
            //System.out.print(" "+compressedMatrices.get(i)+" "+compressedMatrices.get(i+1));
            if(compressedMatrices.get(i)==0&&compressedMatrices.get(i+1)==0)
            {
               
               while(ord<64)
               {
                   Matrix[ord] = (byte)0;ord++;
               }
               ord=0;
               int M[] = decompressMatrix(Matrix,grp);
               for(int p=0;p<M.length;p++){ finalData[k] = M[p];k++;}
               grp++;
               if(grp==4)
               {
                   grp=1;
               }
            }
            else
            {
                int nZeros =compressedMatrices.get(i);
                
                while(nZeros>0)
                   {
                       Matrix[ord] = (byte)0;ord++;
                       nZeros--;
                   }    
                Matrix[ord] = compressedMatrices.get(i+1);ord++;
            }
        }
        int[] RGB = new int[192];
        k=0;
        for(i=0;i<64;i++)
        {
            float Y = (float)finalData[i];
            float Cb = (float)finalData[i+64];
            float Cr = (float)finalData[i+128];
            int r = (int)(Y + 1.40200 * (Cr - 0x80));
            int g = (int)(int)(Y - 0.34414 * (Cb - 0x80) - 0.71414 * (Cr - 0x80));
            int b = (int)(int)(Y + 1.77200 * (Cb - 0x80));
            RGB[k]  = Math.max(0, Math.min(255, r));k++;
            RGB[k]  = Math.max(0, Math.min(255, g));k++;
            RGB[k]  = Math.max(0, Math.min(255, b));k++;
        }
        
        return RGB;
    }
    public void decompressData(byte[] compressedData)
    {
        int i;
        int mat=0;
        int k=0;
        List<Byte> Matrices = new ArrayList();
        for(i=0;i<compressedData.length;i+=2)
        {
            if(compressedData[i]==0&&compressedData[i+1]==0)
            {
               Matrices.add(compressedData[i]);
               Matrices.add(compressedData[i+1]);
               mat++;
               if(mat==3)
               {
                   
                   int M[] = decompressMatrices(Matrices);
                   for(int p=0;p<M.length;p++){
                    RGB[k] = M[p];k++;
                    }
                   
                   Matrices.clear();
                   mat=0;
               }
            }
            else
            {
               Matrices.add(compressedData[i]);
               Matrices.add(compressedData[i+1]);
            }
        }
        System.out.println("Total Matrices: "+mat+" "+Matrices.size()+" k: "+k);
        
    }
    
    public void actionPerformed(ActionEvent ae)
	{
            String str  = ae.getActionCommand();
            str = str.substring(8);
            int version = Integer.parseInt(str);
            if(version==0)
            {
                lock=0;
                repaint();
            }
            else
            {
                int startDes = descriptionStart.get(version-1);
                for(int i=(startDes+2);i<tailerStart.get(version-1);i++)
                {
                    System.out.print((char)data[i]);
                }
                System.out.println();
                int inds = indexStart.get(version-1);
                int rgbs = RGBStart.get(version-1);
                System.out.println("Index Length: "+(rgbs-inds));
                index = new byte[(rgbs-inds)];
                int RGBlength=0;
                int indexLength = (rgbs-inds);
                if(indexLength%64!=0)
                {
                    RGBlength = (int)(Math.ceil((double)(indexLength/4)/64)*64)*3;
                }
                RGB = new int[RGBlength];
                int k=0;
                for(int i=inds;i<rgbs;i++)
                {
                    index[k] = data[i];k++;
                }
                System.out.println("Compress Data Length: "+(startDes-rgbs-2));
                byte[] compressedData = new byte[(startDes-rgbs-2)];
                k=0;
                for(int i=(rgbs+2);i<startDes;i++)
                {
                    compressedData[k] = data[i];k++;
                }
                decompressData(compressedData);
                lock=1;
                repaint();
            }
        }
    public void init() {
            try{
            Scanner sc  = new Scanner(System.in);
            System.out.print("Enter File Name:(with Extention) ");
            String inputFile = sc.next();
            data = readData(new File("D:\\BTECH\\Internship\\Image Version Control\\src\\AppendTailer\\"+inputFile));
            int i;
            for(i=0;i<data.length;i++)
            {
                if(data[i]==(byte)'E'&&data[i+1]==(byte)'O'&&data[i+2]==(byte)'I')
                {
                    System.out.println("End of File Found(last index):"+(i+2));
                    break;
                }
            }
            if(i!=data.length)
            {
                EOI = i+3;
                System.out.println("Tailer Exist: (starting at)"+EOI);
                System.out.println("Start adding Tailer from: "+data.length);
            }
            else
            {
                EOI = i+3;
                System.out.println("Tailer Not Exist: (add from)"+EOI);
            }
           sTail=data.length;
           int nButtons = getTailerButtons(data);
           addButtons(nButtons);
           System.out.println("Total Available Version: "+nButtons);
            }
            catch(Exception e){System.out.println(e);}
    }
    public void paint(Graphics g)
    {
        if(lock==0)
        {
            drawImage(g);
            lock=1;
        }
    }
}
