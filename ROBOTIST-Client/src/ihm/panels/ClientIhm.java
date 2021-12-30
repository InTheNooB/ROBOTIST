package ihm.panels;

import ihm.ItfIhmClientIhm;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ClientIhm extends javax.swing.JPanel {

    private ItfIhmClientIhm refIhm;
    private Thread controllableThread;
    private boolean xboxMode = true;
    private StyledDocument doc;

    public ClientIhm(ItfIhmClientIhm ihm) {
        initComponents();
        this.refIhm = ihm;
        controllableThread = new Thread(controllableNode);
        controllableThread.start();
    }

    public void setControllableState(int state) {
        controllableNode.setState(state);
        synchronized (controllableNode) {
            controllableNode.notify();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        pingLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        switchInputModeBtn = new javax.swing.JButton();
        currentModeLabel = new javax.swing.JLabel();
        sequenceButton = new javax.swing.JButton();
        logoutButton = new javax.swing.JButton();
        leftJoystick = new ihm.JoyStick();
        jLabel4 = new javax.swing.JLabel();
        rightJoystick = new ihm.JoyStick();
        controllableNode = new ihm.nodes.NodePanel();
        robotVideoLbl = new javax.swing.JLabel();
        robotPictureLbl = new javax.swing.JLabel();
        webcamBtn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txpConsole = new javax.swing.JTextPane();

        setPreferredSize(new java.awt.Dimension(500, 500));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Robot View");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Last Picture");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Console");

        pingLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        pingLabel.setForeground(new java.awt.Color(255, 255, 255));
        pingLabel.setText("Ping :   ");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Controllable :");

        switchInputModeBtn.setText("Switch Input Mode");
        switchInputModeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                switchInputModeBtnActionPerformed(evt);
            }
        });

        currentModeLabel.setForeground(new java.awt.Color(255, 255, 255));
        currentModeLabel.setText("Current Mode : XBOX");

        sequenceButton.setText("Sequence");
        sequenceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sequenceButtonActionPerformed(evt);
            }
        });

        logoutButton.setText("Logout");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout leftJoystickLayout = new javax.swing.GroupLayout(leftJoystick);
        leftJoystick.setLayout(leftJoystickLayout);
        leftJoystickLayout.setHorizontalGroup(
            leftJoystickLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 125, Short.MAX_VALUE)
        );
        leftJoystickLayout.setVerticalGroup(
            leftJoystickLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("ROBOTIST");

        javax.swing.GroupLayout rightJoystickLayout = new javax.swing.GroupLayout(rightJoystick);
        rightJoystick.setLayout(rightJoystickLayout);
        rightJoystickLayout.setHorizontalGroup(
            rightJoystickLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );
        rightJoystickLayout.setVerticalGroup(
            rightJoystickLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout controllableNodeLayout = new javax.swing.GroupLayout(controllableNode);
        controllableNode.setLayout(controllableNodeLayout);
        controllableNodeLayout.setHorizontalGroup(
            controllableNodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 18, Short.MAX_VALUE)
        );
        controllableNodeLayout.setVerticalGroup(
            controllableNodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 18, Short.MAX_VALUE)
        );

        webcamBtn.setText("Webcam");
        webcamBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                webcamBtnActionPerformed(evt);
            }
        });

        txpConsole.setBackground(new java.awt.Color(0, 0, 0));
        txpConsole.setForeground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setViewportView(txpConsole);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(leftJoystick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(rightJoystick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(61, 61, 61)
                                        .addComponent(jLabel4))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(156, 156, 156)
                                        .addComponent(jLabel2)
                                        .addGap(168, 168, 168)
                                        .addComponent(jLabel3))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(robotVideoLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(26, 26, 26)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(robotPictureLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel6)
                                                .addGap(8, 8, 8)
                                                .addComponent(controllableNode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(pingLabel))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(sequenceButton, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(webcamBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(43, 43, 43)
                                        .addComponent(logoutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(switchInputModeBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(currentModeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rightJoystick, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(leftJoystick, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addGap(25, 25, 25)
                        .addComponent(robotVideoLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(robotPictureLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(pingLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(controllableNode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(54, 54, 54)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(switchInputModeBtn)
                    .addComponent(sequenceButton)
                    .addComponent(currentModeLabel)
                    .addComponent(logoutButton)
                    .addComponent(webcamBtn))
                .addGap(40, 40, 40))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void sequenceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sequenceButtonActionPerformed
        refIhm.displaySequenceWindow();
    }//GEN-LAST:event_sequenceButtonActionPerformed

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        refIhm.logout();
    }//GEN-LAST:event_logoutButtonActionPerformed

    private void switchInputModeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_switchInputModeBtnActionPerformed
        xboxMode = !xboxMode;
        if (xboxMode) {
            currentModeLabel.setText("Current Mode : Xbox");
            refIhm.setControlMode(true);

        } else {
            currentModeLabel.setText("Current Mode : Webcam");
            refIhm.setControlMode(false);

        }
    }//GEN-LAST:event_switchInputModeBtnActionPerformed

    private void webcamBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webcamBtnActionPerformed
        refIhm.displayWebcamWindow();
    }//GEN-LAST:event_webcamBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ihm.nodes.NodePanel controllableNode;
    private javax.swing.JLabel currentModeLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane2;
    private ihm.JoyStick leftJoystick;
    private javax.swing.JButton logoutButton;
    private javax.swing.JLabel pingLabel;
    private ihm.JoyStick rightJoystick;
    private javax.swing.JLabel robotPictureLbl;
    private javax.swing.JLabel robotVideoLbl;
    private javax.swing.JButton sequenceButton;
    private javax.swing.JButton switchInputModeBtn;
    private javax.swing.JTextPane txpConsole;
    private javax.swing.JButton webcamBtn;
    // End of variables declaration//GEN-END:variables

    public void displayRightJoystick(double direction, double rightMag) {
        rightJoystick.setMag(rightMag);
        rightJoystick.setAngle(direction);
        rightJoystick.repaint();
    }

    public void displayLeftJoystick(double direction, double leftMag) {
        leftJoystick.setMag(leftMag);
        leftJoystick.setAngle(direction);
        leftJoystick.repaint();

    }

    public void displayRobotVideo(BufferedImage robotVideo) {
        if (robotVideo != null) {
            Image dimg = robotVideo.getScaledInstance(robotVideoLbl.getWidth(), robotVideoLbl.getHeight(),
                    Image.SCALE_SMOOTH);
            robotVideoLbl.setIcon(new ImageIcon(dimg));
        }

    }

    public void displayRobotPicture(BufferedImage robotPicture) {
        if (robotPicture != null) {
            Image dimg = robotPicture.getScaledInstance(robotPictureLbl.getWidth(), robotPictureLbl.getHeight(),
                    Image.SCALE_SMOOTH);
            robotPictureLbl.setIcon(new ImageIcon(dimg));
        }

    }

    public void displayPing(String p) {
        pingLabel.setText("Ping : " + p);
    }

    public synchronized void appendTextConsole(String txt) {
        try {
            doc = txpConsole.getStyledDocument();
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
                        default:
                        case 'w':
                        case '-':
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

    public void clearIhm() {
        robotVideoLbl.setIcon(null);
        robotPictureLbl.setIcon(null);
        xboxMode = true;
        currentModeLabel.setText("Current Mode : XBOX");
        pingLabel.setText("Ping :");
        controllableNode.setState(0);
        synchronized (controllableNode) {
            controllableNode.notify();
        }
    }

}
