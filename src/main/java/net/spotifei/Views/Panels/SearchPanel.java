
package net.spotifei.Views.Panels;

//imports
import net.spotifei.Controller.MusicController;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Infrastructure.Factories.MusicInfoComponent.MusicInfoPanelBuilder;
import net.spotifei.Views.Components.MusicListComponent;
import net.spotifei.Views.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author fengl
 */
public class SearchPanel extends javax.swing.JPanel {

    private final MainFrame mainframe;
    private final AppContext appContext;
    private MusicListComponent musicListComponent;
    private final MusicController musicController;
    private JTextField txt_pesquisar;

    /**
     * Construtor da classe `SearchPanel`.
     *
     * @param mainframe A instância da janela principal (`MainFrame`).
     * @param appContext O contexto da aplicação.
     */
    public SearchPanel(MainFrame mainframe, AppContext appContext) {
        this.appContext = appContext;
        this.mainframe = mainframe;
        this.musicController = appContext.getMusicController(this, mainframe);
        initComponents();
        startInitialSearch();
    }

    /**
     * Este método inicializa e configura todos os componentes visuais do painel.
     */
    private void initComponents() {
        setBackground(new java.awt.Color(35, 35, 35));
        setMaximumSize(new java.awt.Dimension(1920, 1080));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Pesquisa");
        titleLabel.setFont(new java.awt.Font("Segoe UI Black", 1, 36));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel greetingLabel = new JLabel("O que vamos ouvir hoje?");
        greetingLabel.setFont(new java.awt.Font("Segoe UI Black", 1, 28));
        greetingLabel.setForeground(Color.white);
        greetingLabel.setAlignmentX(CENTER_ALIGNMENT);

        txt_pesquisar = new JTextField();
        txt_pesquisar.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        txt_pesquisar.setMinimumSize(new Dimension(300, 30));
        txt_pesquisar.setPreferredSize(new Dimension(300, 30));
        txt_pesquisar.setMaximumSize(new Dimension(300, 30));
        txt_pesquisar.setAlignmentX(CENTER_ALIGNMENT);

        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        btnPesquisar.setAlignmentX(CENTER_ALIGNMENT);
        btnPesquisar.addActionListener(this::bt_pesquisarActionPerformed);

        MusicInfoPanelBuilder panelBuilder = appContext.getMusicInfoPanelBuilder(mainframe);
        panelBuilder.selectSearchMusicInfoPanel();
        musicListComponent = new MusicListComponent(panelBuilder);
        musicListComponent.setBackground(new java.awt.Color(35, 35, 35));
        musicListComponent.setPreferredSize(new Dimension(800, 600));
        musicListComponent.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));
        musicListComponent.setAlignmentX(CENTER_ALIGNMENT);

        this.add(titleLabel);
        this.add(Box.createVerticalStrut(10));
        this.add(greetingLabel);
        this.add(Box.createVerticalStrut(20));
        this.add(txt_pesquisar);
        this.add(Box.createVerticalStrut(30));
        this.add(btnPesquisar);
        this.add(Box.createVerticalStrut(20));
        this.add(musicListComponent);
    }

    /**
     * Realiza uma pesquisa inicial ao carregar o painel.
     */
    private void startInitialSearch(){
        txt_pesquisar.setText("a");
        musicController.searchMusic();
        txt_pesquisar.setText("");
    }

    /**
     * Método acionado pelo botão "Pesquisar".
     *
     * @param evt O evento de ação.
     */
    private void bt_pesquisarActionPerformed(java.awt.event.ActionEvent evt) {
        musicController.searchMusicWithUserInfo();
    }

    // Métodos Getters e Setters para as variáveis de instância dos componentes
    public MainFrame getMainframe() {
        return mainframe;
    }

    public JTextField getTxt_pesquisar() {
        return txt_pesquisar;
    }

    public void setTxt_pesquisar(JTextField txt_pesquisar) {
        this.txt_pesquisar = txt_pesquisar;
    }

    public MusicListComponent getMusicListComponent() {
        return musicListComponent;
    }

    public void setMusicListComponent(MusicListComponent musicListComponent) {
        this.musicListComponent = musicListComponent;
    }
}
