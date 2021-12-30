package wrk;


public interface ItfWrkXbox {

    public void setControllable(boolean connected);

    public void buttonYClicked();

    public void buttonBClicked();

    public void buttonAClicked();

    public void buttonXClicked();

    public void moveLeftJoystick(double direction, double magnitudeGauche);

    public void moveRightJoystick(double direction, double magnitudeDroite);

    public void arrowUp();

    public void arrowDown();

}
