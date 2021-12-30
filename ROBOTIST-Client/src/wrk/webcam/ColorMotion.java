package wrk.webcam;

import static constants.Constants.HEIGHT;
import static constants.Constants.MAX_ROBOT_SPEED;
import static constants.Constants.WIDTH;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import wrk.ItfWrkColorMotion;

public class ColorMotion extends Thread {

    // Constants
    public static final int DETECTION_ZONE_HEIGHT = 1;

    public static final int MIN_DETECTED_COLOR_VALUE = 90;
    public static final double MIN_DETECTED_COLOR_FREQUENCY = 0.025;
    public static final int MIN_ACTION_COLOR_HEIGHT = 50;
    public static final int REFRESH_RATE = 10;

    // Thread
    private volatile boolean running;

    // Color Infos
    private int redActionZoneHeight, greenActionZoneHeight, blueActionZoneHeight, yellowActionZoneHeight;

    public ItfWrkColorMotion refWrk;

    public ColorMotion(ItfWrkColorMotion refWrk) {
        this.refWrk = refWrk;
        WebcamStream.getInstance().createWebcam();
    }

    /**
     * Sends webcam image to the ihm. And calculates the max height of each
     * detected colors
     */
    @Override
    public void run() {
        MBFImage nextFrame;
        BufferedImage bImage;
        int[] pixels;
        running = true;
        while (running) {
            // First get the next frame
            nextFrame = WebcamStream.getInstance().getNextFrame();

            // Create a BufferedImage out of it
            if (nextFrame != null) {
                bImage = ImageUtilities.createBufferedImage(nextFrame);
                refWrk.receiveColorWebcamFrame(bImage);
                pixels = bImage.getRGB(0, 0, WIDTH, HEIGHT, null, 0, WIDTH);
                updateActionZone(pixels);
                refWrk.updateActionZoneColor(redActionZoneHeight, greenActionZoneHeight, blueActionZoneHeight, yellowActionZoneHeight);

            }

            sleeep();

        }

    }

    private void sleeep() {
        try {
            Thread.sleep(REFRESH_RATE);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Calculates the left and right speed of the robot tracks who is determined
     * by the height of a certain color in the array of pixels.
     *
     * @param pixels array of pixels
     */
    private void updateActionZone(int[] pixels) {

        // Only get pixels that are inside of the action zone
        int[] detectionZonePixels = Arrays.copyOfRange(pixels,
                0,
                WIDTH * (HEIGHT - DETECTION_ZONE_HEIGHT));
        redActionZoneHeight = findColorHeight(detectionZonePixels, 'r');
        greenActionZoneHeight = findColorHeight(detectionZonePixels, 'g');
        blueActionZoneHeight = findColorHeight(detectionZonePixels, 'b');
        yellowActionZoneHeight = findColorHeight(detectionZonePixels, 'y');

        int actionZoneHeight = HEIGHT - DETECTION_ZONE_HEIGHT;

        if (redActionZoneHeight > 0) {
            redActionZoneHeight = MAX_ROBOT_SPEED / actionZoneHeight * (actionZoneHeight - redActionZoneHeight);
        } else if (greenActionZoneHeight > 0) {
            greenActionZoneHeight = MAX_ROBOT_SPEED / actionZoneHeight * (actionZoneHeight - greenActionZoneHeight);
        }

        if (blueActionZoneHeight > 0) {
            blueActionZoneHeight = MAX_ROBOT_SPEED / actionZoneHeight * (actionZoneHeight - blueActionZoneHeight);
        } else if (yellowActionZoneHeight > 0) {
            yellowActionZoneHeight = MAX_ROBOT_SPEED / actionZoneHeight * (actionZoneHeight - yellowActionZoneHeight);
        }

    }

    /**
     * Finds the max height of a detected color.
     *
     * @param pixels array of pixels
     * @param colorToFind color to find
     * @return max height
     */
    private int findColorHeight(int[] pixels, char colorToFind) {
        int[][] matrixPixels = new int[WIDTH][HEIGHT];
        int x = 0, y = 0;
        for (int i = 0; i < pixels.length; i++) {
            matrixPixels[x][y] = pixels[i];
            x++;
            if (x == WIDTH - 1) {
                y++;
                x = 0;
            }
        }
        Color currentColor;
        int r, g, b;

        for (int[] matrixPixel : matrixPixels) {
            for (int j = 0; j < matrixPixel.length; j++) {
                currentColor = new Color(matrixPixel[j]);
                r = currentColor.getRed();
                g = currentColor.getGreen();
                b = currentColor.getBlue();
                if (isColorFound(r, g, b, colorToFind)) {
                    // Get pixels from the same row as this pixel
                    int matchingPixels = 0;
                    for (int k = j; k < HEIGHT - 1; k++) {
                        currentColor = new Color(matrixPixel[k]);
                        r = currentColor.getRed();
                        g = currentColor.getGreen();
                        b = currentColor.getBlue();
                        matchingPixels = (isColorFound(r, g, b, colorToFind))
                                ? (matchingPixels + 1)
                                : (matchingPixels);
                    }
                    if (matchingPixels >= MIN_ACTION_COLOR_HEIGHT) {
                        return j;
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Checks if a certain color is found in the array of pixels
     *
     * @param pixels array of pixels
     * @param colorToFind color to find
     * @return true if found or false if not
     */
    private boolean findColor(int[] pixels, char colorToFind) {
        boolean result = false;
        int color = 0;
        double goalFrequencyGoal = pixels.length * MIN_DETECTED_COLOR_FREQUENCY;
        for (int i = 0; i < pixels.length; i++) {
            Color currentColor = new Color(pixels[i]);
            int r = currentColor.getRed();
            int g = currentColor.getGreen();
            int b = currentColor.getBlue();
            if (isColorFound(r, g, b, colorToFind)) {
                color++;
            }
            if (color >= goalFrequencyGoal) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Checks if a pixel match to the wanted color.
     *
     * @param r red value of the pixel
     * @param g green value of the pixel
     * @param b blue value of the pixel
     * @param color desired color
     * @return true if it matches or false if not
     */
    public boolean isColorFound(int r, int g, int b, char color) {
        int important = 0, o1 = 0, o2 = 0;
        switch (Character.toUpperCase(color)) {
            case 'r':
            case 'R':
                important = r;
                o1 = g;
                o2 = b;
                break;
            case 'g':
            case 'G':
                important = g;
                o1 = r;
                o2 = b;
                break;
            case 'b':
            case 'B':
                important = b;
                o1 = g;
                o2 = r;
                break;
            case 'y':
            case 'Y':
                return isYellowFound(r, g, b);
        }
        return important > MIN_DETECTED_COLOR_VALUE
                && o1 < MIN_DETECTED_COLOR_VALUE
                && o2 < MIN_DETECTED_COLOR_VALUE;
    }

    public boolean isYellowFound(int r, int g, int b) {
        return b < MIN_DETECTED_COLOR_VALUE
                && g > MIN_DETECTED_COLOR_VALUE
                && r > MIN_DETECTED_COLOR_VALUE;

    }

    //Setter Getter
    public void setRunning(boolean running) {
        this.running = running;
    }

}
