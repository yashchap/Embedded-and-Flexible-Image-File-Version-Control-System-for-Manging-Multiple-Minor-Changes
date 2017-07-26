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
    int EOI,sTail;
    List<Byte> index = new ArrayList();
    List<Byte> RGB = new ArrayList();
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
            EOI = EOI + tLength;
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
    public void actionPerformed(ActionEvent ae)
	{
            String str  = ae.getActionCommand();
            str = str.substring(8);
            int version = Integer.parseInt(str);
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
        drawImage(g);
    }
}
