package app.wrk.server;

/**
 *
 * @author dingl01
 */
public interface ItfServerWrkClient {

    void addTCPMsgToList(String msg, Client aThis);

    void disconnectClient(Client aThis);

}
