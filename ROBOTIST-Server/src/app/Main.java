package app;

import app.ctrl.Ctrl;
import app.ihm.Ihm;
import app.wrk.Wrk;

/**
 *
 * @author dingl01
 */
public class Main {

    public static void main(String[] args)  {
        Ctrl ctrl = new Ctrl();
        Ihm ihm = new Ihm();
        Wrk wrk = new Wrk();
         
        ctrl.setRefIhm(ihm);
        ctrl.setRefWrk(wrk);
        
        wrk.setRefCtrl(ctrl);
        
        ihm.setRefCtrl(ctrl);
        
        ctrl.startUp();
        
    }
    
}
