package app.ihm;

import app.beans.Log;
import app.beans.Sequence;
import app.beans.User;
import app.ihm.panels.LogsIhm;
import app.ihm.panels.MainIhm;
import app.ihm.panels.UsersIhm;
import app.ihm.panels.SequencesIhm;
import app.ctrl.ItfCtrlIhm;
import app.ihm.panels.ItfIhmLogsIhm;
import app.ihm.panels.ItfIhmMainIhm;
import app.ihm.panels.ItfIhmSequencesIhm;
import app.ihm.panels.ItfIhmUsersIhm;
import app.ihm.panels.nodes.ConnectionState;
import static app.utils.DateHandler.findTimeDifference;
import app.utils.SequenceFileHandler;
import com.bulenkov.darcula.DarculaLaf;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.plaf.basic.BasicLookAndFeel;

/**
 *
 * @author dingl01
 */
public class Ihm extends javax.swing.JFrame implements ItfIhmMainIhm, ItfIhmLogsIhm, ItfIhmSequencesIhm, ItfIhmUsersIhm, ItfIhmCtrl {

    private ItfCtrlIhm refCtrl;

    private JPanel cardLayout;
    private MainIhm mainIhm;
    private UsersIhm usersIhm;
    private LogsIhm logsIhm;
    private SequencesIhm sequencesIhm;

    /**
     * Creates new form Ihm
     */
    public Ihm() {
        initLookAndFeel();
        initComponents();
        initCardLayout();
        initEventListeners();
        closeEvent();
        setVisible(true);
    }

    private void initEventListeners() {
        // Triggered when selected log is changed (Mouse or arrows)
        logsIhm.getLstLogs().addListSelectionListener((ListSelectionEvent lse) -> {
            // Prevents the event to be called twice per change
            if (!lse.getValueIsAdjusting()) {
                updateSelectedLog();
            }
        });

        // Triggered when selected sequence is changed (Mouse or arrows)
        sequencesIhm.getLstSequences().addListSelectionListener((ListSelectionEvent lse) -> {
            // Prevents the event to be called twice per change
            if (!lse.getValueIsAdjusting()) {
                updateSelectedSequence();
            }
        });
    }

    private void updateSelectedSequence() {
        // First find the current selected sequence
        Sequence selectedSequence = sequencesIhm.getLstSequences().getSelectedValue();

        if (selectedSequence == null) {
            return;
        }

        // Update the Username text field
        sequencesIhm.getTxfUsername().setText(selectedSequence.getFkUser().getUsername());

        // Clear the list of actions
        sequencesIhm.getLstActions().setListData(new Vector());

        // Read the sequence file
        String sqcFile = new String(selectedSequence.getActionFile(), StandardCharsets.UTF_8);
        Vector actions = new Vector();
        if ((sqcFile.isEmpty()) || (!SequenceFileHandler.checkSequenceFileSyntax(sqcFile))) {
            actions.add("Unreadable Sequence File \"" + selectedSequence.getSequenceName() + "\"");
        } else {

            // Remove the first and last char
            sqcFile = sqcFile.replace("[", "").replace("]", "");

            // Foreach action in the sequence
            for (String action : sqcFile.split(";")) {
                // Get the name of the action
                String[] a = action.split(",");
                actions.add(a[0] + a[1]);
            }
        }
        sequencesIhm.getLstActions().setListData(actions);
    }

