package net.spotifei.Controller;

import net.spotifei.Models.Playlist;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Services.PlaylistService;

import javax.swing.*;

import static net.spotifei.Helpers.ResponseHelper.handleDefaultResponseIfError;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logError;

public class PlaylistController {
    private final PlaylistService playlistService;
    private final JPanel view;

    private PlaylistController(JPanel view, PlaylistService playlistService){
        this.playlistService = playlistService;
        this.view = view;
    }

    public void createPlaylist(){
        String nome = "Playlist muito legal"; // TODO: obter playlist pelo view
        Playlist playlist = new Playlist();
        playlist.setNome(nome);
        Response<Void> response = playlistService.createPlaylist(playlist);
        if(handleDefaultResponseIfError(response)) return;

        logDebug("Playlist com nome" + nome + " criada com sucesso!");
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
