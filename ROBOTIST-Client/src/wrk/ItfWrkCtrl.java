package wrk;

import beans.Sequence;
import java.awt.image.BufferedImage;


public interface ItfWrkCtrl {

    public void receiveWebCamFrame(BufferedImage img2);

    public void checkForFace(boolean b);

    public void startUp();

    public void login(String username, String password);

    public void logout();

    public void closeApplication();

    public void signIn(String username, String password);

    public void playSequence(Sequence s);


    public void createSequence(Sequence s);

    public void connect(String serverIp);

    public void setControlMode(boolean b);

    public void startMotionDetection(boolean start);

}
