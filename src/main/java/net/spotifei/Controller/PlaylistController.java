package net.spotifei.Controller;

// Importes Otimizados
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Infrastructure.Factories.MusicInfoComponent.MusicInfoPanelBuilder;
import net.spotifei.Models.Music;
import net.spotifei.Models.Playlist;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Services.MusicService;
import net.spotifei.Services.PlaylistService;
import net.spotifei.Views.Components.PlaylistInfoComponent;
import net.spotifei.Views.MainFrame;
import net.spotifei.Views.Panels.PlaylistPanel;
import net.spotifei.Views.PopUps.PlaylistEditPopUp;

import javax.swing.*;
import java.util.List;

import static net.spotifei.Helpers.ResponseHelper.handleDefaultResponseIfError;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;

public class PlaylistController {
    private final PlaylistService playlistService;
    private final MusicService musicService;
    private final MainFrame mainFrame;
    private JPanel view;
    private JDialog viewDialog;
    private final MusicController musicController;
    private final AppContext appContext;

    /**
     * Construtor da classe PlaylistController para uso com um JPanel (painel de visualização principal).
     * Inicializa o controlador com as dependências necessárias para gerenciar playlists em um painel principal.
     *
     * @param view O JPanel associado a este controlador (ex: PlaylistPanel).
     * @param playlistService O serviço responsável pela lógica de negócios de playlists.
     * @param appContext O contexto da aplicação, contendo informações do usuário e da música atual.
     * @param musicService O serviço responsável pela lógica de negócios de músicas.
     * @param mainFrame A janela principal da aplicação.
     */
    public PlaylistController(JPanel view, PlaylistService playlistService, AppContext appContext, MusicService musicService, MainFrame mainFrame){
        this.playlistService = playlistService;
        this.appContext = appContext;
        this.musicService = musicService;
        this.mainFrame = mainFrame;
        this.musicController = appContext.getMusicController(null, mainFrame);
        this.view = view;
    }

    /**
     * Construtor da classe PlaylistController para uso com um JDialog (pop-up).
     * Inicializa o controlador com as dependências necessárias para gerenciar playlists em um diálogo pop-up.
     *
     * @param view O JDialog associado a este controlador (ex: PlaylistEditPopUp).
     * @param playlistService O serviço responsável pela lógica de negócios de playlists.
     * @param appContext O contexto da aplicação, contendo informações do usuário e da música atual.
     * @param musicService O serviço responsável pela lógica de negócios de músicas.
     * @param mainFrame A janela principal da aplicação.
     */
    public PlaylistController(JDialog view, PlaylistService playlistService, AppContext appContext, MusicService musicService, MainFrame mainFrame){
        this.playlistService = playlistService;
        this.appContext = appContext;
        this.viewDialog = view;
        this.musicService = musicService;
        this.musicController = appContext.getMusicController(null, mainFrame);
        this.mainFrame = mainFrame;
    }

