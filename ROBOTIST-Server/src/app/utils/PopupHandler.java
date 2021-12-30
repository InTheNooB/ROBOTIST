package app.utils;

import javax.swing.JOptionPane;

/**
 *
 * @author dingl01
 */
public abstract class PopupHandler {

    public static void error(String error) {
        JOptionPane.showMessageDialog(null, error, "Error", 0);
    }

    public static void warning(String warning) {
        JOptionPane.showMessageDialog(null, warning, "Warning", 2);
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
