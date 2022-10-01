import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Visualizer extends JFrame {
    public Visualizer(BufferedImage img) {
        super();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        JSlider blur = new JSlider();
        this.add(blur);
        DrawPanel p = new DrawPanel(
                this.blurImage(img, blur.getValue() / 10.0));
        this.add(p);
        this.setSize(img.getWidth(), img.getHeight() + 50);
        this.setVisible(true);
        blur.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                p.setPic(
                        Visualizer.this.blurImage(img, blur.getValue() / 10.0));
            }
        });

    }

    class DrawPanel extends JPanel {
        BufferedImage img = null;

        public DrawPanel(BufferedImage image) {
            this.setSize(image.getWidth(), image.getHeight());
            this.img = image;
            this.repaint();
        }

        public void setPic(BufferedImage image) {
            this.img = image;
            this.repaint();
        }

        @Override
        public void paintComponent(Graphics g) {
            g.drawImage(this.img, 0, 0, this);
        }
    }

    public BufferedImage blurImage(BufferedImage img, double level) {
        BufferedImage out = null;
        double[][] kernel = ImageProcessor.createKernel((int) level, level);
        out = ImageProcessor.applyKernel(kernel, img);
        return out;
    }
}
