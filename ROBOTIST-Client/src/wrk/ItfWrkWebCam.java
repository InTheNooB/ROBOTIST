package wrk;

import java.awt.image.BufferedImage;


public interface ItfWrkWebCam {


    public void setWebcamState(boolean ok);

    public void receiveWebCamFrame(BufferedImage img2);

    public void faceDetected(BufferedImage img2);

    
}
