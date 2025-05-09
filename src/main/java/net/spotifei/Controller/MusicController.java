package net.spotifei.Controller;

import net.spotifei.Infrastructure.AudioPlayer.AudioPlayerWorker;
import net.spotifei.Models.Music;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Services.MusicService;
import net.spotifei.Views.MainFrame;
import net.spotifei.Views.Panels.MusicPlayerPanel;

import javax.swing.*;

import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logError;

public class MusicController {
    private final JPanel view;
    private final MusicService musicServices;
    private final MainFrame mainFrame;
    private final MusicPlayerPanel musicPlayerPanel;

    public MusicController(JPanel view, MainFrame mainFrame, MusicPlayerPanel musicPlayerPanel) {
        this.view = view;
        this.musicServices = new MusicService(mainFrame.getAudioPlayerWorker());
        this.mainFrame = mainFrame;
        this.musicPlayerPanel = musicPlayerPanel;
    }

    public void playUserNextMusic(){
        int userId = mainFrame.getAppContext().getPersonContext().getIdUsuario();
        Response<Music> response = musicServices.getNextMusicInUserQueue(userId);
        if(!response.isSuccess()){
            logError(response.getMessage());
            return;
        }
        Music musicFound = response.getData();
        mainFrame.getAppContext().setMusicContext(musicFound);
        logDebug("Próxima música do usuário obtida: " + musicFound.getNome());
        playMusic();
    }

    public void playMusic(){
        Music music = mainFrame.getAppContext().getMusicContext();
        logDebug("Solicitação de tocar música recebido! musica:" + music.getNome());
        musicPlayerPanel.getMusicTitle().setText(music.getNome());
        musicPlayerPanel.getMusicArtist().setText(music.getAutor().getNomeArtistico());
        Response<Void> response = musicServices.playMusic(music.getIdMusica());
        if(!response.isSuccess()){
            logError(response.getMessage());
            return;
        }
        logDebug("Tocando agora: " + music.getNome());
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
