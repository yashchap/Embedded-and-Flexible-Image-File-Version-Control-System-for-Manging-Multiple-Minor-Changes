package image.version.control;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;
 /*<applet code="bmp" height=768 width=1366></applet>*/ 
public class Jpeg extends Applet implements ActionListener{

    public BufferedImage img = null;
    byte data[];
    byte data2[];
    int headerIndex[]  = new int[10];
    int index[];
    int RGB[];
    /*Reading byte array from given image file*/
    public byte[] readData(File file) throws Exception 
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
    /*Function for converting bytes to hexadecimal*/
     char[] hexArray="0123456789ABCDEF".toCharArray();
    public String toHex(byte[] bytes)
     {
          char[] hexChars = new char[bytes.length * 2];
        for(int j=0;j<bytes.length;j++)
        {
            int v = bytes[j] & 0XFF;
            hexChars[j*2]=hexArray[v>>>4];
            hexChars[j*2+1] = hexArray[v&0X0F];
        
        }
        return new String(hexChars);
     }
    /*Drawing image from byte array to applet screen*/
    void drawImage(byte[] data, Graphics g)
    {
	InputStream in = new ByteArrayInputStream(data);
	try
        {
            img = ImageIO.read(in);
	}
        catch(Exception e){System.out.println(e);}
	g.drawImage(img,0,0,null);
    }
    /*showing details from jpeg*/
      public void showDetails(byte[] data)
      {
          System.out.println("Length of data array: "+data.length);
          int i;
          byte temp[]={0,0};
          String marker;
          int k=0;
         /* for( i=0;i<data.length-1;i=i+1)
          {
            temp[0] = data[i];
            temp[1]=data[i+1];
            marker = toHex(temp);
            if(marker.equals("FFD8")||marker.equals("FFC0")||marker.equals("FFC2")||marker.equals("FFC4")||marker.equals("FFDB")||marker.equals("FFDD")||marker.equals("FFDA")||marker.equals("FFD0")||marker.equals("FFD1")||marker.equals("FFD2")||marker.equals("FFD3")||marker.equals("FFE0")||marker.equals("FFFE")||marker.equals("FFD9"))
            { headerIndex[k] = i;k++;//System.out.println(marker+": at "+i+" in "+k);
            }  
          }*/
         byte table[] = {16,11,12,14,12,10,16,14,13,14,18,17,16,19,24,40,26,24,22,22,24,49,35,37,29,40,58,51,61,60,57,51,56,55,64,72,92,78,64,68,87,69,55,56,80,109,81,87,95,98,103,104,103,62,77,113,121,112,100,120,92,101,103,99};
         System.out.println(table.length);
         byte[][] qtable = new byte[8][8];
         byte[][] qtable2 = new byte[8][8];
         outer: for(i=0;i<590;i++)
          {
              byte temp1[] = {data[i]};
              //System.out.println(toHex(temp1)+" at "+i);
              if(toHex(temp1).equals("FF"))
              {
                   temp1[0] = data[i+1];
                   System.out.println("this is "+toHex(temp1)+" at" +(i+1));
                   if(toHex(temp1).equals("DB"))
                   {    
                       int d=0;
                       for(int l=0;l<8;l++)
                       {
                           for(int t=0;t<8;t++)
                           {
                               qtable[l][t] = data[i+5];
                               //data[i+5]=(byte)(table[d]/ + 2);
                               qtable2[l][t]=(byte)(table[d] + 2);
                               
                               d++;
                               //System.out.println(i+5);
                               i++;
                           }
                          
                       }
                      
                       
                       //break;
                   }
              }
             
          }
         byte temp1[]= {data[366]};
         System.out.println("at 366 "+toHex(temp1));
         outer: for(i=0;i<data.length;i++)
          {
              temp1[0] = data[i];
              //System.out.println(toHex(temp1)+" at "+i);
              if(toHex(temp1).equals("FF"))
              {
                   temp1[0] = data[i+1];
                   System.out.println(toHex(temp1));
                   if(toHex(temp1).equals("DA"))
                   {   
                       
                       for(int j=(i+2);j<250+(i+4);j++)
                       {
                           temp1[0] = data[j];
                           
                           //System.out.println("test "+toHex(temp1)+"at "+(j));
                       }
                      
                       break outer;
                       
                       //break;
                   }
              }
             
          }
       int lock=0;
         int block=0;
         for(i=378;i<(378+193);i++)
         {
            if(data[i]!=0)
                data[i] = (byte)1;
             if(data[i]==0)
             { 
                block++;
                System.out.println("at "+i);
             }
             
         }
//         /System.out.println("block: "+block);
         //temp1[0] = data[i+5];
         System.out.println(toHex(temp1)+" at "+i);
          //int height=((0xFF & temp[0]) << 8) | (0xFF & temp[1]);
         for(int l=0;l<8;l++)
                       {
                           for(int t=0;t<8;t++)
                           {
                               //System.out.print(" "+(qtable[l][t]));
                           }
                           //System.out.println();
                       }
         System.out.println("-----");
           for(int l=0;l<8;l++)
                       {
                           for(int t=0;t<8;t++)
                           {
                              // System.out.print(" "+(qtable2[l][t]));
                           }
                           //System.out.println();
                       }
           
           /*data2 = new byte[data.length+1000];
          for(i=0;i<data.length;i++)
            data2[i]=data[i];  
          for(i=0;i<1000;i++)
              data2[i+data.length] = (byte)255;
              repaint();*/
      }
      
