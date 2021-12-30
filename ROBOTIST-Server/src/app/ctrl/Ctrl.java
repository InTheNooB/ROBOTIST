package app.ctrl;

import app.beans.Log;
import app.beans.Sequence;
import app.beans.User;
import app.ihm.ItfIhmCtrl;
import app.ihm.panels.nodes.ConnectionState;
import app.wrk.ItfWrkCtrl;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 *
 * @author dingl01
 */
public class Ctrl implements ItfCtrlIhm, ItfCtrlWrk {

    private ItfIhmCtrl refIhm;
    private ItfWrkCtrl refWrk;

    /**
     * Entry point of the application
     */
    public void startUp() {
        // Starts the server
        refIhm.setClientLoggedIn((refWrk.startServer())? (ConnectionState.LOADING) : (ConnectionState.DISCONNECTED));

        // Connects to the database
        refIhm.setDatabaseConnected((refWrk.connectDatabase())
                ? (ConnectionState.CONNECTED)
                : (ConnectionState.DISCONNECTED));

        // Inits the panels of the ihm
        setIhmUserList(refWrk.getUserList());
        setIhmSequenceList(refWrk.getSequenceList());
        setIhmLogList(refWrk.getLogList());

        refWrk.start();
    }

    /**
     * Kills every thread to exit safely the application.
     */
    @Override
    public void exitApplication() {
        refWrk.exitApplication();
        refIhm.exitApplication();
        System.gc();
        System.exit(0);
    }

    // ----- Ihm ----- //
    @Override
    public void setLoggedUserFace(BufferedImage image) {
        refIhm.setLoggedUserFace(image);
    }

    @Override
    public void setIhmUserList(List<User> users) {
        refIhm.setUserList(users);
    }

    @Override
    public void setIhmSequenceList(List<Sequence> sequences) {
        refIhm.setSequenceList(sequences);
    }

    @Override
    public void setIhmLogList(List<Log> logs) {
        refIhm.setLogList(logs);
    }

    @Override
    public void setCurrentSessionTime(String time) {
        refIhm.setCurrentSessionTime(time);
    }

    @Override
    public void freezeRobot() {
        refWrk.freezeRobot();
    }

    @Override
    public void unfreezeRobot() {
        refWrk.unfreezeRobot();
    }

    @Override
    public List<User> getUserList() {
        return refWrk.getUserList();
    }

    /**
     * Asks the wrk to create a new user in the database. If it was successfully
     * added (if the result is <b>true</b>) then return the new list to the IHM.
     * Otherwise, return <b>NULL</b>, meaning the list didn't change.
     *
     * @param username The username of the user to add.
     * @param password The non-encrypted password of the user to add.
     * @return A list of user if the new user has been added, <b>NULL</b>
     * otherwise.
     */
    @Override
    public List<User> addUser(String username, String password) {
        return (refWrk.addUser(username, password)) ? (getUserList()) : (null);
    }

    @Override
    public List<User> deleteUser(User u) {
        return (refWrk.deleteUser(u)) ? (getUserList()) : (null);
    }

    @Override
    public List<Log> getLogList() {
        return refWrk.getLogList();
    }

    @Override
    public Log getLogByPk(int pk) {
        return refWrk.getLogByPk(pk);
    }

    @Override
    public User getUserByPk(int pk) {
        return refWrk.getUserByPk(pk);
    }

    @Override
    public Sequence getSequenceByPk(int pk) {
        return refWrk.getSequenceByPk(pk);
    }

    @Override
    public List<Sequence> getSequenceList() {
        return refWrk.getSequenceList();
    }

    @Override
    public List<Sequence> deleteSequence(Sequence s) {
        return (refWrk.deleteSequence(s)) ? (getSequenceList()) : (null);
    }

    @Override
    public void appendTextConsole(String msg) {
        refIhm.appendTextConsole(msg);
    }

    @Override
    public void disableRobotButtons() {
        refIhm.disableRobotButtons();
    }

    @Override
    public void enableRobotButtons() {
        refIhm.enableRobotButtons();
    }

    // ***** Ihm ***** //
    // ----- Robot ----- //
    @Override
    public void initRobot() {
        refWrk.initRobot();
    }

    @Override
    public void connectRobot() {
        refWrk.connectRobot();
    }

    // ***** Robot ***** //
    // ----- Server ----- //
    public void stopServer() {
        refWrk.stopServer();
    }

    @Override
    public void setRobotConnected(ConnectionState state) {
        refIhm.setRobotConnected(state);
    }

    @Override
    public void setClientLoggedIn(ConnectionState state) {
        refIhm.setClientLoggedIn(state);
    }

    @Override
    public void setDatabaseConnected(ConnectionState state) {
        refIhm.setDatabaseConnected(state);
    }

    // ***** Server ***** //
    public void setRefIhm(ItfIhmCtrl refIhm) {
        this.refIhm = refIhm;
    }

    public void setRefWrk(ItfWrkCtrl refWrk) {
        this.refWrk = refWrk;
    }

}
