package app.wrk.robot;

import static app.consts.Constants.HOME_WIFI_PASSWORD;
import static app.consts.Constants.HOME_WIFI_SSID;
import static app.consts.Constants.ROBOT_IP_ADDRESS;
import static app.consts.Constants.ROBOT_RECONNECT_THREAD_SLEEPING_TIME;
import static app.consts.Constants.ROBOT_SAVE_FILE;
import app.ihm.panels.nodes.ConnectionState;
import app.utils.FileHandler;
import ch.emf.info.robot.links.Robot;
import ch.emf.info.robot.links.bean.RobotState;
import ch.emf.info.robot.links.bean.Wifi;
import ch.emf.info.robot.links.exception.UnreachableRobotException;

/**
 *
 * @author dingl01
 */
public class RobotWrk extends Thread {

    private final ItfWrkRobotWrk refWrk;

    private final Robot robot;
    private RobotData robotData;
    private final Wifi wifi;

    // This attribut is used to automatically reconnect to the robot when it's 
    // connection is lost.
    private boolean hasBeenConnectedOnce;

    private boolean freezed;

    private volatile boolean running;

    public RobotWrk(ItfWrkRobotWrk refWrk) {
        this.refWrk = refWrk;
        robot = new Robot();
        wifi = new Wifi();
        freezed = false;
    }

    /**
     * This method is executed by a new Thread when the application starts.
     * While the application is running, every fixed time, if the robot has been
     * connected once in the past, this Thread will check if the robot is still
     * connected. If it is not connected anymore, then it will try to reconnect.
     * This is required because for some reasons, sometimes the robot just
     * disconnects from the application.
     *
     */
    @Override
    public void run() {
        running = true;
        while (running) {
            if ((robot != null) && (!robot.isConnected()) && (hasBeenConnectedOnce)) {
                reconnectToTheRobotWithNewThread();
            }
            sleeep(ROBOT_RECONNECT_THREAD_SLEEPING_TIME);
        }
    }

    /**
     * Sleeps the current thread for a amount of time.
     *
     * @param ms an int corresponding to the amount of time to sleep
     */
    private void sleeep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
        }
    }

    /**
     * Connects to the robot in order to send him infos about the wifi to
     * connect to. Requires to be connected to his wifi.
     */
    public synchronized void initRobot() {
        refWrk.appendTextConsole("#w[ROBOT] #cConnecting to the robot using his wifi");
        try {

            // Setting up wifi infos
            wifi.setSsid(HOME_WIFI_SSID);
            wifi.setPassword(HOME_WIFI_PASSWORD);

            // Inits a connection to the robot (he needs to be connected to the same wifi)
            robot.startInitConnection();
            refWrk.appendTextConsole("#w[ROBOT] #gSuccessfully connected to the robot using his wifi");

            // Once connected to his wifi, send him data about the wifi to connect to
            robot.connectWifiSync(wifi);

            initRobotData();

            refWrk.appendTextConsole("#w[ROBOT] #cSending infos about wifi (" + HOME_WIFI_SSID + ")");
            refWrk.appendTextConsole("#w[ROBOT] #oWaiting for confirmation (\"I'm Ready\")");
        } catch (UnreachableRobotException ex) {
            refWrk.appendTextConsole("#w[ROBOT] #rError connecting to the robot using his wifi");
        }
    }

    /**
     * This method will try to connect to the robot if he's not already
     * connected. To init a connection, some data are required (ip address, ID
     * and PW). These data are known if the robot has been initialized during
     * this execution, otherwise, it needs to be read from a save file. Once
     * these data are known, it tries to connect to the robot. A message is
     * wrote to the console to warn the user of the state of the connection.
     *
     * If the connection is successfully created, the Id and Pw of the robot are
     * saved in a file.
     *
     * @return a booleann, true if the robot is connected, false otherwise.
     */
    public synchronized boolean connectRobot() {
        if (robot.isConnected()) {
            return true;
        }

        boolean connected = false;
        refWrk.appendTextConsole("#w[ROBOT] #cConnecting to the robot");

        try {
            if (!initRobotData()) {
                return false;
            }
            robot.connect(ROBOT_IP_ADDRESS, robotData.getId(), robotData.getPw());
            if (robot.isConnected()) {
                refWrk.appendTextConsole("#w[ROBOT] #gRobot connected");
                hasBeenConnectedOnce = true;
                connected = true;
                saveRobotData();
            } else {
                refWrk.appendTextConsole("#w[ROBOT] #rError connecting to the robot");
            }
        } catch (UnreachableRobotException ex) {
            refWrk.appendTextConsole("#w[ROBOT] #rError connecting to the robot");
        }
        if (connected) {
            refWrk.setRobotConnected(ConnectionState.CONNECTED);
        } else {
            refWrk.setRobotConnected(ConnectionState.DISCONNECTED);
        }
        return connected;
    }

    /**
     * Tries to get informations about the robot in order to connect to it. If
     * the robot has been initialized during this execution, then it uses the
     * current informations. If the there is a robot save file, then it uses the
     * save file. If there is none of them, warn the user that it is not
     * possible to connect to the robot.
     *
     * @return a boolean, true if data has been found, false if there was none.
     */
    private boolean initRobotData() {
        boolean robotDataFound = true;
        if (robot.getRobotState().getName().equals("unknown")) {
            // Robot wasn't initialized in this run, so get it from a saved file if there is one
            if (FileHandler.fileExists(ROBOT_SAVE_FILE)) {
                robotData = (RobotData) FileHandler.readObjectFromFile(ROBOT_SAVE_FILE);
            }

            // null if the file wasn't read correctly or it didn't contain correct data
            // or null if the file didn't exists.
            if (robotData == null) {
                refWrk.appendTextConsole("#w[ROBOT] #oRobotData required to connect to the robot :");
                refWrk.appendTextConsole(" - Either initialize the robot now");
                refWrk.appendTextConsole(" - Or use a correct save file");
                robotDataFound = false;
            }
        } else {
            // Robot was initilized recently in this run
            robotData = new RobotData("0.0.0.0", robot.getRobotState().getName(), robot.getRobotState().getId(), robot.getRobotState().getPw());
        }
        return robotDataFound;
    }

    /**
     * Creates or overrides a file and serialises the current informations about
     * the robot in it. This file is later read to connect to the robot.
     */
    public void saveRobotData() {
        FileHandler.writeObjectToFile(ROBOT_SAVE_FILE, robotData);
    }

    /**
     * Creates a new thread that will try to reconnect to the robot.
     */
    private synchronized void reconnectToTheRobotWithNewThread() {
        new Thread(() -> {
            connectRobot();
        }, "Reconnecting to the robot").start();
    }

    public void setFreezed(boolean freezed) {
        this.freezed = freezed;
    }

    public void setRightSpeed(short speed) {
        robot.setRightSpeed(speed);
    }

    public void setLeftSpeed(short speed) {
        robot.setLeftSpeed(speed);
    }

    public void setHeadDirection(RobotState.HeadDirection direction) {
        robot.setHeadDirection(direction);
    }

    public void dock() {
        robot.dock();
    }

    public void unDock() {
        robot.undock();
    }

    public byte getBatteryLvl() {
        return robot.getBatteryLevel();
    }

    public byte[] getLastImage() {
        return robot.getLastImage();
    }

    public boolean isConnected() {
        return robot.isConnected();
    }

    public boolean disconnect() {
        return robot.disconnect();
    }

    public boolean isFreezed() {
        return freezed;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
