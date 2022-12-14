import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import org.apache.commons.math3.stat.regression.SimpleRegression;


public class ImageProcessor {

    public static BufferedImage readImage(String name) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(name));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return img;
    }

    public static void saveImage(BufferedImage img, String name) {
        File output = new File(name);
        try {
            ImageIO.write(img, "png", output);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static double[][] createKernel(int k, double blur) {
        int width = k;
        int height = k;
        double sum = 0;
        double[][] kernel = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                double grey = Math.pow(Math.E,
                        (-1.0 / (k * blur) * Math.pow(i - width / 2, 2) / 2))
                        * (Math.pow(Math.E, (-1.0 / (k * blur)
                                * Math.pow(j - height / 2, 2) / 2))
                                / (2 * Math.PI));
                sum += grey;
                kernel[i][j] = grey;
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                kernel[i][j] /= sum;
            }
        }
        return kernel;
    }

    public static BufferedImage applyKernel(double[][] kernel,
            BufferedImage img) {
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);
        for (int i = kernel.length; i < img.getWidth() - kernel.length; i++) {
            for (int j = kernel.length; j < img.getHeight()
                    - kernel.length; j++) {
                double grey = 0;
                for (int x = 0; x < kernel.length; x++) {
                    for (int y = 0; y < kernel.length; y++) {
                        int argb = img.getRGB(i + x - kernel.length / 2,
                                j + y - kernel.length / 2);
                        int blue = (argb) & 0xFF;
                        int green = (argb >> 8) & 0xFF;
                        int red = (argb >> 16) & 0xFF;
                        grey += kernel[x][y] * (red + green + blue) / 3.0;
                    }
                }
                int rgb = 255;
                rgb = (rgb << 8) + (int) grey;
                rgb = (rgb << 8) + (int) grey;
                rgb = (rgb << 8) + (int) grey;
                out.setRGB(i, j, rgb);
            }
        }
        return out;
    }

    public static double[][] applySobelKernel(double[][] kernel,
            BufferedImage img) {
        double[][] out = new double[img.getWidth()][img.getHeight()];
        for (int i = kernel.length; i < img.getWidth() - kernel.length; i++) {
            for (int j = kernel.length; j < img.getHeight()
                    - kernel.length; j++) {
                double grey = 0;
                for (int x = 0; x < kernel.length; x++) {
                    for (int y = 0; y < kernel.length; y++) {
                        int argb = img.getRGB(i + x - kernel.length / 2,
                                j + y - kernel.length / 2);
                        int blue = (argb) & 0xFF;
                        int green = (argb >> 8) & 0xFF;
                        int red = (argb >> 16) & 0xFF;
                        grey += kernel[x][y] * (red + green + blue) / 3.0;
                    }
                }
                out[i][j] = grey;
            }
        }
        return out;
    }

    public static double[][] sobel(BufferedImage img, int edge) {
        double[][] Gx = { { 1, 0, -1 }, { 2, 0, -2 }, { 1, 0, -1 } };
        double[][] Gy = { { 1, 2, 1 }, { 0, 0, 0 }, { -1, -2, -1 } };
        double[][] xProcessed = applySobelKernel(Gx, img);
        double[][] yProcessed = applySobelKernel(Gy, img);
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);
        double[][] result = nonMaxSuppression(xProcessed, yProcessed, edge);
        //for (int i = 0; i < img.getWidth(); i++) {
        //    for (int j = 0; j < img.getHeight(); j++) {
        //        int visual = result[i][j] > edge ? 255 : 0;
        //        int rgb = 255;
        //        rgb = (rgb << 8) + visual;
        //        rgb = (rgb << 8) + visual;
        //        rgb = (rgb << 8) + visual;
        //        out.setRGB(i, j, rgb);
        //    }
        //}
        return result;
    }

    private static double[][] nonMaxSuppression(double[][] xMag,
            double[][] yMag, int edge) {
        double[][] angle = new double[xMag.length][xMag[0].length];
        double[][] grey = new double[xMag.length][xMag[0].length];
        for (int x = 0; x < xMag.length; x++) {
            for (int y = 0; y < xMag[0].length; y++) {
                if (xMag[x][y] == 0 && yMag[x][y] == 0) {
                    continue;
                }
                double greyXScaled = xMag[x][y] / 255.0;
                double greyYScaled = yMag[x][y] / 255.0;
                grey[x][y] = Math.sqrt(
                        greyXScaled * greyXScaled + greyYScaled * greyYScaled)
                        * 255;
                grey[x][y] = grey[x][y] >= grey[x][y]*(edge/10.0) ? 255 : 0;
                //angle[x][y] = 90;
                //if (greyXScaled != 0) {
                //    angle[x][y] = Math
                //            .toDegrees(Math.atan(greyYScaled / greyXScaled));
                //}
            }
        }
        //int width = xMag.length;
        //int height = xMag[0].length;
        //for (int x = 1; x < width - 1; x++) {
        //    for (int y = 1; y < height - 1; y++) {
        //        if (Math.abs(angle[x][y] - 180) <= 22.5
        //                || Math.abs(angle[x][y]) <= 22.5) {
        //            double tis = grey[x][y];
        //            if ((grey[x][y - 1] >= tis) || (grey[x][y + 1] >= tis)) {
        //                grey[x][y] = 0;
        //            }
        //        }
        //        if (Math.abs(angle[x][y] - 45) <= 22.5) {
        //            double tis = grey[x][y];
        //            if ((grey[x - 1][y - 1] >= tis)
        //                    || (grey[x + 1][y + 1] >= tis)) {
        //                grey[x][y] = 0;
        //            }
        //        }
        //        if (Math.abs(angle[x][y] + 90) <= 22.5) {
        //            double tis = grey[x][y];
        //            if ((grey[x - 1][y] >= tis) || (grey[x + 1][y] >= tis)) {
        //                grey[x][y] = 0;
        //            }
        //        }
        //        if (Math.abs(angle[x][y] - 135) <= 22.5) {
        //            double tis = grey[x][y];
        //            if ((grey[x + 1][y - 1] >= tis)
        //                    || (grey[x - 1][y + 1] >= tis)) {
        //                grey[x][y] = 0;
        //            }
        //        }
        //    }
        //}
        return grey;
    }

    public static ArrayList<String> regression(double[][] img, int size){
        ArrayList<String> out = new ArrayList<String>();
        for(int i = 0; i < img.length - size; i+=size/2){
            for(int j = 0; j < img[0].length - size; j+=size/2){
                int count = 0;
                for (int x = 0; x < size; x++){
                    for (int y = 0; y < size; y++){
                        if (img[i+x][j+y] != 0)
                            count++;
                    }
                }
                if(count >= 0.9*size*size || count <= 0.2*size*size || count == 0)
                    continue;
                double[] xCord = new double[count];
                double[] yCord = new double[count];
                count = 0;
                for (int x = 0; x < size; x++){
                    for (int y = 0; y < size; y++){
                        if (img[i+x][j+y] != 0){
                            xCord[count] = x;
                            yCord[count] = y;
                            count++;
                        }
                    }
                }
                SimpleRegression reg = new SimpleRegression();
                for(int v = 0; v < xCord.length; v++)
                    reg.addData(xCord[v],yCord[v]);
                String output = -1*reg.getSlope() + "(x-" + i + ")+" + -1*(reg.getIntercept() + j);
                output += "\\\\{" + i + "<=x<=" + (i+size) + "\\\\}";
                out.add(output);
            }
        }
        return out;
    }
}
