package app.wrk.server.actions;

import app.beans.Action;
import app.wrk.server.Client;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dingl01
 */
public class ActionsList {

    private static List<Action> list;
    private final Object objectToWaitTo;

    private ActionsList() {
        list = new ArrayList();
        objectToWaitTo = new Object();
    }

    /**
     * Synchronously adds a new action in the action list.
     * @param action The action to add in the list
     * @param client The client that sent the message
     */
    public void addAction(String action, Client client) {
        synchronized (list) {
            list.add(new Action(action, client));
        }
    }

    /**
     * Synchronously gets the last action in the action list and deletes it.
     * @return an Action corresponding to the last action in the list if there is one, null otherwise.
     */
    public Action getLastAction() {
        Action action = null;
        synchronized (list) {
            if (!list.isEmpty()) {
                action = list.remove(0);
            }
        }
        return action;
    }
    public static ActionsList getInstance() {
        return ActionsListHolder.INSTANCE;
    }

    public Object getObjectToWaitTo() {
        return objectToWaitTo;
    }
    
    private static class ActionsListHolder {

        private static final ActionsList INSTANCE = new ActionsList();
    }
}
