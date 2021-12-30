package app.wrk.robot.sequence;

import app.consts.SequenceActionType;

/**
 *
 * @author dingl01
 */
public class SequenceAction {

    private final SequenceActionType action;
    private final int duration;

    public SequenceAction(SequenceActionType action, int duration) {
        this.action = action;
        this.duration = duration;
    }

    public SequenceActionType getAction() {
        return action;
    }

    public int getDuration() {
        return duration;
    }

}
