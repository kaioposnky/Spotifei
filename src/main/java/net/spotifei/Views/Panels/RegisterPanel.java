/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package net.spotifei.Views.Panels;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import net.spotifei.Controller.UserController;
import net.spotifei.Views.MainFrame;

/**
 *
 * @author fengl
 */
public class RegisterPanel extends javax.swing.JPanel {


    private final MainFrame mainframe;
    
    public RegisterPanel(MainFrame mainframe) {
        initComponents();
        this.mainframe = mainframe;
        uc = new UserController(this);
    }

    public UserController getUc() {
        return uc;
    }

    public void setUc(UserController uc) {
        this.uc = uc;
    }

    public JButton getBt_cadastrar() {
        return bt_cadastrar;
    }

    public void setBt_cadastrar(JButton bt_cadastrar) {
        this.bt_cadastrar = bt_cadastrar;
    }

    public JButton getBt_voltar_login() {
        return bt_voltar_login;
    }

    public void setBt_voltar_login(JButton bt_voltar_login) {
        this.bt_voltar_login = bt_voltar_login;
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

    public JLabel getjLabel5() {
        return jLabel5;
    }

    public void setjLabel5(JLabel jLabel5) {
        this.jLabel5 = jLabel5;
    }

    public JLabel getjLabel6() {
        return jLabel6;
    }

    public void setjLabel6(JLabel jLabel6) {
        this.jLabel6 = jLabel6;
    }

    public JLabel getjLabel7() {
        return jLabel7;
    }

    public void setjLabel7(JLabel jLabel7) {
        this.jLabel7 = jLabel7;
    }

    public JLabel getjLabel8() {
        return jLabel8;
    }

    public void setjLabel8(JLabel jLabel8) {
        this.jLabel8 = jLabel8;
    }

    public JTextField getTxt_email_cadastro() {
        return txt_email_cadastro;
    }

    public void setTxt_email_cadastro(JTextField txt_email_cadastro) {
        this.txt_email_cadastro = txt_email_cadastro;
    }

    public JTextField getTxt_nome_cadastro() {
        return txt_nome_cadastro;
    }

    public void setTxt_nome_cadastro(JTextField txt_nome_cadastro) {
        this.txt_nome_cadastro = txt_nome_cadastro;
    }

    public JTextField getTxt_senha_cadastro() {
        return txt_senha_cadastro;
    }

    public void setTxt_senha_cadastro(JTextField txt_senha_cadastro) {
        this.txt_senha_cadastro = txt_senha_cadastro;
    }

    public JTextField getTxt_sob_cadastro() {
        return txt_sob_cadastro;
    }

    public void setTxt_sob_cadastro(JTextField txt_sob_cadastro) {
        this.txt_sob_cadastro = txt_sob_cadastro;
    }

    public JTextField getTxt_telefone_cadastro() {
        return txt_telefone_cadastro;
    }

    public void setTxt_telefone_cadastro(JTextField txt_telefone_cadastro) {
        this.txt_telefone_cadastro = txt_telefone_cadastro;
    }
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        bt_voltar_login = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txt_nome_cadastro = new javax.swing.JTextField();
        txt_sob_cadastro = new javax.swing.JTextField();
        txt_telefone_cadastro = new javax.swing.JTextField();
        txt_email_cadastro = new javax.swing.JTextField();
        txt_senha_cadastro = new javax.swing.JTextField();
        bt_cadastrar = new javax.swing.JButton();

        setBackground(new java.awt.Color(35, 35, 35));
        setMaximumSize(new java.awt.Dimension(1920, 1080));
        setMinimumSize(new java.awt.Dimension(800, 600));

        jLabel2.setBackground(new java.awt.Color(250, 250, 250));
        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(250, 250, 250));
        jLabel2.setText("VAMOS REALIZAR SEU CADASTRO!");

        bt_voltar_login.setBackground(new java.awt.Color(0, 0, 0));
        bt_voltar_login.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        bt_voltar_login.setForeground(new java.awt.Color(250, 250, 250));
        bt_voltar_login.setText("Voltar");
        bt_voltar_login.setBorderPainted(false);
        bt_voltar_login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_voltar_loginActionPerformed(evt);
            }
        });

        jLabel3.setBackground(new java.awt.Color(250, 250, 250));
        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(250, 250, 250));
        jLabel3.setText("REGISTRO");

        jLabel4.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(250, 250, 250));
        jLabel4.setText("Nome:");

        jLabel5.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(250, 250, 250));
        jLabel5.setText("Sobrenome:");

        jLabel6.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(250, 250, 250));
        jLabel6.setText("Telefone:");

        jLabel7.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(250, 250, 250));
        jLabel7.setText("E-mail:");

        jLabel8.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(250, 250, 250));
        jLabel8.setText("Senha:");

        txt_nome_cadastro.setBackground(new java.awt.Color(250, 250, 250));

        txt_sob_cadastro.setBackground(new java.awt.Color(250, 250, 250));

        txt_telefone_cadastro.setBackground(new java.awt.Color(250, 250, 250));

        txt_email_cadastro.setBackground(new java.awt.Color(250, 250, 250));

        txt_senha_cadastro.setBackground(new java.awt.Color(250, 250, 250));

        bt_cadastrar.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        bt_cadastrar.setForeground(new java.awt.Color(0, 255, 102));
        bt_cadastrar.setText("Cadastrar");
        bt_cadastrar.setBorderPainted(false);
        bt_cadastrar.setContentAreaFilled(false);
        bt_cadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_cadastrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(69, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_telefone_cadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_nome_cadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txt_sob_cadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel7)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_email_cadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_senha_cadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(bt_voltar_login, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(94, 94, 94)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(186, 186, 186)
                        .addComponent(bt_cadastrar, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(75, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(59, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(bt_voltar_login, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jLabel2)
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt_nome_cadastro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txt_sob_cadastro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txt_telefone_cadastro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txt_email_cadastro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txt_senha_cadastro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(bt_cadastrar, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(142, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void bt_cadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_cadastrarActionPerformed
        // TODO add your handling code here:
        uc.createUser();
    }//GEN-LAST:event_bt_cadastrarActionPerformed

    private void bt_voltar_loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_voltar_loginActionPerformed
        // TODO add your handling code here:
        mainframe.setPanel(MainFrame.LOGIN_PANEL);
    }//GEN-LAST:event_bt_voltar_loginActionPerformed

    private UserController uc;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_cadastrar;
    private javax.swing.JButton bt_voltar_login;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JTextField txt_email_cadastro;
    private javax.swing.JTextField txt_nome_cadastro;
    private javax.swing.JTextField txt_senha_cadastro;
    private javax.swing.JTextField txt_sob_cadastro;
    private javax.swing.JTextField txt_telefone_cadastro;
    // End of variables declaration//GEN-END:variables

    public MainFrame getMainframe() {
        return mainframe;
    }

}
