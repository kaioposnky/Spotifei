
package net.spotifei.Views.Panels.Admin;

import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Views.Components.ConstUserListComponent;
import net.spotifei.Views.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author fengl
 */
public class ADMConsultUserPanel extends javax.swing.JPanel {

    private final MainFrame mainframe;
    private final AppContext appContext;
    private ConstUserListComponent constUserListComponent;
    private JTextField txt_pesquisa;


    /**
     * Construtor da classe `ADMConsultUserPanel`.
     *
     * @param mainframe A instância da janela principal (`MainFrame`).
     * @param appContext O contexto da aplicação.
     */
    public ADMConsultUserPanel(MainFrame mainframe, AppContext appContext) {
        this.appContext = appContext;
        this.mainframe = mainframe;
        initComponents();
    }

    /**
     * Método privado para inicializar e configurar os componentes visuais do painel.
     */
    private void initComponents(){
        setBackground(new java.awt.Color(35, 35, 35));
        setMaximumSize(new java.awt.Dimension(1920, 1080));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("CONSULTAR USUÁRIO");
        titleLabel.setFont(new java.awt.Font("Segoe UI Black", 1, 36));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel greetingLabel = new JLabel("Digite o Email do usuário que deseja consultar");
        greetingLabel.setFont(new java.awt.Font("Segoe UI Black", 1, 28));
        greetingLabel.setForeground(Color.white);
        greetingLabel.setAlignmentX(CENTER_ALIGNMENT);

        txt_pesquisa = new JTextField();
        txt_pesquisa.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        txt_pesquisa.setMinimumSize(new Dimension(300, 30));
        txt_pesquisa.setPreferredSize(new Dimension(300, 30));
        txt_pesquisa.setMaximumSize(new Dimension(300, 30));
        txt_pesquisa.setAlignmentX(CENTER_ALIGNMENT);

        JButton bt_pesquisa = new JButton("Pesquisar");
        bt_pesquisa.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        bt_pesquisa.setForeground(new Color(0,0,0));
        bt_pesquisa.setBackground(new Color(0, 109, 170));
        bt_pesquisa.setAlignmentX(CENTER_ALIGNMENT);
        bt_pesquisa.addActionListener(this::bt_pesquisaActionPerformed);

        constUserListComponent = new ConstUserListComponent(appContext, mainframe);
        constUserListComponent.setBackground(new java.awt.Color(35, 35, 35));
        constUserListComponent.setPreferredSize(new Dimension(400, 300));
        constUserListComponent.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));
        constUserListComponent.setAlignmentX(CENTER_ALIGNMENT);

        JButton bt_voltar = new JButton("Voltar");
        bt_voltar.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        bt_voltar.setAlignmentX(CENTER_ALIGNMENT);
        bt_voltar.addActionListener(this::bt_voltarActionPerformed);

        this.add(titleLabel);
        this.add(Box.createVerticalStrut(10));
        this.add(greetingLabel);
        this.add(Box.createVerticalStrut(20));
        this.add(txt_pesquisa);
        this.add(Box.createVerticalStrut(30));
        this.add(bt_pesquisa);
        this.add(Box.createVerticalStrut(20));
        this.add(constUserListComponent);
        this.add(Box.createVerticalStrut(20));
        this.add(bt_voltar);
        this.add(Box.createVerticalStrut(30));
    }

    /**
     * Método acionado pelo botão "Pesquisar".
     *
     * @param evt O evento de ação.
     */
    private void bt_pesquisaActionPerformed(java.awt.event.ActionEvent evt) {
        appContext.getAdminController(this).constUser();

    }

    /**
     * Método acionado pelo botão "Voltar".
     *
     * @param evt O evento de ação.
     */
    private void bt_voltarActionPerformed(java.awt.event.ActionEvent evt) {
        mainframe.setPanel(MainFrame.ADMHOME_PANEL);

    }

    // Variáveis de declaração
    private javax.swing.JButton bt_pesquisa;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton bt_voltar;


    //Getters e Setters
    public MainFrame getMainframe() {
        return mainframe;
    }

    public JTextField getTxt_pesquisa() {
        return txt_pesquisa;
    }

    public void setTxt_pesquisa(JTextField txt_pesquisa) {
        this.txt_pesquisa = txt_pesquisa;
    }

    public ConstUserListComponent getConstUserListComponent() {
        return constUserListComponent;
    }

    public void setConstUserListComponent(ConstUserListComponent constUserListComponent) {
        this.constUserListComponent = constUserListComponent;
    }

}
