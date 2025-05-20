package net.spotifei.Controller;

import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Services.MusicService;
import net.spotifei.Services.UserService;
import net.spotifei.Views.Panels.Artist.RegisterMusicPanel;

import javax.swing.*;

import static net.spotifei.Helpers.ResponseHelper.handleDefaultResponseIfError;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;

public class ArtistController {
    private final JPanel view;
    private final MusicService musicService;
    private final UserService userService;
    private final AppContext appContext;

    public ArtistController(JPanel view, MusicService musicService, UserService userService, AppContext appContext) {
        this.view = view;
        this.musicService = musicService;
        this.userService = userService;
        this.appContext = appContext;
    }

    public void registerMusic(){
        RegisterMusicPanel registerMusicPanel = (RegisterMusicPanel) view;
        String name = registerMusicPanel.getTxt_nome_musicacad().getText();
        String genre = registerMusicPanel.getTxt_genero_musicacad().getText();
        String musicFilePath = registerMusicPanel.getMusicFilePath();
        int userId = appContext.getPersonContext().getIdUsuario(); // id do usuario = id do artista

        Response<Void> response = musicService.registerMusic(name, genre, userId, musicFilePath);
        if(handleDefaultResponseIfError(response)) return;

        JOptionPane.showMessageDialog(view, "Música cadastrada com sucesso!");

        logDebug("Música cadastrada com sucesso!");
    }
}
