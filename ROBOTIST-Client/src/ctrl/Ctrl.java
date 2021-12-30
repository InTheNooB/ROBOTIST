package ctrl;

import beans.Sequence;
import ihm.ItfIhmCtrl;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import wrk.ItfWrkCtrl;

public class Ctrl implements ItfCtrlIhm, ItfCtrlWrk {

    private ItfIhmCtrl refIhm;
    private ItfWrkCtrl refWrk;

    @Override
    public void receiveWebCamFrame(BufferedImage img2) {
        refIhm.displayWebCamFrame(img2);
    }

    @Override
    public void faceDetected(BufferedImage img2) {
        refIhm.displayFaceDetected(img2);
    }

    @Override
    public void checkForFace(boolean b) {
        refWrk.checkForFace(b);
    }

    @Override
    public void arrowUp() {
        refIhm.arrowUp();
    }

    @Override
    public void arrowDown() {
        refIhm.arrowDown();
    }

    @Override
    public void moveRightJoystick(double direction, double magnitudeDroite) {
        refIhm.moveRightJoystick(direction, magnitudeDroite);
    }

    @Override
    public void moveLeftJoystick(double direction, double magnitudeGauche) {
        refIhm.moveLeftJoystick(direction, magnitudeGauche);
    }

    @Override
    public void buttonAClicked() {
        refIhm.buttonAClicked();
    }

    public void startUp() {
        refWrk.startUp();
    }

    @Override
    public void login(String username, String password) {
        refWrk.login(username, password);
    }

    @Override
    public void setWebcamState(int state) {
        refIhm.setWebcamState(state);
    }

    @Override
    public void setControllable(int state) {
        refIhm.setControllable(state);
    }

    @Override
    public void loginOk(boolean ok) {
        refIhm.loginOk(ok);
    }

    @Override
    public void logout() {
        refWrk.logout();
    }

    @Override
    public void closeApplication() {
        refWrk.closeApplication();
        System.gc();
        System.exit(0);
    }

    @Override
    public void showError(String error) {
        refIhm.showError(error);
    }

    @Override
    public void retrieveRobotVideo(BufferedImage robotVideo) {
        refIhm.displayRobotVideo(robotVideo);
    }

    @Override
    public void retrieveSequence(ArrayList<Sequence> sequenceToDisplay) {
        refIhm.displaySequence(sequenceToDisplay);
    }

    @Override
    public void retrieveRobotPicture(BufferedImage robotPicture) {
        refIhm.displayRobotPicture(robotPicture);
    }

    @Override
    public void signIn(String username, String password) {
        refWrk.signIn(username, password);
    }

    @Override
    public void signInOk(boolean ok) {
        refIhm.signInOk(ok);
    }

    @Override
    public void playSequence(Sequence s) {
        refWrk.playSequence(s);
    }

    @Override
    public void createSequence(Sequence s) {
        refWrk.createSequence(s);
    }

    @Override
    public void retrieveCurrentAction(int currentAction) {
        refIhm.displayCurrentAction(currentAction);
    }

    @Override
    public void retrievePing(String p) {
        refIhm.displayPing(p);
    }

    @Override
    public void connect(String serverIp) {
        refWrk.connect(serverIp);
    }

    @Override
    public void updateServerIp(String ip) {
        refIhm.updateServerIp(ip);
    }

    @Override
    public void serverDisconnected() {
        refIhm.serverDisconnected();
    }

    @Override
    public void receiveColorWebcamFrame(BufferedImage bImage) {
        refIhm.displayColorWebcamFrame(bImage);
    }

    @Override
    public void updateActionZoneColor(int redActionZoneHeight, int greenActionZoneHeight, int blueActionZoneHeight, int yellowActionZoneHeight) {
        refIhm.updateActionZoneColor(redActionZoneHeight, greenActionZoneHeight, blueActionZoneHeight, yellowActionZoneHeight);
    }

    @Override
    public void setControlMode(boolean b) {
        refWrk.setControlMode(b);
    }

    @Override
    public void startMotionDetection(boolean start) {
        refWrk.startMotionDetection(start);
    }

    //Setter Getter
    public ItfIhmCtrl getRefIhm() {
        return refIhm;
    }

    public void setRefIhm(ItfIhmCtrl refIhm) {
        this.refIhm = refIhm;
    }

    public ItfWrkCtrl getRefWrk() {
        return refWrk;
    }

    public void setRefWrk(ItfWrkCtrl refWrk) {
        this.refWrk = refWrk;
    }

    @Override
    public void appendToConsole(String text) {
        refIhm.appendToConsole(text);
    }

}
