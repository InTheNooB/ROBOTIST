package app.wrk;

import app.beans.Log;
import app.beans.Sequence;
import app.beans.User;
import static app.consts.Constants.ROBOT_IMAGE_SEND_DELAY;
import static app.consts.Constants.ROBOT_VIEW_SIZE;
import app.ctrl.ItfCtrlWrk;
import app.exception.MyDBException;
import app.utils.DateHandler;
import app.utils.ConversionHandler;
import app.utils.SequenceFileHandler;
import app.wrk.database.ItfWrkDatabaseWrk;
import app.wrk.database.WrkDatabase;
import app.wrk.robot.RobotWrk;
import app.wrk.server.ServerWrk;
import java.util.List;
import app.wrk.robot.ItfWrkRobotWrk;
import app.wrk.robot.sequence.ItfWrkSequenceExecutor;
import app.wrk.robot.sequence.SequenceExecutor;
import app.wrk.server.Client;
import app.wrk.server.ItfWrkServerWrk;
import app.consts.PacketTypes;
import app.ihm.panels.nodes.ConnectionState;
import ch.emf.info.robot.links.bean.RobotState.HeadDirection;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;

public class Wrk extends Thread implements ItfWrkCtrl, ItfWrkServerWrk, ItfWrkRobotWrk, ItfWrkSequenceExecutor {

    // Associations
    private ItfCtrlWrk refCtrl;
    private ServerWrk serverWrk;
    private final RobotWrk robotWrk;
    private final ItfWrkDatabaseWrk databaseWrk;

    // Robot
    private Thread robotSetupThread;

    // Currently Logged user (null if no user)
    private Log currentSessionLog;

    // Main Loop
    private boolean running;

    public Wrk() {
        super("Thread Image UDP");
        databaseWrk = new WrkDatabase();
        robotWrk = new RobotWrk(this);
        robotWrk.start();
        currentSessionLog = null;
    }

