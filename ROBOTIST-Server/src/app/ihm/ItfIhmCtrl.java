package app.ihm;

import app.beans.Log;
import app.beans.Sequence;
import app.beans.User;
import app.ihm.panels.nodes.ConnectionState;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 *
 * @author dingl01
 */
public interface ItfIhmCtrl {

    // Users Panel
    void setUserList(List<User> users);

    // Sequences Panel
    void setSequenceList(List<Sequence> sequences);

    // Main Panel
    void setRobotConnected(ConnectionState state);
    void setClientLoggedIn(ConnectionState state);
    void setDatabaseConnected(ConnectionState state);
    void setCurrentSessionTime(String currentSessionTime);
    void setLoggedUserFace(BufferedImage image);
    void appendTextConsole(String txt);

    // Logs Panel
    void setLogList(List<Log> logs);

    // Robot
    void enableRobotButtons();
    void disableRobotButtons();

    // Other
    void exitApplication();

}
