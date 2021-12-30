package app.ihm.panels;

import app.ihm.panels.nodes.ConnectionState;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author dingl01
 */
public class MainIhm extends javax.swing.JPanel {

    public final static String IHMNAME = "main";

    private Thread robotNodeThread;
    private Thread clientNodeThread;
    private Thread databaseNodeThread;

    private final ItfIhmMainIhm refIhm;

    public MainIhm(ItfIhmMainIhm refIhm) {
        this.refIhm = refIhm;
        initComponents();
        initThreads();
    }

    /**
     * Appends text in the console, handles colors : If begins with '#', then
     * the following char defines the color. Exemple : #rHey ! Will be written
     * in red -r Red -g Green -b Blue -y Yellow -o Orange -- White -w White
     *
     * @param txt The text to write
     */
    public synchronized void appendTextConsole(String txt) {
        try {
            StyledDocument doc = txpConsole.getStyledDocument();
            Style style = txpConsole.addStyle("I'm a Style", null);
            String[] words = txt.split(" ");
            Color lastColor = Color.white;
            for (String w : words) {
                if (w.startsWith("#")) {
                    //color handling
                    switch (w.charAt(1)) {
                        case 'r':
                            StyleConstants.setForeground(style, Color.red);
                            lastColor = Color.red;
                            break;
                        case 'g':
                            StyleConstants.setForeground(style, Color.green);
                            lastColor = Color.green;
                            break;
                        case 'b':
                            StyleConstants.setForeground(style, Color.blue);
                            lastColor = Color.blue;
                            break;
                        case 'y':
                            StyleConstants.setForeground(style, Color.yellow);
                            lastColor = Color.yellow;
                            break;
                        case 'c':
                            StyleConstants.setForeground(style, Color.cyan);
                            lastColor = Color.cyan;
                            break;
                        case 'o':
                            StyleConstants.setForeground(style, Color.orange);
                            lastColor = Color.orange;
                            break;
                        case 'm':
                            StyleConstants.setForeground(style, Color.magenta);
                            lastColor = Color.magenta;
                            break;
                        case 'p':
                            StyleConstants.setForeground(style, Color.pink);
                            lastColor = Color.pink;
                            break;
                        case 'w':
                        case '-':
                        default:
                            StyleConstants.setForeground(style, Color.white);
                            lastColor = Color.white;
                            break;
                    }
                    doc.insertString(doc.getLength(), w.substring(2), style);
                } else {
                    StyleConstants.setForeground(style, lastColor);
                    doc.insertString(doc.getLength(), w, style);
                }
                doc.insertString(doc.getLength(), " ", style);

            }
            doc.insertString(doc.getLength(), "\n", style);
        } catch (BadLocationException ex) {
        }
    }

    /**
     * Changes the state of the connection in the Robot Node and renders a green
     * /red circle or a loading animation depending on the connection state.
     *
     * @param state The state of the connection on)
     */
    public void setRobotConnected(ConnectionState state) {
        robotNode.setState(state);
        synchronized (robotNode) {
            robotNode.notify();
        }
    }

    /**
     * Changes the state of the connection in the Client Node and renders a
     * green /red circle or a loading animation depending on the connection
     * state.
     *
     * @param state The state of the connection on)
     */
    public void setClientLoggedIn(ConnectionState state) {
        clientNode.setState(state);
        synchronized (clientNode) {
            clientNode.notify();
        }
    }

    /**
     * Changes the state of the connection in the Database Node and renders a
     * green /red circle or a loading animation depending on the connection
     * state.
     *
     * @param state The state of the connection on)
     */
    public void setDatabaseConnected(ConnectionState state) {
        databaseNode.setState(state);
        synchronized (databaseNode) {
            databaseNode.notify();
        }
    }

    private void initThreads() {
        robotNodeThread = new Thread(robotNode);
        robotNodeThread.setName("Robot Node");
        robotNodeThread.start();
        clientNodeThread = new Thread(clientNode);
        clientNodeThread.setName("Client Node");
        clientNodeThread.start();
        databaseNodeThread = new Thread(databaseNode);
        databaseNodeThread.setName("Database Node");
        databaseNodeThread.start();
    }

    public void enableRobotButtons() {
        btnConnectRobot.setEnabled(true);
        btnInitRobot.setEnabled(true);
    }

