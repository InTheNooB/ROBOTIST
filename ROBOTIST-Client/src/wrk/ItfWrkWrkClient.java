/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wrk;

/**
 *
 * @author MayencourtE
 */
public interface ItfWrkWrkClient {

    public void updateServerIp(String ip);

    public void loginOk(String msg);

    public void signInOk(String msg);

    public void serverFull();

    public void retrieveSequence(String msg);

    public void retrieveCurrentAction(String msg);

    public void freezeRobot();

    public void unfreezeRobot();

    public void retrieveRobotPicture(String msg);

    public void retrievePing(String msg);

    public void retrieveRobotVideo(String msg);

    public void serverDisconnected();

    public void appendToConsole(String string);
    
}
