package ihm.panels;

import ihm.ItfIhmWebcamIhm;
import java.awt.image.BufferedImage;
import javax.swing.JToggleButton;

public class WebcamIhm extends javax.swing.JPanel {

    private ItfIhmWebcamIhm refIhm;

    public WebcamIhm(ItfIhmWebcamIhm ihm) {
        this.refIhm = ihm;
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlWebcam = new ihm.panels.WebcamPanel();
        homeBtn = new javax.swing.JButton();
        pnlActionZone = new ihm.panels.ActionZonePanel();
        startTgl = new javax.swing.JToggleButton();

        pnlWebcam.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout pnlWebcamLayout = new javax.swing.GroupLayout(pnlWebcam);
        pnlWebcam.setLayout(pnlWebcamLayout);
        pnlWebcamLayout.setHorizontalGroup(
            pnlWebcamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 601, Short.MAX_VALUE)
        );
        pnlWebcamLayout.setVerticalGroup(
            pnlWebcamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 483, Short.MAX_VALUE)
        );

        homeBtn.setText("Home");
        homeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeBtnActionPerformed(evt);
            }
        });

        pnlActionZone.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout pnlActionZoneLayout = new javax.swing.GroupLayout(pnlActionZone);
        pnlActionZone.setLayout(pnlActionZoneLayout);
        pnlActionZoneLayout.setHorizontalGroup(
            pnlActionZoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 144, Short.MAX_VALUE)
        );
        pnlActionZoneLayout.setVerticalGroup(
            pnlActionZoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        startTgl.setText("Start");
        startTgl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startTglActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnlWebcam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(homeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(startTgl, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(pnlActionZone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(305, 305, 305))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pnlActionZone, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(homeBtn)
                            .addComponent(startTgl))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnlWebcam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void homeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeBtnActionPerformed
        startTgl.setSelected(false);

        startTgl.setText("Start");
        refIhm.startMotionDetection(false);
        refIhm.displayClientWindow();
    }//GEN-LAST:event_homeBtnActionPerformed

    private void startTglActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startTglActionPerformed
        boolean start = false;
        if (startTgl.isSelected()) {
            start = true;
            startTgl.setText("Stop");
            pnlWebcam.setImage(null);
        } else {
            startTgl.setText("Start");
        }
        refIhm.startMotionDetection(start);
    }//GEN-LAST:event_startTglActionPerformed

    public void displayColorWebcamFrame(BufferedImage bImage) {
        pnlWebcam.setImage(bImage);
        pnlWebcam.repaint();
    }

    public void updateActionZoneColor(int redActionZoneHeight, int greenActionZoneHeight, int blueActionZoneHeight, int yellowActionZoneHeight) {
        pnlActionZone.setRedInActionZone(redActionZoneHeight);
        pnlActionZone.setGreenInActionZone(greenActionZoneHeight);
        pnlActionZone.setBlueInActionZone(blueActionZoneHeight);
        pnlActionZone.setYellowInActionZone(yellowActionZoneHeight);
        pnlActionZone.repaint();
    }

    public JToggleButton getStartTgl() {
        return startTgl;
    }

    public void setStartTgl(JToggleButton startTgl) {
        this.startTgl = startTgl;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton homeBtn;
    private ihm.panels.ActionZonePanel pnlActionZone;
    private ihm.panels.WebcamPanel pnlWebcam;
    private javax.swing.JToggleButton startTgl;
    // End of variables declaration//GEN-END:variables

    public void clearIhm() {
        startTgl.setSelected(false);
        pnlWebcam.setImage(null);
        startTgl.setText("Start");

    }

}
