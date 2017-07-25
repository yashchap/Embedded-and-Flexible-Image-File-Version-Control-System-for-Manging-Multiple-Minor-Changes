/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image.version.control;

/**
 *
 * @author Dell
 */
import java.io.ByteArrayOutputStream;  
 import java.io.File;  
 import java.io.FileInputStream;  
 import java.io.FileOutputStream;  
 import java.io.IOException;  
 import java.io.InputStream;  
 import java.util.List;  
 import java.util.Map;  
 import java.util.zip.DataFormatException;  
 import java.util.zip.Deflater;  
 import java.util.zip.Inflater;  
 class compressionUtils {  
  //private static final Logger LOG = Logger.getLogger(CompressionUtils.class);  
  public static byte[] compress(byte[] data) throws IOException {  
   Deflater deflater = new Deflater();  
   deflater.setInput(data);  
   ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);   
   deflater.finish();  
   byte[] buffer = new byte[1024];   
   while (!deflater.finished()) {  
    int count = deflater.deflate(buffer); // returns the generated code... index  
    outputStream.write(buffer, 0, count);   
   }  
   outputStream.close();  
   byte[] output = outputStream.toByteArray();  
   System.out.println("Original: " + data.length );  
   System.out.println("Compressed: " + output.length );  
   return output;  
  }  
  public static byte[] decompress(byte[] data) throws IOException, DataFormatException {  
   Inflater inflater = new Inflater();   
   inflater.setInput(data);  
   ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);  
   byte[] buffer = new byte[1024];  
   while (!inflater.finished()) {  
    int count = inflater.inflate(buffer);  
    outputStream.write(buffer, 0, count);  
   }  
   outputStream.close();  
   byte[] output = outputStream.toByteArray();  
   System.out.println("Original: " + data.length);  
   System.out.println("Compressed: " + output.length);  
   return output;  
  }  
 
 public static void main(String args[])
 {
     int data[]={52,55,61,66,70,61,64,73,63,59,55,90,109,85,69,0,0,90,68,113,144,104,66,73,0,58,71,122,154,106,70,69,67,61,68,104,126,88,68,70,79,65,60,70,77,68,58,75,85,71,64,59,55,61,0,0,0,0,0,0,0,0,0,0};
     byte data2[]=new byte[64];
     for(int i=0;i<64;i++)
     {
         data2[i]=(byte)data[i];
     }
     byte compress[];
     try{compress = compress(data2);}catch(Exception e){};
     
     int integer = 500;
    byte[] bytes = new byte[4];
    for (int i = 0; i < 4; i++) {
    bytes[i] = (byte)(integer >>> (i * 8));
    System.out.println(bytes[i]);
}
    
 }
 
 }