    /**
     * Updates the 2 TextField and the panel containing the image in the
     * LogPanel according to the currently selected log. Must be called to
     * update after a new log has been selected.
     */
    private void updateSelectedLog() {
        // First find the current selected log
        Log selectedLog = logsIhm.getLstLogs().getSelectedValue();
        if (selectedLog == null) {
            return;
        }

        // Updates the username text field
        logsIhm.getTxfUsername().setText(selectedLog.getFkUser().getUsername());

        // Calculate and update the session time text field
        Date endTime;
        if (selectedLog.getEndTime() != null) {
            endTime = selectedLog.getEndTime();
        } else {
            endTime = Calendar.getInstance().getTime();
        }
        logsIhm.getTxfSessionTime().setText(findTimeDifference(selectedLog.getStartTime(), endTime) + "min");

        // Update the image
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(selectedLog.getPicture());
            BufferedImage image = ImageIO.read(bis);
            logsIhm.getPnlUserFace().setImage(image);
            logsIhm.getPnlUserFace().repaint();
        } catch (IOException ex) {
            appendTextConsole("#w[IHM] #oError loading the image of log [" + selectedLog.getPkLog() + "]");
        }

    }

    /**
     * Closes the application after ending every thread and calling the Garbage
     * Collector.
     */
    private void closeEvent() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                refCtrl.exitApplication();
            }
        });
    }

    @Override
    public void exitApplication() {
        mainIhm.exitApplication();
    }

    private void initLookAndFeel() {
        try {
            BasicLookAndFeel darcula = new DarculaLaf();
            UIManager.setLookAndFeel(darcula);
        } catch (UnsupportedLookAndFeelException ex) {
        }
    }

    private void initCardLayout() {
        // Creates the CardLayout
        cardLayout = new JPanel(new CardLayout());

        // Creates the different JPanel
        mainIhm = new MainIhm(this);
        usersIhm = new UsersIhm(this);
        logsIhm = new LogsIhm(this);
        sequencesIhm = new SequencesIhm(this);

        // Adds the JPanel to the CardLayout
        cardLayout.add(mainIhm, MainIhm.IHMNAME);
        cardLayout.add(usersIhm, UsersIhm.IHMNAME);
        cardLayout.add(logsIhm, LogsIhm.IHMNAME);
        cardLayout.add(sequencesIhm, SequencesIhm.IHMNAME);

        // Setup the CardLayout
        setLayout(new BorderLayout());
        add(cardLayout, BorderLayout.CENTER);
        CardLayout cl = (CardLayout) (cardLayout.getLayout());

        // Set the "mainIhm" as the current "layout"
        cl.show(cardLayout, MainIhm.IHMNAME);
    }

    @Override
    public void navigateBetweenIhm(String ihmName) {
        if ((ihmName != null) && (!ihmName.isEmpty())) {
            CardLayout cl = (CardLayout) (cardLayout.getLayout());
            cl.show(cardLayout, ihmName);
        }
    }

    @Override
    public void enableRobotButtons() {
        mainIhm.enableRobotButtons();
    }

    @Override
    public void disableRobotButtons() {
        mainIhm.disableRobotButtons();
    }

    @Override
    public void setRobotConnected(ConnectionState state) {
        mainIhm.setRobotConnected(state);
    }

    @Override
    public void setClientLoggedIn(ConnectionState state) {
        mainIhm.setClientLoggedIn(state);
    }

    @Override
    public void setDatabaseConnected(ConnectionState state) {
        mainIhm.setDatabaseConnected(state);
    }

    @Override
    public void appendTextConsole(String txt) {
        mainIhm.appendTextConsole(txt);
    }

    @Override
    public void setLoggedUserFace(BufferedImage image) {
        mainIhm.setLoggedUserFace(image);
    }

    @Override
    public void freezeRobot() {
        refCtrl.freezeRobot();
    }

    @Override
    public void unfreezeRobot() {
        refCtrl.unfreezeRobot();
    }

    @Override
    public void getLogList() {
        refCtrl.getLogList();
    }

    @Override
    public void getSequenceList() {
        refCtrl.getSequenceList();
    }

    @Override
    public List<Sequence> deleteSequence(Sequence sequence) {
        return refCtrl.deleteSequence(sequence);
    }

    @Override
    public void getUserList() {
        refCtrl.getUserList();
    }

    @Override
    public List<User> addUser(String username, String password) {
        return refCtrl.addUser(username, password);
    }

    @Override
    public List<User> deleteUser(User u) {
        return refCtrl.deleteUser(u);
    }

    @Override
    public void setUserList(List<User> users) {
        if (users != null) {
            usersIhm.fillList(users);
        }
    }

    @Override
    public void setSequenceList(List<Sequence> sequences) {
        if (sequences != null) {
            sequencesIhm.fillList(sequences);
        }
    }

    @Override
    public void setLogList(List<Log> logs) {
        if (logs != null) {
            logsIhm.fillList(logs);
        }
    }

    public void setRefCtrl(ItfCtrlIhm refCtrl) {
        this.refCtrl = refCtrl;
    }

    @Override
    public void initRobot() {
        refCtrl.initRobot();
    }

    @Override
    public void connectRobot() {
        refCtrl.connectRobot();
    }

    @Override
    public void setCurrentSessionTime(String time) {
        mainIhm.getTxfSessionTime().setText(time);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("ROBOTIST Server");
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 868, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 597, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
