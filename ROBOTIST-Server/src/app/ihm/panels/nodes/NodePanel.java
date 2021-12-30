package app.ihm.panels.nodes;

import static app.consts.Constants.IHM_BACKGROUND_COLOR;
import static app.consts.Constants.NODE_PANEL_ANIMATION_SPEED;
import static app.consts.Constants.NODE_PANEL_SLEEP_TIME;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 *
 * @author dingl01
 */
public class NodePanel extends JPanel implements Runnable {

    private static final int ANGLE_TO_ADD = 45;
    private static final int ALPHA_TO_ADD = 31;

    private ConnectionState state;

    // Thread
    private volatile boolean running;

    // Animation
    private AnimationPoint[] points;

    public NodePanel() {
        state = ConnectionState.DISCONNECTED;
    }

    private void initLoadingAnimation() {
        points = new AnimationPoint[8];

        double currentAngle = 0;
        int currentAlpha = 255;

        int pointRadius = getWidth() / 5;

        // Create the animation points
        for (int i = 0; i < points.length; i++) {
            double x = Math.cos(Math.toRadians(currentAngle)) * (getWidth() / 2 - pointRadius / 2);
            x += getWidth() / 2 - pointRadius / 2;
            double y = Math.sin(Math.toRadians(currentAngle)) * (getHeight() / 2 - pointRadius / 2);
            y += getHeight() / 2 - pointRadius / 2;
            points[i] = new AnimationPoint((int) (x), (int) (y));
            points[i].alpha = currentAlpha;
            currentAngle += ANGLE_TO_ADD;
            currentAlpha -= ALPHA_TO_ADD;
        }
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            repaint();
            if (state != ConnectionState.LOADING) {
                try {
                    synchronized (this) {
                        wait();
                    }
                } catch (InterruptedException ex) {
                }
            }
            if (points == null) {
                initLoadingAnimation();
            }
            try {
                Thread.sleep(NODE_PANEL_SLEEP_TIME);
            } catch (InterruptedException ex) {
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        clearBackground(g2);

        // Anti Aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (state != ConnectionState.LOADING) {
            g2.setColor((state == ConnectionState.CONNECTED) ? Color.green : Color.red);
            g2.fillOval(0, 0, getWidth(), getHeight());
        } else {
            drawLoadingAnimation(g2);
        }
    }

    private void drawLoadingAnimation(Graphics2D g) {
        if (points == null) {
            return;
        }
        for (AnimationPoint p : points) {
            if (p == null) {
                return;
            }
            p.alpha -= NODE_PANEL_ANIMATION_SPEED;
            if (p.alpha <= 0) {
                p.alpha = 255;
            }
            g.setColor(new Color(255, 255, 255, p.alpha));
            g.fillOval(p.x, p.y, getWidth() / 5, getHeight() / 5);
        }
    }

    private void clearBackground(Graphics2D g) {
        g.setColor(IHM_BACKGROUND_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    public void setState(ConnectionState state) {
        this.state = state;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
