import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Visualizer extends JFrame {
    DrawPanel p;
    int countStroke = 0;
    public static BufferedImage src;
    public Visualizer(BufferedImage img) {
        super();
        src = img;
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        JSlider blur = new JSlider();
        this.add(blur);
        this.p = new DrawPanel(img);
        this.add(this.p);
        this.setSize(img.getWidth(), img.getHeight() + 50);
        JButton button  = new JButton("confirm");
        this.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(countStroke % 2 == 0)
                    src = Visualizer.blurImage(src, blur.getValue()/10);
                if(countStroke % 2 == 1)
                    src = ImageProcessor.sobel(src, (int)(blur.getValue()*2.55));
                Visualizer.this.countStroke++;
            }
        });
        blur.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                if(countStroke % 2 == 0)
                    p.setPic(Visualizer.blurImage(src, blur.getValue()/10));
                if(countStroke % 2 == 1)
                    p.setPic(ImageProcessor.sobel(src, (int)(blur.getValue()*2.55)));
            }
        });
        this.setVisible(true);
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

    public BufferedImage sobelImage(BufferedImage img, int level) {
        BufferedImage out = null;
        out = ImageProcessor.sobel(img, level);
        return out;
    }

    public static BufferedImage blurImage(BufferedImage img, double level) {
        BufferedImage out = null;
        double[][] kernel = ImageProcessor.createKernel((int) level, level);
        out = ImageProcessor.applyKernel(kernel, img);
        return out;
    }

    public void showImage(BufferedImage image) {
        this.p.setPic(image);
    }
}
