package ihm;

import static constants.Constants.JOYSTICK_SIZE;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class JoyStick extends JPanel {

    public final int STROKE_WIDTH = 9;
    private int positionX = 60;
    private int positionY = 60;
    private double angle = 0;
    private double mag = 0;

    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(60, 63, 65));
        g2.fillRect(0, 0, 1000, 1000);
        g2.setStroke(new BasicStroke(STROKE_WIDTH));
        g2.setColor(Color.white);
        g2.drawOval(getCenter(JOYSTICK_SIZE, positionX), getCenter(JOYSTICK_SIZE, positionY), JOYSTICK_SIZE, JOYSTICK_SIZE);
        g2.setStroke(new BasicStroke(1));
        g2.drawOval(getCenter(20, getStickX(positionX, mag, angle, JOYSTICK_SIZE)), getCenter(20, getStickY(positionY, mag, angle, JOYSTICK_SIZE)), 20, 20);
    }

    private int getCenter(int size, int pos) {
        return pos - size / 2;

    }

    private int getStickX(int posX, double mag, double angle, int size) {
        return (int) (posX + mag * Math.cos(Math.toRadians(angle - 90)) * (size / 2));
    }

    private int getStickY(int posY, double mag, double angle, int size) {
        return (int) (posY + mag * Math.sin(Math.toRadians(angle - 90)) * (size / 2));
    }

    
    //Setter Getter
    
    
    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getMag() {
        return mag;
    }

    public void setMag(double mag) {
        this.mag = mag;
    }
}
