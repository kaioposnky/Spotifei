package net.spotifei.Controller;

import jdk.jshell.spi.ExecutionControl;
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
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logError;

public class PlaylistController {
    private final PlaylistService playlistService;
    private final MusicService musicService;
    private final MainFrame mainFrame;
    private JPanel view;
    private JDialog viewDialog;
    private final AppContext appContext;

    public PlaylistController(JPanel view, PlaylistService playlistService, AppContext appContext, MusicService musicService, MainFrame mainFrame){
        this.playlistService = playlistService;
        this.appContext = appContext;
        this.musicService = musicService;
        this.mainFrame = mainFrame;
        this.view = view;
    }

    public PlaylistController(JDialog view, PlaylistService playlistService, AppContext appContext, MusicService musicService, MainFrame mainFrame){
        this.playlistService = playlistService;
        this.appContext = appContext;
        this.viewDialog = view;
        this.musicService = musicService;
        this.mainFrame = mainFrame;
    }

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
        getUserPlaylists();

        logDebug("Playlist com nome " + nome + " criada com sucesso!");
    }

    public void getUserPlaylists(){
        Response<List<Playlist>> response = playlistService.getPlaylistUser(appContext.getPersonContext().getIdUsuario());
        if(handleDefaultResponseIfError(response)) return;
        PlaylistPanel playlistPanel = ((PlaylistPanel)view);
        playlistPanel.getPlaylistListComponent().setPlaylists(response.getData());
        playlistPanel.getPlaylistListComponent().updateUI();

        logDebug("Playlists do usuário " + appContext.getPersonContext().getNome() + " retornadas com sucesso!");
    }

    public void deletePlaylist(){
        PlaylistInfoComponent playlistInfoComponent = (PlaylistInfoComponent) view;
        Response<Void> response = playlistService.deletePlaylist(playlistInfoComponent.getPlaylist().getIdPlaylist());
        if(handleDefaultResponseIfError(response)) return;

        logDebug("Playlist com nome " + playlistInfoComponent.getPlaylist().getNome() + "deletada com sucesso!");
    }

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

    public void updateMusicsFromPlaylistMusicsPopUp(){
        PlaylistEditPopUp playlistEditPopUp = (PlaylistEditPopUp) viewDialog;
        int playlistId = playlistEditPopUp.getPlaylist().getIdPlaylist();

        Response<List<Music>> playlistMusics = musicService.getMusicsFromPlaylist(playlistId);
        if(handleDefaultResponseIfError(playlistMusics)) return;

        List<Music> musicList = playlistMusics.getData();

        playlistEditPopUp.getMusicFromPlaylistListComponent().setMusics(musicList);
        playlistEditPopUp.getMusicFromPlaylistListComponent().updateUI();
    }

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

    public void savePlaylistName(){
        PlaylistEditPopUp playlistPopUp = (PlaylistEditPopUp) viewDialog;
        Playlist updatedPlaylist = playlistPopUp.getPlaylist();
        updatedPlaylist.setNome(playlistPopUp.getPlaylistNameTextArea().getText());
        Response<Void> response = playlistService.updatePlaylist(updatedPlaylist);
        if(handleDefaultResponseIfError(response)) return;

        logDebug("Nome da playlist atualizado com sucesso!");
    }

    public void playPlaylistMusics() {
        PlaylistInfoComponent playlistInfoComponent = (PlaylistInfoComponent) view;
        logError("Não implementado", new ExecutionControl.NotImplementedException("Ainda não implementado!"));
        JOptionPane.showMessageDialog(null, "Quer tocar a playlist ? implementa então bonzão");
    }

    public void addMusicToPlaylist(int musicId, int playlistId){
        Response<Void> response = playlistService.addMusicToLastPositionInPlaylist(musicId, playlistId);
        if(handleDefaultResponseIfError(response)) return;

        logDebug("Música adicionada na playlist com sucesso!");
    }

    public void removeMusicFromPlaylist(int musicId, int playlistId){
        Response<Void> response = playlistService.removeMusicFromPlaylist(musicId, playlistId);
        if(handleDefaultResponseIfError(response)) return;

        logDebug("Música removida da playlist com sucesso!");
    }
}
