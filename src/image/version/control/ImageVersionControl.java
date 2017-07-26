package image.version.control;


class ImageVersionControl
{
    
    public static double[][] forwardDCTExtreme(float input[][]) {
    
    int N=8;
    double output[][] = new double[N][N];
    int v, u, x, y;
    for (v = 0; v < 8; v++) {
      for (u = 0; u < 8; u++) {
        for (x = 0; x < 8; x++) {
          for (y = 0; y < 8; y++) {
              input[x][y] -=128; 
            output[v][u] += input[x][y]
                * Math.cos(((double) (2 * x + 1) * (double) u * Math.PI) / 16)
                * Math.cos(((double) (2 * y + 1) * (double) v * Math.PI) / 16);
          }
        }
        output[v][u] *= (0.25) * ((u == 0) ? (1.0 / Math.sqrt(2)) : (double) 1.0)
            * ((v == 0) ? (1.0 / Math.sqrt(2)) : (double) 1.0);
      }
    }
    return output;
  }
    public static float[][] iforwardDCTExtreme(double input[][]) {
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
    public static double[][] forwardDCT(float input[][]) {
         int N=8;
         double output[][] = new double[N][N];
         double tmp0, tmp1, tmp2, tmp3, tmp4, tmp5, tmp6, tmp7;
         double tmp10, tmp11, tmp12, tmp13;
         double z1, z2, z3, z4, z5, z11, z13;
         int i;
         int j;

    // Subtracts 128 from the input values
    for (i = 0; i < 8; i++) {
      for (j = 0; j < 8; j++) {
        output[i][j] = (input[i][j] - 128.0);
        // input[i][j] -= 128;

      }
    }

    for (i = 0; i < 8; i++) {
      tmp0 = output[i][0] + output[i][7];
      tmp7 = output[i][0] - output[i][7];
      tmp1 = output[i][1] + output[i][6];
      tmp6 = output[i][1] - output[i][6];
      tmp2 = output[i][2] + output[i][5];
      tmp5 = output[i][2] - output[i][5];
      tmp3 = output[i][3] + output[i][4];
      tmp4 = output[i][3] - output[i][4];

      tmp10 = tmp0 + tmp3;
      tmp13 = tmp0 - tmp3;
      tmp11 = tmp1 + tmp2;
      tmp12 = tmp1 - tmp2;

      output[i][0] = tmp10 + tmp11;
      output[i][4] = tmp10 - tmp11;

      z1 = (tmp12 + tmp13) * 0.707106781;
      output[i][2] = tmp13 + z1;
      output[i][6] = tmp13 - z1;

      tmp10 = tmp4 + tmp5;
      tmp11 = tmp5 + tmp6;
      tmp12 = tmp6 + tmp7;

      z5 = (tmp10 - tmp12) * 0.382683433;
      z2 = 0.541196100 * tmp10 + z5;
      z4 = 1.306562965 * tmp12 + z5;
      z3 = tmp11 * 0.707106781;

      z11 = tmp7 + z3;
      z13 = tmp7 - z3;

      output[i][5] = z13 + z2;
      output[i][3] = z13 - z2;
      output[i][1] = z11 + z4;
      output[i][7] = z11 - z4;
    }

    for (i = 0; i < 8; i++) {
      tmp0 = output[0][i] + output[7][i];
      tmp7 = output[0][i] - output[7][i];
      tmp1 = output[1][i] + output[6][i];
      tmp6 = output[1][i] - output[6][i];
      tmp2 = output[2][i] + output[5][i];
      tmp5 = output[2][i] - output[5][i];
      tmp3 = output[3][i] + output[4][i];
      tmp4 = output[3][i] - output[4][i];

      tmp10 = tmp0 + tmp3;
      tmp13 = tmp0 - tmp3;
      tmp11 = tmp1 + tmp2;
      tmp12 = tmp1 - tmp2;

      output[0][i] = tmp10 + tmp11;
      output[4][i] = tmp10 - tmp11;

      z1 = (tmp12 + tmp13) * 0.707106781;
      output[2][i] = tmp13 + z1;
      output[6][i] = tmp13 - z1;

      tmp10 = tmp4 + tmp5;
      tmp11 = tmp5 + tmp6;
      tmp12 = tmp6 + tmp7;

      z5 = (tmp10 - tmp12) * 0.382683433;
      z2 = 0.541196100 * tmp10 + z5;
      z4 = 1.306562965 * tmp12 + z5;
      z3 = tmp11 * 0.707106781;

      z11 = tmp7 + z3;
      z13 = tmp7 - z3;

      output[5][i] = z13 + z2;
      output[3][i] = z13 - z2;
      output[1][i] = z11 + z4;
      output[7][i] = z11 - z4;
    }

    return output;
  }
    public static double[][] DCT1(float g[][])
    {
        double G1[][] = new double[8][8];
        int v=0,u=0;
        double sum=0;
        double a=1;
        while(u<8)
        {
            sum=0;
            for(int i=0;i<8;i++)
            {
                for(int j=0;j<8;j++)
                {
                    g[i][j]-=128;
                    sum+=g[i][j]*(Math.cos(((2*i+1)*v*Math.PI)/16))*(Math.cos(((2*j+1)*u*Math.PI)/16));
                    //sum+=(g[i][j])*((Math.cos((())/16))*(Math.cos(()/16)));
                }
            }
            if(u==0 && v==0)
                a=8;
            else if((u!=0 && v==0) || (v!=0 && u==0))
                a=4*Math.sqrt((double)2);
            else
                a=4;
            G1[u][v] = sum/a;
            v++;
            if(v>7)
            {
                v=0;
                u++;
            }
        }
        return G1;
    }
    public static void main(String args[])
    {
        float data[]={255,254,55,24,25,25,25,55,25,2,55,90,109,85,69,0,0,90,68,113,144,104,66,73,0,58,71,122,154,106,70,69,67,61,68,104,126,88,68,70,79,65,60,70,77,68,58,75,85,71,64,59,55,61,0,0,0,0,0,0,0,0,0,0};
        for(int i=0;i<40;i++)
            data[i] = (float)252;
        for(int i=40;i<64;i++)
        {
            data[i]=(float)0;
        }
          byte quant[]={16,11,10,16,24,40,51,61,12,12,14,19,26,58,60,55,14,13,16,24,40,57,69,56,14,17,22,29,51,87,80,62,18,22,37,56,68,109,103,77,24,35,55,64,81,104,113,92,49,64,78,87,103,121,120,101,72,92,95,98,112,100,103,99};
       
     /*   quant[0] = 17;
    quant[1] = 18;
    quant[2] = 24;
    quant[3] = 47;
    quant[4] = 99;
    quant[5] = 99;
    quant[6] = 99;
    quant[7] = 99;
    quant[8] = 18;
    quant[9] = 21;
    quant[10] = 26;
    quant[11] = 66;
    quant[12] = 99;
    quant[13] = 99;
    quant[14] = 99;
    quant[15] = 99;
    quant[16] = 24;
    quant[17] = 26;
    quant[18] = 56;
    quant[19] = 99;
    quant[20] = 99;
    quant[21] = 99;
    quant[22] = 99;
    quant[23] = 99;
    quant[24] = 47;
    quant[25] = 66;
    quant[26] = 99;
    quant[27] = 99;
    quant[28] = 99;
    quant[29] = 99;
    quant[30] = 99;
    quant[31] = 99;
    quant[32] = 99;
    quant[33] = 99;
    quant[34] = 99;
    quant[35] = 99;
    quant[36] = 99;
    quant[37] = 99;
    quant[38] = 99;
    quant[39] = 99;
    quant[40] = 99;
    quant[41] = 99;
    quant[42] = 99;
    quant[43] = 99;
    quant[44] = 99;
    quant[45] = 99;
    quant[46] = 99;
    quant[47] = 99;
    quant[48] = 99;
    quant[49] = 99;
    quant[50] = 99;
    quant[51] = 99;
    quant[52] = 99;
    quant[53] = 99;
    quant[54] = 99;
    quant[55] = 99;
    quant[56] = 99;
    quant[57] = 99;
    quant[58] = 99;
    quant[59] = 99;
    quant[60] = 99;
    quant[61] = 99;
    quant[62] = 99;
    quant[63] = 99;*/
         //for(int i=0;i<64;i++)quant[i]/=5;
        int[] jpegNaturalOrder = { 0, 1, 8, 16, 9, 2, 3, 10, 17, 24, 32, 25, 18, 11, 4, 5,
      12, 19, 26, 33, 40, 48, 41, 34, 27, 20, 13, 6, 7, 14, 21, 28, 35, 42, 49, 56, 57, 50, 43, 36,
      29, 22, 15, 23, 30, 37, 44, 51, 58, 59, 52, 45, 38, 31, 39, 46, 53, 60, 61, 54, 47, 55, 62,
      63 };
        System.out.println(data.length + " "+ quant.length+ " " + jpegNaturalOrder.length);
        double[][] G = new double[8][8];
        double[][] G1 = new double[8][8];
        float[][] g = new float[8][8];
         float[][] g1 = new float[8][8];
        int k=0;
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                g[i][j] = (float)data[k];
                
                k++;
            }
            
        }
        System.out.println("Original Data: Y: ");
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                System.out.print("|"+g[i][j]+"|");
            }
             System.out.println();
            
        }
        G1 = forwardDCTExtreme(g);
        //G1=DCT1(g);
        
         System.out.println("DCT I: Cosine Transformation:\n");
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
               G[i][j] = G1[j][i];
                System.out.print(" "+G[i][j]);
            }
             System.out.println();
            
        }
        int compressData[][] = new int[8][8];
        k=0;
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                compressData[i][j] = (int)(Math.round(G[i][j]/quant[k]));k++;
            }
            
            
        }
        System.out.println("compressed Data: \n");
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                System.out.print(" "+compressData[i][j]);
            }
             System.out.println();
            
        }
        k=0;
         for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                G1[i][j] = (int)(Math.round(compressData[i][j]*quant[k]));k++;
            }
            
            
        }
         System.out.println("-------------------------------------------------------------");
         System.out.println("Decompressing: ");
         System.out.println("multiplying: with quantum: DCT: \n");
         for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                System.out.print(" "+G1[i][j]);
            }
             System.out.println();
            
        }
        g=iforwardDCTExtreme(G1);
        System.out.println("Decompressed Final Data: \n");
        int finalData[][] = new int[8][8];
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
               
                finalData[i][j] = (Math.round(g[i][j])+128);
                //finalData[i][j] =  (int) ((finalData[i][j]/0.299  + finalData[i][j]/0.587 +  finalData[i][j]/0.114));
                System.out.print(" "+(Math.round(g[i][j])+128));
            }
             System.out.println();
            
        }
        int error=0;
        k=0;
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
               error+=Math.abs(finalData[i][j]-data[k]);
               k++;
                
            }
             //System.out.println();
            
        }
        System.out.println("\n----------\n|Error: "+Math.round(error/64)+"|\n----------");
        int value = 24;
        byte byt[] ={
            (byte)(value >>> 24),
            (byte)(value >>> 16),
            (byte)(value >>> 8),
            (byte)value};
        int value2 = byt[0] << 24 | (byt[1] & 0xFF) << 16 | (byt[2] & 0xFF) << 8 | (byt[3] & 0xFF);
        System.out.println(value2);
        String str = "Yash";
        byte[] des = str.getBytes();
        for(int i=0;i<des.length;i++)
        {
            System.out.print((char)(des[i]));
        }
    }
}