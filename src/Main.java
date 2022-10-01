
import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) {
        BufferedImage img;
        double[][] kernel = ImageProcessor.createKernel(5);
        img = ImageProcessor.readImage("wall.png");
        ImageProcessor.applyKernel(kernel, img);
        ImageProcessor.saveImage(img, "test");
    }

}
