
import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) {
        BufferedImage img;
        img = ImageProcessor.readImage("lignt.png");
        Visualizer window = new Visualizer(img);
    }

}
