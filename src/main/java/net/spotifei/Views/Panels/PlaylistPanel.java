
package net.spotifei.Views.Panels;

//imports
import net.spotifei.Controller.PlaylistController;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Views.Components.PlaylistListComponent;
import net.spotifei.Views.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 *
 * @author fengl
 */
public class PlaylistPanel extends javax.swing.JPanel {

    private final MainFrame mainframe;
    private final AppContext appContext;
    private PlaylistListComponent playlistListComponent;
    private final PlaylistController playlistController;
    private SwingWorker<Void, Void> updaterWorker;
    private JTextField txt_criar;


    /**
     * Construtor da classe `PlaylistPanel`.
     *
     * @param mainframe A instância da janela principal .
     * @param appContext O contexto da aplicação.
     */
    public PlaylistPanel(MainFrame mainframe, AppContext appContext) {
        this.appContext = appContext;
        this.mainframe = mainframe;
        this.playlistController = appContext.getPlayListController(this, mainframe);
        initComponents();
        addShowListeners();
    }

    /**
     * Este método inicializa e configura todos os componentes visuais do painel.
     */
    private void initComponents(){
        setBackground(new java.awt.Color(35, 35, 35));
        setMaximumSize(new java.awt.Dimension(1920, 1080));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Playlist");
        titleLabel.setFont(new java.awt.Font("Segoe UI Black", 1, 36));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel greetingLabel = new JLabel("Vamos criar sua playlist?");
        greetingLabel.setFont(new java.awt.Font("Segoe UI Black", 1, 28));
        greetingLabel.setForeground(Color.white);
        greetingLabel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel createPlaylistPanel = new JPanel();
        createPlaylistPanel.setLayout(new BoxLayout(createPlaylistPanel, BoxLayout.X_AXIS));

        JLabel textPlaylistName = new JLabel("Digite o nome da playlist:");
        textPlaylistName.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        textPlaylistName.setForeground(Color.white);
        textPlaylistName.setOpaque(false);

        txt_criar = new JTextField();
        txt_criar.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        txt_criar.setMinimumSize(new Dimension(300, 30));
        txt_criar.setPreferredSize(new Dimension(300, 30));
        txt_criar.setMaximumSize(new Dimension(300, 30));
        txt_criar.setAlignmentX(CENTER_ALIGNMENT);

        createPlaylistPanel.add(textPlaylistName);
        createPlaylistPanel.add(Box.createHorizontalStrut(10));
        createPlaylistPanel.add(txt_criar);

        JButton bt_create = new JButton("Criar!");
        bt_create.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        bt_create.setAlignmentX(CENTER_ALIGNMENT);
        bt_create.addActionListener(this::bt_createActionPerformed);

        JButton bt_update = new JButton("Atualizar");
        bt_update.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        bt_update.setAlignmentX(CENTER_ALIGNMENT);
        bt_update.addActionListener(this::bt_updateActionPerformed);

        playlistListComponent = new PlaylistListComponent(appContext, mainframe);
        playlistListComponent.setBackground(new java.awt.Color(35, 35, 35));
        playlistListComponent.setPreferredSize(new Dimension(800, 600));
        playlistListComponent.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));
        playlistListComponent.setAlignmentX(CENTER_ALIGNMENT);

        this.add(Box.createVerticalStrut(10));
        this.add(titleLabel);
        this.add(Box.createVerticalStrut(10));
        this.add(greetingLabel);
        this.add(Box.createVerticalStrut(20));
        this.add(createPlaylistPanel);
        this.add(Box.createVerticalStrut(30));
        this.add(bt_create);
        this.add(Box.createVerticalStrut(20));
        this.add(bt_update);
        this.add(Box.createVerticalStrut(20));
        this.add(playlistListComponent);
    }

    /**
     * Método acionado pelo botão "Criar!".
     *
     * @param evt O evento de ação.
     */
    private void bt_createActionPerformed(java.awt.event.ActionEvent evt) {
        playlistController.createPlaylist();

    }

    /**
     * Método acionado pelo botão "Atualizar".
     *
     * @param evt O evento de ação.
     */
    private void bt_updateActionPerformed(java.awt.event.ActionEvent evt){
        playlistController.getUserUpdatedPlaylists();
    }

    // Métodos Getters e Setters para as variáveis de instância dos componentes
    public MainFrame getMainframe() {
        return mainframe;
    }

    public JTextField getTxt_criar() {
        return txt_criar;
    }

    public void setTxt_criar(JTextField txt_criar) {
        this.txt_criar = txt_criar;
    }

    public PlaylistListComponent getPlaylistListComponent() {
        return playlistListComponent;
    }

    public void setPlaylistListComponent(PlaylistListComponent playlistListComponent) {
        this.playlistListComponent = playlistListComponent;
    }

    /**
     * Inicia um `SwingWorker` para atualizar a lista de playlists em segundo plano.
     */
    private void startPlaylistUpdateWorker(){
        updaterWorker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground(){
                try{
                    while(true){
                        playlistController.getUserUpdatedPlaylists();
                        Thread.sleep(1500);
                    }
                } catch (InterruptedException e){
                    return null;
                }
            }
        };
        updaterWorker.execute();
    }

    /**
     * Adiciona listeners de componente ao painel para iniciar e parar o `SwingWorker`
     * quando o painel é mostrado ou escondido.
     */
    private void addShowListeners(){
        this.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentShown(ComponentEvent e) {
                startPlaylistUpdateWorker();
                super.componentShown(e);
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                if (updaterWorker == null) return;
                updaterWorker.cancel(true);
                super.componentHidden(e);
            }
        });
    }

}