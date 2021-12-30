package wrk;

import java.awt.image.BufferedImage;


public interface ItfWrkColorMotion {

    public void receiveColorWebcamFrame(BufferedImage bImage);

    public void updateActionZoneColor(int redActionZoneHeight, int greenActionZoneHeight, int blueActionZoneHeight, int yellowActionZoneHeight);
    
}
