package net.spotifei.Controller;

import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.Playlist;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Services.PlaylistService;
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

    public PlaylistController(JPanel view, PlaylistService playlistService, AppContext appContext){
        this.playlistService = playlistService;
        this.appContext = appContext;
        this.view = view;
    }

    public void createPlaylist(){
        int userId = appContext.getPersonContext().getIdUsuario();
        String nome = ((PlaylistPanel)view).getTxt_criar().getText();
        Playlist playlist = new Playlist();
        playlist.setNome(nome);
        Response<Void> response = playlistService.createPlaylist(playlist, userId);
        if(handleDefaultResponseIfError(response)) return;

        logDebug("Playlist com nome" + nome + " criada com sucesso!");
    }

    public void getUserPlaylists(){
        Response<List<Playlist>> response = playlistService.getPlaylistUser(appContext.getPersonContext().getIdUsuario());
        if(handleDefaultResponseIfError(response)) return;
        PlaylistPanel playlistPanel = ((PlaylistPanel)view);
        playlistPanel.getPlaylistListComponent().setPlaylists(response.getData());
        playlistPanel.getPlaylistListComponent().renderPlaylists();

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
