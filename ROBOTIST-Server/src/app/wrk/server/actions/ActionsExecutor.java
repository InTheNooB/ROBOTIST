package app.wrk.server.actions;

import app.beans.Action;
import app.wrk.server.Client;

/**
 *
 * @author dingl01
 */
public class ActionsExecutor extends Thread {

    private final ItfServerWrkActionsExecutor refServerWrk;
    private volatile boolean running;

    public ActionsExecutor(String name, ItfServerWrkActionsExecutor refServerWrk) {
        super(name);
        this.refServerWrk = refServerWrk;
    }

    /**
     * This method is executed by a new Thread. While running is true, the
     * thread will wait() on a object contained in the Singleton ActionsList.
     * Whenever it is woken up, it will get the last action from the singleton
     * and if it is not null, execute it. These actions are messages received
     * from a client.
     * Once the action is done, the thread will go back to wait.
     */
    @Override
    public void run() {
        running = true;
        Action action;
        while (running) {
            try {
                synchronized (ActionsList.getInstance().getObjectToWaitTo()) {
                    ActionsList.getInstance().getObjectToWaitTo().wait();
                }
            } catch (InterruptedException ex) {
            }
            action = ActionsList.getInstance().getLastAction();

            if (action != null) {
                String msg = action.getAction();
                Client client = action.getClient();
                refServerWrk.processMsg(msg, client);
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
