package ihm.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import static wrk.webcam.ColorMotion.DETECTION_ZONE_HEIGHT;

public class WebcamPanel extends JPanel {

    private BufferedImage image;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(image, 0, 0, this);
        renderDetectionZone(g);
    }

    private void renderDetectionZone(Graphics g) {
        g.setColor(Color.black);
        g.drawRect(0,
                getHeight() - DETECTION_ZONE_HEIGHT,
                getWidth(),
                getHeight());
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

}
