/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package net.spotifei.Views.Panels;

import javax.swing.JButton;
import javax.swing.JLabel;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Views.MainFrame;

import static net.spotifei.Helpers.AssetsLoader.loadImageIcon;

/**
 *
 * @author fengl
 */
public class RedirectPanel extends javax.swing.JPanel {

    private final MainFrame mainframe;
    private final AppContext appContext;
    
    public RedirectPanel(MainFrame mainframe, AppContext appContext) {
        initComponents();
        this.mainframe = mainframe;
        this.appContext = appContext;     
    }

    public JButton getBt_historico() {
        return bt_historico;
    }

    public void setBt_historico(JButton bt_historico) {
        this.bt_historico = bt_historico;
    }

    public JButton getBt_pesquisa() {
        return bt_pesquisa;
    }

    public void setBt_pesquisa(JButton bt_pesquisa) {
        this.bt_pesquisa = bt_pesquisa;
    }

    public JButton getBt_playlist() {
        return bt_playlist;
    }

    public void setBt_playlist(JButton bt_playlist) {
        this.bt_playlist = bt_playlist;
    }

    public JLabel getjLabel1() {
        return jLabel1;
    }

    public void setjLabel1(JLabel jLabel1) {
        this.jLabel1 = jLabel1;
    }

    public JLabel getjLabel2() {
        return jLabel2;
    }

    public void setjLabel2(JLabel jLabel2) {
        this.jLabel2 = jLabel2;
    }

    public JLabel getjLabel3() {
        return jLabel3;
    }

    public void setjLabel3(JLabel jLabel3) {
        this.jLabel3 = jLabel3;
    }

    public JLabel getjLabel4() {
        return jLabel4;
    }

    public void setjLabel4(JLabel jLabel4) {
        this.jLabel4 = jLabel4;
    }

    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        bt_playlist = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        bt_pesquisa = new javax.swing.JButton();
        bt_historico = new javax.swing.JButton();

        setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(250, 250, 250));
        jLabel1.setText("NAVEGUE");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        bt_playlist.setBackground(new java.awt.Color(0, 204, 102));
        bt_playlist.setIcon(loadImageIcon("playlistImg.png")); // NOI18N
        bt_playlist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_playlistActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(250, 250, 250));
        jLabel2.setText("PESQUISA");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(250, 250, 250));
        jLabel3.setText("PLAYLIST");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(250, 250, 250));
        jLabel4.setText("HISTÓRICO");

        bt_pesquisa.setBackground(new java.awt.Color(0, 204, 102));
        bt_pesquisa.setIcon(loadImageIcon("searchImg.png")); // NOI18N
        bt_pesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_pesquisaActionPerformed(evt);
            }
        });

        bt_historico.setBackground(new java.awt.Color(0, 204, 102));
        bt_historico.setIcon(loadImageIcon("historyImg.png")); // NOI18N
        bt_historico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_historicoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bt_playlist, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bt_historico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bt_pesquisa, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(44, 44, 44)
                .addComponent(bt_pesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(55, 55, 55)
                .addComponent(bt_playlist, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(85, 85, 85)
                .addComponent(bt_historico, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addContainerGap(83, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void bt_pesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_pesquisaActionPerformed
        // TODO add your handling code here:
        mainframe.setPanel(MainFrame.SEARCH_PANEL);
    }//GEN-LAST:event_bt_pesquisaActionPerformed

    private void bt_playlistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_playlistActionPerformed
        // TODO add your handling code here:
        mainframe.setPanel(MainFrame.PLAYLIST_PANEL);
    }//GEN-LAST:event_bt_playlistActionPerformed

    private void bt_historicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_historicoActionPerformed
        // TODO add your handling code here:
        mainframe.setPanel(MainFrame.HISTORY_PANEL);
    }//GEN-LAST:event_bt_historicoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_historico;
    private javax.swing.JButton bt_pesquisa;
    private javax.swing.JButton bt_playlist;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables

    public MainFrame getMainframe() {
        return mainframe;
    }


}
