package wrk;

import beans.ActionSequence;
import beans.Sequence;
import static constants.Constants.MAX_ROBOT_SPEED;
import ctrl.ItfCtrlWrk;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import wrk.client.PacketTypes;
import wrk.client.WrkClient;
import wrk.utils.Encrypt;
import wrk.utils.FileUtilies;
import wrk.utils.ImageHandler;
import wrk.webcam.ColorMotion;
import wrk.webcam.LoginWebcam;
import wrk.xbox.Xbox;

public class Wrk implements ItfWrkCtrl, ItfWrkWebCam, ItfWrkXbox, ItfWrkColorMotion, ItfWrkWrkClient {

    private ItfCtrlWrk refCtrl;
    private Xbox xb;
    private LoginWebcam webcam;
    private ColorMotion colorMotionWebcam;

    private WrkClient wrkClient;
    private double leftTrackSpeed = 0;
    private double rightTrackSpeed = 0;
    private boolean doNotMoveXbox = false;
    private boolean isMotionDetectionStarted = false;
    private boolean pauseRobotVideo = false;

    /**
     * This method is used to start the application. It init all the workers
     */
    @Override
    public void startUp() {
        wrkClient = new WrkClient(this);
        connectXboxController(true);
        refCtrl.setWebcamState(-1);
        initWebcam();
        initColorMotionWebcam();

    }

    /**
     * Disconnects the client tcp.
     */
    public void disconnectFormServer() {
        wrkClient.disconnectTcp();
    }

    /**
     * Inits the Login Webcam
     *
     * @return true if the webcam has correctly been created or false if not
     */
    public boolean initWebcam() {
        boolean ok = true;
        webcam = new LoginWebcam(600, 600, this);
        webcam.start();
        return ok;
    }

    /**
     * Pauses the Login Webcam
     *
     * @return boolean, return true if the webcam is paused correctly or false
     * if not
     */
    public boolean pauseWebcam() {
        boolean ok = false;
        if (webcam != null) {
            webcam.setPaused(!webcam.isPaused());
            ok = true;
        }
        return ok;
    }

    /**
     * Pauses the Login Webcam
     *
     * @return boolean, return true if the webcam has been stopped correctly or
     * false if not
     */
    public boolean stopWebcam() {
        boolean ok = false;
        if (webcam != null) {
            webcam.setReading(false);
            try {
                webcam.join();
                webcam = null;
                ok = true;
            } catch (InterruptedException ex) {
                System.out.println("Error stopping the webcam");
            }
        }
        return ok;
    }

    /**
     * Connects to the xbox controller
     *
     * @param connection, true to connect, false to disconnect
     * @return boolean, true if connected correcly or false if not
     */
    public boolean connectXboxController(boolean connection) {
        boolean retour = connection;
        if (connection) {
            xb = new Xbox(this);
            xb.connectController();
            retour = true;
        } else {
            if (xb != null) {
                xb.disconnect();
            }
            retour = false;
        }
        return retour;
    }

    /**
     * Receives image of the webcam.
     *
     * @param img2 the image to display on the Ihm
     */
    @Override
    public void receiveWebCamFrame(BufferedImage img2) {
        refCtrl.receiveWebCamFrame(img2);
    }

    /**
     * Displays the detected face
     *
     * @param img2 detected face
     */
    @Override
    public void faceDetected(BufferedImage img2) {
        refCtrl.faceDetected(img2);
    }

    /**
     * Sets detect face
     *
     * @param detectFace true if we want to detect face
     */
    @Override
    public void checkForFace(boolean detectFace) {
        webcam.setDetecteFace(detectFace);
    }

    //------------------ Xbox Controll Processing ------------------
    /**
     * Selects the previous sequence in the sequence list of the ihm.
     */
    @Override
    public void arrowUp() {
        refCtrl.arrowUp();
    }

    /**
     * Selects the next sequence in the sequence list of the ihm.
     */
    @Override
    public void arrowDown() {
        refCtrl.arrowDown();
    }

