package AppendTailer;
import static com.oracle.jrockit.jfr.ContentType.Bytes;
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
import java.util.logging.Level;
import java.util.logging.Logger;


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
 public static void writeData(List<Byte> finalData,String Extension)
 {
       byte[] read2 = new  byte[finalData.size()];
       int i = 0;
       for (Byte e : finalData)  
            read2[i++] = e.byteValue();
       FileOutputStream fos;
     try {
         fos = new FileOutputStream("D:\\BTECH\\Internship\\Image Version Control\\src\\AppendTailer\\output"+Extension);
         fos.write(read2);
         fos.close();
     } catch (Exception ex) {
         Logger.getLogger(java.class.getName()).log(Level.SEVERE, null, ex);
     }
       
 }
 public static void main(String args[])
 {
     byte[] data;  
     List<Byte> finalData = new ArrayList<Byte>();
    
     try {
            data = readData(new File("D:\\BTECH\\Internship\\Image Version Control\\src\\image\\version\\control\\car.jpg"));
            int i;
            for(i=0;i<data.length;i++)
            {
                finalData.add(data[i]);
                if(data[i]==(byte)255&&data[i+1]==(byte)217)
                {
                    System.out.println("End of File Found(last index):"+(i+1));
                    finalData.add(data[i+1]);
                    break;
                }
            }
            int EOI;
            if(i!=data.length)
            {
                EOI = data.length;
                System.out.println("JPG Image: "+EOI);
                 writeData(finalData,".jpg");

            }
            else
            {
                finalData.add((byte)255);
                finalData.add((byte)217);
                EOI = data.length+2;
                System.out.println("Other Format: "+EOI);
                writeData(finalData,".bmp");
            }
           System.out.println("data length: "+data.length+" arraylist: "+finalData.size());
     } catch (Exception ex) {
            Logger.getLogger(java.class.getName()).log(Level.SEVERE, null, ex);
        }
            
 }
}
