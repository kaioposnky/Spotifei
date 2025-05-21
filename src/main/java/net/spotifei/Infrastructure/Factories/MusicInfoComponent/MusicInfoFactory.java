package net.spotifei.Infrastructure.Factories.MusicInfoComponent;

//imports
import net.spotifei.Controller.AdminController;
import net.spotifei.Controller.MusicController;
import net.spotifei.Controller.PlaylistController;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.Music;
import net.spotifei.Models.Playlist;
import net.spotifei.Views.Components.FeedBackComponent;
import net.spotifei.Views.MainFrame;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;

import static net.spotifei.Helpers.AssetsLoader.loadImageIcon;

public abstract class MusicInfoFactory {
    private Playlist playlist;
    private final AppContext appContext;
    private final MainFrame mainframe;
    private final MusicController musicController;
    private final PlaylistController playlistController;
    private final AdminController adminController;

    /**
     * Construtor da fábrica. Recebe o `AppContext` e o `MainFrame` para obter as dependências de controladores.
     *
     * @param appContext O contexto da aplicação para acesso a serviços e repositórios.
     * @param mainframe O frame principal da aplicação, usado para exibição de modais e navegação.
     */
    protected MusicInfoFactory(AppContext appContext, MainFrame mainframe){
        // a view pode ser nula porque a única coisa que será feita é tocar a música, para isso não precisa de uma view
        this.musicController = appContext.getMusicController(null, mainframe);
        this.playlistController = appContext.getPlayListController((JPanel) null,  mainframe);
        this.appContext = appContext;
        this.mainframe = mainframe;
        this.adminController = appContext.getAdminController(null);
    }

    /**
     * Cria um painel de informações de música para resultados de busca geral.
     *
     * @param music A música a ser exibida.
     * @return Um JPanel configurado para exibição em resultados de busca.
     */
    protected JPanel getSearchMusicInfoPanel(Music music){
        JPanel basePanel = createContainerPanel(music);

        basePanel.add(createMusicGenrePanel(music));
        basePanel.add(Box.createHorizontalStrut(50));
        basePanel.add(createMusicFeedbackPanel(music));
        basePanel.add(Box.createHorizontalStrut(50));
        basePanel.add(createMusicPlayButtonPanel(music));

        basePanel.add(Box.createHorizontalStrut(20));
        basePanel.add(createMusicTimePanel(music));
        basePanel.add(Box.createHorizontalStrut(20));

        return basePanel;
    }

    /**
     * Cria um painel para músicas mais visualizadas.
     *
     * @param music A música a ser exibida.
     * @return Um JPanel configurado para exibição em listas de músicas mais vistas.
     */
    protected JPanel getMostViewedMusicInfoPanel(Music music){

        JPanel mainPanel = createContainerPanel(music);

        mainPanel.add(createMusicFeedbackPanel(music));
        mainPanel.add(Box.createHorizontalStrut(50));
        mainPanel.add(createPlayCountPanel(music));
        mainPanel.add(Box.createHorizontalStrut(40));
        mainPanel.add(createMusicTimePanel(music));
        mainPanel.add(Box.createHorizontalStrut(20));

        addHoverListeners(mainPanel);
        return mainPanel;
    }

    /**
     * Cria um painel para músicas que o usuário curtiu ou descurtiu.
     *
     * @param music A música a ser exibida.
     * @return Um JPanel configurado para exibição em listas de músicas curtidas/descurtidas.
     */
    protected JPanel getUserLikedOrDislikedMusicInfoPanel(Music music){

        JPanel mainPanel = createContainerPanel(music);

        mainPanel.add(createMusicFeedbackPanel(music));
        mainPanel.add(Box.createHorizontalStrut(50));
        mainPanel.add(createMusicTimePanel(music));
        mainPanel.add(Box.createHorizontalStrut(20));

        addHoverListeners(mainPanel);
        return mainPanel;
    }

    /**
     * Cria um painel para músicas em resultados de busca que podem ser adicionadas a uma playlist.
     *
     * @param music A música a ser exibida.
     * @return Um JPanel configurado para seleção de músicas para adicionar a uma playlist.
     */
    protected JPanel getSearchMusicInfoForPlaylistPanel(Music music){
        JPanel mainPanel = createContainerPanel(music);
        mainPanel.add(Box.createHorizontalStrut(20));

        mainPanel.add(createMusicGenrePanel(music)); // genero
        mainPanel.add(Box.createHorizontalStrut(50));
        mainPanel.add(createMusicFeedbackPanel(music)); // feedback
        mainPanel.add(Box.createHorizontalStrut(50));
        mainPanel.add(createAddMusicToPlaylistButton(music)); // add to playlist
        mainPanel.add(Box.createHorizontalStrut(20));
        mainPanel.add(createMusicTimePanel(music)); // musictime
        mainPanel.add(Box.createHorizontalStrut(20));

        addHoverListeners(mainPanel);
        return mainPanel;
    }

