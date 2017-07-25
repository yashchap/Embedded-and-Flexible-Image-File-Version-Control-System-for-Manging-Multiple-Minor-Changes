/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image.version.control;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Dell
 */
public class Jpegv3 extends Applet {

    /**
     * Initialization method that will be called after the applet is loaded into
     * the browser.
     */
    BufferedImage img;
    int index[];
    int RGB[];
    public void init() {
        try {
            // TODO start asynchronous download of heavy resources
          img=ImageIO.read(new File("D:\\BTECH\\Internship\\Image Version Control\\src\\image\\version\\control\\car.jpg"));
          index = new int[3200];
          RGB = new int[3200*3];
          int k=0;
          for(int i=0;i<index.length;i++)
          {
              RGB[k]=255;k++;
              RGB[k]=255;k++;
              RGB[k]=255;k++;
          }
          double YCC[] = new double[3200*3];
          k=0;
          for(int i=0;i<RGB.length;i++)
          {
              int r = RGB[i];i++;
              int g = RGB[i];i++;
             int b =  RGB[i];i++;
             YCC[k] = 0.299*r + 0.587*g + 0.114*b;k++;
             YCC[k] = (-0.169*r -0.331*g + 0.5*b)+128;k++;
             YCC[k] = 0.5*r-0.419*g-0.081*b+128;k++;
         
          }
          for(int i=0;i<15;i+=3)
          {
              System.out.println(YCC[i]+" "+YCC[i+1]+" "+YCC[i+2]);
          }
          
          
        } catch (IOException ex) {
            Logger.getLogger(Jpegv3.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    public void paint(Graphics g)
    {
        g.drawImage(img,0,0,null);
    }
    
    // TODO overwrite start(), stop() and destroy() methods
}