    /**
     * This thread will update the "currentSessionTime" on the ihm every 500ms
     * and send image from the robot to the client if a client is logged in.
     */
    @Override
    public void run() {
        running = true;
        long lastUpdate = 0;
        long updateDelay = 500;

        while (running) {
            // Update the currentSessionTime
            if (serverWrk != null) {
                if (System.currentTimeMillis() - lastUpdate >= updateDelay) {
                    lastUpdate = System.currentTimeMillis();
                    refCtrl.setCurrentSessionTime(serverWrk.getCurrentSessionTime());
                }

                // Send image from robot to client
                if ((serverWrk != null) && (robotWrk != null) && (robotWrk.isConnected())) {
                    // If a client is logged in
                    if ((currentSessionLog != null) && (currentSessionLog.getFkUser() != null)) {
                        try {
                            serverWrk.sendRobotView(
                                    ConversionHandler.scaleImage(
                                            robotWrk.getLastImage(),
                                            ROBOT_VIEW_SIZE, ROBOT_VIEW_SIZE));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }

            try {
                Thread.sleep(ROBOT_IMAGE_SEND_DELAY);
            } catch (InterruptedException ex) {
            }
        }
    }

    
    
    /**
     * This method safely stops every running thread and disconnects from the
     * client/robot/database.
     */
    @Override
    public void exitApplication() {
        running = false;
        try {
            this.join();
        } catch (InterruptedException ex) {
        }
        if ((currentSessionLog != null) && (currentSessionLog.getFkUser() != null)) {
            disconnectClient(serverWrk.getConnectedClient(), true);
        }
        disconnectDatabase();
        stopServer();
        disconnectRobot();
    }

    // ----- Server ----- //
    /**
     * Starts the server and launches a new thread that will listen for incoming
     * request.
     *
     * @return True if the server has started successfully, false otherwise.
     */
    @Override
    public boolean startServer() {
        appendTextConsole("#w[SERVER] #cStarting the server");
        serverWrk = new ServerWrk(this);
        return serverWrk.startServer();
    }

    /**
     * Stops the server by killing every thread
     */
    @Override
    public void stopServer() {
        try {
            serverWrk.stopServer();
        } catch (InterruptedException ex) {
            appendTextConsole("#w[SERVER] #rError stopping the server");
        }
    }

    public void setClientLoggedIn(ConnectionState i) {
        refCtrl.setClientLoggedIn(i);
    }

    @Override
    public void setRobotConnected(ConnectionState i) {
        refCtrl.setRobotConnected(i);
    }

    public void setDatabaseConnected(ConnectionState i) {
        refCtrl.setDatabaseConnected(i);
    }

    /**
     * This methods processes a received message containing an image (the face
     * of the client trying to log in). If the message is not null or empty if
     * there is a logged client, it convert the message to get the image and
     * updates the database and ihm accordingly.
     *
     * @param msg A String, The message containing the image.
     */
    @Override
    public void processLoggedInUserFace(String msg) {
        if ((msg == null) || (msg.isEmpty())) {
            return;
        }

        if (currentSessionLog == null) {
            // Client isn't logged in
            return;
        }

        // convert byte[] to a BufferedImage
        byte[] bytes = Base64.getDecoder().decode(msg);
        BufferedImage image;
        try {
            image = ConversionHandler.bytesToImage(bytes);
            refCtrl.setLoggedUserFace(image);
            currentSessionLog.setPicture(bytes);
        } catch (IOException ex) {
            appendTextConsole("#w[SERVER] #rError receiving image (face) from client");
        }
    }

    /**
     * Disconnects a specified client. If the boolean is true, that means we are
     * disconnecting the logged client, then it first logs him out. Once logged
     * out, the Ihm is updated.
     *
     * @param client The client that needs to be disconnected
     * @param isLoggedClient A boolean defining if the client that needs to be
     * disconnected is the logged one.
     */
    @Override
    public void disconnectClient(Client client, boolean isLoggedClient) {
        if ((currentSessionLog != null) && (isLoggedClient)) {
            logOutClient(client);
        }
        String hostAddress = client.getSocket().getInetAddress().getHostAddress();
        appendTextConsole("#w[SERVER] #oClient disconnected (" + hostAddress + ")");
        setClientLoggedIn(ConnectionState.LOADING);
        refCtrl.setLoggedUserFace(null);
    }

    /**
     * Logs out the logged client (if there is one) by adding a new LOG entry in
     * the database and setting the current logged client to null.
     *
     * @param client The client that is currently logged in.
     */
    private void logOutClient(Client client) {
        if ((currentSessionLog == null) || (currentSessionLog.getFkUser() == null)) {
            return;
        }
        Log l = new Log();
        l.setFkUser(currentSessionLog.getFkUser());
        l.setStartTime(DateHandler.timeMillisToDate(client.getStartTime()));
        l.setEndTime(DateHandler.timeMillisToDate(System.currentTimeMillis()));
        l.setPicture(
                (currentSessionLog.getPicture() == null)
                ? (new byte[2])
                : (currentSessionLog.getPicture()));
        try {
            databaseWrk.addLog(l);
            refCtrl.setIhmLogList(databaseWrk.readLogs());
            appendTextConsole("#w[DATABASE] #cSuccessfully added new log to the database");
        } catch (MyDBException ex) {
            appendTextConsole("#w[DATABASE] #oError adding log to the database");
        }
        currentSessionLog = null;
    }

    // ***** Server ***** //
    // ----- Robot ----- //
    /**
     * Freezes the robot by setting every of it's movement to 0 and sends a
     * message to the logged client if there's one to warn him he can't control
     * the robot anymore.
     */
    @Override
    public void freezeRobot() {
        robotWrk.setFreezed(true);
        robotWrk.setLeftSpeed((short) 0);
        robotWrk.setRightSpeed((short) 0);
        robotWrk.setHeadDirection(HeadDirection.NONE);
        if ((currentSessionLog != null) && (currentSessionLog.getFkUser() != null)) {
            // If a user is connected and logged in
            serverWrk.sendFreezeRobot();
        }
    }

    /**
     * Unfreezes the robot by sending a message to the logged client if there's
     * one to warn him he can control the robot.
     */
    @Override
    public void unfreezeRobot() {
        robotWrk.setFreezed(false);
        if ((currentSessionLog != null) && (currentSessionLog.getFkUser() != null)) {
            // If a user is connected and logged in
            serverWrk.sendUnfreezeRobot();
        }
    }

    /**
     * Connects to the robot by asking the robotWrk to do so using a new Thread
     * to prevent the ihm to freeze. Updates the ihm accordingly.
     */
    @Override
    public void connectRobot() {
        if (robotSetupThread != null) {
            try {
                robotSetupThread.join();
            } catch (InterruptedException ex) {
            }
        }
        robotSetupThread = new Thread(() -> {
            refCtrl.disableRobotButtons();
            setRobotConnected(ConnectionState.LOADING);
            setRobotConnected((robotWrk.connectRobot()) ? (ConnectionState.CONNECTED) : (ConnectionState.DISCONNECTED));
            refCtrl.enableRobotButtons();
        });
        robotSetupThread.start();
    }

    /**
     * Inits the robot by asking the robotWrk to do so using a new Thread to
     * prevent the ihm to freeze.
     */
    @Override
    public void initRobot() {
        if (robotSetupThread != null) {
            try {
                robotSetupThread.join();
            } catch (InterruptedException ex) {
            }
        }
        robotSetupThread = new Thread(() -> {
            refCtrl.disableRobotButtons();
            robotWrk.initRobot();
            refCtrl.enableRobotButtons();
        });
        robotSetupThread.start();
    }

    /**
     * Disconnects from the robot by asking the robotWrk to do so.
     */
    private void disconnectRobot() {
        // Stop init thread
        if (robotSetupThread != null) {
            try {
                robotSetupThread.join();
            } catch (InterruptedException ex) {
            }
            robotSetupThread = null;
        }
        
        // Stop looping thread
        robotWrk.setRunning(false);
        try {
            robotWrk.join();
        } catch (InterruptedException e) {
        }
        robotWrk.disconnect();
    }

    /**
     * If the robot isn't freezed, get his last taken image and asks the
     * serverWrk to send it to the logged client.
     */
    @Override
    public void takeAndSendRobotPicture() {
        if (robotWrk.isFreezed()) {
            return;
        }
        byte[] image = robotWrk.getLastImage();
        serverWrk.takeAndSendRobotPicture(image);
    }

    /**
     * If the robot isn't freezed, processes a message to extract value about
     * the movement of the robot's head. Does so if the msg is no null or empty.
     * If the message has correctly been processed, updates the robot with the
     * new head movement.
     *
     * @param msg The received message containing the head movement.
     */
    @Override
    public void moveRobotHead(String msg) {
        if (robotWrk.isFreezed()) {
            return;
        }
        if ((msg != null) && (!msg.isEmpty())) {
            try {
                HeadDirection direction = HeadDirection.valueOf(msg);
                robotWrk.setHeadDirection(direction);
                return;
            } catch (NumberFormatException e) {
            }
        }
        appendTextConsole("#w[SERVER] #mUnreadable #wMOVE_ROBOT_HEAD #mpacket received");
    }

    /**
     * If the robot isn't freezed, processes a message to extract value about
     * the movement of the robot's body. Does so if the msg is no null or empty.
     * If the message has correctly been processed, updates the robot with the
     * new body movement.
     *
     * @param msg The received message containing the body movement.
     */
    @Override
    public void moveRobot(String msg) {
        if (robotWrk.isFreezed() || (currentSessionLog == null)) {
            return;
        }
        if ((msg != null) && (!msg.isEmpty())) {
            String[] data = msg.split(";");
            if ((data != null) && (data.length == 2)) {
                try {
                    short leftSpeed = Short.parseShort(data[0]);
                    short rightSpeed = Short.parseShort(data[1]);
                    robotWrk.setLeftSpeed(leftSpeed);
                    robotWrk.setRightSpeed(rightSpeed);
                    return;
                } catch (NumberFormatException e) {
                }
            }
        }
        appendTextConsole("#w[SERVER] #mUnreadable #wMOVE_ROBOT #mpacket received");
    }

    /**
     * If the robot isn't freezed and the msg is not null or empty, tries to
     * extract data about a sequence from the msg. If everything has been
     * processed correctly, check if the sequence syntax is correct and start a
     * new Thread to execute it.
     *
     * @param msg The message containing the sequence to execute.
     */
    @Override
    public void readSequence(String msg) {
        if (robotWrk.isFreezed()) {
            return;
        }
        if ((msg != null) && (!msg.isEmpty())) {
            String[] data = msg.split("\\[");
            if ((data != null) && (data.length == 2)) {
                String sequenceName = data[0];
                msg = msg.substring(sequenceName.length());
                if (SequenceFileHandler.checkSequenceFileSyntax(msg)) {
                    // The syntax is correct, create an executor and start it.
                    // Also, freeze the robot so that the client can't override a movement
                    // (That means that when executing an action, it is impossible to stop it)
                    robotWrk.setFreezed(true);
                    SequenceExecutor se = new SequenceExecutor(msg, sequenceName, robotWrk, this, serverWrk);
                    se.start();
                }
            }
        }

    }

    // ***** Robot ***** //
    // ----- Database ----- //
    /**
     * Tries to connect the server to the database. Displays a message on the
     * console depending on the connection state, success or error
     *
     * @return True if there the server is connected, false otherwise.
     */
    @Override
    public boolean connectDatabase() {
        setDatabaseConnected(ConnectionState.LOADING);
        refCtrl.appendTextConsole("#w[DATABASE] #cConnecting to the database");
        boolean connected = false;
        try {
            databaseWrk.connectDatabase();
            connected = databaseWrk.isConnected();
        } catch (MyDBException e) {
        }
        if (connected == false) {
            refCtrl.appendTextConsole("#w[DATABASE] #rError connecting to the database");
        } else {
            refCtrl.appendTextConsole("#w[DATABASE] #gConnected to the database");
        }
        return connected;
    }

    /**
     * Disconnects from the database by asking the <b>DatabaseWrk</b> to do so.
     */
    @Override
    public void disconnectDatabase() {
        databaseWrk.disconnect();
    }

    /**
     * Convert a received message into credentials and tries to match it in the
     * database. Update the IHM and the client and warn the client depending on
     * the login result.
     *
     * @param msg The received message from the client
     * @param client The client that sent the message
     */
    @Override
    public void loginUser(String msg, Client client) {
        if ((msg == null) || (msg.isEmpty())) {
            appendTextConsole("#w[SERVER] #mUnreadable #wLOGIN #mpacket received");
            return;
        }
        String[] data = msg.split(";");
        if ((data == null) || (data.length != 2)) {
            appendTextConsole("#w[SERVER] #mUnreadable #wLOGIN #mpacket received");
            return;
        }
        String username = data[0];
        String password = data[1];
        User user = null;

        List<User> users = getUserList();
        if (users != null) {
            for (User u : users) {
                if (u.getUsername().equals(username)) {
                    if (u.getPassword().equals(password)) {
                        user = u;
                        break;
                    }
                }
            }
        }
        if (user != null) {
            acknowledgeLogin(user, client);
        } else {
            refuseLogin(client, username);
        }
    }

    /**
     * Acknowledge a new login from the connected client. Updates the IHM and
     * the client object and sends a confirmation message to the client
     *
     * @param user The connected database user
     * @param client The connected client object
     */
    private void acknowledgeLogin(User user, Client client) {
        appendTextConsole("#w[SERVER] \"" + user.getUsername() + "\" #gsuccessfully logged in");
        client.write(PacketTypes.LOGIN_CREDENTIALS + "OK");
        setClientLoggedIn(ConnectionState.CONNECTED);
        client.setStartTime(System.currentTimeMillis());
        currentSessionLog = new Log();
        currentSessionLog.setFkUser(user);
    }

    /**
     * Refuses a login attempt from the connected client. Writes a message in
     * the console and sends a message to prevent the client that his attempt
     * has been refused
     *
     * @param client The connected client
     * @param username The username used in the attempt.
     */
    private void refuseLogin(Client client, String username) {
        appendTextConsole("#w[SERVER] #oUnsuccessfully login #w\"" + username + "\"");
        client.write(PacketTypes.LOGIN_CREDENTIALS + "KO");
    }

    /**
     * Creates a new user in the database using credentials extracted from the
     * message received from a client.
     *
     * @param msg The received message from the client
     * @param client The client that sent the message
     */
    @Override
    public void signInUser(String msg, Client client) {
        String[] data = msg.split(";");
        String username = data[0];
        String password = data[1];
        if (addUser(username, password)) {
            // Successfully created
            client.write(PacketTypes.SIGN_IN + "OK");
            refCtrl.setIhmUserList(getUserList());
        } else {
            // ERROR
            client.write(PacketTypes.SIGN_IN + "KO");
        }
    }

    /**
     * Adds a sequence to the database. If the message is null/emtpy or there's
     * no logged client or the message syntax isn't correct, does nothing.
     * Otherwise, processes the msg and asks the databaseWrk to add a new
     * sequence.
     *
     * @param msg The message containing the sequence to add.
     */
    @Override
    public void addSequence(String msg) {
        if ((msg != null) && (!msg.isEmpty())) {
            if (currentSessionLog != null) {
                String[] data = msg.split("\\[");
                if ((data != null) && (data.length == 2)) {
                    String sequenceName = data[0];
                    msg = msg.substring(sequenceName.length());
                    byte[] actions = msg.getBytes();
                    Sequence s = new Sequence();
                    s.setActionFile(actions);
                    s.setFkUser(currentSessionLog.getFkUser());
                    s.setSequenceName(sequenceName);
                    try {
                        databaseWrk.addSequence(s);
                        refCtrl.setIhmSequenceList(databaseWrk.readSequences());
                        serverWrk.sendSequenceList();
                        appendTextConsole("#w[DATABASE] #cSuccessfully added a new sequence");
                    } catch (MyDBException ex) {
                        appendTextConsole("#w[DATABASE] #oError adding a new sequence");
                    }
                }
            }
        }
    }

    /**
     * Asks the WrkDatabase to create a new User. Writes a message on the
     * console
     *
     * @param username The username of the user
     * @param password The password of the user (not encrypted)
     * @return True if the user was created successfully, false otherwise.
     */
    @Override
    public boolean addUser(String username, String password) {
        boolean created = false;
        if (databaseWrk.isConnected()) {
            User u = new User();
            u.setUsername(username);
            u.setPassword(password);
            try {
                databaseWrk.addUser(u);
                created = true;
            } catch (MyDBException ex) {
            }
        }
        if (!created) {
            appendTextConsole("#w[DATABASE] #oError creating a new user (" + username + ", " + password + ")");
        } else {
            appendTextConsole("#w[DATABASE] #cNew user added to the database (" + username + ", " + password + ")");
        }
        return created;
    }

    /**
     * Asks the databaseWrk to deletes a specified user from the database.
     *
     * @param u The user to delete.
     * @return True if the user was successfully deleted, false otherwise.
     */
    @Override
    public boolean deleteUser(User u) {
        boolean deleted = false;
        if ((databaseWrk.isConnected()) && (u != null)) {
            try {
                databaseWrk.removeUser(u);
                deleted = true;
            } catch (MyDBException ex) {
            }
        }
        if (!deleted) {
            appendTextConsole("#w[DATABASE] #oError deleting a user (" + u.getUsername() + ")");
        } else {
            appendTextConsole("#w[DATABASE] #cDeleted user from the database (" + u.getUsername() + ")");
        }
        return deleted;
    }

    /**
     * Asks the databaseWrk to deletes a specified sequence from the database.
     *
     * @param s The sequence to delete.
     * @return True if the sequence was successfully deleted, false otherwise.
     */
    @Override
    public boolean deleteSequence(Sequence s) {
        boolean deleted = false;
        if (databaseWrk.isConnected()) {
            try {
                databaseWrk.removeSequence(s);
                deleted = true;
            } catch (MyDBException ex) {
            }
        }
        if (!deleted) {
            appendTextConsole("#w[DATABASE] #oError deleting a sequence (" + s.getSequenceName() + ")");
        } else {
            appendTextConsole("#w[DATABASE] #cDeleted a sequence from the database (" + s.getSequenceName() + ")");
        }
        return deleted;
    }

    /**
     * Asks the databaseWrk to get every users from the database.
     *
     * @return a List containing every User from the database.
     */
    @Override
    public List<User> getUserList() {
        List<User> users = null;
        if (databaseWrk.isConnected()) {
            try {
                users = databaseWrk.readUsers();
            } catch (MyDBException ex) {
            }
        }
        if (users == null) {
            appendTextConsole("#w[DATABASE] #oError reading the list of users");
        }
        return users;
    }

    /**
     * Asks the databaseWrk to get every logs from the database.
     *
     * @return a List containing every Log from the database.
     */
    @Override
    public List<Log> getLogList() {
        List<Log> logs = null;
        if (databaseWrk.isConnected()) {
            try {
                logs = databaseWrk.readLogs();
            } catch (MyDBException ex) {
            }
        }
        if (logs == null) {
            appendTextConsole("#w[DATABASE] #oError reading the list of logs");
        }
        return logs;
    }

    /**
     * Asks the databaseWrk to get every sequences from the database.
     *
     * @return a List containing every Sequence from the database.
     */
    @Override
    public List<Sequence> getSequenceList() {
        List<Sequence> sequences = null;
        if (databaseWrk.isConnected()) {
            try {
                sequences = databaseWrk.readSequences();
            } catch (MyDBException e) {
            }
        }
        if (sequences == null) {
            appendTextConsole("#w[DATABASE] #oError reading the list of sequences");
        }
        return sequences;
    }

    /**
     * Asks the database to get the log corresponding to a specified Primary
     * Key.
     *
     * @param pk The primary key of the log.
     * @return The corresponding Log if there's one, null otherwise.
     */
    @Override
    public Log getLogByPk(int pk) {
        Log l = null;
        if (databaseWrk.isConnected()) {
            try {
                l = databaseWrk.readLog(pk);
            } catch (MyDBException ex) {
            }
        }
        if (l == null) {
            appendTextConsole("#w[DATABASE] #oError reading log by pk (" + pk + ")");
        }
        return l;
    }

    /**
     * Asks the database to get the sequence corresponding to a specified
     * Primary Key.
     *
     * @param pk The primary key of the sequence.
     * @return The corresponding Sequence if there's one, null otherwise.
     */
    @Override
    public Sequence getSequenceByPk(int pk) {
        Sequence s = null;
        if (databaseWrk.isConnected()) {
            try {
                s = databaseWrk.readSequence(pk);
            } catch (MyDBException ex) {
            }
        }
        if (s == null) {
            appendTextConsole("#w[DATABASE] #oError reading sequence by pk (" + pk + ")");
        }
        return s;
    }

    /**
     * Asks the database to get the user corresponding to a specified Primary
     * Key.
     *
     * @param pk The primary key of the user.
     * @return The corresponding User if there's one, null otherwise.
     */
    @Override
    public User getUserByPk(int pk) {
        User u = null;
        if (databaseWrk.isConnected()) {
            try {
                u = databaseWrk.readUser(pk);
            } catch (MyDBException ex) {
            }
        }
        if (u == null) {
            appendTextConsole("#w[DATABASE] #oError reading user by pk (" + pk + ")");
        }
        return u;
    }

    // ***** Database ***** //
    // ----- IHM ----- //
    /**
     * Asks the Ctrl to append text to the console.
     *
     * @param msg The message to append.
     */
    @Override
    public void appendTextConsole(String msg) {
        refCtrl.appendTextConsole(msg);
    }

    /**
     * Asks the serverWrk to calculate the current session time of the logged
     * client if there's one.
     *
     * @return The calculated session time if there's one, an empty String
     * otherwise.
     */
    @Override
    public String getCurrentSessionTime() {
        return (serverWrk != null) ? (serverWrk.getCurrentSessionTime()) : ("");
    }

    // ***** IHM ***** //
    public void setRefCtrl(ItfCtrlWrk refCtrl) {
        this.refCtrl = refCtrl;
    }

    @Override
    public Log getCurrentSessionLog() {
        return currentSessionLog;
    }

}
