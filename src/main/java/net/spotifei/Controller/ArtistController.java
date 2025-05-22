package net.spotifei.Controller;

// Importes Otimizados
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


    /**
     * Construtor da classe.
     * Inicializa o controlador com as dependências necessárias.
     *
     * @param view O painel da interface gráfica onde as ações do artista ocorrem.
     * @param musicService O serviço responsável pela lógica de negócios de músicas.
     * @param userService O serviço responsável pela lógica de negócios de usuários.
     * @param appContext O contexto da aplicação.
     */
    public ArtistController(JPanel view, MusicService musicService, UserService userService, AppContext appContext) {
        this.view = view;
        this.musicService = musicService;
        this.userService = userService;
        this.appContext = appContext;
    }

    /**
     * Registra uma nova música no sistema, associando-a ao artista logado.
     * Exibe uma mensagem de sucesso ou lida com erros, caso ocorram.
     */
    public void registerMusic(){
        RegisterMusicPanel registerMusicPanel = (RegisterMusicPanel) view;
        String name = registerMusicPanel.getTxt_nome_musicacad().getText();
        String genre = registerMusicPanel.getTxt_genero_musicacad().getText();
        String musicFilePath = registerMusicPanel.getMusicFilePath();
        int userId = appContext.getPersonContext().getIdUsuario(); // id do usuario = id do artista

        Response<Void> response = musicService.registerMusic(musicFilePath, name, userId, genre);
        if(handleDefaultResponseIfError(response)) return;

        JOptionPane.showMessageDialog(view, "Música cadastrada com sucesso!");

        logDebug("Música cadastrada com sucesso!");
    }

}
