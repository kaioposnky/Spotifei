package net.spotifei.Controller;

import net.spotifei.Models.Artist;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Models.User;
import net.spotifei.Services.MusicService;
import net.spotifei.Services.UserService;
import net.spotifei.Views.Panels.Admin.ADMCadArtistPanel;
import net.spotifei.Views.Panels.Admin.ADMRegisterMusicPanel;

import javax.swing.*;

import static net.spotifei.Helpers.ResponseHelper.handleDefaultResponseIfError;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logError;

public class AdminController {
    private final JPanel view;
    private final MusicService musicService;
    private final UserService userService;
    public AdminController(JPanel view, MusicService musicService, UserService userService){
        this.view = view;
        this.musicService = musicService;
        this.userService = userService;
    }

    public void registerMusic(){
        ADMRegisterMusicPanel registerMusicPanel = (ADMRegisterMusicPanel) view;
        String musicFilePath = registerMusicPanel.getMusicFilePath();
        if (musicFilePath == null || musicFilePath.isEmpty()){
            createJDialog("Você deve incluir o arquivo da música!");
            return;
        }
        String musicName = registerMusicPanel.getTxt_nome_musicacad().getText();
        if (musicName == null || musicName.isEmpty()){
            createJDialog("Você deve incluir o nome da música!");
            return;
        }
        String musicArtistName = registerMusicPanel.getTxt_artista_musicacad().getText();
        if (musicArtistName == null || musicArtistName.isEmpty()){
            createJDialog("Você deve incluir o nome do artista!");
            return;
        }
        String musicGenreName = registerMusicPanel.getTxt_genero_musicacad().getText();
        if (musicGenreName == null || musicGenreName.isEmpty()){
            createJDialog("Você deve incluir o gênero da música!");
            return;
        }

        Response<Void> response = musicService.registerMusic(musicFilePath, musicName, musicArtistName, musicGenreName);
        if(handleDefaultResponseIfError(response)) return;

        createJDialog("Música registrada com sucesso!");
        logDebug("Música registrada com sucesso!");
    }

    public void registerArtist(){
        ADMCadArtistPanel cadArtistPanel = (ADMCadArtistPanel) view;
        Artist newArtist = new Artist();
        newArtist.setNome(cadArtistPanel.getTxt_nome_cadastro().getText());
        if (newArtist.getNome() == null || newArtist.getNome().isEmpty()){
            createJDialog("Você deve incluir o nome do artista!");
            return;
        }
        newArtist.setEmail(cadArtistPanel.getTxt_email_cadastro().getText());
        if (newArtist.getEmail() == null || newArtist.getEmail().isEmpty()){
            createJDialog("Você deve incluir o email do artista");
            return;
        }
        newArtist.setSenha(cadArtistPanel.getTxt_senha_cadastro().getText());
        if (newArtist.getSenha() == null || newArtist.getSenha().isEmpty()){
            createJDialog("Você deve incluir a senha do artista!");
            return;
        }
        newArtist.setTelefone(cadArtistPanel.getTxt_telefone_cadastro().getText());
        if(newArtist.getTelefone() == null || newArtist.getTelefone().isEmpty()){
            createJDialog("Você deve incluir o telefone do artista!");
            return;
        }
        newArtist.setSobrenome(cadArtistPanel.getTxt_sob_cadastro().getText());
        if (newArtist.getSobrenome() == null || newArtist.getSobrenome().isEmpty()){
            createJDialog("Você deve incluir o sobrenome do artista!");
            return;
        }
        newArtist.setNomeArtistico(cadArtistPanel.getTxt_nome_artistico().getText());
        if (newArtist.getNomeArtistico() == null || newArtist.getNomeArtistico().isEmpty()){
            createJDialog("Você deve incluir o nome artístico do artista!");
            return;
        }

        Response<Void> response = userService.createArtist(newArtist);
        if(handleDefaultResponseIfError(response)) return;

        createJDialog("Artista cadastrado com sucesso!");
        logDebug("Artista cadastrado com sucesso!");

    }

    private void createJDialog(String message){
        JOptionPane.showMessageDialog(view, message);
    }
}
