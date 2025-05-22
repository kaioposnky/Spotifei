package net.spotifei.Controller;

// Importes Otimizados
import net.spotifei.Infrastructure.AudioPlayer.AudioUpdateListener;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.Music;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Services.MusicService;
import net.spotifei.Views.Components.FeedBackComponent;
import net.spotifei.Views.MainFrame;
import net.spotifei.Views.Panels.QueueMusicInfoPanel;
import net.spotifei.Views.Panels.SearchPanel;

import javax.swing.*;
import java.util.List;

import static net.spotifei.Helpers.ResponseHelper.handleDefaultResponseIfError;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logError;

public class MusicController implements AudioUpdateListener {
    private final JPanel view;
    private final MusicService musicServices;
    private final MainFrame mainFrame;
    private final AppContext appContext;
    private JDialog waitDialog;

    /**
     * Construtor da classe.
     * Inicializa o controlador de música com as dependências necessárias.
     *
     * @param view O painel da interface gráfica atual.
     * @param mainFrame A janela principal da aplicação.
     * @param musicServices O serviço responsável pela lógica de negócios de músicas.
     * @param appContext O contexto da aplicação, contendo informações do usuário e da música atual.
     */
    public MusicController(JPanel view, MainFrame mainFrame, MusicService musicServices, AppContext appContext) {
        this.view = view;
        this.musicServices = musicServices;
        this.mainFrame = mainFrame;
        this.appContext = appContext;
    }

    /**
     * Toca a próxima música na fila de reprodução do usuário.
     * Obtém o ID do usuário do AppContext e solicita a próxima música.
     */
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
     *
     * @param musicId Id da música a ser tocada
     */
    public void playMusicById(int musicId) {
        Response<Music> responseMusica = musicServices.getMusicById(musicId);
        if(handleDefaultResponseIfError(responseMusica)) return;
        Music music = responseMusica.getData();

        playMusicInBackground(music);

        logDebug("Tocando agora: " + music.getNome());
    }

    /**
     * Define o volume do áudio.
     *
     * @param volume O valor do volume a ser definido.
     */
    public void setAudioVolume(float volume){
        Response<Void> response = musicServices.setAudioVolume(volume);
        if(handleDefaultResponseIfError(response)) return;

        logDebug("Volume alterado para " + volume + " com sucesso!");
    }

    /**
     * Define o tempo de reprodução atual da música.
     *
     * @param musicTime O tempo (em segundos ou milissegundos, dependendo da implementação do serviço) para o qual pular.
     */
    public void setMusicTime(float musicTime){
        Response<Void> response = musicServices.setMusicTime(musicTime);
        if(handleDefaultResponseIfError(response)) return;

        logDebug("Tempo da música alterado para " + musicTime + " com sucesso!");
    }

    /**
     * Alterna o estado de pausa da música (pausa se estiver tocando, despausa se estiver pausada).
     */
    public void togglePauseMusic(){
        Response<Void> response = musicServices.pauseMusic();
        if(handleDefaultResponseIfError(response)) return;

        // modificar o status do appcontext na musicontext para deixar pausada ou despausada
        logDebug("Música pausada com sucesso!");
    }

    /**
     * Insere ou atualiza a avaliação (like/dislike) do usuário para a música atual.
     * Obtém o ID da música e se ela foi curtida ou não do FeedBackComponent.
     * Chama o musicServices para registrar a avaliação do usuário.
     */
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

    /**
     * Realiza uma busca por músicas, incluindo detalhes de preferência do usuário (se ele curtiu ou não).
     * Chama o musicServices para pesquisar as músicas e atualiza a lista de músicas na interface.
     */
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

    /**
     * Realiza uma busca genérica por músicas.
     * Chama o musicServices para pesquisar as músicas e atualiza a lista de músicas na interface.
     */
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

    /**
     * Pula para a próxima música na fila de reprodução do usuário.
     * Solicita a próxima música ao musicServices e inicia sua reprodução.
     */
    public void skipMusic(){
        playUserNextMusic();

        logDebug("Tocando a próxima música!");
    }

    /**
     * Volta para a música anterior na fila de reprodução do usuário.
     * Solicita a música anterior ao musicServices e inicia sua reprodução.
     */
    public void previousMusic(){
        Response<Music> response = musicServices.getUserPreviousMusic(appContext.getPersonContext().getIdUsuario());
        if(handleDefaultResponseIfError(response)) return;

        Music musicFound = response.getData();

        appContext.setMusicContext(musicFound);
        playMusicInBackground(musicFound);

        logDebug("Tocando a música anterior!");
    }

    /**
     * Carrega a fila de músicas do usuário.
     */
    public void loadUserMusicQueue(){
        QueueMusicInfoPanel queueMusicInfoPanel = (QueueMusicInfoPanel) view;
        int userId = appContext.getPersonContext().getIdUsuario();

        Response<List<Music>> responseUserMusicQueue = musicServices.getUserQueue(userId);
        if (handleDefaultResponseIfError(responseUserMusicQueue)) return;

        List<Music> queueMusics = responseUserMusicQueue.getData();

        SwingUtilities.invokeLater(() -> {
            queueMusicInfoPanel.updateMusicListPanel(queueMusics);
        });

        // log removido por spamar muito no console por conta do worker de queue
//        logDebug("Músicas da fila obtidas com sucesso!");
    }

    /**
     * Função auxiliar privada para salvar a música atualmente em reprodução no histórico do usuário.
     *
     * @param music A música a ser salva no histórico.
     * @return true se a música foi salva com sucesso no histórico, false caso contrário.
     */
    private boolean handleSaveMusicToHistory(Music music) {
        Response<Void> responseSaveMusicToHistory = musicServices.
                addMusicToUserHistory(appContext.getPersonContext().getIdUsuario(), music.getIdMusica());
        return handleDefaultResponseIfError(responseSaveMusicToHistory);
    }

    // Ações do Listener que é notificado em AudioPlayerWorker

    /**
     * Método de callback chamado quando uma nova música é selecionada para reprodução.
     * Atualiza o contexto da música no AppContext e a salva no histórico.
     *
     * @param music A música selecionada.
     */
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

        boolean musicPlayIsPending = musicServices.isNewMusicSelected() &&
                (currentContextMusic == null || musicServices.getNewMusicSelectedId() != currentContextMusic.getIdMusica());

        // ignorar pedido se já tem um pendente
        if(musicPlayIsPending){
            return;
        }
        musicServices.setNewMusicSelectedId(0);
        musicServices.setNewMusicSelected(false);
        playUserNextMusic();

        logDebug("Tocando próxima música da fila.");
    }

    /**
     * Inicia a reprodução de uma música em segundo plano.
     * Exibe um JDialog de "aguarde" enquanto a música é carregada e preparada para tocar.
     *
     * @param music A música a ser reproduzida.
     */
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
