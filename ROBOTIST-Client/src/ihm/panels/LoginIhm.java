package ihm.panels;

import ihm.ItfIhmLoginIhm;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import ihm.popup.Popup;

public class LoginIhm extends javax.swing.JPanel {

    private ItfIhmLoginIhm refIhm;
    private int countFace = 0;
    private boolean faceDetected = false;
    private Thread webcamStateThread;

    public LoginIhm(ItfIhmLoginIhm ihm) {
        initComponents();
        this.refIhm = ihm;
        hideForm(false);
        hideWebcamBtn(false);
        initThread();
    }

    public void setWebcamState(int state) {
        webcamState.setState(state);
        synchronized (webcamState) {
            webcamState.notify();
        }
    }

    private void initThread() {
        webcamStateThread = new Thread(webcamState);
        webcamStateThread.start();
    }

    public void displayWebCamFrame(BufferedImage img2) {
        if (img2 != null) {
            if (faceDetected == false) {
                Image dimg = img2.getScaledInstance(webcamLabel.getWidth(), webcamLabel.getHeight(),
                        Image.SCALE_SMOOTH);
                webcamLabel.setIcon(new ImageIcon(dimg));
            }
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        loginBtn = new javax.swing.JButton();
        signinBtn = new javax.swing.JButton();
        passwordField = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        webcamLabel = new javax.swing.JLabel();
        checkForFaceBtn = new javax.swing.JButton();
        webcamState = new ihm.nodes.NodePanel();
        jLabel5 = new javax.swing.JLabel();
        serverField = new javax.swing.JTextField();
        connectToServer = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("ROBOTIST");

        usernameField.setText("Table");
        usernameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameFieldActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Username :");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Password :");

        loginBtn.setText("Login");
        loginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginBtnActionPerformed(evt);
            }
        });

        signinBtn.setText("Sign In");
        signinBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signinBtnActionPerformed(evt);
            }
        });

        passwordField.setText("password");

        jLabel4.setText("Webcam Status : ");

        webcamLabel.setToolTipText("");
        webcamLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        checkForFaceBtn.setText("Check For Face");
        checkForFaceBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkForFaceBtnActionPerformed(evt);
            }
        });

        webcamState.setPreferredSize(new java.awt.Dimension(24, 24));

        javax.swing.GroupLayout webcamStateLayout = new javax.swing.GroupLayout(webcamState);
        webcamState.setLayout(webcamStateLayout);
        webcamStateLayout.setHorizontalGroup(
            webcamStateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );
        webcamStateLayout.setVerticalGroup(
            webcamStateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Server : ");

        serverField.setText("localhost");
        serverField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serverFieldActionPerformed(evt);
            }
        });

        connectToServer.setText("Connect");
        connectToServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectToServerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(174, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(webcamLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel3))
                                .addGap(19, 19, 19)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(signinBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(passwordField)
                                    .addComponent(usernameField, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(serverField, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(18, 18, 18))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(webcamState, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(253, 253, 253)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(checkForFaceBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                            .addComponent(connectToServer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(139, 139, 139))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(serverField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(connectToServer, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loginBtn)
                    .addComponent(signinBtn))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkForFaceBtn)
                    .addComponent(webcamState, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addComponent(webcamLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void loginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginBtnActionPerformed
        refIhm.login(usernameField.getText(), new String(passwordField.getPassword()));
    }//GEN-LAST:event_loginBtnActionPerformed

    private void checkForFaceBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkForFaceBtnActionPerformed
        refIhm.checkForFace(true);
    }//GEN-LAST:event_checkForFaceBtnActionPerformed

    private void signinBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signinBtnActionPerformed
        refIhm.signIn(usernameField.getText(), new String(passwordField.getPassword()));
    }//GEN-LAST:event_signinBtnActionPerformed

    private void connectToServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectToServerActionPerformed
        String serverIp = serverField.getText();
        refIhm.connect(serverIp);
    }//GEN-LAST:event_connectToServerActionPerformed

    private void usernameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usernameFieldActionPerformed

    private void serverFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serverFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_serverFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton checkForFaceBtn;
    private javax.swing.JButton connectToServer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JButton loginBtn;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JTextField serverField;
    private javax.swing.JButton signinBtn;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel webcamLabel;
    private ihm.nodes.NodePanel webcamState;
    // End of variables declaration//GEN-END:variables

    public void displayFaceDetected(BufferedImage img2) {
        countFace += 1;
        faceDetected = true;
        if (countFace == 1) {
            Image dimg = img2.getScaledInstance(webcamLabel.getWidth(), webcamLabel.getHeight(),
                    Image.SCALE_SMOOTH);
            webcamLabel.setIcon(new ImageIcon(dimg));
            hideForm(true);
        }

    }

    private void hideForm(boolean hide) {
        usernameField.setEnabled(hide);
        passwordField.setEnabled(hide);
        loginBtn.setEnabled(hide);
        signinBtn.setEnabled(hide);
    }

    public void resetWebCamView() {
        hideForm(false);
        refIhm.checkForFace(false);
        faceDetected = false;
        countFace = 0;

    }

    public void loginKo() {
        Popup.error("Wrong Credentials");
    }

    public void updateServerIp(String ip) {
        checkForFaceBtn.setVisible(true);
        serverField.setText(ip);
    }

    public void hideWebcamBtn(boolean visible) {
        checkForFaceBtn.setVisible(visible);
    }

    public void clearIhm() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
