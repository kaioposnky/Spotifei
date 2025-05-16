package net.spotifei.Controller;

import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Infrastructure.Factories.MusicInfoComponent.MusicInfoPanelBuilder;
import net.spotifei.Models.Artist;
import net.spotifei.Models.Music;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Models.User;
import net.spotifei.Services.MusicService;
import net.spotifei.Services.UserService;
import net.spotifei.Views.Panels.Admin.*;
import net.spotifei.Views.Panels.HistoryPanel;
import net.spotifei.Views.PopUps.MusicsPopUp;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

import static net.spotifei.Helpers.ResponseHelper.handleDefaultResponseIfError;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logError;

public class AdminController {
    private final JPanel view;
    private final MusicService musicService;
    private final UserService userService;
    private final AppContext appContext;

    public AdminController(JPanel view, MusicService musicService, UserService userService, AppContext appContext) {
        this.view = view;
        this.musicService = musicService;
        this.userService = userService;
        this.appContext = appContext;
    }

    public void registerMusic() {
        ADMRegisterMusicPanel registerMusicPanel = (ADMRegisterMusicPanel) view;
        String musicFilePath = registerMusicPanel.getMusicFilePath();
        if (musicFilePath == null || musicFilePath.isEmpty()) {
            createJDialog("Você deve incluir o arquivo da música!");
            return;
        }
        String musicName = registerMusicPanel.getTxt_nome_musicacad().getText();
        if (musicName == null || musicName.isEmpty()) {
            createJDialog("Você deve incluir o nome da música!");
            return;
        }
        String musicArtistName = registerMusicPanel.getTxt_artista_musicacad().getText();
        if (musicArtistName == null || musicArtistName.isEmpty()) {
            createJDialog("Você deve incluir o nome do artista!");
            return;
        }
        String musicGenreName = registerMusicPanel.getTxt_genero_musicacad().getText();
        if (musicGenreName == null || musicGenreName.isEmpty()) {
            createJDialog("Você deve incluir o gênero da música!");
            return;
        }

        Response<Void> response = musicService.registerMusic(musicFilePath, musicName, musicArtistName, musicGenreName);
        if (handleDefaultResponseIfError(response)) return;

        createJDialog("Música registrada com sucesso!");
        logDebug("Música registrada com sucesso!");
    }

