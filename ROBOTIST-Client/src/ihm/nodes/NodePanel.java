package ihm.nodes;

import static constants.Constants.IHM_BACKGROUND_COLOR;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class NodePanel extends JPanel implements Runnable {

    // -1 => loading
    // 0  => off
    // 1  => on
    private int state;

    // Thread
    private boolean running;

    // Animation
    private Point[] points;
    private int pointRadius;

    public NodePanel() {
        state = 0;
    }

    private void initLoadingAnimation() {
        points = new Point[8];

        double currentAngle = 0;
        int currentAlpha = 255;
        double angleToAdd = 45;
        int alphaToAdd = 31;

        pointRadius = getWidth() / 5;
        for (int i = 0; i < points.length; i++) {
            double x = Math.cos(Math.toRadians(currentAngle)) * (getWidth() / 2 - pointRadius / 2);
            x += getWidth() / 2 - pointRadius / 2;
            double y = Math.sin(Math.toRadians(currentAngle)) * (getHeight() / 2 - pointRadius / 2);
            y += getHeight() / 2 - pointRadius / 2;
            points[i] = new Point((int) (x), (int) (y));
            points[i].alpha = currentAlpha;
            currentAngle += angleToAdd;
            currentAlpha -= alphaToAdd;
        }
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            repaint();
            if (state != -1) {
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
            sleeep();
        }
    }

    private void sleeep() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        clearBackground(g2);

        // Anti Aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (state != -1) {
            g2.setColor((state == 1) ? Color.green : Color.red);
            g2.fillOval(0, 0, getWidth(), getHeight());
        } else {
            drawLoadingAnimation(g2);
        }
    }

    private void drawLoadingAnimation(Graphics2D g) {
        if (points == null) {
            return;
        }
        for (Point p : points) {
            if (p == null) {
                return;
            }
            p.alpha -= 5;
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

    public void setState(int state) {
        this.state = state;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