    public void getVersionButtons(int no_header)
	{	Button button[] = new Button[no_header+1];
		if(no_header!=0)
		{
			for(int i=0;i<=no_header;i++)
			{
				button[i] = new Button();
				button[i].setLabel("version "+i);
				button[i].setLocation(600, 50+(i*10));
				button[i].addActionListener(this);
				this.add(button[i]);
				
				
			}
		}
		
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
                else if(version==1)
                {
                    lock=1;
                    int k=0;
                    for(int i=0;i<index.length;i++)
                    {
                           
                            RGB[k]=255;k++;
                            RGB[k]=255;k++;
                            RGB[k]=255;k++;
                    }
                    //System.out.println("image2");
                   repaint();
                }
                else
                {
                    lock=1;
                    int k=0;
                    for(int i=0;i<index.length;i++)
                    {
                           
                            RGB[k]=200;k++;
                            RGB[k]=100;k++;
                            RGB[k]=130;k++;
                    }
                    //System.out.println("image2");
                   repaint();
                }
        }
    void changeImage(Graphics g)
    {
        BufferedImage img2 = img;
        System.out.println("image2");
        int k=0;
        int width = img.getWidth(null);
        for(int i=0;i<index.length;i++)
        {
            int ind = index[i];
            int xpos = ind % width;
            int ypos = (int)Math.ceil((double)ind/width);
            int R = RGB[k];k++;
            int G = RGB[k];k++;
            int B = RGB[k];k++;
            int rgb = new Color(R,G,B).getRGB();
            img2.setRGB(xpos,ypos,rgb);
            
        }
        g.drawImage(img2,0,0,null);
        
    }
    public void init() 
    {
        try
        {
            data = readData(new File("D:\\BTECH\\Internship\\Image Version Control\\src\\image\\version\\control\\car.jpg"));
            //showDetails(data);
            index = new int[5000];
            RGB = new int[5000*3];
            int k=0;
            for(int i=0;i<index.length;i++)
            {
                index[i]=i+15000;
                
            }
            getVersionButtons(2);
            
        }catch(Exception e){System.out.println(e);}
        
    }
    int lock=0;
    public void paint(Graphics g)
    {
          if(lock==0){  
        drawImage(data,g);
          lock=1;
          }
      else
           changeImage(g);

    }
   
}