    /**
     * Calculates the postition of the robot head with the direction and
     * magnitude of the joystick. The head position is then send to the server.
     * This method is called when the left joystick is moved.
     *
     * @param direction the direction of the left joystick
     * @param magnitudeGauche the magnitude of the left joystick
     */
    @Override
    public void moveLeftJoystick(double direction, double magnitudeGauche) {
        if (!doNotMoveXbox) {
            refCtrl.moveLeftJoystick(direction, magnitudeGauche);
            String headPosition = "";
            // Head Up
            if (((direction >= 0 && direction <= 90) || (direction >= 270 && direction <= 360)) && magnitudeGauche >= 0.3) {
                headPosition = "UP";
            } //Head down
            else if ((direction >= 90 && direction <= 270) && magnitudeGauche >= 0.3) {
                headPosition = "DOWN";
            } // Head none
            else if (magnitudeGauche > 0.0 || magnitudeGauche < 0.3) {
                headPosition = "NONE";
            }

            wrkClient.tcpWrite(PacketTypes.MOVE_ROBOT_HEAD + headPosition);
        }
    }

    /**
     * Sends direction and magnitude to the controller to display the joystick
     * in the Ihm. It sends the left and right track speed to the server.This
     * method is called when the right joystick is moved.
     *
     * @param direction the direction of the right joystick
     * @param magnitude the magnitude of the right joystick
     */
    @Override
    public void moveRightJoystick(double direction, double magnitude) {
        if (!doNotMoveXbox) {
            refCtrl.moveRightJoystick(direction, magnitude);
            double newSpeedLeft = throttleAngleToThrust(magnitude, direction)[0] * MAX_ROBOT_SPEED;
            double newSpeedRight = throttleAngleToThrust(magnitude, direction)[1] * MAX_ROBOT_SPEED;
            if ((newSpeedLeft > leftTrackSpeed + 10 || newSpeedLeft > leftTrackSpeed - 10) || (newSpeedLeft < leftTrackSpeed + 10 || newSpeedLeft < leftTrackSpeed - 10)) {
                leftTrackSpeed = newSpeedLeft;
                if ((newSpeedRight > rightTrackSpeed + 10 || newSpeedRight > rightTrackSpeed - 10) || (newSpeedRight < rightTrackSpeed + 10 || newSpeedRight < rightTrackSpeed - 10)) {
                    rightTrackSpeed = newSpeedRight;
                    wrkClient.tcpWrite(PacketTypes.MOVE_ROBOT_BODY + "" + (int) (leftTrackSpeed) + ";" + (int) (rightTrackSpeed));
                }
            }
        }
    }

    /**
     * Tranforms datas from joytick to left and right speed
     *
     * @param r , magnitude of the joystick
     * @param theta, angle of the joystick
     * @return [leftSpeed,rightSpeed]
     */
    public double[] throttleAngleToThrust(double r, double theta) {
        if (r >= Double.MAX_VALUE) {
            r = 1;
        }
        if (r <= Double.MIN_VALUE) {
            r = -1;
        }

        if (theta >= Double.MAX_VALUE || theta <= Double.MIN_VALUE) {
            theta = 0;
        }
        theta = theta * -1.0 + 90.0;
        if (theta < 0) {
            theta += 360;
        }
        double x = Math.cos(Math.toRadians(theta)) * r;
        double y = Math.sin(Math.toRadians(theta)) * r;
        double left = (x * Math.sqrt(2.0) / 2.0 + y * Math.sqrt(2.0) / 2.0) * 1.4;
        double right = (-x * Math.sqrt(2.0) / 2.0 + y * Math.sqrt(2.0) / 2.0) * 1.4;

        return new double[]{left, right};
    }

    /**
     * Takes picture with the robot.
     */
    @Override
    public void buttonYClicked() {
        if (!doNotMoveXbox) {
            wrkClient.tcpWrite(PacketTypes.TAKE_PICTURE_ROBOT.name());
            appendToConsole("[CLIENT] #cTake Picture");
        }
    }

    @Override
    public void buttonXClicked() {
        if (!doNotMoveXbox) {
        }

    }

    /**
     * Plays the selected sequence in the sequence list contained in the ihm.
     */
    @Override
    public void buttonAClicked() {
        if (!doNotMoveXbox) {
            refCtrl.buttonAClicked();
        }
    }

