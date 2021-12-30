package app.utils;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public abstract class IPAddressHandler {

    public static InetAddress getOwnIpAddress() {

        // This way works well when there are multiple network interfaces.
        // It always returns the preferred outbound IP.
        // The destination 8.8.8.8 is not needed to be reachable.
        InetAddress ip = null;
        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress();
            socket.close();
        } catch (SocketException | UnknownHostException e) {
        }
        return ip;
    }

}