    /**
     * Cria um painel para músicas dentro do editor de uma playlist.
     *
     * @param music A música a ser exibida.
     * @return Um JPanel configurado para gerenciamento de músicas dentro de uma playlist.
     */
    protected JPanel getMusicInfoFromPlaylistEditorPanel(Music music){
        JPanel mainPanel = createContainerPanel(music);
        mainPanel.add(Box.createHorizontalStrut(20));

        mainPanel.add(createMusicGenrePanel(music)); // genero
        mainPanel.add(Box.createHorizontalStrut(50));
        mainPanel.add(createMusicFeedbackPanel(music)); // feedback
        mainPanel.add(Box.createHorizontalStrut(50));
        mainPanel.add(createRemoveMusicFromPlaylistButton(music)); // remove playlist
        mainPanel.add(Box.createHorizontalStrut(20));
        mainPanel.add(createMusicTimePanel(music)); // musictime
        mainPanel.add(Box.createHorizontalStrut(20));

        addHoverListeners(mainPanel);
        return mainPanel;
    }

    /**
     * Cria um painel para músicas dentro de uma playlist.
     *
     * @param music A música a ser exibida.
     * @return Um JPanel configurado para exibição de músicas em uma playlist.
     */
    protected JPanel getMusicsFromPlaylist(Music music){
        JPanel mainPanel = createContainerPanel(music);
        mainPanel.add(Box.createHorizontalStrut(20));

        mainPanel.add(createMusicGenrePanel(music)); //genero
        mainPanel.add(Box.createHorizontalStrut(50));
        mainPanel.add(createMusicFeedbackPanel(music)); //feedback
        mainPanel.add(Box.createHorizontalStrut(50));
        mainPanel.add(createMusicTimePanel(music)); //time
        mainPanel.add(Box.createHorizontalStrut(20));

        addHoverListeners(mainPanel);
        return mainPanel;
    }

    /**
     * Cria um painel para músicas que podem ser deletadas.
     *
     * @param music A música a ser exibida.
     * @return Um JPanel configurado para exclusão de músicas por administradores.
     */
    protected JPanel getMusicDeleted(Music music){
        JPanel mainPanel = createContainerPanel(music);
        mainPanel.add(Box.createHorizontalStrut(20));
        mainPanel.add(createMusicGenrePanel(music));
        mainPanel.add(Box.createHorizontalStrut(20));
        mainPanel.add(createRemoveMusic(music));
        mainPanel.add(Box.createHorizontalStrut(20));

        addHoverListeners(mainPanel);
        return mainPanel;

    }

