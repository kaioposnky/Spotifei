package net.spotifei.Views.Panels;

//imports
import net.spotifei.Controller.AuthController;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Views.MainFrame;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends javax.swing.JPanel {

    private final MainFrame mainframe;
    private final AppContext appContext;
    private final AuthController ac;

    private JLabel titleLabel;
    private JLabel greetingLabel;
    private JLabel infoLabel1;
    private JLabel infoLabel2;
    private JLabel infoLabel3;
    private JLabel infoLabel4;
    private JLabel infoLabel5;
    private JTextField txt_nome_cadastro;
    private JTextField txt_sob_cadastro;
    private JTextField txt_telefone_cadastro;
    private JTextField txt_email_cadastro;
    private JTextField txt_senha_cadastro;
    private JButton bt_cadastrar;
    private JButton bt_voltar_login;

    /**
     * Construtor da classe `RegisterPanel`.
     *
     * @param mainframe A instância da janela principal (`MainFrame`).
     * @param appContext O contexto da aplicação.
     */
    public RegisterPanel(MainFrame mainframe, AppContext appContext) {
        this.appContext = appContext;
        this.mainframe = mainframe;
        this.ac = appContext.getAuthController(this, mainframe);
        initComponents();
    }

    /**
     * Este método inicializa e configura todos os componentes visuais do painel.
     */
    private void initComponents() {
        setBackground(new Color(35, 35, 35));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(null);
        setAlignmentX(CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("REGISTRO");
        titleLabel.setFont(new java.awt.Font("Segoe UI Black", 1, 36));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel greetingLabel = new JLabel("Vamos realizar seu cadastro?");
        greetingLabel.setFont(new java.awt.Font("Segoe UI Black", 1, 28));
        greetingLabel.setForeground(Color.white);
        greetingLabel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel nomeRegisterInfo = nomeRegisterInfo();
        JPanel sobrenomeRegisterInfo = sobrenomeRegisterInfo();
        JPanel telefoneRegisterInfo = telefoneRegisterInfo();
        JPanel emailRegisterInfo = emailRegisterInfo();
        JPanel senhaRegisterInfo = emailRegisterInfo();
        JPanel registerConfirm = registerConfirm();


        this.add(Box.createVerticalGlue());
        this.add(titleLabel);
        this.add(Box.createVerticalStrut(50));
        this.add(greetingLabel);
        this.add(Box.createVerticalStrut(20));
        this.add(nomeRegisterInfo());
        this.add(Box.createVerticalStrut(20));
        this.add(sobrenomeRegisterInfo());
        this.add(Box.createVerticalStrut(20));
        this.add(telefoneRegisterInfo());
        this.add(Box.createVerticalStrut(20));
        this.add(emailRegisterInfo());
        this.add(Box.createVerticalStrut(20));
        this.add(senhaRegisterInfo());
        this.add(Box.createVerticalStrut(30));
        this.add(registerConfirm());
        this.add(Box.createVerticalGlue());



        setMaximumSize(new Dimension(1920, 1080));
        setMinimumSize(new Dimension(800, 600));

    }

    /**
     * Cria e retorna um JPanel para o campo de entrada do nome.
     *
     * @return Um JPanel contendo o rótulo "Nome:" e o campo de texto.
     */
    private JPanel nomeRegisterInfo(){
        JPanel nomeRegisterInfo = new JPanel();
        nomeRegisterInfo.setLayout(new BoxLayout(nomeRegisterInfo, BoxLayout.X_AXIS));
        nomeRegisterInfo.setBackground(new Color(35, 35, 35));

        JLabel infoLabel1 = new JLabel("Nome: ");
        infoLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        infoLabel1.setForeground(Color.white);
        infoLabel1.setAlignmentX(CENTER_ALIGNMENT);

        txt_nome_cadastro = new JTextField("");
        txt_nome_cadastro.setPreferredSize(new Dimension(200, 30));
        txt_nome_cadastro.setMaximumSize(new Dimension(250, 30));
        txt_nome_cadastro.setAlignmentX(CENTER_ALIGNMENT);

        nomeRegisterInfo.setMaximumSize(new Dimension(300, nomeRegisterInfo.getPreferredSize().height));
        nomeRegisterInfo.add(infoLabel1);
        nomeRegisterInfo.add(txt_nome_cadastro);

        return nomeRegisterInfo;
    }

    /**
     * Cria e retorna um JPanel para o campo de entrada do sobrenome.
     *
     * @return Um JPanel contendo o rótulo "Sobrenome:" e o campo de texto.
     */
    private JPanel sobrenomeRegisterInfo(){
        JPanel sobrenomeRegisterInfo = new JPanel();
        sobrenomeRegisterInfo.setLayout(new BoxLayout(sobrenomeRegisterInfo, BoxLayout.X_AXIS));
        sobrenomeRegisterInfo.setBackground(new Color(35, 35, 35));

        JLabel infoLabel2 = new JLabel("Sobrenome: ");
        infoLabel2.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        infoLabel2.setForeground(Color.white);
        infoLabel2.setAlignmentX(CENTER_ALIGNMENT);

        txt_sob_cadastro = new JTextField("");
        txt_sob_cadastro.setPreferredSize(new Dimension(200, 30));
        txt_sob_cadastro.setMaximumSize(new Dimension(250, 30));
        txt_sob_cadastro.setAlignmentX(CENTER_ALIGNMENT);


        sobrenomeRegisterInfo.setMaximumSize(new Dimension(300, sobrenomeRegisterInfo.getPreferredSize().height));
        sobrenomeRegisterInfo.add(infoLabel2);
        sobrenomeRegisterInfo.add(txt_sob_cadastro);

        return sobrenomeRegisterInfo;
    }

    /**
     * Cria e retorna um JPanel para o campo de entrada do telefone.
     *
     * @return Um JPanel contendo o rótulo "Telefone:" e o campo de texto.
     */
    private JPanel telefoneRegisterInfo(){
        JPanel telefoneRegisterInfo = new JPanel();
        telefoneRegisterInfo.setLayout(new BoxLayout(telefoneRegisterInfo, BoxLayout.X_AXIS));
        telefoneRegisterInfo.setBackground(new Color(35, 35, 35));

        JLabel infoLabel3 = new JLabel("Telefone: ");
        infoLabel3.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        infoLabel3.setForeground(Color.white);
        infoLabel3.setAlignmentX(CENTER_ALIGNMENT);

        txt_telefone_cadastro = new JTextField("");
        txt_telefone_cadastro.setPreferredSize(new Dimension(200, 30));
        txt_telefone_cadastro.setMaximumSize(new Dimension(250, 30));
        txt_telefone_cadastro.setAlignmentX(CENTER_ALIGNMENT);

        telefoneRegisterInfo.setMaximumSize(new Dimension(300, telefoneRegisterInfo.getPreferredSize().height));
        telefoneRegisterInfo.add(infoLabel3);
        telefoneRegisterInfo.add(txt_telefone_cadastro);

        return telefoneRegisterInfo;
    }

    /**
     * Cria e retorna um JPanel para o campo de entrada do e-mail.
     *
     * @return Um JPanel contendo o rótulo "E-mail:" e o campo de texto.
     */
    private JPanel emailRegisterInfo(){
        JPanel emailRegisterInfo = new JPanel();
        emailRegisterInfo.setLayout(new BoxLayout(emailRegisterInfo, BoxLayout.X_AXIS));
        emailRegisterInfo.setBackground(new Color(35, 35, 35));

        JLabel infoLabel4 = new JLabel("E-mail: ");
        infoLabel4.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        infoLabel4.setForeground(Color.white);
        infoLabel4.setAlignmentX(CENTER_ALIGNMENT);

        txt_email_cadastro = new JTextField("");
        txt_email_cadastro.setPreferredSize(new Dimension(200, 30));
        txt_email_cadastro.setMaximumSize(new Dimension(250, 30));
        txt_email_cadastro.setAlignmentX(CENTER_ALIGNMENT);


        emailRegisterInfo.setMaximumSize(new Dimension(300, emailRegisterInfo.getPreferredSize().height));
        emailRegisterInfo.add(infoLabel4);
        emailRegisterInfo.add(txt_email_cadastro);

        return emailRegisterInfo;
    }

    /**
     * Cria e retorna um JPanel para o campo de entrada da senha.
     *
     * @return Um JPanel contendo o rótulo "Senha:" e o campo de texto.
     */
    private JPanel senhaRegisterInfo(){
        JPanel senhaRegisterInfo = new JPanel();
        senhaRegisterInfo.setLayout(new BoxLayout(senhaRegisterInfo, BoxLayout.X_AXIS));
        senhaRegisterInfo.setBackground(new Color(35, 35, 35));

        JLabel infoLabel5 = new JLabel("Senha: ");
        infoLabel5.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        infoLabel5.setForeground(Color.white);
        infoLabel5.setAlignmentX(CENTER_ALIGNMENT);

        txt_senha_cadastro = new JTextField("");
        txt_senha_cadastro.setPreferredSize(new Dimension(200, 30));
        txt_senha_cadastro.setMaximumSize(new Dimension(250, 30));
        txt_senha_cadastro.setAlignmentX(CENTER_ALIGNMENT);


        senhaRegisterInfo.add(infoLabel5);
        senhaRegisterInfo.setMaximumSize(new Dimension(300, senhaRegisterInfo.getPreferredSize().height));
        senhaRegisterInfo.add(txt_senha_cadastro);

        return senhaRegisterInfo;
    }

    /**
     * Cria e retorna um JPanel para os botões de confirmação de registro e retorno ao login.
     *
     * @return Um JPanel contendo os botões "Cadastrar" e "Voltar".
     */
    private JPanel registerConfirm(){
        JPanel registerConfirm = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerConfirm.setLayout(new BoxLayout(registerConfirm, BoxLayout.Y_AXIS));
        registerConfirm.setBackground(new Color(35, 35, 35));

        JButton bt_cadastrar = new JButton("Cadastrar");
        bt_cadastrar.setFont(new java.awt.Font("Segoe UI Black", 1, 24));
        bt_cadastrar.setAlignmentX(CENTER_ALIGNMENT);
        bt_cadastrar.addActionListener(this::bt_cadastrarActionPerformed);
        bt_cadastrar.setPreferredSize(new Dimension(150, 30));
        bt_cadastrar.setMaximumSize(new Dimension(300, 30));
        bt_cadastrar.setBackground(Color.BLACK);
        bt_cadastrar.setForeground(new Color(0,109,170));
        bt_cadastrar.setContentAreaFilled(false);
        bt_cadastrar.setBorderPainted(false);


        JButton bt_voltar_login = new JButton("Voltar");
        bt_voltar_login.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        bt_voltar_login.setAlignmentX(CENTER_ALIGNMENT);
        bt_voltar_login.addActionListener(this::bt_voltar_loginActionPerformed);
        bt_voltar_login.setMaximumSize(new Dimension(200, 50));
        bt_voltar_login.setBackground(Color.BLACK);
        bt_voltar_login.setForeground(Color.white);

        registerConfirm.add(bt_cadastrar);
        registerConfirm.add(Box.createVerticalStrut(50));
        registerConfirm.add(bt_voltar_login);

        return registerConfirm;
    }

    /**
     * Método acionado pelo botão "Cadastrar".
     *
     * @param evt O evento de ação.
     */
    private void bt_cadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_cadastrarActionPerformed
        // TODO add your handling code here:
        ac.createUser();
    }

    /**
     * Método acionado pelo botão "Voltar".
     *
     * @param evt O evento de ação.
     */
    private void bt_voltar_loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_voltar_loginActionPerformed
        // TODO add your handling code here:
        mainframe.setPanel(MainFrame.LOGIN_PANEL);
    }

    // Métodos Getters e Setters para as variáveis de instância dos componentes
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

    public JLabel getInfoLabel3() {
        return infoLabel3;
    }

    public void setInfoLabel3(JLabel infoLabel3) {
        this.infoLabel3 = infoLabel3;
    }

    public JLabel getInfoLabel4() {
        return infoLabel4;
    }

    public void setInfoLabel4(JLabel infoLabel4) {
        this.infoLabel4 = infoLabel4;
    }

    public JLabel getInfoLabel5() {
        return infoLabel5;
    }

    public void setInfoLabel5(JLabel infoLabel5) {
        this.infoLabel5 = infoLabel5;
    }

    public JTextField getTxt_nome_cadastro() {
        return txt_nome_cadastro;
    }

    public void setTxt_nome_cadastro(JTextField txt_nome_cadastro) {
        this.txt_nome_cadastro = txt_nome_cadastro;
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

    public JTextField getTxt_email_cadastro() {
        return txt_email_cadastro;
    }

    public void setTxt_email_cadastro(JTextField txt_email_cadastro) {
        this.txt_email_cadastro = txt_email_cadastro;
    }

    public JTextField getTxt_senha_cadastro() {
        return txt_senha_cadastro;
    }

    public void setTxt_senha_cadastro(JTextField txt_senha_cadastro) {
        this.txt_senha_cadastro = txt_senha_cadastro;
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
}