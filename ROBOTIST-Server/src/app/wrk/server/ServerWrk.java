package app.wrk.server;

import app.consts.PacketTypes;
import app.wrk.server.actions.ItfServerWrkActionsExecutor;
import app.wrk.server.actions.ActionsExecutor;
import app.wrk.server.actions.ActionsList;
import app.beans.Sequence;
import app.beans.User;
import static app.consts.Constants.NBR_ACTION_LIST_THREADS;
import static app.consts.Constants.TCP_PORT;
import static app.consts.Constants.UDP_PORT;
import app.utils.ConversionHandler;
import app.utils.IPAddressHandler;
import app.wrk.Wrk;
import app.wrk.robot.sequence.ItfServerWrkSequenceExecutor;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dingl01
 */
public class ServerWrk implements ItfServerWrkActionsExecutor, ItfServerWrkSequenceExecutor, ItfServerWrkTCPServer,ItfServerWrkUDPServer,ItfServerWrkClient {

    private TCPServer tcpServer;
    private UDPServer udpServer;

    private final ItfWrkServerWrk refWrk;

    private final List<ActionsExecutor> actionExecutors;

    public ServerWrk(Wrk wrk) {
        this.refWrk = wrk;
        tcpServer = new TCPServer(this);
        udpServer = new UDPServer(this);

        actionExecutors = new ArrayList();
    }

    /**
     * Asks the specified workers to starts their server (TCP-UDP) and setups
     * the executors that will execute received messages.
     *
     * @return A boolean, true if everything went right, false otherwise.
     */
    public boolean startServer() {
        boolean started = false;
        try {
            tcpServer.startServer();
            udpServer.startServer();

            setupExecutors();
            appendTextConsole("#w[SERVER] #gServer started");
            appendTextConsole("#w[SERVER] #wListening at #o" + getHostIp() + ": #m" + TCP_PORT + " #r...");
            started = true;
        } catch (IOException ex) {
            appendTextConsole("#w[SERVER] #rError starting the server");
        } catch (Exception exc) {
            appendTextConsole("#w[SERVER] #rError creating the server");
        }
        return started;
    }

    /**
     * This method will create a number of  <b>ActionExecutor </b> (defined by
     * <b>NBR_ACTION_LIST_THREADS</b>) and start them to generate new threads.
     */
    private void setupExecutors() {
        for (int i = 0; i < NBR_ACTION_LIST_THREADS; i++) {
            ActionsExecutor a = new ActionsExecutor("ActionsExecutor" + i, this);
            actionExecutors.add(a);
            a.start();
        }
    }

    /**
     * This method correctly and safely stops every <b>ActionExecutor</b>.
     */
    private void stopExecutors() {
        for (ActionsExecutor a : actionExecutors) {
            if (a != null) {
                a.setRunning(false);
            }
        }
        synchronized (ActionsList.getInstance().getObjectToWaitTo()) {
            ActionsList.getInstance().getObjectToWaitTo().notifyAll();
        }
        for (ActionsExecutor a : actionExecutors) {
            if (a != null) {
                try {
                    a.join();
                } catch (InterruptedException ex) {
                }
                a = null;
            }
        }
        actionExecutors.clear();
    }

    /**
     * This method correctly and safely stops both TCP and UDP servers.
     *
     * @throws InterruptedException Throw an InterruptedException if there was
     * an error closing the servers.
     */
    public void stopServer() throws InterruptedException {
        try {
            tcpServer.stopServer();
            tcpServer.join();
            tcpServer = null;

            udpServer.stopServer();
            udpServer.join();
            udpServer = null;
            stopExecutors();
        } catch (IOException ex) {
            appendTextConsole("#w[SERVER] #rError closing the server");
        }

    }

