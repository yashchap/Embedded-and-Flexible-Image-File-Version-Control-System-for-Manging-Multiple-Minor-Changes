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
    byte[] RGB;
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
    public int decompressMatrix(byte[] Matrix,int grp)
    {
        System.out.println("DecompressMatrix");
        int i;
        if(grp==1)
        {
           int[] quant = Yquant;
        }
        else if(grp==2||grp==3)
        {
            int[] quant = Cquant;
        }
        for(i=0;i<Matrix.length;i++)
        {
           System.out.print(" "+Matrix[i]);
        }
        System.out.println();
        return 0;
    }
    public int decompressMatrices(List<Byte> compressedMatrices)
    {
        System.out.println("DecompressMatrices");
        byte[] Matrix = new byte[64];
        int ord=0;
        int grp = 1;
        int i;
        for(i=0;i<compressedMatrices.size();i+=2)
        {
            //System.out.print(" "+compressedMatrices.get(i)+" "+compressedMatrices.get(i+1));
            if(compressedMatrices.get(i)==0&&compressedMatrices.get(i+1)==0)
            {
               while(ord!=64)
               {
                   Matrix[ord] = (byte)0;ord++;
               }
               ord=0;
               decompressMatrix(Matrix,grp);
               System.out.println();
               grp++;
               if(grp==4)
               {
                   grp=1;
               }
            }
            else
            {
               Matrix[ord] = compressedMatrices.get(i);ord++;
               Matrix[ord] = compressedMatrices.get(i+1);ord++;
            }
        }
        return 0;
    }
    public int decompressData(byte[] compressedData)
    {
        int i;
        int mat=0;
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
                   decompressMatrices(Matrices);
                   mat=0;
               }
            }
            else
            {
               Matrices.add(compressedData[i]);
               Matrices.add(compressedData[i+1]);
            }
        }
        System.out.println("Total Matrices: "+mat);
        return 0;
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
