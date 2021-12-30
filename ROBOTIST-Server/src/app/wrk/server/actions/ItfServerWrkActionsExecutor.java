package app.wrk.server.actions;

import app.wrk.server.Client;

/**
 *
 * @author dingl01
 */
public interface ItfServerWrkActionsExecutor {

    void processMsg(String msg, Client client);
}
