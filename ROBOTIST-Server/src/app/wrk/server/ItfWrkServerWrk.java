package app.wrk.server;

import app.beans.Log;
import app.beans.Sequence;
import java.util.List;

/**
 *
 * @author dingl01
 */
public interface ItfWrkServerWrk {

    void appendTextConsole(String msg);

    void loginUser(String msg, Client client);

    void signInUser(String msg, Client client);

    void processLoggedInUserFace(String msg);

    void disconnectClient(Client client, boolean isLoggedClient);

    void takeAndSendRobotPicture();

    void moveRobotHead(String msg);

    void moveRobot(String msg);

    Log getCurrentSessionLog();

    List<Sequence> getSequenceList();

    void addSequence(String msg);

    void readSequence(String msg);

}
