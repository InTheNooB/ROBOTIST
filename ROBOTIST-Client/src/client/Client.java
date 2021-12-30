package client;

import ctrl.Ctrl;
import ihm.Ihm;
import javax.swing.SwingUtilities;
import wrk.Wrk;

public class Client {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            Ihm ihm = new Ihm();
            Ctrl ctrl = new Ctrl();
            Wrk wrk = new Wrk();
            
            ihm.setRefCtrl(ctrl);
            
            ctrl.setRefIhm(ihm);
            
            ctrl.setRefWrk(wrk);
            
            wrk.setRefCtrl(ctrl);
            
            ctrl.startUp();
        });

    }

}
