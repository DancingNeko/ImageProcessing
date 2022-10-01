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

    public static double[][] createKernel(int k) {
        int width = k;
        int height = k;
        double sum = 0;
        double[][] kernel = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                double grey = Math.pow(Math.E,
                        (-1.0 / k * Math.pow(i - width / 2, 2) / 2))
                        * (Math.pow(Math.E,
                                (-1.0 / k * Math.pow(j - height / 2, 2) / 2))
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

    public static void applyKernel(double[][] kernel, BufferedImage img) {
        for (int i = kernel.length; i < img.getWidth() - kernel.length; i++) {
            for (int j = kernel.length; j < img.getHeight()
                    - kernel.length; j++) {
                double r = 0;
                double g = 0;
                double b = 0;
                for (int x = 0; x < kernel.length; x++) {
                    for (int y = 0; y < kernel.length; y++) {
                        int argb = img.getRGB(i + x - kernel.length / 2,
                                j + y - kernel.length / 2);
                        int blue = (argb) & 0xFF;
                        int green = (argb >> 8) & 0xFF;
                        int red = (argb >> 16) & 0xFF;
                        r += kernel[x][y] * red;
                        g += kernel[x][y] * green;
                        b += kernel[x][y] * blue;
                    }
                }
                int rgb = 255;
                rgb = (rgb << 8) + (int) r;
                rgb = (rgb << 8) + (int) g;
                rgb = (rgb << 8) + (int) b;
                img.setRGB(i, j, rgb);
            }
        }
    }
}
