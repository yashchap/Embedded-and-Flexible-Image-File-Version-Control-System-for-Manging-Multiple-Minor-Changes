package AppendTailer;
import static com.oracle.jrockit.jfr.ContentType.Bytes;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/*
EOI: starting index of Tailer
sTail: start adding tailer from this index
Tlength: length of tailer
data: data read from file
finalData: array list after appending tailer
bufferedimage is used to take default width and height
Note: if change in width and height please specify change width and height;
index = provide x,y position of image 
RGB = provide new RGB specific to x,y value index
NOTE: by default constant array of index is taken please uncomment the code if want to specify by yourself
*/

public class java {
    
 public static byte[] readData(File file) throws Exception 
    {
        ByteArrayOutputStream ous = null;
	InputStream ios = null;
	try 
        {
            byte[] buffer = new byte[15144];
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
 public static void writeData(List<Byte> finalData)
 {
       System.out.print("Enter File Name(output):(with Extention): ");
       Scanner sc  = new Scanner(System.in);
       String output = sc.next();
        byte[] read2 = new  byte[finalData.size()];
       int i = 0;
       for (Byte e : finalData)  
            read2[i++] = e.byteValue();
       FileOutputStream fos;
     try {
         fos = new FileOutputStream("D:\\BTECH\\Internship\\Image Version Control\\src\\AppendTailer\\"+output);
         fos.write(read2);
         fos.close();
     } catch (Exception ex) {
         Logger.getLogger(java.class.getName()).log(Level.SEVERE, null, ex);
     }
       
 }
 
 public static void compressRGB(byte[] index,byte[] RGB)
 {
     int i,R,G,B,k=0;
     float YCC[] = new float[RGB.length];
     for(i=0;i<RGB.length;i++)
     {
         R = RGB[i] & 0xFF;i++;
         G = RGB[i] & 0xFF;i++;
         B = RGB[i] & 0xFF;
         YCC[k] = (float)(0.299*R+0.587*G+0.114*B);k++;
         YCC[k]=(float)(-0.169*R-0.331*G+0.5*B + 128.0);k++;
         YCC[k]=(float)(0.5*R-0.419*G-0.081*B + 128.0);k++;
     }
     System.out.println("YCC length: "+YCC.length);
     
 }
 public static void addTailer(int sTail, int EOI, byte[] data,BufferedImage img)
 {
     byte[] tailer;
     int version=1;
     while(EOI!=sTail)
     {
         version++;
         int tLength = data[EOI+5] << 24 | (data[EOI+6] & 0xFF) << 16 | (data[EOI+7] & 0xFF) << 8 | (data[EOI+8] & 0xFF);
         EOI = EOI + tLength;
     }
     Scanner sc  = new Scanner(System.in);
     int changedWidth = img.getWidth();//change if required
     int changedHeight = img.getHeight();//change if required
     /* for manual entry
     List<Byte> index = new ArrayList();
     List<Byte> RGB = new ArrayList();
     System.out.print("Want to Change any pixel(y/n):");
     char ch = sc.next().charAt(0);
     int val;
     while(ch!='n')
     {
         System.out.print("Enter X: ");
         int xpos = sc.nextInt();
         System.out.print("Enter Y: ");
         int ypos = sc.nextInt();
         val = xpos*ypos;
         index.add((byte)(val>>> 24));index.add((byte)(val>>> 16));index.add((byte)(val>>> 8));index.add((byte)(val));
         System.out.print("Enter R: ");
         val = sc.nextInt();
         RGB.add((byte)val);
         System.out.print("Enter G: ");
         val = sc.nextInt();
         RGB.add((byte)val);
         System.out.print("Enter B: ");
         val = sc.nextInt();
         RGB.add((byte)val);
         System.out.print("Want to Change any pixel(y/n):");
         ch = sc.next().charAt(0);
     }
     */
     byte index[] = new byte[1000*4];
     int k=0;
     int i;
     for( i=2000;i<3000;i++)
     {
         index[k] = (byte)(i>>> 24);k++;
         index[k] = (byte)(i>>> 16);k++;
         index[k] = (byte)(i>>> 8);k++;
         index[k] = (byte)(i);k++;
     }
     int indexLength = index.length;
     int RGBlength = (index.length/4)*3;
     if(indexLength%64!=0)
     {
         RGBlength = (int)(Math.ceil((double)(indexLength/4)/64)*64)*3;
     }
     byte RGB[] = new byte[RGBlength];
     
     for(i=0;i<(index.length/4)*3;i++)
     {
         RGB[i]=(byte)252;i++;
         RGB[i]=(byte)252;i++;
         RGB[i]=(byte)252;
     }
     int j;
     for(j=i;j<RGBlength;j++)
     {
         RGB[j]=(byte)128;j++;
         RGB[j]=(byte)128;j++;
         RGB[j]=(byte)128;
     }
     System.out.println("RGBlength: "+RGBlength);
     byte[] description;
     System.out.println("Enter Description about Image: ");
     String str = sc.nextLine();
     description = str.getBytes();
     System.out.println("Description length: "+description.length);
     System.out.println("Version: "+version+" Image width: "+changedWidth+" Image height:"+changedHeight);
     System.out.println("Compressing RGB....");
     compressRGB(index,RGB);
     //return tailer;
 }
 public static void main(String args[])
 {
     byte[] data;  
     List<Byte> finalData = new ArrayList<Byte>();
    
     try {
            Scanner sc  = new Scanner(System.in);
            System.out.print("Enter File Name:(with Extention) ");
            String inputFile = sc.next();
            data = readData(new File("D:\\BTECH\\Internship\\Image Version Control\\src\\image\\version\\control\\"+inputFile));
            BufferedImage img = null;
            img = ImageIO.read(new File("D:\\BTECH\\Internship\\Image Version Control\\src\\image\\version\\control\\"+inputFile));
            int i;
            for(i=0;i<data.length;i++)
            {
                finalData.add(data[i]);
                if(data[i]==(byte)'E'&&data[i+1]==(byte)'O'&&data[i+2]==(byte)'I')
                {
                    System.out.println("End of File Found(last index):"+(i+2));
                    finalData.add(data[i+1]);
                    break;
                }
            }
            int EOI;
            if(i!=data.length)
            {
                EOI = i+3;
                
                System.out.println("Tailer Exist: (starting at)"+EOI);
                System.err.println("Start adding Tailer from: "+data.length);
                //writeData(finalData);

            }
            else
            {
                finalData.add((byte)'E');
                finalData.add((byte)'O');
                finalData.add((byte)'I');
                EOI = i+3;
                System.out.println("Tailer Not Exist: (add from)"+EOI);
                //writeData(finalData);
            }
           int sTail=finalData.size();
           System.out.println("data length: "+data.length+" arraylist: "+finalData.size());
           addTailer(sTail,EOI,data,img);
           
     } catch (Exception ex) {
            Logger.getLogger(java.class.getName()).log(Level.SEVERE, null, ex);
        }
            
 }
}
