package net.spotifei.Controller;

import net.spotifei.Infrastructure.AudioPlayer.AudioPlayerWorker;
import net.spotifei.Models.Music;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Services.MusicService;
import net.spotifei.Views.Panels.HomePanel;

import javax.swing.*;

import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logError;

public class MusicController {
    private final JPanel view;
    private final MusicService musicServices;

    public MusicController(JPanel view, AudioPlayerWorker audioPlayerWorker) {
        this.view = view;
        this.musicServices = new MusicService(audioPlayerWorker);
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

    public void setAudioVolume(float volume){
        Response<Void> response = musicServices.setAudioVolume(volume);
        if(!response.isSuccess()){
            if(response.isError()){
                logError(response.getMessage(), response.getException());
            } else{
                logError(response.getMessage());
            }
            return;
        }
        logDebug("Volume alterado para " + volume + " com sucesso!");
    }

    public void setMusicTime(float musicTime){
        Response<Void> response = musicServices.setMusicTime(musicTime);
        if(!response.isSuccess()){
            if(response.isError()){
                logError(response.getMessage(), response.getException());
            } else{
                logError(response.getMessage());
            }
            return;
        }
        logDebug("Tempo da música alterado para " + musicTime + " com sucesso!");
    }

    public void pauseMusic(){
        Response<Void> response = musicServices.pauseMusic();
        if(!response.isSuccess()){
            if(response.isError()){
                logError(response.getMessage(), response.getException());
            } else{
                logError(response.getMessage());
            }
            return;
        }
        // modificar o status do appcontext na musicontext para deixar pausada ou despausada
        logDebug("Música pausada com sucesso!");
    }
}
