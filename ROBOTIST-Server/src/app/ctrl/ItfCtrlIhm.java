package app.ctrl;

import app.beans.Log;
import app.beans.Sequence;
import app.beans.User;
import java.util.List;

/**
 *
 * @author dingl01
 */
public interface ItfCtrlIhm {

    // --- MainIhm ---
    // Freeze Button
    void freezeRobot();

    void unfreezeRobot();

    void initRobot();

    void connectRobot();

    // --- Users Ihm ---
    List<User> getUserList();

    List<User> addUser(String username, String password);

    List<User> deleteUser(User u);

    User getUserByPk(int pk);

    // --- Logs Ihm ---
    List<Log> getLogList();

    Log getLogByPk(int pk);

    // --- Sequences Ihm ---
    List<Sequence> getSequenceList();

    Sequence getSequenceByPk(int pk);

    List<Sequence> deleteSequence(Sequence s);

    void exitApplication();

}
