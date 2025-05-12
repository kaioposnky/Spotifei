package net.spotifei.Controller;

import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Models.User;
import net.spotifei.Services.UserService;
import net.spotifei.Views.MainFrame;

import javax.swing.*;

import static net.spotifei.Infrastructure.Logger.LoggerRepository.*;

public class UserController {

    private final MainFrame mainFrame;
    private final UserService userService;
    private final JPanel view;
    private final AppContext appContext;

    public UserController(JPanel view, MainFrame mainFrame, UserService userService, AppContext appContext){
        this.mainFrame = mainFrame;
        this.userService = userService;
        this.view = view;
        this.appContext = appContext;
    }

    public void getUserInfo(){
        String email = "kposansky@gmail.com";
        User user = new User();
        user.setEmail(email);
        Response<User> response = userService.getUsuarioByEmail(user);
        System.out.println(response.getMessage());
        System.out.println(response.getData().getNome());
        System.out.println(response.getData().getEmail());
        System.out.println(response.getData().getTelefone());
        System.out.println(response.getData().getSenha());
    }

    public void handleLoginSucess(String email){
        Response<User> responseUser = userService.getUsuarioByEmail(email);
        if(!responseUser.isSuccess()){
            if(responseUser.isError()){
                logError(responseUser.getMessage(), responseUser.getException());
            } else{
                logError(responseUser.getMessage());
            }
            return;
        }
        User user = responseUser.getData();
        appContext.setPersonContext(user);

        logInfo("User " + user.getNome() + " logado com sucesso! Id do usuario: " + user.getIdUsuario() + "");

        Response<Boolean> responseUserAdmin = userService.checkUserAdmin(user);
        if(!responseUserAdmin.isSuccess()){
            if(responseUserAdmin.isError()){
                logError(responseUserAdmin.getMessage(), responseUserAdmin.getException());
            } else{
                logError(responseUserAdmin.getMessage());
            }
            return;
        }
        boolean isAdmin = responseUserAdmin.getData();
        if (isAdmin){
            logDebug("Administrador logado com sucesso!");

            Response<Void> response = userService.updateAdminLastLoginByEmail(user.getEmail());
            if(!response.isSuccess()){
                if(response.isError()){
                    logError(response.getMessage(), response.getException());
                } else{
                    logError(response.getMessage());
                }
                return;
            }
            logDebug("Último login do administrador atualizado com sucesso!");
            mainFrame.setPanel(MainFrame.ADMHOME_PANEL);
        } else{
            logDebug("Usuário logado com sucesso!");
            mainFrame.setPanel(MainFrame.SEARCH_PANEL);
        }
    }
}
