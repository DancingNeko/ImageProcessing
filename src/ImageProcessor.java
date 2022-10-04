import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
                grey = Math.abs(grey);
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
                grey = Math.abs(grey);
                out[i][j] = grey;
            }
        }
        return out;
    }

    public static BufferedImage sobel(BufferedImage img) {
        double[][] Gx = { { 1, 0, -1 }, { 2, 0, -2 }, { 1, 0, -1 } };
        double[][] Gy = { { 1, 2, 1 }, { 0, 0, 0 }, { -1, -2, -1 } };
        double[][] xProcessed = applySobelKernel(Gx, img);
        double[][] yProcessed = applySobelKernel(Gy, img);
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                double greyXScaled = xProcessed[x][y] / 255.0;
                double greyYScaled = yProcessed[x][y] / 255.0;
                double grey = Math.sqrt(
                        greyXScaled * greyXScaled + greyYScaled * greyYScaled)
                        * 255;
                int rgb = 255;
                rgb = (rgb << 8) + (int) grey;
                rgb = (rgb << 8) + (int) grey;
                rgb = (rgb << 8) + (int) grey;
                out.setRGB(x, y, rgb);
            }
        }
        return out;
    }
}
