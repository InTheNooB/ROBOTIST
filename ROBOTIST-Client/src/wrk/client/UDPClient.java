package wrk.client;

import static constants.NetworkConstants.UDP_PORT;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPClient extends Thread {

    private volatile boolean running;
    private byte[] bufReceived;
    private byte[] bufSend;
    private DatagramSocket socket;
    private DatagramPacket udpPacket;

    public UDPClient() {
        super("UDP Client Thread");
        bufReceived = new byte[65535];
        bufSend = new byte[65535];
    }

    /**
     *
     * Checks if an UDP packet is received. The packet is then added to the
     * ActionList
     */
    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                DatagramPacket dp = new DatagramPacket(bufReceived, bufReceived.length);
                socket.receive(dp);
                ActionList.getInstance().addAction(new String(dp.getData(), 0, dp.getLength()));
                synchronized (ActionList.getInstance()) {
                    ActionList.getInstance().notify();
                }
            } catch (IOException e) {
            }
        }
        if (socket != null) {
            socket.close();
        }
    }

    /**
     * Create the socket and then run the current thread.
     */
    public void startClient() {
        try {
            socket = new DatagramSocket(UDP_PORT);
        } catch (SocketException ex) {
            Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        start();
    }

    /**
     * Send udp message to the server.
     *
     * @param msg the message to send
     * @param address the address of the server
     */
    public void sendUDPPacket(String msg, InetAddress address) {
        try {
            bufSend = msg.getBytes();
            udpPacket = new DatagramPacket(bufSend, bufSend.length, address, UDP_PORT);
            socket.send(udpPacket);
        } catch (IOException ex) {
        }
    }

    /**
     * Disconnects from the server.
     */
    public void disconnect() {
        if (socket != null) {
            socket.close();
        }
        running = false;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
