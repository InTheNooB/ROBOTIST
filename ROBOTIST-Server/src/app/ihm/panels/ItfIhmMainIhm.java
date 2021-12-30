package app.ihm.panels;

/**
 *
 * @author dingl01
 */
public interface ItfIhmMainIhm {

     void freezeRobot();
     void unfreezeRobot();
    
     void navigateBetweenIhm(String ihmName);

     void initRobot();
     void connectRobot();
    
}