    /**
     * Cria o painel base para cada linha de informação de música, contendo
     * o título e os artistas, além de configurar o estilo visual padrão.
     *
     * @param music A música para a qual o painel está sendo criado.
     * @return Um JPanel que serve como contêiner base.
     */
    private JPanel createContainerPanel(Music music){
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.X_AXIS));
        containerPanel.setBackground(Color.decode("#121212"));
        containerPanel.setOpaque(true);

        Border borderLine = new MatteBorder(0, 0, 1, 0, new Color(50, 50, 50));
        Border borderInside = BorderFactory.createEmptyBorder(5, 5, 5, 5);

        containerPanel.setBorder(BorderFactory.createCompoundBorder(borderLine, borderInside));

        containerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        containerPanel.add(createMusicInfoPanel(music));
        containerPanel.add(Box.createHorizontalGlue()); // joga os proximos elementos pra direita

        return containerPanel;
    }

    /**
     * Cria o painel que exibe o título da música e os nomes dos artistas.
     *
     * @param music A música.
     * @return Um JPanel com o título e os artistas.
     */
    private JPanel createMusicInfoPanel(Music music) {
        JPanel musicInfoPanel = new JPanel();
        musicInfoPanel.setLayout(new BoxLayout(musicInfoPanel, BoxLayout.X_AXIS));
        musicInfoPanel.setOpaque(false);

        JPanel infoWrapperPanel = new JPanel();
        infoWrapperPanel.setLayout(new BoxLayout(infoWrapperPanel, BoxLayout.Y_AXIS));
        infoWrapperPanel.setOpaque(false);

        JLabel musicTitle = new JLabel(music.getNome());
        musicTitle.setFont(new Font("Arial", Font.BOLD, 14));
        musicTitle.setForeground(Color.white);

        JLabel musicAuthors = new JLabel(music.getArtistsNames());
        musicAuthors.setFont(new Font("Arial", Font.BOLD, 12));
        musicAuthors.setForeground(Color.gray);

        infoWrapperPanel.add(musicTitle);
        infoWrapperPanel.add(musicAuthors);

        musicInfoPanel.add(infoWrapperPanel);
        musicInfoPanel.add(Box.createHorizontalStrut(40));

        return musicInfoPanel;
    }

    /**
     * Cria o painel que exibe o tempo total da música.
     *
     * @param music A música.
     * @return Um JPanel com a duração da música.
     */
    private JPanel createMusicTimePanel(Music music) {
        JPanel musicTimePanel = new JPanel();
        musicTimePanel.setLayout(new BoxLayout(musicTimePanel, BoxLayout.X_AXIS));
        musicTimePanel.setOpaque(false);

        musicTimePanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        musicTimePanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel musicTimeTotal = new JLabel(getMusicTimeTotal(music));
        musicTimeTotal.setFont(new Font("Arial", Font.BOLD, 12));
        musicTimeTotal.setForeground(Color.decode("#aeaeae"));

        long musicSeconds = music.getDuracaoMs() / 1_000_000;
        long musicMinutes = musicSeconds / 60;
        musicTimeTotal.setText(String.format("%01d:%02d", musicMinutes, musicSeconds % 60));

        musicTimePanel.add(musicTimeTotal);

        return musicTimePanel;
    }

    /**
     * Cria o painel com o botão de reprodução da música.
     *
     * @param music A música.
     * @return Um JPanel com o botão de play.
     */
    private JPanel createMusicPlayButtonPanel(Music music) {
        JPanel musicPlayButtonPanel = new JPanel();
        musicPlayButtonPanel.setLayout(new BoxLayout(musicPlayButtonPanel, BoxLayout.X_AXIS));
        musicPlayButtonPanel.setOpaque(false);

        JButton musicPlayButton = new JButton();
        musicPlayButton.setIcon(loadImageIcon("musicIcons/play.png", 20, 20));
        musicPlayButton.setBorder(new EmptyBorder(0,0,0,5));
        musicPlayButton.setFocusPainted(false);
        musicPlayButton.setContentAreaFilled(false);
        musicPlayButton.setOpaque(false);
        musicPlayButton.addActionListener(event -> handlePlayButton(music));

        musicPlayButtonPanel.add(musicPlayButton);

        return musicPlayButtonPanel;
    }

    /**
     * Cria o painel que exibe o componente de feedback (curtir/descurtir) para a música.
     *
     * @param music A música.
     * @return Um JPanel com o FeedBackComponent.
     */
    private JPanel createMusicFeedbackPanel(Music music){
        JPanel feedbackPanel = new JPanel();
        feedbackPanel.setLayout(new BoxLayout(feedbackPanel, BoxLayout.X_AXIS));
        feedbackPanel.setOpaque(false);

        // nunca que os botões de avaliação serão usados nesse caso, só se pode avaliar uma música ouvindo ela!!!
        FeedBackComponent feedBackComponent = new
                FeedBackComponent(appContext, mainframe, music);

        feedbackPanel.add(feedBackComponent);
        return feedbackPanel;
    }

    /**
     * Cria o painel que exibe o número de vezes que a música foi tocada.
     *
     * @param music A música.
     * @return Um JPanel com a contagem de reproduções.
     */
    private JPanel createPlayCountPanel(Music music){
        JPanel playCountPanel = new JPanel();
        playCountPanel.setLayout(new BoxLayout(playCountPanel, BoxLayout.X_AXIS));
        playCountPanel.setOpaque(false);

        JLabel viewCount = new JLabel(music.getVezesTocada() + " reproduções");
        viewCount.setFont(new Font("Arial", Font.BOLD, 12));
        viewCount.setForeground(Color.decode("#aeaeae"));

        playCountPanel.add(viewCount);

        return playCountPanel;
    }

    /**
     * Cria o painel que exibe o gênero da música.
     *
     * @param music A música.
     * @return Um JPanel com o gênero da música.
     */
    private JPanel createMusicGenrePanel(Music music){
        JPanel genrePanel = new JPanel();
        genrePanel.setLayout(new BoxLayout(genrePanel, BoxLayout.X_AXIS));
        genrePanel.setOpaque(false);

        JLabel genreLabel = new JLabel(music.getGenre().getName());
        genreLabel.setFont(new Font("Arial", Font.BOLD, 12));
        genreLabel.setForeground(Color.decode("#aeaeae"));

        genrePanel.add(genreLabel);

        return genrePanel;
    }

    /**
     * Cria o botão para remover uma música de uma playlist.
     *
     * @param music A música a ser removida.
     * @return Um JButton de remoção.
     */
    private JButton createRemoveMusicFromPlaylistButton(Music music){
        JButton removeMusicFromPlaylistButton = new JButton();
        removeMusicFromPlaylistButton.setIcon(loadImageIcon("trashcan_icon.png", 20, 20));
        removeMusicFromPlaylistButton.addActionListener(event -> handleRemoveMusicFromPlaylist(music));
        removeMusicFromPlaylistButton.setContentAreaFilled(false);
        removeMusicFromPlaylistButton.setOpaque(false);

        return removeMusicFromPlaylistButton;
    }

    /**
     * Cria o botão para remover uma música do sistema.
     *
     * @param music A música a ser deletada.
     * @return Um JButton de exclusão.
     */
    private JButton createRemoveMusic(Music music){
        JButton removeMusicFromPlaylistButton = new JButton();
        removeMusicFromPlaylistButton.setIcon(loadImageIcon("trashcan_icon.png", 20, 20));
        removeMusicFromPlaylistButton.addActionListener(event -> handleRemoveMusic(music));
        removeMusicFromPlaylistButton.setContentAreaFilled(false);
        removeMusicFromPlaylistButton.setOpaque(false);

        return removeMusicFromPlaylistButton;
    }

    /**
     * Cria o botão para adicionar uma música a uma playlist.
     *
     * @param music A música a ser adicionada.
     * @return Um JButton de adição.
     */
    private JButton createAddMusicToPlaylistButton(Music music){
        JButton addMusicToPlaylistButton = new JButton();
        addMusicToPlaylistButton.setIcon(loadImageIcon("add_icon.png", 20, 20));
        addMusicToPlaylistButton.addActionListener(event -> handleAddMusicToPlaylist(music));
        addMusicToPlaylistButton.setContentAreaFilled(false);
        addMusicToPlaylistButton.setOpaque(false);

        return addMusicToPlaylistButton;
    }

    /**
     * Formata a duração da música de microssegundos para minutos e segundos.
     *
     * @param music A música.
     * @return Uma String formatada com a duração da música.
     */
    private String getMusicTimeTotal(Music music){
        long musicMicrosseconds = music.getDuracaoMs();
        long musicInSeconds = musicMicrosseconds / 1_000_000;
        long musicMinutes = musicInSeconds / 60;
        long musicSeconds = musicInSeconds % 60;
        return String.format("%01d:%02d", musicMinutes, musicSeconds);
    }

    /**
     * Manipula o clique no botão de play.
     *
     * @param music A música a ser tocada.
     */
    private void handlePlayButton(Music music){
        musicController.playMusicById(music.getIdMusica());
    }

    /**
     * Manipula a remoção de uma música de uma playlist.
     *
     * @param music A música a ser removida da playlist.
     */
    private void handleRemoveMusicFromPlaylist(Music music){
        playlistController.removeMusicFromPlaylist(music.getIdMusica(), playlist.getIdPlaylist());
    }

    /**
     * Manipula a remoção de uma música do sistema.
     *
     * @param music A música a ser deletada.
     */
    private void handleRemoveMusic(Music music){
        adminController.deleteMusic(music);
    }

    /**
     * Manipula a adição de uma música a uma playlist.
     *
     * @param music A música a ser adicionada à playlist.
     */
    private void handleAddMusicToPlaylist(Music music){
        playlistController.addMusicToPlaylist(music.getIdMusica(), playlist.getIdPlaylist());
    }

    /**
     * Adiciona listeners de mouse para efeitos de hover no painel.
     *
     * @param panel O JPanel ao qual os listeners serão adicionados.
     */
    private void addHoverListeners(JPanel panel){
        addActionListeners(panel, new MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBackground(new Color(35, 35, 35)); // Cor ao passar o mouse
                super.mouseEntered(evt);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel.setBackground(Color.decode("#121212")); // Cor original ao sair
                super.mouseExited(evt); // Corrigido para mouseExited
            }
        });
    }

    /**
     * Método auxiliar para adicionar um MouseAdapter a um JPanel.
     *
     * @param panel O JPanel.
     * @param mouseAdapterListeners O MouseAdapter a ser adicionado.
     */
    private void addActionListeners(JPanel panel, MouseAdapter mouseAdapterListeners){
        panel.addMouseListener(mouseAdapterListeners);
    }

    /**
     * Define a playlist para as operações de adição/remoção de música.
     *
     * @param playlist A Playlist a ser definida como contexto.
     */
    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    /**
     * Retorna o MusicController associado a esta fábrica.
     *
     * @return O MusicController.
     */
    public MusicController getMusicController() {
        return musicController;
    }
}
