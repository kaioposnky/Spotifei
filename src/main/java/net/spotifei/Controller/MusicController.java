package net.spotifei.Controller;

import net.spotifei.Infrastructure.AudioPlayer.AudioUpdateListener;
import net.spotifei.Infrastructure.Container.AppContext;
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
    public void playMusic(){
        MusicPlayerPanel musicPlayerPanel = (MusicPlayerPanel) view;
        Music music = appContext.getMusicContext();
        handleSaveMusic(musicPlayerPanel, music);
    }

    /**
     * Toca a música pelo id dela
     * @param musicId Id da música a ser tocada
     */
    public void playMusic(int musicId) {
        MusicPlayerPanel musicPlayerPanel = (MusicPlayerPanel) view;
        Response<Music> responseMusica = musicServices.getMusicById(musicId);

        if(handleDefaultResponseIfError(responseMusica)) return;

        Music music = responseMusica.getData();
        handleSaveMusic(musicPlayerPanel, music);
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

    private boolean handlePlayMusic(MusicPlayerPanel musicPlayerPanel, Music music) {
        logDebug("Solicitação de tocar música recebido! musica:" + music.getNome());

        Response<Void> responsePlay = musicServices.playMusic(music.getIdMusica());
        if(handleDefaultResponseIfError(responsePlay)) return true;

        musicPlayerPanel.getMusicTitle().setText(music.getNome());
        musicPlayerPanel.getMusicArtist().setText(music.getAuthorNames());
        musicPlayerPanel.getFeedbackPanel().getLblLikeNumber().setText(String.valueOf(music.getLikes()));
        musicPlayerPanel.getFeedbackPanel().getLblDisLikeNumber().setText(String.valueOf(music.getDislikes()));

        logDebug("Tocando agora: " + music.getNome());
        return false;
    }

    private void handleSaveMusic(MusicPlayerPanel musicPlayerPanel, Music music) {
        if (handlePlayMusic(musicPlayerPanel, music)) return;
        Response<Void> responseSaveMusicToHistory = musicServices.
                addMusicToUserHistory(appContext.getPersonContext().getIdUsuario(), music.getIdMusica());
        if(handleDefaultResponseIfError(responseSaveMusicToHistory)) return;

        logDebug("Música salva ao histórico com sucesso!");
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
