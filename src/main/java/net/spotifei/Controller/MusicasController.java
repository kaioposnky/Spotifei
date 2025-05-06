package net.spotifei.Controller;

import net.spotifei.Models.Music;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Services.MusicService;
import net.spotifei.Views.HomePanel;

import javax.swing.*;

import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logError;

public class MusicasController {
    private final JPanel view;
    private final MusicService musicServices;

    public MusicasController(JPanel view) {
        this.view = view;
        this.musicServices = new MusicService(
                ((HomePanel) view).getMainframe().getAudioPlayerWorker()
        );
    }

    public void playUserNextMusic(){
        HomePanel homePanel = (HomePanel) view;
        int userId = homePanel.getMainframe().getAppContext().getPersonContext().getIdUsuario();
        Response<Music> response = musicServices.getNextMusicInUserQueue(userId);
        if(!response.isSuccess()){
            logError(response.getMessage());
            return;
        }
        Music musicFound = response.getData();
        Response<Void> response2 = musicServices.playMusic(musicFound.getIdMusica());
        if(!response2.isSuccess()){
            logError(response.getMessage());
            return;
        }
        logDebug("Tocando agora: " + musicFound.getNome());
        homePanel.getMainframe().getAppContext().setMusicContext(musicFound);
        // update do UI com a nova música tocando
    }
}
