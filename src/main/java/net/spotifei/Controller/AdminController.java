package net.spotifei.Controller;

import net.spotifei.Models.Responses.Response;
import net.spotifei.Services.MusicService;
import net.spotifei.Views.Panels.Admin.ADMRegisterMusicPanel;

import javax.swing.*;

import static net.spotifei.Helpers.ResponseHelper.handleDefaultResponseIfError;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logError;

public class AdminController {
    private final JPanel view;
    private final MusicService musicService;
    public AdminController(JPanel view, MusicService musicService){
        this.view = view;
        this.musicService = musicService;
    }

    public void registerMusic(){
        ADMRegisterMusicPanel registerMusicPanel = (ADMRegisterMusicPanel) view;
        String musicFilePath = registerMusicPanel.getMusicFilePath();
        if (musicFilePath == null || musicFilePath.isEmpty()){
            createJDialog("Você deve incluir o arquivo da música!");
        }
        String musicName = registerMusicPanel.getTxt_nome_musicacad().getText();
        if (musicName == null || musicName.isEmpty()){
            createJDialog("Você deve incluir o nome da música!");
        }
        String musicArtistName = registerMusicPanel.getTxt_artista_musicacad().getText();
        if (musicArtistName == null || musicArtistName.isEmpty()){
            createJDialog("Você deve incluir o nome do artista!");
        }
        String musicGenreName = registerMusicPanel.getTxt_genero_musicacad().getText();
        if (musicGenreName == null || musicGenreName.isEmpty()){
            createJDialog("Você deve incluir o gênero da música!");
        }
        Response<Void> response = musicService.registerMusic(musicFilePath, musicName, musicArtistName, musicGenreName);
        if(handleDefaultResponseIfError(response)) return;

        createJDialog("Música registrada com sucesso!");
        logDebug("Música registrada com sucesso!");
    }

    private void createJDialog(String message){
        JOptionPane.showMessageDialog(view, message);
    }
}
