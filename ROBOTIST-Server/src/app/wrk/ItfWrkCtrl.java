package app.wrk;

import app.beans.Log;
import app.beans.Sequence;
import app.beans.User;
import java.util.List;

/**
 *
 * @author dingl01
 */
public interface ItfWrkCtrl {

    // --- MainIhm ---
    // Freeze Button
    void freezeRobot();
    void unfreezeRobot();
    String getCurrentSessionTime();

    // --- Users Ihm ---
    List<User> getUserList();
    boolean addUser(String username, String password);
    boolean deleteUser(User u);

    // --- Logs Ihm ---
    List<Log> getLogList();

    // --- Sequences Ihm ---
    List<Sequence> getSequenceList();
    boolean deleteSequence(Sequence s);

    // --- Server ---
    boolean startServer();
    void stopServer();

    // ----- Database ----- 
    boolean connectDatabase();
    void disconnectDatabase();
    User getUserByPk(int pk);
    Log getLogByPk(int pk);
    Sequence getSequenceByPk(int pk);

    // ----- Robot -----
    void initRobot();
    void connectRobot();

    // ----- Other -----
    void exitApplication();
    void start();

}
