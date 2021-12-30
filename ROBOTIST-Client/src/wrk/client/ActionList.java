package wrk.client;

import java.util.ArrayList;

public class ActionList {

    private ArrayList<String> actionList;

    private ActionList() {
        actionList = new ArrayList<>();
    }

    /**
     * Add action in the actions list.
     *
     * @param action action to add
     */
    public void addAction(String action) {
        synchronized (actionList) {
            actionList.add(action);
        }
    }

    /**
     * Get the last action in the actions list.
     *
     * @return last action
     */
    public String getLastAction() {
        String action = "";

        synchronized (actionList) {
            if (actionList.size() > 0) {
                action = actionList.remove(0);
            }
        }
        return action;
    }

    public static ActionList getInstance() {
        return ActionListHolder.INSTANCE;
    }

    private static class ActionListHolder {

        private static final ActionList INSTANCE = new ActionList();
    }
}
