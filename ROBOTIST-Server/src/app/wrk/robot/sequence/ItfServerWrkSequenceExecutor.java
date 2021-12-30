package app.wrk.robot.sequence;

public interface ItfServerWrkSequenceExecutor {

    void sendCurrentActionToClient(int i);

    void sendUnfreezeRobot();
}