    /**
     * Freeze or unfreeze the robot video.
     */
    @Override
    public void buttonBClicked() {
        if (!doNotMoveXbox) {
            pauseRobotVideo = !pauseRobotVideo;
            if (pauseRobotVideo) {
                appendToConsole("[CLIENT] #wFreeze robot video");
            } else {
                appendToConsole("[CLIENT] #wUnfreeze robot video");
            }
        }
    }

    /**
     * Login with the username and password. The username and the encrypted
     * password are sent to server to check the credentials
     *
     * @param username the username to login with
     * @param password the password to login with
     */
    @Override
    public void login(String username, String password) {
        String encrpytedPassword = Encrypt.toHexString(Encrypt.getSHA(password));
        appendToConsole("[CLIENT] #cLogin with : " + username);
        wrkClient.tcpWrite(PacketTypes.LOGIN_CREDENTIALS + username + ";" + encrpytedPassword);
    }

    /**
     * Creates user with a username and password. The username and the encrypted
     * password are sent to the server to create the record in the database
     *
     * @param username the username to sign in with
     * @param password the password to sign in with
     */
    @Override
    public void signIn(String username, String password) {
        appendToConsole("[CLIENT] #cCreate user : " + username);
        String encrpytedPassword = Encrypt.toHexString(Encrypt.getSHA(password));
        wrkClient.tcpWrite(PacketTypes.SIGN_IN + username + ";" + encrpytedPassword);
    }

    /**
     * Sets the state of the webcam ndicator
     *
     * @param ok if true the indicator is green and if false the indicator is
     * red
     */
    @Override
    public void setWebcamState(boolean ok) {
        refCtrl.setWebcamState(ok == true ? 1 : 0);
    }

    /**
     * Sets the state of the controllable indicator
     *
     * @param ok if true the indicator is green and if false the indicator is
     * red
     */
    @Override
    public void setControllable(boolean ok) {
        refCtrl.setControllable(ok == true ? 1 : 0);
    }

