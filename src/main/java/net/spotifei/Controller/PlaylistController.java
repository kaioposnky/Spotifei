package net.spotifei.Controller;

import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.Playlist;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Services.MusicService;
import net.spotifei.Services.PlaylistService;
import net.spotifei.Views.Components.PlaylistInfoComponent;
import net.spotifei.Views.Components.PlaylistListComponent;
import net.spotifei.Views.Panels.PlaylistPanel;

import javax.swing.*;

import java.util.List;

import static net.spotifei.Helpers.ResponseHelper.handleDefaultResponseIfError;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logError;

public class PlaylistController {
    private final PlaylistService playlistService;
    private final JPanel view;
    private final AppContext appContext;

    public PlaylistController(JPanel view, PlaylistService playlistService, AppContext appContext, MusicService musicService){
        this.playlistService = playlistService;
        this.appContext = appContext;
        this.view = view;
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
        playlistPanel.getPlaylistListComponent().renderPlaylists();
        playlistPanel.getPlaylistListComponent().updateUI();

        logDebug("Playlists do usuário " + appContext.getPersonContext().getNome() + " retornadas com sucesso!");
    }

    public void deletePlaylist(){
        PlaylistInfoComponent playlistInfoComponent = (PlaylistInfoComponent) view;
        Response<Void> response = playlistService.deletePlaylist(playlistInfoComponent.getPlaylist().getIdPlaylist());
        if(handleDefaultResponseIfError(response)) return;
        getUserPlaylists();

        logDebug("Playlist com nome " + playlistInfoComponent.getPlaylist().getNome() + "deletada com sucesso!");
    }

    public void editPlaylist(){
        PlaylistInfoComponent playlistInfoComponent = (PlaylistInfoComponent) view;

        Playlist playlistUpdated = null; // implementar popup para colocar os novos dados da playlist
        Response<Void> response = playlistService.updatePlaylist(playlistUpdated);
    }

    public void playPlaylistMusics(){
        PlaylistInfoComponent playlistInfoComponent = (PlaylistInfoComponent) view;
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
