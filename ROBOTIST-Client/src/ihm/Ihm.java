package ihm;

import ihm.panels.ClientIhm;
import ihm.panels.LoginIhm;
import ihm.panels.SequenceIhm;
import beans.Sequence;
import com.bulenkov.darcula.DarculaLaf;
import ctrl.ItfCtrlIhm;
import ihm.panels.WebcamIhm;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.basic.BasicLookAndFeel;
import wrk.utils.FileUtilies;
import ihm.popup.Popup;

public class Ihm extends javax.swing.JFrame implements ItfIhmCtrl, ItfIhmClientIhm, ItfIhmLoginIhm, ItfIhmSequenceIhm, ItfIhmWebcamIhm {

    private JPanel cards;
    private final String clientPanel = "clientPanel";
    private final String loginPanel = "loginPanel";
    private final String sequencePanel = "sequencePanel";
    private final String webcamPanel = "webcamPanel";
    private ItfCtrlIhm refCtrl;
    private ClientIhm refClientIhm;
    private LoginIhm refLoginIhm;
    private SequenceIhm refSequenceIhm;
    private WebcamIhm refWebcamIhm;
    private boolean isSequencePanel;

    public Ihm() {
        initLookAndFeel();
        initComponents();
        closeEvent();
        createPanel();
        createCardLayout();
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuImageFolder = new javax.swing.JMenuItem();
        separator = new javax.swing.JPopupMenu.Separator();
        menuQuit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jMenu1.setText("File");

        menuImageFolder.setText("Open Image Folder");
        menuImageFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuImageFolderActionPerformed(evt);
            }
        });
        jMenu1.add(menuImageFolder);
        jMenu1.add(separator);

        menuQuit.setText("Quit");
        menuQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuQuitActionPerformed(evt);
            }
        });
        jMenu1.add(menuQuit);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 582, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuQuitActionPerformed
        closeApplication();
    }//GEN-LAST:event_menuQuitActionPerformed

    private void menuImageFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuImageFolderActionPerformed
        FileUtilies.displayFileExplorer();
    }//GEN-LAST:event_menuImageFolderActionPerformed

    private void closeEvent() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeApplication();
            }

        });
    }

    private void closeApplication() {
        refCtrl.closeApplication();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem menuImageFolder;
    private javax.swing.JMenuItem menuQuit;
    private javax.swing.JPopupMenu.Separator separator;
    // End of variables declaration//GEN-END:variables

    private void createPanel() {
        refClientIhm = new ClientIhm(this);
        refLoginIhm = new LoginIhm(this);
        refSequenceIhm = new SequenceIhm(this);
        refWebcamIhm = new WebcamIhm(this);
    }

    private void createCardLayout() {
        cards = new JPanel(new CardLayout());
        cards.add(refClientIhm, clientPanel);
        cards.add(refLoginIhm, loginPanel);
        cards.add(refSequenceIhm, sequencePanel);
        cards.add(refWebcamIhm, webcamPanel);
        setLayout(new BorderLayout());
        add(cards, BorderLayout.CENTER);
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, loginPanel);
    }

    @Override
    public void login(String username, String password) {
        refCtrl.login(username, password);

    }

    @Override
    public void displaySequenceWindow() {
        isSequencePanel = true;
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, sequencePanel);
    }

    public void displayLoginWindow() {
        isSequencePanel = false;
        refLoginIhm.resetWebCamView();
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, loginPanel);
    }

    @Override
    public void displayClientWindow() {
        isSequencePanel = false;
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, clientPanel);
    }

    @Override
    public void displayWebcamWindow() {
        isSequencePanel = false;
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, webcamPanel);
    }

    @Override
    public void displayWebCamFrame(BufferedImage img2) {
        refLoginIhm.displayWebCamFrame(img2);
    }

    @Override
    public void displayFaceDetected(BufferedImage img2) {
        refLoginIhm.displayFaceDetected(img2);
    }

    private void initLookAndFeel() {
        try {
            BasicLookAndFeel darcula = new DarculaLaf();
            UIManager.setLookAndFeel(darcula);
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void checkForFace(boolean b) {
        refCtrl.checkForFace(b);
    }

    @Override
    public void arrowUp() {
        refSequenceIhm.arrowUp();
    }

    @Override
    public void arrowDown() {
        refSequenceIhm.arrowDown();
    }

    @Override
    public void moveRightJoystick(double direction, double magnitudeDroite) {
        refClientIhm.displayRightJoystick(direction, magnitudeDroite);
    }

    @Override
    public void moveLeftJoystick(double direction, double magnitudeGauche) {
        refClientIhm.displayLeftJoystick(direction, magnitudeGauche);
    }

    @Override
    public void buttonAClicked() {
        refSequenceIhm.playSequence();
    }

    @Override
    public void setWebcamState(int state) {
        refLoginIhm.setWebcamState(state);
    }

    @Override
    public void setControllable(int state) {
        refClientIhm.setControllableState(state);
    }

    @Override
    public void logout() {
        displayLoginWindow();
        refCtrl.logout();
    }

    @Override
    public void showError(String error) {
        Popup.error(error);
    }

    @Override
    public void displayRobotVideo(BufferedImage robotVideo) {
        refClientIhm.displayRobotVideo(robotVideo);
    }

    @Override
    public void displaySequence(ArrayList<Sequence> sequenceToDisplay) {
        refSequenceIhm.displaySequence(sequenceToDisplay);
    }

    @Override
    public void displayRobotPicture(BufferedImage robotPicture) {
        refClientIhm.displayRobotPicture(robotPicture);
    }

    @Override
    public void signIn(String username, String password) {
        refCtrl.signIn(username, password);
    }

    @Override
    public void signInOk(boolean ok) {
        if (ok) {
            Popup.info("User created successfully\nYou can now sign in with the user");
        } else {
            Popup.info("There was a problem while creating the user");
        }
    }

    @Override
    public void loginOk(boolean ok) {
        if (ok) {
            Popup.info("You are successfully logged in");
            displayClientWindow();
            refLoginIhm.hideWebcamBtn(false);
        } else {
            Popup.info("Unsuccessful login\n Check credentials and retry");
        }
    }

    @Override
    public void playSequence(Sequence s) {
        if (isSequencePanel) {
            refCtrl.playSequence(s);

        }

    }

    @Override
    public void createSequence(Sequence s) {
        refCtrl.createSequence(s);
    }

    @Override
    public void displayCurrentAction(int currentAction) {
        refSequenceIhm.displayCurrentAction(currentAction);
    }

    @Override
    public void displayPing(String p) {
        refClientIhm.displayPing(p);
    }

    @Override
    public void connect(String serverIp) {
        refCtrl.connect(serverIp);
    }

    @Override
    public void updateServerIp(String ip) {
        if (ip.equals("noServerFound")) {
            Popup.error("Cannot connect to the server");
        } else {
            refLoginIhm.updateServerIp(ip);
            Popup.info("Connection successful");
        }

    }

    @Override
    public void serverDisconnected() {
        Popup.error("Server disconnected unexpectedly");
        displayLoginWindow();
    }

    @Override
    public void setControlMode(boolean b) {
        refCtrl.setControlMode(b);
    }

    @Override
    public void displayColorWebcamFrame(BufferedImage bImage) {
        refWebcamIhm.displayColorWebcamFrame(bImage);
    }

    @Override
    public void updateActionZoneColor(int redActionZoneHeight, int greenActionZoneHeight, int blueActionZoneHeight, int yellowActionZoneHeight) {
        refWebcamIhm.updateActionZoneColor(redActionZoneHeight, greenActionZoneHeight, blueActionZoneHeight, yellowActionZoneHeight);
    }

    @Override
    public void startMotionDetection(boolean start) {
        refCtrl.startMotionDetection(start);
    }

    @Override
    public void appendToConsole(String text) {
        refClientIhm.appendTextConsole(text);
    }

    //Getter and Setter
    public ItfCtrlIhm getRefCtrl() {
        return refCtrl;
    }

    public void setRefCtrl(ItfCtrlIhm refCtrl) {
        this.refCtrl = refCtrl;
    }

    public void clearIhmsFields() {
        refClientIhm.clearIhm();
        refSequenceIhm.clearIhm();
        refLoginIhm.clearIhm();
        refWebcamIhm.clearIhm();
    }

}
