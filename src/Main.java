
import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) {
        BufferedImage img;
        img = ImageProcessor.readImage("gura.png");
        Visualizer window = new Visualizer(img);
        BufferedImage blurredImg = ImageProcessor
                .applyKernel(ImageProcessor.createKernel(5, 1.4), img);
        BufferedImage sobeledImage = ImageProcessor.sobel(blurredImg);
        window.showImage(sobeledImage);

    }

}
