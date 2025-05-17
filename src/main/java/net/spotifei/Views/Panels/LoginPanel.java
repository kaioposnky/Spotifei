package net.spotifei.Views.Panels;

import javax.swing.*;
import java.awt.*;

import net.spotifei.Controller.AuthController;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.User;
import net.spotifei.Views.MainFrame;

public class LoginPanel extends javax.swing.JPanel {
    private final MainFrame mainframe;
    private final AppContext appContext;
    private final AuthController ac;

    private JLabel titleLabel;
    private JLabel greetingLabel;
    private JLabel greetingLabel3;
    private JButton bt_entrar;
    private JButton bt_register;
    private JLabel infoLabel1;
    private JLabel infoLabel2;
    private JTextField txt_email_login;
    private JPasswordField txt_senha_login;

    public LoginPanel(MainFrame mainframe, AppContext appContext) {
        this.appContext = appContext;
        this.mainframe = mainframe;
        this.ac = appContext.getAuthController(this, mainframe);
        initComponents();
    }

    private void initComponents(){
        setBackground(new Color(35, 35, 35));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(null);
        setAlignmentX(CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("LOGIN");
        titleLabel.setFont(new java.awt.Font("Segoe UI Black", 1, 36));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel greetingLabel = new JLabel("Sejam Bem-Vindos!");
        greetingLabel.setFont(new java.awt.Font("Segoe UI Black", 1, 28));
        greetingLabel.setForeground(Color.white);
        greetingLabel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel emailInfoPanel = emailLoginInfo();
        JPanel senhaInfoPanel = senhaLoginInfo();
        JPanel loginConfirmPanel = loginConfirm();

        this.add(Box.createVerticalGlue());
        this.add(titleLabel);
        this.add(Box.createVerticalStrut(50));
        this.add(greetingLabel);
        this.add(Box.createVerticalStrut(30));
        this.add(emailInfoPanel);
        this.add(Box.createVerticalStrut(30));
        this.add(senhaInfoPanel);
        this.add(Box.createVerticalStrut(30));
        this.add(loginConfirmPanel);
        this.add(Box.createVerticalGlue());

        setMaximumSize(new Dimension(1920, 1080));
        setMinimumSize(new Dimension(800, 600));
    }

    private JPanel emailLoginInfo(){
        JPanel emailLoginInfoPanel = new JPanel();
        emailLoginInfoPanel.setLayout(new BoxLayout(emailLoginInfoPanel, BoxLayout.X_AXIS));
        emailLoginInfoPanel.setBackground(new Color(35, 35, 35));

        JLabel infoLabel1 = new JLabel("E-mail: ");
        infoLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        infoLabel1.setForeground(Color.white);
        infoLabel1.setAlignmentX(CENTER_ALIGNMENT);

        txt_email_login = new JTextField(""); // Inicializa a variável de instância
        txt_email_login.setBackground(new java.awt.Color(250, 250, 250));
        txt_email_login.setForeground(new java.awt.Color(35, 35, 35));
        txt_email_login.setPreferredSize(new Dimension(200, 30));
        txt_email_login.setMaximumSize(new Dimension(250, 30));
        txt_email_login.setAlignmentX(CENTER_ALIGNMENT);

        emailLoginInfoPanel.add(infoLabel1);
        emailLoginInfoPanel.add(txt_email_login);

        return emailLoginInfoPanel;
    }

    private JPanel senhaLoginInfo(){
        JPanel senhaLoginInfoPanel = new JPanel();
        senhaLoginInfoPanel.setLayout(new BoxLayout(senhaLoginInfoPanel, BoxLayout.X_AXIS));
        senhaLoginInfoPanel.setBackground(new Color(35, 35, 35));

        JLabel infoLabel2 = new JLabel("Senha: ");
        infoLabel2.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        infoLabel2.setForeground(Color.white);
        infoLabel2.setAlignmentX(CENTER_ALIGNMENT);

        txt_senha_login = new JPasswordField(""); // Inicializa a variável de instância
        txt_senha_login.setPreferredSize(new Dimension(200, 30));
        txt_senha_login.setBackground(new java.awt.Color(250, 250, 250));
        txt_senha_login.setForeground(new java.awt.Color(35, 35, 35));
        txt_senha_login.setPreferredSize(new Dimension(200, 30));
        txt_senha_login.setMaximumSize(new Dimension(250, 30));
        txt_senha_login.setAlignmentX(CENTER_ALIGNMENT);

        senhaLoginInfoPanel.add(infoLabel2);
        senhaLoginInfoPanel.add(txt_senha_login);

        return senhaLoginInfoPanel;
    }

    private JPanel loginConfirm(){
        JPanel loginConfirmPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginConfirmPanel.setLayout(new BoxLayout(loginConfirmPanel, BoxLayout.Y_AXIS));
        loginConfirmPanel.setBackground(new Color(35, 35, 35));

        JButton bt_entrar = new JButton("ENTRAR");
        bt_entrar.setFont(new java.awt.Font("Segoe UI Black", 1, 14));
        bt_entrar.setAlignmentX(CENTER_ALIGNMENT);
        bt_entrar.addActionListener(this::bt_entrarActionPerformed);
        bt_entrar.setPreferredSize(new Dimension(150, 30));
        bt_entrar.setMaximumSize(new Dimension(300, 30));
        bt_entrar.setBackground(Color.BLACK);
        bt_entrar.setForeground(Color.white);

        JLabel greetingLabel3 = new JLabel("Não tem Login?");
        greetingLabel3.setFont(new java.awt.Font("Segoe UI Black", 1, 28));
        greetingLabel3.setForeground(Color.white);
        greetingLabel3.setAlignmentX(CENTER_ALIGNMENT);

        JButton bt_register = new JButton("Registrar");
        bt_register.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        bt_register.setAlignmentX(CENTER_ALIGNMENT);
        bt_register.addActionListener(this::bt_registerActionPerformed);
        bt_register.setMaximumSize(new Dimension(300, 30));
        bt_register.setBackground(Color.BLACK);
        bt_register.setForeground(new Color(0,109,170));
        bt_register.setContentAreaFilled(false);
        bt_register.setBorderPainted(false);

        loginConfirmPanel.add(bt_entrar);
        loginConfirmPanel.add(Box.createVerticalStrut(30));
        loginConfirmPanel.add(greetingLabel3);
        loginConfirmPanel.add(Box.createVerticalStrut(30));
        loginConfirmPanel.add(bt_register);

        return loginConfirmPanel;
    }

    private void bt_entrarActionPerformed(java.awt.event.ActionEvent evt) {
        User user = new User();
        user.login(ac, this);
    }

    private void bt_registerActionPerformed(java.awt.event.ActionEvent evt) {
        mainframe.setPanel(MainFrame.REGISTER_PANEL);
    }

    public MainFrame getMainframe() {
        return mainframe;
    }

    public AppContext getAppContext() {
        return appContext;
    }

    public AuthController getAc() {
        return ac;
    }

    public JLabel getTitleLabel() {
        return titleLabel;
    }

    public void setTitleLabel(JLabel titleLabel) {
        this.titleLabel = titleLabel;
    }

    public JLabel getGreetingLabel() {
        return greetingLabel;
    }

    public void setGreetingLabel(JLabel greetingLabel) {
        this.greetingLabel = greetingLabel;
    }

    public JLabel getGreetingLabel3() {
        return greetingLabel3;
    }

    public void setGreetingLabel3(JLabel greetingLabel3) {
        this.greetingLabel3 = greetingLabel3;
    }

    public JButton getBt_entrar() {
        return bt_entrar;
    }

    public void setBt_entrar(JButton bt_entrar) {
        this.bt_entrar = bt_entrar;
    }

    public JButton getBt_register() {
        return bt_register;
    }

    public void setBt_register(JButton bt_register) {
        this.bt_register = bt_register;
    }

    public JLabel getInfoLabel1() {
        return infoLabel1;
    }

    public void setInfoLabel1(JLabel infoLabel1) {
        this.infoLabel1 = infoLabel1;
    }

    public JLabel getInfoLabel2() {
        return infoLabel2;
    }

    public void setInfoLabel2(JLabel infoLabel2) {
        this.infoLabel2 = infoLabel2;
    }

    public JTextField getTxt_email_login() {
        return txt_email_login;
    }

    public void setTxt_email_login(JTextField txt_email_login) {
        this.txt_email_login = txt_email_login;
    }

    public JPasswordField getTxt_senha_login() {
        return txt_senha_login;
    }

    public void setTxt_senha_login(JPasswordField txt_senha_login) {
        this.txt_senha_login = txt_senha_login;
    }
}