package net.spotifei.Controller;

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
    public HistoryController(AppContext appContext, MusicService musicService, JPanel view){
        this.musicService = musicService;
        this.appContext = appContext;
        this.view = view;
    }

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

    private void openMusicsPopUp(HistoryPanel historyPanel, List<Music> musics, MusicInfoPanelBuilder panelBuilder, String title){
        historyPanel.setMusicsPopUp(new MusicsPopUp(
                appContext, historyPanel.getMainframe(), title, musics, panelBuilder));

        historyPanel.getMusicsPopUp().setVisible(true);
        historyPanel.getMusicsPopUp().getMusicListComponent().setMusics(musics);
    }
}
