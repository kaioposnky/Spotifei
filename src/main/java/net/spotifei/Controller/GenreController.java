package net.spotifei.Controller;

// Importes Otimizados
import net.spotifei.Models.Responses.Response;
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

    /**
     * Construtor da classe.
     * Inicializa o controlador de gêneros musicais com as dependências necessárias.
     *
     * @param view O painel da interface gráfica atual onde o controlador está operando (ADMCadGenre).
     * @param mainFrame A janela principal da aplicação (embora não diretamente usada no método presente).
     * @param musicService O serviço responsável pela lógica de negócios relacionada a músicas e gêneros.
     */
    public GenreController(JPanel view, MainFrame mainFrame, MusicService musicService) {
        this.musicService = musicService;
        this.mainFrame = mainFrame;
        this.view = view;
    }

    /**
     * Cria um novo gênero musical no sistema.
     * Exibe uma mensagem de sucesso ao usuário caso o gênero seja criado,
     * ou de erro caso a operação falhe.
     */
    public void createGenre(){
        ADMCadGenre cadGenreView = (ADMCadGenre) view;
        String genreName = cadGenreView.getGenreTextField().getText();

        String genreNameLower = genreName.toLowerCase();

        String genreNameCap = capitalize(genreNameLower);

        Response<Void> response = musicService.createGenre(genreNameCap);
        if(handleDefaultResponseIfError(response)) return;

        JOptionPane.showMessageDialog(view, "Gênero " + genreNameCap + " criado com sucesso!");
        logDebug("Gênero " + genreNameCap + " criado com sucesso!");
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

}
