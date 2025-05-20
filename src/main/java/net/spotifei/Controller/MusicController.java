package net.spotifei.Controller;

import net.spotifei.Infrastructure.AudioPlayer.AudioUpdateListener;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.Music;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Services.MusicService;
import net.spotifei.Views.Components.FeedBackComponent;
import net.spotifei.Views.MainFrame;
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
    private JDialog waitDialog;

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
        playMusicFromContext();
    }

    /**
     * Toca a música carregada no AppContext
     */
    public void playMusicFromContext() {
        Response<Music> responseMusica = musicServices.getMusicById(appContext.getMusicContext().getIdMusica());
        if(handleDefaultResponseIfError(responseMusica)) return;

        Music music = responseMusica.getData();
        appContext.setMusicContext(music);
        playMusicInBackground(music);

        logDebug("Tocando agora: " + music.getNome());
    }

    /**
     * Toca a música informada pelo id
     * @param musicId Id da música a ser tocada
     */
    public void playMusicById(int musicId) {
        Response<Music> responseMusica = musicServices.getMusicById(musicId);
        if(handleDefaultResponseIfError(responseMusica)) return;
        Music music = responseMusica.getData();

        playMusicInBackground(music);

        logDebug("Tocando agora: " + music.getNome());
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

    public void togglePauseMusic(){
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

    public void searchMusicWithUserInfo(){
        SearchPanel searchPanel = (SearchPanel) view;
        String searchTerm = searchPanel.getTxt_pesquisar().getText();
        int userId = appContext.getPersonContext().getIdUsuario();

        Response<List<Music>> response = musicServices.searchMusicsWithUserDetails(searchTerm, userId);
        if(handleDefaultResponseIfError(response)) return;

        List<Music> musics = response.getData();
        searchPanel.getMusicListComponent().setMusics(musics);
        searchPanel.getMusicListComponent().updateUI();
        logDebug("Músicas encontradas para a pesquisa \"" + searchTerm + "\": " + musics.size());
    }

    public void searchMusic(){
        SearchPanel searchPanel = (SearchPanel) view;
        String searchTerm = searchPanel.getTxt_pesquisar().getText();

        Response<List<Music>> response = musicServices.searchMusics(searchTerm);
        if(handleDefaultResponseIfError(response)) return;

        List<Music> musics = response.getData();
        searchPanel.getMusicListComponent().setMusics(musics);
        searchPanel.getMusicListComponent().updateUI();
        logDebug("Músicas encontradas para a pesquisa \"" + searchTerm + "\": " + musics.size());
    }

    public void skipMusic(){
        Response<Music> response = musicServices.getNextMusicInUserQueue(appContext.getPersonContext().getIdUsuario());
        if(handleDefaultResponseIfError(response)) return;

        Music musicFound = response.getData();

        playMusicInBackground(musicFound);

        logDebug("Tocando a próxima música!");
    }

    public void previousMusic(){
        Response<Music> response = musicServices.getUserPreviousMusic(appContext.getPersonContext().getIdUsuario());
        if(handleDefaultResponseIfError(response)) return;

        Music musicFound = response.getData();

        appContext.setMusicContext(musicFound);
        playMusicInBackground(musicFound);

        logDebug("Tocando a música anterior!");
    }

    private boolean handleSaveMusicToHistory(Music music) {
        Response<Void> responseSaveMusicToHistory = musicServices.
                addMusicToUserHistory(appContext.getPersonContext().getIdUsuario(), music.getIdMusica());
        return handleDefaultResponseIfError(responseSaveMusicToHistory);
    }

    // Ações do Listener que é notificado em AudioPlayerWorker

    @Override
    public void onSelectMusic(Music music) {
        if(musicServices.isNewMusicSelected() && musicServices.getNewMusicSelectedId() == music.getIdMusica()){
            musicServices.setNewMusicSelected(false);
            musicServices.setNewMusicSelectedId(0);

            appContext.setMusicContext(music);
            handleSaveMusicToHistory(music);
            logDebug("Música " + music.getNome() + " salva ao histórico com sucesso!");
        }

    }

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
        Music currentContextMusic = appContext.getMusicContext();

        boolean musicFinishedWasPendingSelection = musicServices.isNewMusicSelected() && currentContextMusic != null &&
                musicServices.getNewMusicSelectedId() == currentContextMusic.getIdMusica();

        // ignorar pedido se já tem um pendente
        if(musicServices.isNewMusicSelected() && musicFinishedWasPendingSelection){
            return;
        }
        musicServices.setNewMusicSelectedId(0);
        musicServices.setNewMusicSelected(false);
        playUserNextMusic();
    }

    public void playMusicInBackground(Music music){
        waitDialog = new JDialog();
        JLabel messageLabel = new JLabel("Estamos preparando sua música para tocar...");
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        waitDialog.add(messageLabel);
        waitDialog.pack();
        waitDialog.setLocationRelativeTo(null);
        waitDialog.setModal(false);
        waitDialog.setVisible(true);
        SwingWorker<Void, Void> backgroundWorker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Response<Void> responsePlay = musicServices.playMusic(music.getIdMusica(),
                        appContext.getPersonContext().getIdUsuario());
                handleDefaultResponseIfError(responsePlay);
                return null;
            }

            @Override
            protected void done() {
                waitDialog.dispose();
            }
        };
        backgroundWorker.execute();
    }
}
