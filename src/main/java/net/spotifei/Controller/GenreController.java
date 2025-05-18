package net.spotifei.Controller;

import net.spotifei.Models.Responses.Response;
import net.spotifei.Services.AuthService;
import net.spotifei.Services.MusicService;
import net.spotifei.Views.MainFrame;
import net.spotifei.Views.Panels.Admin.ADMCadGenre;

import javax.swing.*;

import static net.spotifei.Helpers.ResponseHelper.handleDefaultResponseIfError;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;

public class GenreController {

    private final MainFrame mainFrame;
    private final JPanel view;
    private final MusicService musicService;

    public GenreController(JPanel view, MainFrame mainFrame, MusicService musicService) {
        this.musicService = musicService;
        this.mainFrame = mainFrame;
        this.view = view;
    }
    
    public void createGenre(){
        ADMCadGenre cadGenreView = (ADMCadGenre) view;
        String genreName = cadGenreView.getGenreTextField().getText();
        Response<Void> response = musicService.createGenre(genreName);
        if(handleDefaultResponseIfError(response)) return;

        JOptionPane.showMessageDialog(view, "Gênero " + genreName + " criado com sucesso!");
        logDebug("Gênero " + genreName + " criado com sucesso!");
    }
}
