package net.spotifei.Controller;

import net.spotifei.Infrastructure.AudioPlayer.AudioUpdateListener;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Infrastructure.Repository.MusicRepository;
import net.spotifei.Models.Music;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Services.MusicService;
import net.spotifei.Views.Components.FeedBackComponent;
import net.spotifei.Views.MainFrame;
import net.spotifei.Views.Panels.MusicPlayerPanel;
import net.spotifei.Views.Panels.SearchPanel;

import javax.swing.*;

import java.util.List;

import static net.spotifei.Helpers.ResponseHelper.handleDefaultResponseIfError;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.*;

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
    public void playMusic() {
        Response<Music> responseMusica = musicServices.getMusicById(appContext.getMusicContext().getIdMusica());
        if(handleDefaultResponseIfError(responseMusica)) return;

        Music music = responseMusica.getData();
        handlePlayMusic(music);
        logDebug("Tocando agora: " + music.getNome());
        handleSaveMusic(music);
        logDebug("Música salva ao histórico com sucesso!");
    }

    public void setAudioVolume(float volume){
        Response<Void> response = musicServices.setAudioVolume(volume);
        if(handleDefaultResponseIfError(response)) return;

        logDebug("Volume alterado para " + volume + " com sucesso!");
    }

    public void setMusicTime(float musicTime){
        Response<Void> response = musicServices.setMusicTime(musicTime);
        if(handleDefaultResponseIfError(response)) return;

        logDebug("Tempo da música alterado para " + musicTime + " com sucesso!");
    }

    public void pauseMusic(){
        Response<Void> response = musicServices.pauseMusic();
        if(handleDefaultResponseIfError(response)) return;

        // modificar o status do appcontext na musicontext para deixar pausada ou despausada
        logDebug("Música pausada com sucesso!");
    }

    public void insertUserRating(){
        if(appContext.getPersonContext() == null) return;
//
        FeedBackComponent feedBackComponent = (FeedBackComponent) view;
        if(feedBackComponent.getMusic() == null) return;

        int musicId = feedBackComponent.getMusic().getIdMusica();
        if(musicId <= 0) return; // musica 0 é pq provavelmente tá desativado, melhor ignorar

        int userId = appContext.getPersonContext().getIdUsuario();
        Boolean liked = feedBackComponent.isMusicLiked();

        Response<Void> response = musicServices.setOrInsertMusicUserRating(musicId, userId, liked);
        if(handleDefaultResponseIfError(response)) return;

        String ratingStatus = (liked == null)? "nulo"
                : (liked ? "like" : "dislike");


        logDebug("Avaliação da música com id " + musicId +
                " alterada para " + ratingStatus +
                " para o usuário com id " + userId + " com sucesso!");
    }

    public void searchMusic(){
        SearchPanel searchPanel = (SearchPanel) view;
        String searchTerm = searchPanel.getTxt_pesquisar().getText();
        int userId = appContext.getPersonContext().getIdUsuario();

        Response<List<Music>> response = musicServices.searchMusics(searchTerm, userId);
        if(handleDefaultResponseIfError(response)) return;

        List<Music> musics = response.getData();
        searchPanel.getMusicListComponent().setMusics(musics);
        searchPanel.getMusicListComponent().updateUI();
        logDebug("Músicas encontradas para a pesquisa \"" + searchTerm + "\": " + musics.size());
    }

    private boolean handlePlayMusic(Music music) {
        Response<Void> responsePlay = musicServices.playMusic(music.getIdMusica(),
                appContext.getPersonContext().getIdUsuario());
        return handleDefaultResponseIfError(responsePlay);
    }

    private boolean handleSaveMusic(Music music) {
        Response<Void> responseSaveMusicToHistory = musicServices.
                addMusicToUserHistory(appContext.getPersonContext().getIdUsuario(), music.getIdMusica());
        return handleDefaultResponseIfError(responseSaveMusicToHistory);
    }

    // Ações do Listener que é notificado em AudioPlayerWorker

    /**
     * Não aplicável para essa classe
     */
    @Override
    public void onSelectMusic(Music music) {}

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
