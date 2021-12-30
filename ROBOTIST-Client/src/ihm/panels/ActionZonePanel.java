package ihm.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class ActionZonePanel extends JPanel {


    private int redInActionZone,
            greenInActionZone,
            blueInActionZone,
            yellowInActionZone;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        g2.setFont(new Font("Arial", Font.BOLD, 12));

        if (redInActionZone != 0) {
            g2.setColor(Color.red);
            g2.fillRect(20, 20, 20, 20);
            g2.drawString("Forward : " + redInActionZone, 50, 45);
        }
        if (greenInActionZone != 0) {
            g2.setColor(Color.green);
            g2.fillRect(20, 65, 20, 20);
            g2.drawString("Backward : " + greenInActionZone, 50, 90);
        }
        if (blueInActionZone != 0) {
            g2.setColor(Color.blue);
            g2.fillRect(20, 110, 20, 20);
            g2.drawString("Left : " + blueInActionZone, 50, 135);
        }
        if (yellowInActionZone != 0) {
            g2.setColor(Color.yellow);
            g2.fillRect(20, 155, 20, 20);
            g2.drawString("Right : " + yellowInActionZone, 50, 180);
        }

    }

    public void setRedInActionZone(int redInActionZone) {
        this.redInActionZone = redInActionZone;
    }

    public void setGreenInActionZone(int greenInActionZone) {
        this.greenInActionZone = greenInActionZone;
    }

    public void setBlueInActionZone(int blueInActionZone) {
        this.blueInActionZone = blueInActionZone;
    }

    public void setYellowInActionZone(int yellowInActionZone) {
        this.yellowInActionZone = yellowInActionZone;
    }


    

}
