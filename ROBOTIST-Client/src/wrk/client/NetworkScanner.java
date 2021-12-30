package wrk.client;

import static constants.Constants.TIMEOUT;
import static constants.NetworkConstants.UDP_CLT_BROADCAST_LOOKUP_PORT;
import static constants.NetworkConstants.UDP_PORT;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class NetworkScanner {

    /**
     * Broadcasts a FIND_SERVER_ADDRESS packet to try to find a server.
     *
     * @param wrkClient the client worker
     */
    public static void broadcastUDPLookupServer(WrkClient wrkClient) {

        new Thread(() -> {

            ArrayList<String> servers = new ArrayList();

            // Packet for receiving response from server
            byte[] receiveBuffer = new byte[65535];
            DatagramPacket receiveBroadcastPacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

            // Create a Datagram (UDP) socket on any available port
            DatagramSocket broadcastSocket = createSocket();

            // send a known request string (server checks this)
            byte[] packetData = PacketTypes.FIND_SERVER_ADDRESS.name().getBytes();

            // try the widest broadcast address first
            InetAddress broadcastAddress = null;
            try {
                broadcastAddress = InetAddress.getByName("255.255.255.255");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            DatagramPacket packet = new DatagramPacket(packetData,
                    packetData.length, broadcastAddress, UDP_PORT);

            long time = System.currentTimeMillis();
            try {
                broadcastSocket.send(packet);

                while (true) {
                    broadcastSocket.receive(receiveBroadcastPacket);
                    String reply = new String(receiveBroadcastPacket.getData(), 0, receiveBroadcastPacket.getLength());
                    if (reply.startsWith(PacketTypes.FIND_SERVER_ADDRESS.name())) {
                        InetAddress address = receiveBroadcastPacket.getAddress();
                        servers.add(address.getHostAddress());
                        time = System.currentTimeMillis();
                    }
                    if (System.currentTimeMillis() - time > TIMEOUT) {
                        broadcastSocket.close();
                        break;
                    }
                }
            } catch (IOException ex) {
                broadcastSocket.close();
            }

            wrkClient.serverFound(servers);
        }, "Network Scanner Thread").start();

    }

    /**
     * Creates UDP socket.
     *
     * @return the datagramsocket
     */
    public static DatagramSocket createSocket() {
        // Create a Datagram (UDP) socket on any available port
        DatagramSocket socket = null;
        // Create a socket for sending UDP broadcast packets
        try {
            socket = new DatagramSocket(UDP_CLT_BROADCAST_LOOKUP_PORT);
            socket.setBroadcast(true);
            // use a timeout and resend broadcasts instead of waiting forever
            socket.setSoTimeout((int) TIMEOUT);
        } catch (SocketException sex) {
            sex.printStackTrace();
        }
        return socket;
    }
}
