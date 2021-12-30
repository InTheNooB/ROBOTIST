package wrk.xbox;

import ch.aplu.xboxcontroller.*;
import wrk.ItfWrkXbox;

public class Xbox {

    private XboxController xc;
    private ItfWrkXbox refWrk;
    private static double magnitudeGauche;
    private static double magnitudeDroite;

    public Xbox(ItfWrkXbox wrk) {
        this.refWrk = wrk;
    }

    /**
     * Creates the controller and add an event listener.
     */
    public void connectController() {
        XboxController xc = new XboxController();
        if (!xc.isConnected()) {
            xc.release();
            return;
        }

        refWrk.setControllable(xc.isConnected());
        if (!xc.isConnected()) {
            xc.release();
            return;
        }
        //This eventListener is used to receive all controller input
        xc.addXboxControllerListener(new XboxControllerAdapter() {
            @Override
            public void leftTrigger(double value) {
            }

            @Override
            public void rightTrigger(double value) {
            }

            @Override
            public void buttonY(boolean pressed) {
                if (pressed) {
                    refWrk.buttonYClicked();
                }
            }

            @Override
            public void buttonX(boolean pressed) {
                if (pressed) {
                    refWrk.buttonXClicked();
                }
            }

            @Override
            public void buttonA(boolean pressed) {
                if (pressed) {
                    refWrk.buttonAClicked();
                }
            }

            @Override
            public void buttonB(boolean pressed) {
                if (pressed) {
                    refWrk.buttonBClicked();
                }
            }

            @Override
            public void leftThumbMagnitude(double magnitude) {
                magnitudeGauche = magnitude;
            }

            @Override
            public void leftThumbDirection(double direction) {
                refWrk.moveLeftJoystick(direction, magnitudeGauche);
            }

            @Override
            public void rightThumbMagnitude(double magnitude) {
                magnitudeDroite = magnitude;
            }

            @Override
            public void rightThumbDirection(double direction) {
                refWrk.moveRightJoystick(direction, magnitudeDroite);
            }

            @Override
            public void dpad(int direction, boolean pressed) {
                switch (direction) {
                    case 0:
                        if (pressed) {
                            refWrk.arrowUp();
                        }
                        break;
                    case 4:
                        if (pressed) {
                            refWrk.arrowDown();
                        }
                        break;
                }
            }

            @Override
            public void rightShoulder(boolean pressed) {
            }

            @Override
            public void leftShoulder(boolean pressed) {
            }

            @Override
            public void start(boolean pressed) {
            }

            @Override
            public void isConnected(boolean connected) {
            }

        });
    }

    public void disconnect() {
        if (xc != null) {
            xc.release();

        }
    }

}
