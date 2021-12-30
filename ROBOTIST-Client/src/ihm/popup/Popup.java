/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ihm.popup;

import javax.swing.JOptionPane;

/**
 *
 * @author MayencourtE
 */
public class Popup {

    public static void error(String error) {
        JOptionPane.showMessageDialog(null, error, "Error", 0);
    }

    public static void warning(String warning) {
        JOptionPane.showMessageDialog(null, warning, "Warning", 2);
    }

    public static void info(String info) {
        JOptionPane.showMessageDialog(null, info, "Success", 1);
    }

    public static String askData(String txt) {
        return (String) JOptionPane.showInputDialog(txt);
    }

    /**
     * Ask for a user confirmation
     *
     * @param txt The text to display
     * @return 0 (YES), 1 (NO), 2 (CANCEL)
     */
    public static int askConfirmation(String txt) {
        return JOptionPane.showConfirmDialog(null, txt);
    }
}
