package app.wrk.server;

import java.net.InetAddress;

/**
 *
 * @author dingl01
 */
public interface ItfServerWrkUDPServer {

    void appendTextConsole(String s);

    void addUDPMsgToList(String msg, InetAddress address);

}
