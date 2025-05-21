package net.spotifei.Controller;

// Importes Otimizados
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Infrastructure.Factories.MusicInfoComponent.MusicInfoPanelBuilder;
import net.spotifei.Models.Music;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Services.MusicService;
import net.spotifei.Views.Panels.HistoryPanel;
import net.spotifei.Views.PopUps.MusicsPopUp;

import javax.swing.*;
import java.util.List;

import static net.spotifei.Helpers.ResponseHelper.handleDefaultResponseIfError;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;

public class HistoryController {
    private final MusicService musicService;
    private final AppContext appContext;
    private final JPanel view;

    /**
     * Construtor da classe.
     * Inicializa o controlador de histórico de músicas com as dependências necessárias.
     *
     * @param appContext O contexto da aplicação, contendo informações do usuário logado.
     * @param musicService O serviço responsável pela lógica de negócios de músicas, incluindo histórico e likes/dislikes.
     * @param view O painel da interface gráfica atual (HistoryPanel).
     */
    public HistoryController(AppContext appContext, MusicService musicService, JPanel view){
        this.musicService = musicService;
        this.appContext = appContext;
        this.view = view;
    }

    /**
     * Exibe as músicas mais pesquisadas (ou tocadas) pelo usuário logado.
     * Busca as músicas mais pesquisadas através do musicService.
     * Constrói e abre um pop-up com as informações das músicas.
     */
    public void showUserMostSearchedMusics(){
        HistoryPanel historyPanel = (HistoryPanel) view;

        int userId = appContext.getPersonContext().getIdUsuario();
        int limit = 50; // alterável limite
        Response<List<Music>> response = musicService.getUserMostSearchedMusics(userId, limit);
        if(handleDefaultResponseIfError(response)) return;

        List<Music> musics = response.getData();

        MusicInfoPanelBuilder panelBuilder = new MusicInfoPanelBuilder(appContext, historyPanel.getMainframe());
        panelBuilder.selectMostPlayedMusicInfoPanel();
        openMusicsPopUp(historyPanel, musics, panelBuilder, "Últimas músicas tocadas");

        logDebug("Mostrando 10 músicas mais buscadas do usuário!");
    }

    /**
     * Exibe as músicas que o usuário logado curtiu (deu "like").
     * Busca as músicas curtidas pelo usuário.
     * Constrói e abre um pop-up para exibir a lista dessas músicas.
     */
    public void showUserLikedMusics(){
        HistoryPanel historyPanel = (HistoryPanel) view;

        int userId = appContext.getPersonContext().getIdUsuario();
        int limit = 50; // alterável limite
        Response<List<Music>> response = musicService.getUserLikedMusics(userId, limit);
        if(handleDefaultResponseIfError(response)) return;

        List<Music> musics = response.getData();

        MusicInfoPanelBuilder panelBuilder = new MusicInfoPanelBuilder(appContext, historyPanel.getMainframe());
        panelBuilder.selectLikedOrDislikedMusicInfoPanel();
        openMusicsPopUp(historyPanel, musics, panelBuilder, "Músicas que você deu like");

        logDebug("Mostrando músicas curtidas pelo usuário ");
    }

    /**
     * Exibe as músicas que o usuário logado não curtiu (deu "dislike").
     * Busca as músicas com "dislike" do usuário.
     * Constrói e abre um pop-up para exibir a lista dessas músicas.
     */
    public void showUserDislikedMusics(){
        HistoryPanel historyPanel = (HistoryPanel) view;

        int userId = appContext.getPersonContext().getIdUsuario();
        int limit = 50; // alterável limite
        Response<List<Music>> response = musicService.getUserDislikedMusics(userId, limit);
        if(handleDefaultResponseIfError(response)) return;

        List<Music> musics = response.getData();

        MusicInfoPanelBuilder panelBuilder = new MusicInfoPanelBuilder(appContext, historyPanel.getMainframe());
        panelBuilder.selectLikedOrDislikedMusicInfoPanel();
        openMusicsPopUp(historyPanel, musics, panelBuilder, "Músicas que você deu dislike");

        logDebug("Mostrando músicas curtidas pelo usuário ");
    }

    /**
     * Abre um pop-up (MusicsPopUp) para exibir uma lista de músicas.
     *
     * @param historyPanel O painel de histórico que invocou o pop-up.
     * @param musics A lista de objetos Music a serem exibidos no pop-up.
     * @param panelBuilder O builder para configurar como as informações das músicas serão renderizadas no pop-up.
     * @param title O título a ser exibido no pop-up.
     */
    private void openMusicsPopUp(HistoryPanel historyPanel, List<Music> musics, MusicInfoPanelBuilder panelBuilder, String title){
        historyPanel.setMusicsPopUp(new MusicsPopUp(
                appContext, historyPanel.getMainframe(), title, musics, panelBuilder));

        historyPanel.getMusicsPopUp().setVisible(true);
        historyPanel.getMusicsPopUp().getMusicListComponent().setMusics(musics);
    }
}
