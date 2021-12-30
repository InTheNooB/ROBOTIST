package app.wrk.server;

import app.consts.PacketTypes;
import static app.consts.Constants.UDP_PORT;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServer extends Thread {

    private final ItfServerWrkUDPServer refServerWrk;

    private volatile boolean running;
    private byte[] sendBuffer;
    private byte[] receiveBuffer;
    private DatagramSocket udpSocket;
    private DatagramPacket udpPacket;

    public UDPServer(ServerWrk refServerWrk) {
        super("UDP Server");
        this.refServerWrk = refServerWrk;
        sendBuffer = new byte[65535];
        receiveBuffer = new byte[65535];
    }

    /**
     * Starts the UPD Server by creating a new <b>DatagramSocket</b>. Then
     * starts a new Thread in this class.
     *
     * @throws SocketException Throws a SocketException if something went wrong.
     */
    public void startServer() throws SocketException {
        udpSocket = new DatagramSocket(UDP_PORT);
        start();
    }

    /**
     * Stops the UDP Server by closing the socket and stopping the thread.
     */
    public void stopServer() {
        if (udpSocket != null) {
            udpSocket.close();
        }
        running = false;
    }

    /**
     * This method is executed by a new Thread. This Thread will listen to
     * message and on receival, check if it is a broadcast packet to find a
     * server, in which case it instantly answers, otherwise it adds the message
     * to the <b>ActionsList</b> to be executed by the <b>ActionsExecutor</b>.
     * When the thread has to stop, it closes the <b>DatagramSocket</b>.
     */
    @Override
    public void run() {
        running = true;
        String msg;
        while (running) {
            try {
                DatagramPacket dp = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                udpSocket.receive(dp);
                msg = new String(dp.getData(), 0, dp.getLength());

                if (msg.startsWith(PacketTypes.FIND_SERVER_ADDRESS.name())) {
                    write(msg, dp.getAddress(), dp.getPort());
                    continue;
                }

                refServerWrk.addUDPMsgToList(msg, dp.getAddress());
            } catch (IOException e) {
            }
        }
        stopServer();
    }

    /**
     * If the message is not null or empty and the ip address isn't nul, sends
     * the message to the specified ip address using the specified port.
     *
     * @param msg The message to send.
     * @param address The ip address to which send the message.
     * @param port The port to use to send the message.
     */
    public void write(String msg, InetAddress address, int port) {
        if ((msg != null) && (!msg.isEmpty()) && (address != null)) {
            try {
                sendBuffer = msg.getBytes();
                udpPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
                udpSocket.send(udpPacket);
            } catch (IOException ex) {
            }
        }
    }

}
