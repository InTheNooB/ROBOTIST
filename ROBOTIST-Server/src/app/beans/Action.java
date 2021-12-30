package app.beans;

import app.wrk.server.Client;

/**
 *
 * @author dingl01
 */
public class Action {

    private final Client client;
    private final String action;

    public Action(String action, Client client) {
        this.client = client;
        this.action = action;
    }

    public Client getClient() {
        return client;
    }

    public String getAction() {
        return action;
    }

}
