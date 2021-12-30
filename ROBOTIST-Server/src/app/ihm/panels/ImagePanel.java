package app.ihm.panels;

import static app.consts.Constants.IHM_BACKGROUND_COLOR;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author dingl01
 */
public class ImagePanel extends JPanel {

    private volatile BufferedImage image;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(IHM_BACKGROUND_COLOR);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

}
