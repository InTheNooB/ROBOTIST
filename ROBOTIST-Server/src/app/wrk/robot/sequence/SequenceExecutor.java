package app.wrk.robot.sequence;

import app.consts.SequenceActionType;
import static app.consts.Constants.ROBOT_MAX_TRACK_SPEED;
import app.wrk.Wrk;
import app.wrk.robot.RobotWrk;
import app.wrk.server.ServerWrk;
import ch.emf.info.robot.links.bean.RobotState;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dingl01
 */
public class SequenceExecutor extends Thread {

    // Sequences
    private String sequence;
    private final String sequenceName;
    private final List<SequenceAction> actions;

    // Required to send picture to the client
    private final ItfWrkSequenceExecutor refWrk;

    // Required to control the robot
    private final RobotWrk refRobotWrk;

    private final ItfServerWrkSequenceExecutor refServerWrk;

    public SequenceExecutor(String sequence, String sequenceName, RobotWrk refRobotWrk, Wrk refWrk, ServerWrk refServerWrk) {
        super("Sequence Executor");
        this.sequence = sequence;
        this.sequenceName = sequenceName;
        this.refRobotWrk = refRobotWrk;
        this.refServerWrk = refServerWrk;
        this.refWrk = refWrk;
        actions = new ArrayList();
    }

    /**
     * If the sequence isn't null or empty, processes it in order to extract every actions and their
     * duration. See <b>PacketTypes</b> to know the format of the message
     * received from a client.
     */
    private void processSequence() {
        if ((sequence == null) || (sequence.isEmpty())) {
            return;
        }
        sequence = sequence.replaceAll("\\[", "").replaceAll("\\]", "");
        for (String action : sequence.split(";")) {
            // Find type of movement
            String[] data = action.split(",");
            SequenceActionType actionType = SequenceActionType.valueOf(data[0]);
            int duration = -1;
            try {
                duration = Integer.parseInt(data[1]);
            } catch (NumberFormatException e) {
            }
            if ((actionType == null) && (duration <= 0)) {
                continue;
            }
            actions.add(new SequenceAction(actionType, duration));
        }
    }

    /**
     * This method is executed by a new thread whenever a sequence has been
     * processed and needs to be executed. This thread will freeze the robot to
     * prevent any exterior movement, then go through the list of actions and
     * tell the robot to move accordingly, then wait the duration time. When the
     * whole sequence has been executed, the robot is set to idle and is
     * unfreezed.
     *
     * While executing, on each new executed action, a packet is sent to the
     * connected client to tell him which action is currently being executed.
     */
    @Override
    public void run() {
        // First, get every actions and durations
        processSequence();
        refWrk.appendTextConsole("#w[ROBOT] Executing sequence \" #c" + sequenceName + "  #w\"");
        for (int i = 0; i < actions.size(); i++) {
            // Get the current action
            SequenceAction action = actions.get(i);

            // Send the current action to the client
            refServerWrk.sendCurrentActionToClient(i);
            refWrk.appendTextConsole("#w[ROBOT] #cAction N°" + i + " : (" + action.getAction() + ";" + action.getDuration() + ")");

            // Start action
            switch (action.getAction()) {
                case FWD:
                    refRobotWrk.setLeftSpeed(ROBOT_MAX_TRACK_SPEED);
                    refRobotWrk.setRightSpeed(ROBOT_MAX_TRACK_SPEED);
                    break;
                case BWD:
                    refRobotWrk.setLeftSpeed((short) -ROBOT_MAX_TRACK_SPEED);
                    refRobotWrk.setRightSpeed((short) -ROBOT_MAX_TRACK_SPEED);
                    break;
                case LFT:
                    refRobotWrk.setLeftSpeed((short) 0);
                    refRobotWrk.setRightSpeed(ROBOT_MAX_TRACK_SPEED);
                    break;
                case RGT:
                    refRobotWrk.setLeftSpeed(ROBOT_MAX_TRACK_SPEED);
                    refRobotWrk.setRightSpeed((short) 0);
                    break;
                case HDD:
                    refRobotWrk.setHeadDirection(RobotState.HeadDirection.DOWN);
                    break;
                case HDU:
                    refRobotWrk.setHeadDirection(RobotState.HeadDirection.UP);
                    break;
                case TPC:
                default:
                    refWrk.takeAndSendRobotPicture();
                    break;
            }

            // Sleep duration time (seconds * 1000 = ms)
            sleeep(action.getDuration() * 1000);

            // Reset robot to idle state (stop action)
            setIdle();

            // Unfreeze the robot
            refRobotWrk.setFreezed(false);
            refServerWrk.sendUnfreezeRobot();
        }
    }

    /**
     * Resets the robots movement and sets him to idle.
     */
    private void setIdle() {
        refRobotWrk.setLeftSpeed((short) 0);
        refRobotWrk.setRightSpeed((short) 0);
        refRobotWrk.setHeadDirection(RobotState.HeadDirection.NONE);
    }

    /**
     * Sleeps the current thread for a amount of time.
     *
     * @param ms an int corresponding to the amount of time to sleep
     */
    private void sleeep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
        }
    }

}