    public void registerArtist() {
        ADMCadArtistPanel cadArtistPanel = (ADMCadArtistPanel) view;
        String email = cadArtistPanel.getTxt_email_cadastro().getText();
        String nome = cadArtistPanel.getTxt_nome_cadastro().getText();
        String sobrenome = cadArtistPanel.getTxt_sob_cadastro().getText();
        String telefone = cadArtistPanel.getTxt_telefone_cadastro().getText();
        String senha = cadArtistPanel.getTxt_senha_cadastro().getText();
        String nomeArtistico = cadArtistPanel.getTxt_nome_artistico().getText();

        if (email.isEmpty() || nome.isEmpty() || sobrenome.isEmpty() ||
                telefone.isEmpty() || senha.isEmpty() || nomeArtistico.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Todos os campos são obrigatórios!", "Erro de Cadastro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        Artist newArtist = new Artist();
        newArtist.setNome(cadArtistPanel.getTxt_nome_cadastro().getText());
        if (newArtist.getNome() == null || newArtist.getNome().isEmpty()) {
            createJDialog("Você deve incluir o nome do artista!");
            return;
        }
        newArtist.setEmail(cadArtistPanel.getTxt_email_cadastro().getText());
        if (newArtist.getEmail() == null || newArtist.getEmail().isEmpty()) {
            createJDialog("Você deve incluir o email do artista");
            return;
        }
        newArtist.setSenha(cadArtistPanel.getTxt_senha_cadastro().getText());
        if (newArtist.getSenha() == null || newArtist.getSenha().isEmpty()) {
            createJDialog("Você deve incluir a senha do artista!");
            return;
        }
        newArtist.setTelefone(cadArtistPanel.getTxt_telefone_cadastro().getText());
        if (newArtist.getTelefone() == null || newArtist.getTelefone().isEmpty()) {
            createJDialog("Você deve incluir o telefone do artista!");
            return;
        }
        newArtist.setSobrenome(cadArtistPanel.getTxt_sob_cadastro().getText());
        if (newArtist.getSobrenome() == null || newArtist.getSobrenome().isEmpty()) {
            createJDialog("Você deve incluir o sobrenome do artista!");
            return;
        }
        newArtist.setNomeArtistico(cadArtistPanel.getTxt_nome_artistico().getText());
        if (newArtist.getNomeArtistico() == null || newArtist.getNomeArtistico().isEmpty()) {
            createJDialog("Você deve incluir o nome artístico do artista!");
            return;
        }

        Response<Void> response = userService.createArtist(newArtist);
        if (handleDefaultResponseIfError(response)) return;

        createJDialog("Artista cadastrado com sucesso!");
        logDebug("Artista cadastrado com sucesso!");

    }

    private void createJDialog(String message) {
        JOptionPane.showMessageDialog(view, message);
    }

    public void deleteMusic() {
        ADMDelMusicPanel admDelMusicPanel = (ADMDelMusicPanel) view;
        String musicIdText = admDelMusicPanel.getTxt_id_musicadel().getText();
        if (musicIdText == null || musicIdText.isEmpty()) {
            createJDialog("Você deve incluir o ID da música a ser deletada!");
            return;
        }
        try {
            int musicIdToDelete = Integer.parseInt(musicIdText);
            Response<List<Music>> response = musicService.deletMusic(musicIdToDelete);
            if (handleDefaultResponseIfError(response)) {
                return;
            }
            createJDialog("Música deletada com sucesso!");
            logDebug("Música deletada com sucesso!");
        } catch (NumberFormatException e) {
            createJDialog("O ID da música deve ser um número inteiro válido!");
            logError("Erro ao deletar música", e);
        } catch (Exception e) {
            createJDialog("Erro ao tentar deletar a música.");
            logError("Erro ao deletar música", e);
        }
    }

    public void showUserDislikedMusics() {
        HistoryPanel historyPanel = (HistoryPanel) view;

        int userId = appContext.getPersonContext().getIdUsuario();
        int limit = 10; // alterável limite
        Response<List<Music>> response = musicService.getUserDislikedMusics(userId, limit);
        if (handleDefaultResponseIfError(response)) return;

        List<Music> musics = response.getData();

        MusicInfoPanelBuilder panelBuilder = new MusicInfoPanelBuilder(appContext, historyPanel.getMainframe());
        panelBuilder.selectLikedOrDislikedMusicInfoPanel();
        openMusicsPopUp(historyPanel, musics, panelBuilder, "");

        logDebug("Mostrando músicas curtidas pelo usuário ");
    }

    public void loadSystemStatistics() {
        ADMEstatisticasPanel statisticsPanel = (ADMEstatisticasPanel) view;
        Response<Long> responseMusics = musicService.getTotalMusics();
        if (handleDefaultResponseIfError(responseMusics)) return;
        Response<Long> responseUsers = userService.getTotalUsers();
        if (handleDefaultResponseIfError(responseUsers)) return;

        long totalMusics = responseMusics.getData();
        long totalUsers = responseUsers.getData();
        statisticsPanel.setTotalMusics(totalMusics);
        statisticsPanel.setTotalUsers(totalUsers);

        Response<List<Music>> responseMostLikedMusics = musicService.getMostLikedMusics();
        if (handleDefaultResponseIfError(responseMostLikedMusics)) return;

        Response<List<Music>> responseMostDislikedMusics = musicService.getMostDislikedMusics();
        if (handleDefaultResponseIfError(responseMostDislikedMusics)) return;

        statisticsPanel.setMostLikedMusics(responseMostLikedMusics.getData());
        statisticsPanel.setMostDislikedMusics(responseMostDislikedMusics.getData());

        for (Music music : responseMostLikedMusics.getData() ){
            logDebug("Música: " + music.getNome() + " com " + music.getLikes() + " curtidas");
        }
        for (Music music : responseMostDislikedMusics.getData() ){
            logDebug("Música: " + music.getNome() + " com " + music.getDislikes() + " deslikes");
        }

    }

    private void openMusicsPopUp(HistoryPanel historyPanel, List<Music> musics, MusicInfoPanelBuilder panelBuilder, String title) {
        historyPanel.setMusicsPopUp(new MusicsPopUp(
                appContext, historyPanel.getMainframe(), title, musics, panelBuilder));

        historyPanel.getMusicsPopUp().setVisible(true);
        historyPanel.getMusicsPopUp().getMusicListComponent().setMusics(musics);
    }






    public void ConstUser() {
        ADMConsultUserPanel admConsultUserPanel = (ADMConsultUserPanel) view;
        String searchTerm = admConsultUserPanel.getTxt_pesquisa().getText();

        Response<User> response = userService.getUsuarioByEmail(searchTerm);
        if (handleDefaultResponseIfError(response)) return;
        User user = response.getData();
        List<User> users = new ArrayList<>();
        if (user != null) {
            users.add(user);
        }
        admConsultUserPanel.getConstUserListComponent().setUser(users);
        admConsultUserPanel.getConstUserListComponent().updateUI();
        logDebug("Músicas encontradas para a pesquisa \"" + searchTerm + "\": " + users.size());
    }
}


