package net.spotifei.Controller;

// Importes Otimizados
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.Artist;
import net.spotifei.Models.Music;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Models.User;
import net.spotifei.Services.MusicService;
import net.spotifei.Services.UserService;
import net.spotifei.Views.MainFrame;

import javax.swing.*;

import static net.spotifei.Helpers.ResponseHelper.handleDefaultResponseIfError;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;

public class UserController {

    private final MainFrame mainFrame;
    private final UserService userService;
    private final MusicService musicService;
    private final AppContext appContext;
    private final JPanel view;

    /**
     * Construtor da classe.
     * Inicializa o controlador de usuário com as dependências necessárias para gerenciar
     * o fluxo de login e pós-login
     *
     * @param view O painel da interface gráfica atual.
     * @param mainFrame A janela principal da aplicação, usada para navegação entre painéis.
     * @param appContext O contexto da aplicação, que armazena informações do usuário logado e da música atual.
     */
    public UserController(JPanel view, MainFrame mainFrame, AppContext appContext){
        this.mainFrame = mainFrame;
        this.userService = appContext.getUserService();
        this.view = view;
        this.musicService = appContext.getMusicService();
        this.appContext = appContext;
    }

    /**
     * Lida com o fluxo após um login bem-sucedido.
     * Baseado no email fornecido, recupera os detalhes do usuário, e verifica seu tipo
     * direcionando a interface para o painel apropriado.
     *
     * @param email O email do usuário que efetuou o login com sucesso.
     */
    public void handleLoginSucess(String email){
        Response<User> responseUser = userService.getUsuarioByEmail(email);
        if(handleDefaultResponseIfError(responseUser)) return;

        User user = responseUser.getData();

        appContext.setPersonContext(user); // seta o usuario atual

        Response<Boolean> responseUserAdmin = userService.checkUserAdmin(user);
        if(handleDefaultResponseIfError(responseUserAdmin)) return;

        boolean isAdmin = responseUserAdmin.getData();
        if (isAdmin){
            logDebug("Administrador logado com sucesso!");

            Response<Void> responseUpdateAdminLogin = userService.updateAdminLastLoginByEmail(user.getEmail());
            if(handleDefaultResponseIfError(responseUpdateAdminLogin)) return;

            logDebug("Último login do administrador atualizado com sucesso!");
            mainFrame.setPanel(MainFrame.ADMHOME_PANEL);
        } else{
            Response<Boolean> responseUserArtist = userService.checkUserArtist(user);
            if(handleDefaultResponseIfError(responseUserArtist)) return;

            boolean isArtist = responseUserArtist.getData();
            if(isArtist){
                Response<Artist> responseGetArtist = userService.getArtistById(user.getIdUsuario());
                if(handleDefaultResponseIfError(responseGetArtist)) return;

                Artist artist = responseGetArtist.getData();
                artist.setEmail(user.getEmail());
                artist.setNome(user.getNome());
                artist.setSobrenome(user.getSobrenome());
                artist.setSenha(user.getSenha());
                artist.setIdUsuario(user.getIdUsuario());
                artist.setTelefone(user.getTelefone());
                appContext.setPersonContext(artist); // agora o personcontext tem a classe de artista carregada
            }

            logDebug("Usuário logado com sucesso!");

            Response<Music> responseMusic = musicService.getUserLastPlayedMusic(user.getIdUsuario());
            if(handleDefaultResponseIfError(responseMusic)) return;

            Music music = responseMusic.getData();

            appContext.setMusicContext(music); // seta a musica atual

            mainFrame.setHUDVisible(true);
            mainFrame.setPanel(MainFrame.SEARCH_PANEL);

            handlePlayMusicBackground(music);

            Response<Void> responsePause = musicService.pauseMusic();
            if(handleDefaultResponseIfError(responsePause)) return;
            logDebug("Música pausada com sucesso! :" + appContext.getAudioPlayerWorker().isPlaying());
        }
    }

    /**
     * Tenta iniciar a reprodução de uma música em segundo plano.
     * Em caso de erro (ex: nenhuma música tocada anteriormente), exibe uma mensagem ao usuário.
     * A música é pausada logo após ser preparada para que o usuário possa controlá-la.
     *
     * @param music A música a ser potencialmente reproduzida (geralmente a última tocada).
     */
    private void handlePlayMusicBackground(Music music){
        SwingWorker<Void, Void> backgroundWorker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                Response<Void> responsePlay = musicService.playMusic(music.getIdMusica(),
                        appContext.getPersonContext().getIdUsuario());
                if (handleDefaultResponseIfError(responsePlay)) {
                    JOptionPane.showMessageDialog(view, "Eita, parece que você não escutou nenhuma música ainda! " +
                            "Selecione uma para começar!");
                }
                Response<Void> responsePause = musicService.pauseMusic();
                handleDefaultResponseIfError(responsePause);
                return null;
            }
        };
        backgroundWorker.execute();
    }
}
