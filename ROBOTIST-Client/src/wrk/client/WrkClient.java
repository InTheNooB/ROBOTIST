package wrk.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import wrk.ItfWrkWrkClient;
import wrk.Wrk;

public class WrkClient implements ItfWrkClientActionExecutor {

    private ItfWrkWrkClient refWrk;
    private TCPClient tcpClient;
    private UDPClient udpClient;
    private PingHandler ping;
    private ArrayList<ActionExecutor> actionExecutors;

    public WrkClient(ItfWrkWrkClient refWrk) {
        actionExecutors = new ArrayList<>();
        this.refWrk = refWrk;
    }

    /**
     * Inits the TCP, UDP, Ping and ActionExecutor.
     */
    private void initClient() {
        if (tcpClient == null) {
            setupActionExecutors();
            tcpClient = new TCPClient(this);
            udpClient = new UDPClient();
            tcpClient.start();
            udpClient.startClient();
            ping = new PingHandler(tcpClient);
            ping.start();
        }

    }

    /**
     * Creates the action executors and start them.
     */
    private void setupActionExecutors() {
        for (int i = 0; i < 10; i++) {
            actionExecutors.add(new ActionExecutor(this, "Action Executor " + i));
        }
        for (ActionExecutor actionExecutor : actionExecutors) {
            actionExecutor.start();
        }
    }

    /**
     * Stops all the action executors.
     */
    private void stopExecutors() {
        for (ActionExecutor a : actionExecutors) {
            if (a != null) {
                a.setRunning(false);
            }
        }
        synchronized (ActionList.getInstance()) {
            ActionList.getInstance().notifyAll();
        }
        for (ActionExecutor a : actionExecutors) {
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
     * Sends message to server using TCP.
     *
     * @param msg the message to send
     */
    public void tcpWrite(String msg) {
        if (tcpClient != null) {
            tcpClient.writeMessage(msg);

        }
    }

    /**
     * Sends message to server using UDP.
     *
     * @param msg the message to send
     * @param ip ip address of the server
     */
    public void udpWrite(String msg, String ip) {
        try {
            udpClient.sendUDPPacket(msg, InetAddress.getByName(ip));
        } catch (UnknownHostException ex) {
            Logger.getLogger(WrkClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Connects to server using TCP, if the connection is unsuccessful it will
     * try to broadcast to find the ip of the server.
     *
     * @param ip ip address of the server
     */
    public void connectTcp(String ip) {
        initClient();

        boolean connected = false;
        try {
            InetAddress test = InetAddress.getByName(ip);
            if (!ip.equals("")) {
                connected = tcpClient.connectToServer(ip);
                if (!connected) {
                    NetworkScanner.broadcastUDPLookupServer(this);
                } else {
                    refWrk.updateServerIp(ip);
                }
            }
        } catch (UnknownHostException ex) {
            NetworkScanner.broadcastUDPLookupServer(this);
        }

    }

    /**
     * Disconnect TCP client from server.
     */
    public void disconnectTcp() {
        tcpClient.disconnect();
    }

    /**
     * Disconnect UDP client from server.
     */
    public void disconnectUdp() {
        udpClient.disconnect();
    }

    public boolean isTcpConnected() {
        return tcpClient.isConnected();
    }

    /**
     * Closes all TCP/UDP connection and stoping action executors and ping
     * thread.
     */
    public void shutdownTcpUdp() {
        new Thread(() -> {
            if (tcpClient != null) {
                tcpClient.setRunning(false);
                tcpClient.disconnect();
                try {
                    tcpClient.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(WrkClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                tcpClient = null;
            }
            if (udpClient != null) {
                udpClient.disconnect();
                udpClient.setRunning(false);
                try {
                    udpClient.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(WrkClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                udpClient = null;
            }
            if (ping != null) {
                ping.setRunning(false);
                ping.setDoNotSend(false);
                try {
                    ping.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(WrkClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                ping = null;
            }

            stopExecutors();
        }, "Closing Thread").start();

    }

    /**
     * Connects to server if broadcast found a server and then update the ip
     * address field in the ihm.
     *
     * @param servers list of found servers
     */
    public void serverFound(ArrayList<String> servers) {
        String result = "noServerFound";
        if (!servers.isEmpty()) {
            for (String s : servers) {
                if (servers.indexOf(s) == 0) {
                    result = s;
                    tcpClient.connectToServer(s);
                }
            }
        }
        refWrk.updateServerIp(result);

    }

    /**
     * Finds the type of a received packet.
     *
     * @param msg the packet name
     * @return the packet type
     */
    private PacketTypes getPacketType(String msg) {
        for (PacketTypes pt : PacketTypes.values()) {
            if (msg.startsWith(pt.name())) {
                return pt;
            }
        }
        return null;
    }

    /**
     * Process packet by the type of packet. This method is called when a packet
     * need to be process by the action exectors
     *
     * @param msg the packet
     */
    public void processPacket(String msg) {
        PacketTypes pt = getPacketType(msg);

        if (pt == null) {
            // Received an unreadble packet
            //refWrk.appendToConsole("#w[SERVER] #rUnreadable packet received : " + msg);
            return;
        }
        msg = msg.substring(pt.name().length());
        switch (pt) {
            case LOGIN_CREDENTIALS:
                refWrk.loginOk(msg);
                break;
            case SIGN_IN:
                refWrk.signInOk(msg);
                break;
            case SERVER_FULL:
                refWrk.serverFull();
                break;
            case SEQUENCE_LIST:
                refWrk.retrieveSequence(msg);
                break;
            case CURRENT_ACTION:
                refWrk.retrieveCurrentAction(msg);
            case FREEZE_ROBOT:
                refWrk.freezeRobot();
                break;
            case UNFREEZE_ROBOT:
                refWrk.unfreezeRobot();
                break;
            case TAKE_PICTURE_ROBOT:
                refWrk.retrieveRobotPicture(msg);
                break;
            case PING:
                refWrk.retrievePing(msg);
                break;
            case ROBOT_VIEW:
                refWrk.retrieveRobotVideo(msg);
                break;
        }
    }

    /**
     * Sends ping to the server.
     */
    public void sendPing() {
        ping.setDoNotSend(false);
    }

    /**
     * Disconnects client completely. This method is called if the server
     * disconnect
     */
    public void serverDisconnected() {
        shutdownTcpUdp();
        refWrk.serverDisconnected();
    }

}
