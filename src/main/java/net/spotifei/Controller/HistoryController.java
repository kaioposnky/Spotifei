package net.spotifei.Controller;

import net.spotifei.Infrastructure.Container.AppContext;
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
        int limit = 10; // alterável limite
        Response<List<Music>> response = musicService.getUserMostSearchedMusics(userId, limit);
        if(handleDefaultResponseIfError(response)) return;

        List<Music> musics = response.getData();

        historyPanel.setMusicsPopUp(new MusicsPopUp(
                appContext, historyPanel.getMainframe(), "Suas 10 músicas mais buscadas", musics));

        historyPanel.getMusicsPopUp().setVisible(true);
        historyPanel.getMusicsPopUp().getMusicListComponent().setMusics(musics);

        logDebug("Mostrando 10 músicas mais buscadas do usuário!");
    }
}
