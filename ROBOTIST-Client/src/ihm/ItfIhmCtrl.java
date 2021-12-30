package ihm;

import beans.Sequence;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public interface ItfIhmCtrl {

    void displayWebCamFrame(BufferedImage img);

    public void displayFaceDetected(BufferedImage img2);

    public void arrowUp();

    public void arrowDown();

    public void moveRightJoystick(double direction, double magnitudeDroite);

    public void moveLeftJoystick(double direction, double magnitudeGauche);

    public void buttonAClicked();

    public void setWebcamState(int state);

    public void setControllable(int state);

    public void showError(String error);

    public void displayRobotVideo(BufferedImage robotVideo);

    public void displaySequence(ArrayList<Sequence> sequenceToDisplay);

    public void displayRobotPicture(BufferedImage robotPicture);

    public void signInOk(boolean ok);

    public void loginOk(boolean ok);

    public void displayCurrentAction(int currentAction);

    public void displayPing(String p);

    public void updateServerIp(String ip);

    public void serverDisconnected();

    public void displayColorWebcamFrame(BufferedImage bImage);

    public void updateActionZoneColor(int redActionZoneHeight, int greenActionZoneHeight, int blueActionZoneHeight, int yellowActionZoneHeight);


    public void appendToConsole(String text);
}