    /**
     * Adds an action to the <b>ActionsList</b> Singleton.
     *
     * @param msg A String representing the message to add.
     * @param ip An InetAddress Object representing the ip address of the client that sent this message.
     */
    @Override
    public void addUDPMsgToList(String msg, InetAddress ip) {
        if ((tcpServer.getClient() != null) && ip.equals(tcpServer.getClient().getIpAddress())) {
            ActionsList.getInstance().addAction(msg, tcpServer.getClient());
        } else {
            // Broadcast or unknown client
            ActionsList.getInstance().addAction(msg, null);
        }
        synchronized (ActionsList.getInstance()) {
            ActionsList.getInstance().notifyAll();
        }
    }

    /**
     * Adds an action to the <b>ActionsList</b> Singleton.
     *
     * @param msg A String representing the message to add.
     * @param client A Client Object representing the client that has sent this message.
     */
    @Override
    public void addTCPMsgToList(String msg, Client client) {
        ActionsList.getInstance().addAction(msg, client);

        synchronized (ActionsList.getInstance().getObjectToWaitTo()) {
            ActionsList.getInstance().getObjectToWaitTo().notifyAll();
        }
    }

    /**
     * Processes a message received from a client.
     *
     * @param msg A String representing the message received from the client.
     * @param client The client that sent the message, if null, then the packets
     * comes from a broad
     */
    @Override
    public void processMsg(String msg, Client client) {
        if ((msg == null) || (msg.isEmpty())) {
            return;
        }
        PacketTypes pt = getPacketType(msg);
        if (pt == null) {
            // Received an unreadble packet
            if (client != null) {
                appendTextConsole("#w[SERVER] #oUnreadable packet received from #w[ #c" + client.getIpAddress() + " #w] : \"" + msg + "\"");
            } else {
                appendTextConsole("#w[SERVER] #oUnreadable packet received from  : \"" + msg + "\"");
            }
            return;
        }

        // Remove the packetType letters
        msg = msg.substring(pt.name().length());

        switch (pt) {
            case LOGIN_CREDENTIALS:
                refWrk.loginUser(msg, client);
                break;
            case SIGN_IN:
                refWrk.signInUser(msg, client);
                break;
            case LOGIN_USER_FACE:
                refWrk.processLoggedInUserFace(msg);
                break;
            case PING:
                client.write(PacketTypes.PING + msg);
                break;
            case TAKE_PICTURE_ROBOT:
                refWrk.takeAndSendRobotPicture();
                break;
            case MOVE_ROBOT_HEAD:
                refWrk.moveRobotHead(msg);
                break;
            case MOVE_ROBOT_BODY:
                refWrk.moveRobot(msg);
                break;
            case SEQUENCE_LIST:
                sendSequenceList();
                break;
            case ADD_SEQUENCE:
                refWrk.addSequence(msg);
                break;
            case READ_SEQUENCE:
                refWrk.readSequence(msg);
                break;
            default:
                break;
        }
    }

    /**
     * If a client is logged in, gets every sequences from the database and find
     * the ones that belong to the client. Creates a message that contains all
     * these sequences and send it to the client.
     */
    public void sendSequenceList() {
        if (refWrk.getCurrentSessionLog() != null) {
            // Get current logged in user
            User u = refWrk.getCurrentSessionLog().getFkUser();
            if (u != null) {
                // Get every sequences in the databse
                List<Sequence> allSequences = refWrk.getSequenceList();
                if (allSequences != null) {
                    // Only get sequences from user
                    List<Sequence> userSequences = new ArrayList();
                    for (Sequence s : allSequences) {
                        if (s.getFkUser().equals(u)) {
                            userSequences.add(s);
                        }
                    }

                    if (userSequences.isEmpty()) {
                        return;
                    }
                    // Then send the content of the list to the client
                    String msg = PacketTypes.SEQUENCE_LIST.name();

                    // Add each sequence name followed by the sequence itself.
                    // After each sequence, add a "|" to separate the sequences
                    for (Sequence s : userSequences) {
                        msg += s.getSequenceName();
                        msg += new String(s.getActionFile(), StandardCharsets.UTF_8) + "|";
                    }

                    // Remove last "|"
                    msg = msg.substring(0, msg.length() - 1);

                    // Send the message to the client
                    tcpServer.writeClient(msg);
                    appendTextConsole("#w[SERVER] #cSequence list sent to the client");
                }
            }
        }
    }

