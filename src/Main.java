
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedImage img;
        img = ImageProcessor.readImage("cat.png");
        //Visualizer window = new Visualizer(img);
        BufferedImage blurredImg = ImageProcessor
                .applyKernel(ImageProcessor.createKernel(5, 1.4), img);
        double[][] sobeledImage = ImageProcessor.sobel(blurredImg,2);
        //window.showImage(blurredImg);
        ArrayList<String> functions = ImageProcessor.regression(sobeledImage, 10);
        HTMLWritter test = new HTMLWritter("noooo", functions);
    }

}