    /**
     * Cria uma nova playlist para o usuário logado.
     * Obtém o nome da playlist do campo de texto no PlaylistPanel.
     * Valida se o nome não está vazio antes de tentar criar a playlist através do playlistService.
     * Após a criação bem-sucedida, atualiza a lista de playlists do usuário na interface.
     */
    public void createPlaylist(){
        int userId = appContext.getPersonContext().getIdUsuario();
        String nome = ((PlaylistPanel)view).getTxt_criar().getText();
        if(nome.isEmpty()){
            JOptionPane.showMessageDialog(view, "Não é possível criar Playlist sem nome!", "Erro de Criação", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Playlist playlist = new Playlist();
        playlist.setNome(nome);
        Response<Void> response = playlistService.createPlaylist(playlist, userId);
        if(handleDefaultResponseIfError(response)) return;
        getUserUpdatedPlaylists();

        logDebug("Playlist com nome " + nome + " criada com sucesso!");
    }

    /**
     * Obtém e atualiza a lista de playlists do usuário logado na interface.
     * Solicita as playlists do usuário ao playlistService e as exibe no PlaylistListComponent do PlaylistPanel.
     */
    public void getUserUpdatedPlaylists(){
        Response<List<Playlist>> response = playlistService.getPlaylistUser(appContext.getPersonContext().getIdUsuario());
        if(handleDefaultResponseIfError(response)) return;

        PlaylistPanel playlistPanel = ((PlaylistPanel)view);
        playlistPanel.getPlaylistListComponent().setPlaylists(response.getData());
        playlistPanel.getPlaylistListComponent().updateUI();

    }

    /**
     * Exclui uma playlist selecionada.
     * Chama o playlistService para remover a playlist e exibe uma mensagem de sucesso ao usuário.
     */
    public void deletePlaylist(){
        PlaylistInfoComponent playlistInfoComponent = (PlaylistInfoComponent) view;
        Response<Void> response = playlistService.deletePlaylist(playlistInfoComponent.getPlaylist().getIdPlaylist());
        if(handleDefaultResponseIfError(response)) return;

        JOptionPane.showMessageDialog(view,
                "Playlist " + playlistInfoComponent.getPlaylist().getNome() + " deletada com sucesso!");

        logDebug("Playlist com nome " + playlistInfoComponent.getPlaylist().getNome() + " deletada com sucesso!");
    }

    /**
     * Abre um pop-up para edição de uma playlist específica.
     * Obtém a playlist a ser editada e suas músicas.
     * Configura dois MusicInfoPanelBuilders para diferentes tipos de visualização de músicas (na playlist e na busca).
     * Cria e exibe o PlaylistEditPopUp com as músicas e a playlist para edição.
     */
    public void showEditPlaylistPopUp(){
        PlaylistInfoComponent playlistInfoComponent = (PlaylistInfoComponent) view;
        int playlistId = playlistInfoComponent.getPlaylist().getIdPlaylist();

        Response<Playlist> playlistResponse = playlistService.getPlaylistById(playlistId);
        if(handleDefaultResponseIfError(playlistResponse)) return;
        Playlist playlist = playlistResponse.getData();

        Response<List<Music>> playlistMusicsResponse = musicService.getMusicsFromPlaylist(playlist.getIdPlaylist());
        if(handleDefaultResponseIfError(playlistMusicsResponse)) return;

        List<Music> playlistMusics = playlistMusicsResponse.getData();

        MusicInfoPanelBuilder panelBuilderPlaylist = new MusicInfoPanelBuilder(appContext, mainFrame);
        panelBuilderPlaylist.selectMusicFromPlaylistEditorPanel(playlist);

        MusicInfoPanelBuilder panelBuilderSearch = new MusicInfoPanelBuilder(appContext, mainFrame);
        panelBuilderSearch.selectMusicToPlaylistEditorPanel(playlist);

        // infelizmente n tem uma maneira de criar isso sem precisar criar um novo toda hora :(
        PlaylistEditPopUp playlistEditPopUp = new PlaylistEditPopUp(
                appContext, mainFrame, playlistMusics, playlist, panelBuilderPlaylist, panelBuilderSearch);
        playlistEditPopUp.setVisible(true);
        playlistEditPopUp.getMusicFromPlaylistListComponent().setMusics(playlistMusics);

        logDebug("PopUp de edição de playlist criado com sucesso!");
    }

    /**
     * Atualiza a lista de músicas exibidas dentro do pop-up de edição de playlist.
     * Recupera as músicas atualizadas da playlist e as renderiza no MusicFromPlaylistListComponent.
     */
    public void updateMusicsFromPlaylistMusicsPopUp(){
        PlaylistEditPopUp playlistEditPopUp = (PlaylistEditPopUp) viewDialog;
        int playlistId = playlistEditPopUp.getPlaylist().getIdPlaylist();

        Response<List<Music>> playlistMusics = musicService.getMusicsFromPlaylist(playlistId);
        if(handleDefaultResponseIfError(playlistMusics)) return;

        List<Music> musicList = playlistMusics.getData();

        playlistEditPopUp.getMusicFromPlaylistListComponent().setMusics(musicList);
        playlistEditPopUp.getMusicFromPlaylistListComponent().updateUI();
    }

    /**
     * Realiza uma busca por músicas para serem adicionadas a uma playlist.
     * Obtém o termo de pesquisa do campo de busca do PlaylistEditPopUp.
     * Busca as músicas através do musicService e as exibe no MusicFromSearchListComponent.
     */
    public void searchMusicsToPlaylist(){
        PlaylistEditPopUp playlistEditPopUp = (PlaylistEditPopUp) viewDialog;
        String searchTerm = playlistEditPopUp.getSearchField().getText();

        Response<List<Music>> musicsSearched = musicService.searchMusics(searchTerm);
        if(handleDefaultResponseIfError(musicsSearched)) return;
        List<Music> musics = musicsSearched.getData();

        playlistEditPopUp.getMusicFromSearchListComponent().setMusics(musics);
        playlistEditPopUp.getMusicFromSearchListComponent().updateUI();

        logDebug("Músicas obtidas pelo termo de pesquisa: " + searchTerm + " retornadas com sucesso!");
    }

    /**
     * Salva as alterações no nome de uma playlist.
     * Obtém o nome atualizado do campo de texto no PlaylistEditPopUp.
     * Chama o playlistService para atualizar o nome da playlist no sistema.
     */
    public void savePlaylistName(){
        PlaylistEditPopUp playlistPopUp = (PlaylistEditPopUp) viewDialog;
        Playlist updatedPlaylist = playlistPopUp.getPlaylist();
        updatedPlaylist.setNome(playlistPopUp.getPlaylistNameTextArea().getText());
        Response<Void> response = playlistService.updatePlaylist(updatedPlaylist);
        if(handleDefaultResponseIfError(response)) return;

        logDebug("Nome da playlist atualizado com sucesso!");
    }

    /**
     * Inicia a reprodução de uma playlist completa.
     * Define a playlist selecionada como a fila de reprodução do usuário.
     * Obtém a primeira música dessa fila e a passa para o MusicController para iniciar a reprodução.
     */
    public void playPlaylistMusics() {
        PlaylistInfoComponent playlistInfoComponent = (PlaylistInfoComponent) view;

        int playlistId = playlistInfoComponent.getPlaylist().getIdPlaylist();
        int userId = appContext.getPersonContext().getIdUsuario();
        Response<Void> responsePlayPlaylist = playlistService.setPlaylistAsQueueForUser(playlistId, userId);
        if(handleDefaultResponseIfError(responsePlayPlaylist)) return;

        Response<Music> responseGetQueueFirstMusic = musicService.getFirstMusicInUserQueue(userId);
        if(handleDefaultResponseIfError(responseGetQueueFirstMusic)) return;

        Response<Void> responseRemoveFirstMusicFromQueue = musicService.deleteFirstMusicFromUserQueue(userId);
        if(handleDefaultResponseIfError(responseRemoveFirstMusicFromQueue)) return;

        Music music = responseGetQueueFirstMusic.getData();
        musicController.playMusicInBackground(music);

        logDebug("Playlist " + playlistInfoComponent.getPlaylist().getNome() + " tocada com sucesso!");
    }

    /**
     * Adiciona uma música a uma playlist.
     * Chama o playlistService para adicionar a música na última posição da playlist.
     *
     * @param musicId O ID da música a ser adicionada.
     * @param playlistId O ID da playlist onde a música será adicionada.
     */
    public void addMusicToPlaylist(int musicId, int playlistId){
        Response<Void> response = playlistService.addMusicToLastPositionInPlaylist(musicId, playlistId);
        if(handleDefaultResponseIfError(response)) return;

        logDebug("Música adicionada na playlist com sucesso!");
    }

    /**
     * Remove uma música de uma playlist.
     * Chama o playlistService para remover a música da playlist.
     *
     * @param musicId O ID da música a ser removida.
     * @param playlistId O ID da playlist de onde a música será removida.
     */
    public void removeMusicFromPlaylist(int musicId, int playlistId){
        Response<Void> response = playlistService.removeMusicFromPlaylist(musicId, playlistId);
        if(handleDefaultResponseIfError(response)) return;

        logDebug("Música removida da playlist com sucesso!");
    }
}
