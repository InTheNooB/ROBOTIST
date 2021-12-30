package app.wrk.robot;

import app.ihm.panels.nodes.ConnectionState;

/**
 *
 * @author lione
 */
public interface ItfWrkRobotWrk {

    void appendTextConsole(String string);

    void setRobotConnected(ConnectionState i);

}