    public void disableRobotButtons() {
        btnConnectRobot.setEnabled(false);
        btnInitRobot.setEnabled(false);
    }

    public void setLoggedUserFace(BufferedImage image) {
        pnlLoggedUserFace.setImage(image);
        pnlLoggedUserFace.repaint();
    }

    /**
     * Kills safely the node threads
     */
    public void exitApplication() {
        robotNode.setRunning(false);
        clientNode.setRunning(false);
        databaseNode.setRunning(false);

        synchronized (robotNode) {
            robotNode.notify();
        }
        synchronized (clientNode) {
            clientNode.notify();
        }
        synchronized (databaseNode) {
            databaseNode.notify();
        }

        try {
            robotNodeThread.join();
            clientNodeThread.join();
            databaseNodeThread.join();
        } catch (InterruptedException ex) {
        }
        robotNodeThread = null;
        clientNodeThread = null;
        databaseNodeThread = null;
    }

    public JTextField getTxfSessionTime() {
        return txfSessionTime;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblRobotconnect = new javax.swing.JLabel();
        lblClientConnected = new javax.swing.JLabel();
        lblDatabaseConnected = new javax.swing.JLabel();
        tbtFreezeRobot = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txpConsole = new javax.swing.JTextPane();
        btnUsers = new javax.swing.JButton();
        btnSequences = new javax.swing.JButton();
        btnLogs = new javax.swing.JButton();
        lblConsole = new javax.swing.JLabel();
        lblConnectedUser = new javax.swing.JLabel();
        lblSessionTime = new javax.swing.JLabel();
        txfSessionTime = new javax.swing.JTextField();
        btnInitRobot = new javax.swing.JButton();
        btnConnectRobot = new javax.swing.JButton();
        robotNode = new app.ihm.panels.nodes.NodePanel();
        databaseNode = new app.ihm.panels.nodes.NodePanel();
        clientNode = new app.ihm.panels.nodes.NodePanel();
        pnlLoggedUserFace = new app.ihm.panels.ImagePanel();

        lblRobotconnect.setText("Robot Connected : ");

        lblClientConnected.setText("Client Logged In:");

        lblDatabaseConnected.setText("Database Connected :");

        tbtFreezeRobot.setText("FREEZE ROBOT");
        tbtFreezeRobot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbtFreezeRobotActionPerformed(evt);
            }
        });

        txpConsole.setEditable(false);
        txpConsole.setBackground(new java.awt.Color(0, 0, 0));
        txpConsole.setDoubleBuffered(true);
        jScrollPane1.setViewportView(txpConsole);

        btnUsers.setText("Users");
        btnUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUsersActionPerformed(evt);
            }
        });

        btnSequences.setText("Sequences");
        btnSequences.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSequencesActionPerformed(evt);
            }
        });

        btnLogs.setText("Logs");
        btnLogs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogsActionPerformed(evt);
            }
        });

        lblConsole.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblConsole.setText("Console");

        lblConnectedUser.setText("Logged User");

        lblSessionTime.setText("Current Session time :");

        txfSessionTime.setEditable(false);
        txfSessionTime.setFocusable(false);
        txfSessionTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfSessionTimeActionPerformed(evt);
            }
        });

        btnInitRobot.setText("Init Robot");
        btnInitRobot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInitRobotActionPerformed(evt);
            }
        });

        btnConnectRobot.setText("Connect Robot");
        btnConnectRobot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectRobotActionPerformed(evt);
            }
        });

        robotNode.setPreferredSize(new java.awt.Dimension(20, 20));

        javax.swing.GroupLayout robotNodeLayout = new javax.swing.GroupLayout(robotNode);
        robotNode.setLayout(robotNodeLayout);
        robotNodeLayout.setHorizontalGroup(
            robotNodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        robotNodeLayout.setVerticalGroup(
            robotNodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        databaseNode.setPreferredSize(new java.awt.Dimension(20, 20));

        javax.swing.GroupLayout databaseNodeLayout = new javax.swing.GroupLayout(databaseNode);
        databaseNode.setLayout(databaseNodeLayout);
        databaseNodeLayout.setHorizontalGroup(
            databaseNodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        databaseNodeLayout.setVerticalGroup(
            databaseNodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        clientNode.setPreferredSize(new java.awt.Dimension(20, 20));

        javax.swing.GroupLayout clientNodeLayout = new javax.swing.GroupLayout(clientNode);
        clientNode.setLayout(clientNodeLayout);
        clientNodeLayout.setHorizontalGroup(
            clientNodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        clientNodeLayout.setVerticalGroup(
            clientNodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlLoggedUserFaceLayout = new javax.swing.GroupLayout(pnlLoggedUserFace);
        pnlLoggedUserFace.setLayout(pnlLoggedUserFaceLayout);
        pnlLoggedUserFaceLayout.setHorizontalGroup(
            pnlLoggedUserFaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 306, Short.MAX_VALUE)
        );
        pnlLoggedUserFaceLayout.setVerticalGroup(
            pnlLoggedUserFaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 330, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(139, 139, 139)
                        .addComponent(lblConnectedUser))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlLoggedUserFace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(tbtFreezeRobot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblSessionTime)
                                        .addComponent(lblRobotconnect))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(robotNode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txfSessionTime, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblDatabaseConnected)
                                        .addComponent(lblClientConnected))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(databaseNode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(clientNode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(2, 2, 2))))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnConnectRobot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnInitRobot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(23, 23, 23)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnSequences, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                                    .addComponent(btnUsers, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnLogs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 228, Short.MAX_VALUE)
                        .addComponent(lblConsole)
                        .addGap(218, 218, 218))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSessionTime)
                    .addComponent(txfSessionTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblConsole))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnUsers)
                            .addComponent(btnInitRobot))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSequences)
                            .addComponent(btnConnectRobot)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblRobotconnect)
                            .addComponent(robotNode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblClientConnected)
                            .addComponent(clientNode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblDatabaseConnected)
                                .addGap(25, 25, 25)
                                .addComponent(lblConnectedUser))
                            .addComponent(databaseNode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnlLoggedUserFace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLogs)
                    .addComponent(tbtFreezeRobot))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tbtFreezeRobotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbtFreezeRobotActionPerformed
        if (tbtFreezeRobot.isSelected()) {
            // Pressed
            refIhm.freezeRobot();
        } else {
            // Unpressed
            refIhm.unfreezeRobot();
        }
    }//GEN-LAST:event_tbtFreezeRobotActionPerformed

    private void btnUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUsersActionPerformed
        refIhm.navigateBetweenIhm(UsersIhm.IHMNAME);
    }//GEN-LAST:event_btnUsersActionPerformed

    private void btnSequencesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSequencesActionPerformed
        refIhm.navigateBetweenIhm(SequencesIhm.IHMNAME);
    }//GEN-LAST:event_btnSequencesActionPerformed

    private void btnLogsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogsActionPerformed
        refIhm.navigateBetweenIhm(LogsIhm.IHMNAME);
    }//GEN-LAST:event_btnLogsActionPerformed

    private void txfSessionTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfSessionTimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfSessionTimeActionPerformed

    private void btnInitRobotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInitRobotActionPerformed
        refIhm.initRobot();
    }//GEN-LAST:event_btnInitRobotActionPerformed

    private void btnConnectRobotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectRobotActionPerformed
        refIhm.connectRobot();
    }//GEN-LAST:event_btnConnectRobotActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConnectRobot;
    private javax.swing.JButton btnInitRobot;
    private javax.swing.JButton btnLogs;
    private javax.swing.JButton btnSequences;
    private javax.swing.JButton btnUsers;
    private app.ihm.panels.nodes.NodePanel clientNode;
    private app.ihm.panels.nodes.NodePanel databaseNode;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblClientConnected;
    private javax.swing.JLabel lblConnectedUser;
    private javax.swing.JLabel lblConsole;
    private javax.swing.JLabel lblDatabaseConnected;
    private javax.swing.JLabel lblRobotconnect;
    private javax.swing.JLabel lblSessionTime;
    private app.ihm.panels.ImagePanel pnlLoggedUserFace;
    private app.ihm.panels.nodes.NodePanel robotNode;
    private javax.swing.JToggleButton tbtFreezeRobot;
    private javax.swing.JTextField txfSessionTime;
    private javax.swing.JTextPane txpConsole;
    // End of variables declaration//GEN-END:variables

}
