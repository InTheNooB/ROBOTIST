package ctrl;

import beans.Sequence;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public interface ItfCtrlWrk {

    public void receiveWebCamFrame(BufferedImage img2);

    public void faceDetected(BufferedImage img2);

    public void arrowUp();

    public void arrowDown();

    public void moveRightJoystick(double direction, double magnitudeDroite);

    public void moveLeftJoystick(double direction, double magnitudeGauche);

    public void buttonAClicked();

    public void setWebcamState(int state);

    public void setControllable(int i);

    public void showError(String error);

    public void retrieveRobotVideo(BufferedImage robotVideo);

    public void retrieveSequence(ArrayList<Sequence> sequenceToDisplay);

    public void retrieveRobotPicture(BufferedImage robotPicture);

    public void signInOk(boolean ok);

    public void loginOk(boolean ok);

    public void retrieveCurrentAction(int currentAction);

    public void retrievePing(String p);

    public void updateServerIp(String cannot_connect_to_server);

    public void serverDisconnected();

    public void receiveColorWebcamFrame(BufferedImage bImage);

    public void updateActionZoneColor(int redActionZoneHeight, int greenActionZoneHeight, int blueActionZoneHeight, int yellowActionZoneHeight);

    public void appendToConsole(String text);

}
