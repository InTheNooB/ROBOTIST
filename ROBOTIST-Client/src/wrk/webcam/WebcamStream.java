/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wrk.webcam;

import static constants.Constants.HEIGHT;
import static constants.Constants.WIDTH;
import org.openimaj.image.MBFImage;
import org.openimaj.video.capture.VideoCapture;
import org.openimaj.video.capture.VideoCaptureException;
import static wrk.webcam.LoginWebcam.FPS;


public class WebcamStream {

    private static VideoCapture vc1;

    private WebcamStream() {
    }

    /**
     * Creates a VideoCapture. The VideoCapture object is used to retreive
     * webcamFrame
     *
     * @return true if webcam created successfully or false if not
     */
    public synchronized boolean createWebcam() {
        boolean ok = true;

        if (vc1 == null) {
            try {
                vc1 = new VideoCapture(WIDTH, HEIGHT);
                vc1.setFPS(FPS);
            } catch (VideoCaptureException ex) {
                ok = false;
            }
        }
        return ok;
    }

    /**
     * Allows to retrieve the images from the camera.
     *
     * @return the next frame
     */
    public MBFImage getNextFrame() {
        if (vc1 == null) {
            createWebcam();
        }
        if (vc1 == null) {
            return null;
        }

        synchronized (vc1) {
            return vc1.getNextFrame();
        }
    }

    /**
     * Gets an instance of the WebcamStreamHolder class.
     *
     * @return instance of WebcamStream
     */
    public static WebcamStream getInstance() {
        return WebcamStreamHolder.INSTANCE;
    }

    private static class WebcamStreamHolder {

        private static final WebcamStream INSTANCE = new WebcamStream();
    }
}
    