    /**
     * Closes the application safely. The method close all threads by setting
     * the main loop to false and then waiting on the end of their tasks
     */
    @Override
    public void closeApplication() {
        //Client disconnection
        wrkClient.shutdownTcpUdp();

        //Webcam disconnection
        webcam.setPaused(false);
        webcam.setRunning(false);
        webcam.setReading(false);
        try {
            webcam.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Wrk.class.getName()).log(Level.SEVERE, null, ex);
        }
        webcam = null;
        //Controller disconnection
        xb.disconnect();
        xb = null;
        System.gc();
    }

    /**
     * Show popup error.
     *
     * @param error the error to display
     */
    public void showError(String error) {
        refCtrl.showError(error);
    }

    /**
     * Check if the login was OK and then send an image of the user and ask the
     * sequence list. The client window is displayed if the login was OK.This
     * method is called when a LOGIN_CREDENTIALS packet is received.
     *
     * @param msg OK or KO
     */
    @Override
    public void loginOk(String msg) {
        boolean ok = false;
        if (msg.equalsIgnoreCase("OK")) {
            appendToConsole("[SERVER] #gLogin OK");
            ok = true;
            webcam.setDetecteFace(false);
            webcam.setRunning(false);
            wrkClient.sendPing();
            wrkClient.tcpWrite(PacketTypes.LOGIN_USER_FACE + ImageHandler.imageToString(webcam.getUserFace()));
            wrkClient.tcpWrite(PacketTypes.SEQUENCE_LIST.name());
        }
        refCtrl.loginOk(ok);

    }

    /**
     * Check if the sign was OK. This method is called when a SIGN_IN packet is
     * received.
     *
     * @param msg OK or KO
     */
    @Override
    public void signInOk(String msg) {
        refCtrl.signInOk(msg.equalsIgnoreCase("OK"));
    }

    /**
     * Sets controllable indicator to red.
     */
    @Override
    public void freezeRobot() {
        appendToConsole("[SERVER] #gFreeze robot");
        refCtrl.setControllable(0);
    }

    /**
     * Sets controllable indicator to green.
     */
    @Override
    public void unfreezeRobot() {
        appendToConsole("[SERVER] #gUnfreeze robot");
        refCtrl.setControllable(1);
    }

    /**
     * Retrieves the image and save it in the user images directory. This method
     * is called when a TAKE_PICTURE_ROBOT packet is received
     *
     * @param robotImg the robot picture
     */
    @Override
    public void retrieveRobotPicture(String robotImg) {
        BufferedImage robotPicture = null;
        robotPicture = ImageHandler.stringToImage(robotImg);
        FileUtilies.saveImage(robotPicture);
        refCtrl.retrieveRobotPicture(robotPicture);

    }

    /**
     * Retrieves the webcamThis method play selected sequence in the sequence
     * contained in the ihm frame and send it to the controller to display it on
     * the ihm. This method is called when a ROBOT_VIEW packet is received
     *
     * @param robotVideo the robot video
     */
    @Override
    public void retrieveRobotVideo(String robotVideo) {
        if (!pauseRobotVideo) {
            BufferedImage robotVideo1 = null;
            robotVideo1 = ImageHandler.stringToImage(robotVideo);
            refCtrl.retrieveRobotVideo(robotVideo1);
        }

    }

    /**
     * Sends the ping to the controller to display it on the ihm Another ping is
     * then sent to the server. This method is called when a PING packet is
     * received
     *
     * @param pingMsg the ping
     */
    @Override
    public void retrievePing(String pingMsg) {
        long ping = System.currentTimeMillis() - Long.parseLong(pingMsg);
        String p = ping + " ms";
        refCtrl.retrievePing(p);
        wrkClient.sendPing();
    }

    /**
     *
     * Retreives all the related user sequences. This method is called when a
     * SEQUENCE_LIST packet is received
     *
     * @param sequence all sequences related to the user
     */
    @Override
    public void retrieveSequence(String sequence) {
        ArrayList<Sequence> sequences = parseSequence(sequence);
        for (Sequence s : sequences) {
            appendToConsole("[SERVER] #gReceived sequence : #w" + s.getName());
        }
        refCtrl.retrieveSequence(sequences);
    }

    public ArrayList<Sequence> parseSequence(String sequence) {
        String[] allSequence = sequence.split("\\|");
        ArrayList<Sequence> sequenceToDisplay = new ArrayList<>();
        for (String s1 : allSequence) {
            String name = s1.split("\\[")[0];
            String s = s1.split("\\[")[1];
            s = s.substring(0, s.length() - 1);
            ArrayList<ActionSequence> allActions = new ArrayList<>();
            for (String action : s.split(";")) {
                String actionName = action.split(",")[0];
                String delay = action.split(",")[1];

                allActions.add(new ActionSequence(actionName, Integer.parseInt(delay)));
            }
            sequenceToDisplay.add(new Sequence(name, allActions));

        }
        return sequenceToDisplay;
    }

    /**
     * Shows a popup if the server is full. This method is called when a
     * SERVER_FULL packet is received
     */
    @Override
    public void serverFull() {
        showError("Server Full");
    }

    /**
     * Plays the sequence that is selected in the ihm.
     *
     * @param sequenceToPlay the sequence to play
     */
    @Override
    public void playSequence(Sequence sequenceToPlay) {
        appendToConsole("[CLIENT] #cPlay sequence : " + sequenceToPlay.getName());
        wrkClient.tcpWrite(PacketTypes.READ_SEQUENCE + sequenceToPlay.sequenceToTxt());
    }

    /**
     * Add a new sequence in the database.
     *
     * @param sequenceToAdd the sequence to add
     */
    @Override
    public void createSequence(Sequence sequenceToAdd) {
        appendToConsole("[CLIENT] #cAdd sequence : " + sequenceToAdd.getName());
        wrkClient.tcpWrite(PacketTypes.ADD_SEQUENCE + sequenceToAdd.sequenceToTxt());
    }

    /**
     * Shows on the ihm the current action playing in the sequence. This method
     * is called when a CURRENT_ACTION packet is received
     *
     * @param currentAction the current action in the sequence
     */
    @Override
    public void retrieveCurrentAction(String currentAction) {
        int currentActionPlaying = Integer.parseInt(currentAction);
        refCtrl.retrieveCurrentAction(currentActionPlaying);
    }

    /**
     * Connects to the server with TCP.
     *
     * @param serverIp the server ip address
     */
    @Override
    public void connect(String serverIp) {
        wrkClient.connectTcp(serverIp);
    }

    /**
     * Disconnects the current connected user, disconnect tcp and udp, redisplay
     * the login webcam.
     */
    @Override
    public void logout() {
        webcam = new LoginWebcam(600, 600, this);
        webcam.start();
        wrkClient.disconnectTcp();
        wrkClient.disconnectUdp();

    }

    /**
     * Updates the server ip field in the ihm by inserting the new ip address
     *
     * @param ip server ip address
     */
    @Override
    public void updateServerIp(String ip) {
        refCtrl.updateServerIp(ip);
    }

    /**
     * Disconnects all tcp and ucp connection, it also stop the ping and action
     * executor.
     */
    @Override
    public void serverDisconnected() {
        refCtrl.serverDisconnected();
    }

    /**
     * Sends the colorMotionWebcam frame to the controller to display it on the
     * ihm.
     *
     * @param bImage image of the color motion webcam
     */
    @Override
    public void receiveColorWebcamFrame(BufferedImage bImage) {
        if (isMotionDetectionStarted) {
            if (doNotMoveXbox) {
                refCtrl.receiveColorWebcamFrame(bImage);
            }
        }

    }

    /**
     * Moves the robot using the webcamColorMotion and display the detected
     * color on the ihm.
     *
     * @param redActionZoneHeight speed of the track by red
     * @param greenActionZoneHeight speed of the track by green
     * @param blueActionZoneHeight speed of the track by blue
     * @param yellowActionZoneHeight speed of the track by yellow
     */
    @Override
    public void updateActionZoneColor(int redActionZoneHeight, int greenActionZoneHeight, int blueActionZoneHeight, int yellowActionZoneHeight) {
        if (isMotionDetectionStarted) {

            if (doNotMoveXbox) {
                int right = 0, left = 0;
                if (redActionZoneHeight > 0) {
                    right = redActionZoneHeight;
                    left = redActionZoneHeight;
                } else if (greenActionZoneHeight > 0) {
                    right = -greenActionZoneHeight;
                    left = -greenActionZoneHeight;
                }
                if (blueActionZoneHeight > 0) {
                    right = (right == 0) ? (MAX_ROBOT_SPEED) : (right);
                    left = (left != 0) ? (left / 2) : (left);
                } else if (yellowActionZoneHeight > 0) {
                    left = (left == 0) ? (MAX_ROBOT_SPEED) : (left);
                    right = (right != 0) ? (right / 2) : (right);
                }
                wrkClient.tcpWrite(PacketTypes.MOVE_ROBOT_BODY.name() + left + ";" + right);
                refCtrl.updateActionZoneColor(redActionZoneHeight, greenActionZoneHeight, blueActionZoneHeight, yellowActionZoneHeight);
            }
        }
    }

    /**
     * Creates the colorMotionWebcam.
     */
    private void initColorMotionWebcam() {
        colorMotionWebcam = new ColorMotion(this);
        colorMotionWebcam.start();
    }

    /**
     * Sets which control mode is currently used.
     *
     * @param xbox set to true to control the with xbox and false to control
     * with the webcam
     */
    @Override
    public void setControlMode(boolean xbox) {
        doNotMoveXbox = !xbox;
    }

    /**
     * Start or Stop the color motion detection.
     *
     * @param start set to true to start and false to stop
     */
    @Override
    public void startMotionDetection(boolean start) {
        isMotionDetectionStarted = start;
        if (!start) {
            wrkClient.tcpWrite(PacketTypes.MOVE_ROBOT_BODY + "0;0");
        }
    }

    /**
     * Append text to the console
     *
     * @param text the text to add
     */
    public void appendToConsole(String text) {
        refCtrl.appendToConsole(text);
    }

    //Setter Getter 
    public ItfCtrlWrk getRefCtrl() {
        return refCtrl;
    }

    public void setRefCtrl(ItfCtrlWrk refCtrl) {
        this.refCtrl = refCtrl;
    }

}
