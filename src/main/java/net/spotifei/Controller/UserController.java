package net.spotifei.Controller;

import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.Music;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Models.User;
import net.spotifei.Services.MusicService;
import net.spotifei.Services.UserService;
import net.spotifei.Views.MainFrame;

import javax.swing.*;

import static net.spotifei.Helpers.ResponseHelper.handleDefaultResponseIfError;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.*;

public class UserController {

    private final MainFrame mainFrame;
    private final UserService userService;
    private final MusicService musicService;
    private final AppContext appContext;
    private final JPanel view;

    public UserController(JPanel view, MainFrame mainFrame, AppContext appContext){
        this.mainFrame = mainFrame;
        this.userService = appContext.getUserService();
        this.view = view;
        this.musicService = appContext.getMusicService();
        this.appContext = appContext;
    }

    public void handleLoginSucess(String email){
        Response<User> responseUser = userService.getUsuarioByEmail(email);
        if(handleDefaultResponseIfError(responseUser)) return;

        User user = responseUser.getData();

        Response<Music> responseMusic = musicService.getUserLastPlayedMusic(user.getIdUsuario());
        if(handleDefaultResponseIfError(responseMusic)) return;

        Music music = responseMusic.getData();

        appContext.setPersonContext(user); // seta o usuario atual
        appContext.setMusicContext(music); // seta a musica atual

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
            logDebug("Usuário logado com sucesso!");
            mainFrame.setHUDVisible(true);
            mainFrame.setPanel(MainFrame.SEARCH_PANEL);

            musicService.pauseMusic();
            Response<Void> response = musicService.playMusic(music.getIdMusica());
            if (!response.isSuccess()){
                JOptionPane.showMessageDialog(view, "Eita, parece que você não escutou nenhuma música ainda! " +
                        "Selecione uma para começar!");
            }
        }
    }
}
