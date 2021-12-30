package app.ctrl;

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
public interface ItfCtrlWrk {

     void appendTextConsole(String msg);
     void setRobotConnected(ConnectionState state);
     void setClientLoggedIn(ConnectionState state);
     void setDatabaseConnected(ConnectionState state);    
     void enableRobotButtons();
     void disableRobotButtons();
     void setCurrentSessionTime(String currentSessionTime);
     void setLoggedUserFace(BufferedImage image);
    
     void setIhmUserList(List<User> users);
     void setIhmSequenceList(List<Sequence> sequences);
     void setIhmLogList(List<Log> logs);
}
