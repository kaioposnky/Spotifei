package net.spotifei.Controller;

import net.spotifei.Models.Music;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Services.MusicService;
import net.spotifei.Views.HomePanel;

import javax.swing.*;

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
        int userId = ((HomePanel) view).getMainframe().getAppContext().getPersonContext().getIdUsuario();
        Response<Music> response = musicServices.getNextMusicForUser(userId);
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
        // update do UI com a nova música tocando
    }
}
