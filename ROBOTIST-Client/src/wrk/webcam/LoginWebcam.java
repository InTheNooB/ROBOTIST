package wrk.webcam;

import java.awt.image.BufferedImage;

import java.util.ArrayList;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.colour.Transforms;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.FaceDetector;
import org.openimaj.image.processing.face.detection.keypoints.FKEFaceDetector;
import org.openimaj.image.processing.face.detection.keypoints.KEDetectedFace;
import org.openimaj.math.geometry.shape.Rectangle;
import wrk.ItfWrkWebCam;

public class LoginWebcam extends Thread {

    public static final double FPS = 15.0;
    public static final int CONNECTION_DELAY = 100;
    public static final int FRAME_RATE = 10;
    private final ItfWrkWebCam refWrk;
    private volatile boolean running;
    private volatile boolean reading;
    private volatile boolean paused;
    private int width;
    private int height;

    private MBFImage frame1;
    private boolean detecteFace;
    private BufferedImage userFace;

    public LoginWebcam(int width, int height, ItfWrkWebCam refWrk) {
        this.refWrk = refWrk;
        this.width = width;
        this.height = height;
        setName("Webcam Handler Thread");
        reading = true;
        paused = false;
    }

    /**
     * Sends webcam image to the ihm and try to detect a face if needed.
     */
    @Override
    public void run() {
        running = true;
        reading = true;
        paused = false;
        while (running) {
            boolean ok = WebcamStream.getInstance().createWebcam();
            refWrk.setWebcamState(ok);
            if (ok) {
                while (reading) {
                    if (!paused) {

                        frame1 = WebcamStream.getInstance().getNextFrame();
                        if (detecteFace) {
                            detectFace();
                        }
                        BufferedImage img2 = ImageUtilities.createBufferedImage(frame1);
                        refWrk.receiveWebCamFrame(img2);
                    } else {
                        sleeep(FRAME_RATE);
                    }
                }
            }
            sleeep(CONNECTION_DELAY);
        }
    }

    /**
     * Detects if face is on current frame. If a face is detected the face is
     * sent to the ihm to display it
     */
    private void detectFace() {
        FaceDetector<KEDetectedFace, FImage> fd = new FKEFaceDetector();
        ArrayList<KEDetectedFace> faces = (ArrayList<KEDetectedFace>) fd.detectFaces(Transforms.calculateIntensity(frame1));
        boolean isFaceDetected = false;
        for (DetectedFace face : faces) {
            isFaceDetected = true;
            Rectangle rect = face.getBounds();
            rect.x -= 22.5;
            rect.y -= 40;
            rect.height += 50;
            rect.width += 50;
            face.setBounds(rect);
            frame1.drawShape(face.getShape(), RGBColour.GREEN);
        }
        if (isFaceDetected) {
            running = false;
            BufferedImage img2 = ImageUtilities.createBufferedImage(frame1);
            userFace = img2;
            refWrk.faceDetected(img2);
        }

    }

    /**
     * Sends the image to the wrk to be displayed.
     *
     * @param image webcam frame
     */
    public void receiveFlux(BufferedImage image) {
        refWrk.receiveWebCamFrame(image);
    }

    /**
     * This method is used to sleep the thread
     *
     * @param mili time to sleep
     */
    private void sleeep(int mili) {
        try {
            sleep(mili);
        } catch (InterruptedException ex) {
        }
    }

    //Setter Getter
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isReading() {
        return reading;
    }

    public void setReading(boolean reading) {
        this.reading = reading;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isDetecteFace() {
        return detecteFace;
    }

    public void setDetecteFace(boolean detecteFace) {
        this.detecteFace = detecteFace;
    }

    public BufferedImage getUserFace() {
        return userFace;
    }

    public void setUserFace(BufferedImage userFace) {
        this.userFace = userFace;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
