package net.spotifei.Controller;

import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Models.User;
import net.spotifei.Services.UserService;
import net.spotifei.Views.MainFrame;
import net.spotifei.Views.Panels.RegisterPanel;

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
        mainFrame.setPanel(MainFrame.HOME_PANEL);
        Response<User> response = userService.getUsuarioByEmail(email);
        if(!response.isSuccess()){
            if(response.isError()){
                logError(response.getMessage(), response.getException());
            } else{
                logError(response.getMessage());
            }
            return;
        }
        User user = response.getData();
        appContext.setPersonContext(user);
    }
}
