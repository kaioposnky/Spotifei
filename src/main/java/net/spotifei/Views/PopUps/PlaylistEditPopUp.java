package net.spotifei.Views.PopUps;

//imports
import net.spotifei.Controller.PlaylistController;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Infrastructure.Factories.MusicInfoComponent.MusicInfoPanelBuilder;
import net.spotifei.Models.Music;
import net.spotifei.Models.Playlist;
import net.spotifei.Views.Components.MusicListComponent;
import net.spotifei.Views.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistEditPopUp extends JDialog {
    private final MusicListComponent musicFromPlaylistListComponent;
    private final MusicListComponent musicFromSearchListComponent;
    private final PlaylistController playlistController;
    private JTextField playlistNameTextArea;
    private final Playlist playlist;
    private JTextField searchField;
    private JLabel titleLabel;

    /**
     * Construtor da classe `PlaylistEditPopUp`.
     *
     * @param appContext O contexto da aplicação.
     * @param mainFrame A janela principal que será a proprietária deste diálogo.
     * @param musics A lista inicial de músicas que estão na playlist (para `musicFromPlaylistListComponent`).
     * @param playlist A instância da `Playlist` que está sendo editada.
     * @param panelBuilderPlaylist O builder usado para criar os painéis de informação para músicas da playlist.
     * @param panelBuilderSearch O builder usado para criar os painéis de informação para músicas de busca.
     */
    public PlaylistEditPopUp(AppContext appContext, MainFrame mainFrame, List<Music> musics, Playlist playlist,
                             MusicInfoPanelBuilder panelBuilderPlaylist, MusicInfoPanelBuilder panelBuilderSearch){
        super(mainFrame, true);
        this.playlist = playlist;
        this.musicFromPlaylistListComponent = new MusicListComponent(
                musics, panelBuilderPlaylist);

        this.musicFromSearchListComponent = new MusicListComponent(
                new ArrayList<>(), panelBuilderSearch);

        initComponents();
        this.playlistController = appContext.getPlayListController(this, mainFrame);
        startPlaylistUpdateWorker();
    }

    /**
     * Este método inicializa e configura todos os componentes visuais dentro do pop-up.
     */
    private void initComponents() {
        this.setMinimumSize(new Dimension(500, 500));
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(48, 45, 45));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setAlignmentX(CENTER_ALIGNMENT);

        titleLabel = new JLabel("Editando a playlist " + playlist.getNome());
        titleLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 26));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JButton btnSavePlaylistName = new JButton("Salvar");
        btnSavePlaylistName.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        btnSavePlaylistName.setAlignmentX(CENTER_ALIGNMENT);
        btnSavePlaylistName.addActionListener(e -> handleSavePlaylistName());

        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(createChangePlaylistNameWrapper());
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(btnSavePlaylistName);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(musicFromPlaylistListComponent);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(createMusicListsPanelWrapper());
        contentPanel.add(Box.createVerticalStrut(20));

        getContentPane().add(contentPanel);

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Cria um JPanel para a área de edição do nome da playlist.
     *
     * @return Um JPanel contendo o rótulo "Nome:" e o campo de texto para o nome da playlist.
     */
    private JPanel createChangePlaylistNameWrapper(){
        JPanel changePlaylistNameWrapper = new JPanel();
        changePlaylistNameWrapper.setLayout(new BoxLayout(changePlaylistNameWrapper, BoxLayout.X_AXIS));
        changePlaylistNameWrapper.setAlignmentY(CENTER_ALIGNMENT);

        JLabel musicNameLabel = new JLabel("Nome: ");
        musicNameLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        musicNameLabel.setAlignmentX(CENTER_ALIGNMENT);

        playlistNameTextArea = new JTextField(playlist.getNome());
        playlistNameTextArea.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        playlistNameTextArea.setMaximumSize(new Dimension(300, 30));
        playlistNameTextArea.setMinimumSize(new Dimension(300, 30));
        playlistNameTextArea.setPreferredSize(new Dimension(300, 30));
        playlistNameTextArea.setAlignmentX(CENTER_ALIGNMENT);
        playlistNameTextArea.setText(playlist.getNome());

        changePlaylistNameWrapper.add(musicNameLabel);
        changePlaylistNameWrapper.add(Box.createHorizontalStrut(20));
        changePlaylistNameWrapper.add(playlistNameTextArea);

        return changePlaylistNameWrapper;
    }

    /**
     * Cria um JPanel que envolve os dois painéis de lista de músicas (playlist e pesquisa).
     *
     * @return Um JPanel contendo `createMusicsFromPanel()` e `createSearchMusicsPanel()`.
     */
    private JPanel createMusicListsPanelWrapper(){
        JPanel musicListsPanelWrapper = new JPanel();
        musicListsPanelWrapper.setLayout(new BoxLayout(musicListsPanelWrapper, BoxLayout.X_AXIS));
        musicListsPanelWrapper.setBorder(new EmptyBorder(20, 20, 20, 20));

        musicListsPanelWrapper.add(createMusicsFromPanel());
        musicListsPanelWrapper.add(Box.createHorizontalStrut(20));
        musicListsPanelWrapper.add(createSearchMusicsPanel());

        return musicListsPanelWrapper;
    }

    /**
     * Cria o JPanel para exibir as músicas que já estão na playlist.
     *
     * @return Um JPanel com o título "Músicas da playlist" e o `musicFromPlaylistListComponent`.
     */
    private JPanel createMusicsFromPanel(){
        JPanel playlistMusicsPanel = new JPanel();
        playlistMusicsPanel.setLayout(new BoxLayout(playlistMusicsPanel, BoxLayout.Y_AXIS));

        JLabel playlistMusicsLabel = new JLabel("Músicas da playlist");
        playlistMusicsLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 24));
        playlistMusicsLabel.setAlignmentX(CENTER_ALIGNMENT);

        musicFromPlaylistListComponent.setBackground(new Color(76, 59, 59));
        musicFromPlaylistListComponent.setPreferredSize(new Dimension(800, 600));
        musicFromPlaylistListComponent.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));
        musicFromPlaylistListComponent.setAlignmentX(CENTER_ALIGNMENT);
        musicFromPlaylistListComponent.renderMusics();

        playlistMusicsPanel.add(playlistMusicsLabel);
        playlistMusicsPanel.add(Box.createVerticalStrut(10));
        playlistMusicsPanel.add(musicFromPlaylistListComponent);

        return playlistMusicsPanel;
    }

    /**
     * Cria o JPanel para a área de pesquisa de músicas (para adicionar à playlist).
     *
     * @return Um JPanel com o campo de pesquisa, botão e o `musicFromSearchListComponent`.
     */
    private JPanel createSearchMusicsPanel(){
        JPanel searchMusicsPanel = new JPanel();
        searchMusicsPanel.setLayout(new BoxLayout(searchMusicsPanel, BoxLayout.Y_AXIS));

        JLabel searchLabel = new JLabel("Procure músicas para sua playlist");
        searchLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 24));
        searchLabel.setAlignmentX(CENTER_ALIGNMENT);

        JButton btnSearch = new JButton("Pesquisar");
        btnSearch.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        btnSearch.setAlignmentX(CENTER_ALIGNMENT);
        btnSearch.addActionListener(e -> handleSearchMusics());

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        searchField.setMaximumSize(new Dimension(300, 30));

        musicFromSearchListComponent.setBackground(new Color(76, 59, 59));
        musicFromSearchListComponent.setPreferredSize(new Dimension(800, 600));
        musicFromSearchListComponent.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));
        musicFromSearchListComponent.setAlignmentX(CENTER_ALIGNMENT);
        musicFromSearchListComponent.renderMusics();

        searchMusicsPanel.add(searchLabel);
        searchMusicsPanel.add(Box.createVerticalStrut(10));
        searchMusicsPanel.add(searchField);
        searchMusicsPanel.add(Box.createVerticalStrut(10));
        searchMusicsPanel.add(btnSearch);
        searchMusicsPanel.add(Box.createVerticalStrut(10));
        searchMusicsPanel.add(musicFromSearchListComponent);
        searchMusicsPanel.add(Box.createVerticalStrut(10));

        return searchMusicsPanel;
    }

    /**
     * Método acionado pelo botão "Pesquisar" na área de busca de músicas.
     */
    private void handleSearchMusics(){
        playlistController.searchMusicsToPlaylist();
    }

    /**
     * Método acionado pelo botão "Salvar" para o nome da playlist.
     */
    private void handleSavePlaylistName(){
        playlistController.savePlaylistName();
        playlist.setNome(playlistNameTextArea.getText());
        titleLabel.setText("Editando a playlist " + playlist.getNome());
    }

    /**
     * Inicia um `SwingWorker` em segundo plano para atualizar periodicamente a lista de músicas da playlist.
     */
    private void startPlaylistUpdateWorker(){
        SwingWorker<Void, Void> updaterWorker = new SwingWorker<>() {
            @Override
            protected Void doInBackground(){
                try{
                    while(true){
                        playlistController.updateMusicsFromPlaylistMusicsPopUp();
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e){
                    return null;
                }
            }
        };
        updaterWorker.execute();
    }

    //  Métodos Getters para os componentes
    public MusicListComponent getMusicFromPlaylistListComponent() {
        return musicFromPlaylistListComponent;
    }

    public MusicListComponent getMusicFromSearchListComponent() {
        return musicFromSearchListComponent;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public JTextField getPlaylistNameTextArea() {
        return playlistNameTextArea;
    }
}