    /**
     * Get the <b>PacketTypes</b> that correspond to the <b>s</b> parameter.
     *
     * @param s a String representing the name of an entry of the
     * <b>PacketTypes</b> enum.
     * @return The corresponding <b>PacketTypes</b> if there is one, null
     * otherwise.
     */
    private PacketTypes getPacketType(String s) {
        for (PacketTypes pt : PacketTypes.values()) {
            if (s.startsWith(pt.name())) {
                return pt;
            }
        }
        return null;
    }

    /**
     * If a client is connected, sends a him packet to freeze the robot.
     */
    @Override
    public void sendUnfreezeRobot() {
        if ((tcpServer.getClient() != null) && (tcpServer.getClient().getSocket().isConnected())) {
            tcpServer.writeClient(PacketTypes.UNFREEZE_ROBOT.name());
        }
    }

    /**
     * If a client is connected, sends a him packet to freeze the robot.
     */
    public void sendFreezeRobot() {
        if ((tcpServer.getClient() != null) && (tcpServer.getClient().getSocket().isConnected())) {
            tcpServer.writeClient(PacketTypes.FREEZE_ROBOT.name());
        }
    }

    /**
     * If a client is connected, sends him via UDP the current view of the
     * robot.
     *
     * @param image The image to send.
     * @throws IOException Throws an exception if there was an error.
     */
    public void sendRobotView(BufferedImage image) throws IOException {
        if (image == null) {
            return;
        }
        if ((tcpServer.getClient() != null) && (tcpServer.getClient().getSocket() != null)) {
            if (!tcpServer.getClient().getSocket().isClosed()) {
                udpServer.write(PacketTypes.ROBOT_VIEW
                        + ConversionHandler.imageToString(image),
                        tcpServer.getClient().getIpAddress(), UDP_PORT);
            }
        }
    }

    /**
     * If a client is connected, sends him the currently executed action.
     *
     * @param i The id of the action that is being executed.
     */
    @Override
    public void sendCurrentActionToClient(int i) {
        if ((tcpServer.getClient() != null) && (tcpServer.getClient().getSocket() != null)) {
            if (!tcpServer.getClient().getSocket().isClosed()) {
                tcpServer.writeClient(PacketTypes.CURRENT_ACTION.name() + i);
            }
        }
    }

    /**
     * If a client is connected, sends him the current view of the robot.
     *
     * @param image The image to send
     */
    public void takeAndSendRobotPicture(byte[] image) {
        if (tcpServer.getClient() != null) {
            tcpServer.writeClient(PacketTypes.TAKE_PICTURE_ROBOT.name() + ConversionHandler.bytesToString(image));
        }
    }

    /**
     * If a client is connected and logged in, get the current session time and
     * returns it.
     *
     * @return A String representing the curent session time of the logged client.
     */
    public String getCurrentSessionTime() {
        return ((tcpServer != null) && (tcpServer.getClient() != null)) ? tcpServer.getClient().getCurrentSessionTime() : "";
    }

    /**
     * @return A String representing the IP address of the server
     */
    public String getHostIp() {
        return IPAddressHandler.getOwnIpAddress().getHostAddress();
    }

    /**
     * Asks the wrk to disconnects the client.
     *
     * @param client The client to disconnect.
     */
    @Override
    public void disconnectClient(Client client) {
        refWrk.disconnectClient(client, client.equals(tcpServer.getClient()));
    }

    /**
     * Asks the wrk to append text in the console.
     *
     * @param msg A String representing the message to append to the console.
     */
    @Override
    public void appendTextConsole(String msg) {
        refWrk.appendTextConsole(msg);
    }

    public Client getConnectedClient() {
        return tcpServer.getClient();
    }

}
