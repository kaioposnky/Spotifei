package net.spotifei.Controller;

import jdk.jshell.spi.ExecutionControl;
import net.spotifei.Infrastructure.AudioPlayer.AudioUpdateListener;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.Music;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Services.MusicService;
import net.spotifei.Views.MainFrame;
import net.spotifei.Views.Panels.MusicPlayerPanel;

import javax.swing.*;

import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logError;

public class MusicController implements AudioUpdateListener {
    private final JPanel view;
    private final MusicService musicServices;
    private final MainFrame mainFrame;
    private final AppContext appContext;

    public MusicController(JPanel view, MainFrame mainFrame, MusicService musicServices, AppContext appContext) {
        this.view = view;
        this.musicServices = musicServices;
        this.mainFrame = mainFrame;
        this.appContext = appContext;
    }

    public void playUserNextMusic(){
        int userId = appContext.getPersonContext().getIdUsuario();
        Response<Music> response = musicServices.getNextMusicInUserQueue(userId);
        if(!response.isSuccess()){
            logError(response.getMessage());
            return;
        }
        Music musicFound = response.getData();
        appContext.setMusicContext(musicFound);
        logDebug("Próxima música do usuário obtida: " + musicFound.getNome());
        playMusic();
    }

    /**
     * Toca a música carregada no AppContext
     */
    public void playMusic(){
        MusicPlayerPanel musicPlayerPanel = (MusicPlayerPanel) view;
        Music music = appContext.getMusicContext();
        logDebug("Solicitação de tocar música recebido! musica:" + music.getNome());
        musicPlayerPanel.getMusicTitle().setText(music.getNome());
        musicPlayerPanel.getMusicArtist().setText(music.getAuthorNames());
        Response<Void> response = musicServices.playMusic(music.getIdMusica());
        if(!response.isSuccess()){
            logError(response.getMessage());
            return;
        }
        logDebug("Tocando agora: " + music.getNome());
    }

    /**
     * Toca a música pelo id dela
     * @param musicId Id da música a ser tocada
     */
    public void playMusic(int musicId) throws ExecutionControl.NotImplementedException {
        MusicPlayerPanel musicPlayerPanel = (MusicPlayerPanel) view;
        Response<Music> responseMusica = null; //musicServices.getMusicById(musicId); (CRIAR FUNÇÃO)
        if (responseMusica == null) throw new ExecutionControl.NotImplementedException("Necessário implementar no Repository!");

        if(!responseMusica.isSuccess()){
            logError(responseMusica.getMessage());
            return;
        }
        Music music = responseMusica.getData();
        logDebug("Solicitação de tocar música recebido! musica:" + music.getNome());
        musicPlayerPanel.getMusicTitle().setText(music.getNome());
        musicPlayerPanel.getMusicArtist().setText(music.getAuthorNames());
        Response<Void> responsePlay = musicServices.playMusic(music.getIdMusica());
        if(!responsePlay.isSuccess()){
            if(responsePlay.isError()){
                logError(responsePlay.getMessage(), responsePlay.getException());
            } else{
                logError(responsePlay.getMessage());
            }
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

    // Ações do Listener que é notificado em AudioPlayerWorker

    /**
     * Não aplicável para essa classe
     */
    @Override
    public void onMusicProgressUpdate(long musicTime, long musicTotalTime) {}

    /**
     * Não aplicável para essa classe
     */
    @Override
    public void onMusicPlayingStatusUpdate(boolean isPlaying) {}

    /**
     * Gerencia as ações depois da finalização da música
     */
    @Override
    public void onEndOfMusic() {
        playUserNextMusic();
    }
}
