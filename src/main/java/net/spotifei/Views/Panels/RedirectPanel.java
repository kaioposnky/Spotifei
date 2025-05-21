package net.spotifei.Views.Panels;

//imports
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.Artist;
import net.spotifei.Views.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;

import static net.spotifei.Helpers.AssetsLoader.loadImageIcon;


public class RedirectPanel extends javax.swing.JPanel{

    private final MainFrame mainframe;
    private final AppContext appContext;

    private javax.swing.JButton bt_historico;
    private javax.swing.JButton bt_pesquisa;
    private javax.swing.JButton bt_playlist;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private JLabel createMusicLabel;
    private JButton bt_createMusic;

    /**
     * Construtor da classe `RedirectPanel`.
     *
     * @param mainframe A instância da janela principal.
     * @param appContext O contexto da aplicação.
     */
    public RedirectPanel(MainFrame mainframe, AppContext appContext) {
        this.mainframe = mainframe;
        this.appContext = appContext;
        initComponents();
        addStartListener();
    }

    /**
     * Este método inicializa e configura todos os componentes visuais do painel.
     */
    private void initComponents() {
        setBackground(new Color(0, 0, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(null);
        setAlignmentX(CENTER_ALIGNMENT);


        Dimension buttonSize = new Dimension(70, 70);

        JButton bt_pesquisa = new JButton();
        bt_pesquisa.setBackground(new java.awt.Color(0,109,170));
        bt_pesquisa.setIcon(loadImageIcon("searchImg.png"));
        bt_pesquisa.addActionListener(this::bt_pesquisaActionPerformed);
        bt_pesquisa.setPreferredSize(buttonSize);
        bt_pesquisa.setAlignmentX(CENTER_ALIGNMENT);

        JLabel jLabel2 = new JLabel("PESQUISA");
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(250, 250, 250));
        jLabel2.setAlignmentX(CENTER_ALIGNMENT);

        JButton bt_playlist = new JButton();
        bt_playlist.setBackground(new java.awt.Color(0,109,170));
        bt_playlist.setIcon(loadImageIcon("playlistImg.png"));
        bt_playlist.addActionListener(this::bt_playlistActionPerformed);
        bt_playlist.setPreferredSize(buttonSize);
        bt_playlist.setAlignmentX(CENTER_ALIGNMENT);

        JLabel jLabel3 = new JLabel("PLAYLIST");
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(250, 250, 250));
        jLabel3.setAlignmentX(CENTER_ALIGNMENT);

        JButton bt_historico = new JButton();
        bt_historico.setBackground(new java.awt.Color(0,109,170));
        bt_historico.setIcon(loadImageIcon("historyImg.png"));
        bt_historico.addActionListener(this::bt_historicoActionPerformed);
        bt_historico.setPreferredSize(buttonSize);
        bt_historico.setAlignmentX(CENTER_ALIGNMENT);

        JLabel jLabel4 = new JLabel("HISTÓRICO");
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(250, 250, 250));
        jLabel4.setAlignmentX(CENTER_ALIGNMENT);

        createMusicLabel = new JLabel("CRIAR MÚSICA");
        createMusicLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        createMusicLabel.setForeground(new java.awt.Color(250, 250, 250));
        createMusicLabel.setAlignmentX(CENTER_ALIGNMENT);
        createMusicLabel.setVisible(false);

        bt_createMusic = new JButton();
        bt_createMusic.setBackground(new java.awt.Color(0,109,170));
        bt_createMusic.setIcon(loadImageIcon("create_music.png"));
        bt_createMusic.addActionListener(this::bt_createMusicActionPerformed);
        bt_createMusic.setPreferredSize(buttonSize);
        bt_createMusic.setAlignmentX(CENTER_ALIGNMENT);
        bt_createMusic.setVisible(false);

        this.add(Box.createVerticalGlue());
        this.add(bt_pesquisa);
        this.add(jLabel2);
        this.add(Box.createVerticalStrut(30));
        this.add(bt_playlist);
        this.add(jLabel3);
        this.add(Box.createVerticalStrut(30));
        this.add(bt_historico);
        this.add(jLabel4);
        this.add(Box.createVerticalStrut(30));
        this.add(bt_createMusic);
        this.add(createMusicLabel);
        this.add(Box.createVerticalGlue());

        setMaximumSize(new Dimension(100, 600));
        setMinimumSize(new Dimension(100, 600));
        setPreferredSize(new Dimension(100, 600));
    }

    /**
     * Método acionado pelo botão "Pesquisa".
     *
     * @param evt O evento de ação.
     */
    private void bt_pesquisaActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        mainframe.setPanel(MainFrame.SEARCH_PANEL);
    }

    /**
     * Método acionado pelo botão "Criar Música".
     * .
     * @param evt O evento de ação.
     */
    private void bt_createMusicActionPerformed(java.awt.event.ActionEvent evt) {
        mainframe.setPanel(MainFrame.ARTISTREGMUSIC_PANEL);
    }

    /**
     * Método acionado pelo botão "Playlist".
     *
     * @param evt O evento de ação.
     */
    private void bt_playlistActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        mainframe.setPanel(MainFrame.PLAYLIST_PANEL);
    }

    /**
     * Método acionado pelo botão "Histórico".
     *
     * @param evt O evento de ação.
     */
    private void bt_historicoActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        mainframe.setPanel(MainFrame.HISTORY_PANEL);
    }

    /**
     * Adiciona um listener de componente que é ativado quando o painel é mostrado.
     * Este listener verifica o tipo de usuário e exibe/oculta o botão "Criar Música"
     * e seu rótulo, conforme apropriado para um artista.
     */
    private void addStartListener(){
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                if (appContext.getPersonContext() instanceof Artist){
                    createMusicLabel.setVisible(true);
                    bt_createMusic.setVisible(true);
                } else{
                    createMusicLabel.setVisible(false);
                    bt_createMusic.setVisible(false);
                }
                super.componentShown(e);
            }
        });
    }

    // Métodos Getters e Setters para as variáveis de instância dos componentes
    public MainFrame getMainframe() {
        return mainframe;
    }

    public AppContext getAppContext() {
        return appContext;
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
}
