package net.spotifei.Controller;

// Importes Otimizados
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.Artist;
import net.spotifei.Models.Music;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Models.User;
import net.spotifei.Services.MusicService;
import net.spotifei.Services.UserService;
import net.spotifei.Views.Panels.Admin.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static net.spotifei.Helpers.ResponseHelper.handleDefaultResponseIfError;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;


public class AdminController {
    private final JPanel view;
    private final MusicService musicService;
    private final UserService userService;
    private final AppContext appContext;

    /**
     * Construtor da classe.
     * Inicializa o controlador com suas dependências necessárias.
     *
     * @param view O painel da interface gráfica onde as ações ocorrem.
     * @param musicService O serviço responsável pela lógica de negócios de músicas.
     * @param userService O serviço responsável pela lógica de negócios de usuários.
     * @param appContext O contexto da aplicação.
     */
    public AdminController(JPanel view, MusicService musicService, UserService userService, AppContext appContext) {
        this.view = view;
        this.musicService = musicService;
        this.userService = userService;
        this.appContext = appContext;
    }

    /**
     * Registra uma nova música no sistema.
     * Valida se todos os campos obrigatórios foram preenchidos.
     * Em caso de sucesso, exibe uma mensagem de confirmação; caso contrário, exibe uma mensagem de erro.
     */
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

    /**
     * Cadastra um novo artista no sistema.
     * Garante que todos os campos obrigatórios estejam preenchidos.
     * Se a validação for bem-sucedida, um novo artista é criado.
     * Exibe mensagens de sucesso ou erro ao usuário.
     */
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

    /**
     * Função auxiliar para exibir mensagens em uma caixa de diálogo JOptionPane.
     *
     * @param message A mensagem a ser exibida.
     */
    private void createJDialog(String message) {
        JOptionPane.showMessageDialog(view, message);
    }

    /**
     * Exclui uma música existente do sistema.
     * Recebe um objeto Music como parâmetro e utiliza o musicService para remover a música.
     * Exibe uma mensagem de sucesso se a exclusão for bem-sucedida.
     *
     * @param music O objeto Music a ser deletado.
     */
    public void deleteMusic(Music music) {
        Response<Void> responseDelete = musicService.deleteMusic(music.getIdMusica());
        if (handleDefaultResponseIfError(responseDelete)) return;

        JOptionPane.showMessageDialog(view, "Música " + music.getNome() + " deletada com sucesso!");
        logDebug("Músicas encontradas!");
    }

    /**
     * Carrega e exibe todas as músicas disponíveis no painel de exclusão de músicas.
     */
    public void loadAllMusicsForDelete(){
        ADMDelMusicPanel admDelMusicPanel = (ADMDelMusicPanel) view;

        Response<List<Music>> responseMusics = musicService.getAllMusics();
        if(handleDefaultResponseIfError(responseMusics)) return;

        admDelMusicPanel.getMusicListComponent().setMusics(responseMusics.getData());
        admDelMusicPanel.getMusicListComponent().updateUI();
        logDebug("Músicas encontradas!");
    }

    /**
     * Carrega e exibe estatísticas do sistema.
     * Atualiza o painel de estatísticas com os dados obtidos.
     */
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
    }

    /**
     * Consulta um usuário no sistema.
     */
    public void constUser() {
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


