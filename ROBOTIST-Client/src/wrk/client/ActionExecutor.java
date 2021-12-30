package wrk.client;

public class ActionExecutor extends Thread {

    private ItfWrkClientActionExecutor refWrkClient;
    private final ActionList list;
    private volatile boolean running;

    public ActionExecutor(WrkClient refWrk,String name) {
        setName(name);
        this.refWrkClient = refWrk;
        list = ActionList.getInstance();
    }

    /**
     * Waits and process a packet when added to the list.
     */
    @Override
    public void run() {
        running = true;
        String action;
        while (running) {
            try {
                synchronized (ActionList.getInstance()) {
                    ActionList.getInstance().wait();
                }
            } catch (InterruptedException ex) {
            }
            action = list.getLastAction();
            if (action != null) {
                refWrkClient.processPacket(action);
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
