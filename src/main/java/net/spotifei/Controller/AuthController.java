package net.spotifei.Controller;

import net.spotifei.Models.Responses.ErrorResponse;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Models.User;
import net.spotifei.Services.AuthService;
import net.spotifei.Services.UserService;
import net.spotifei.Views.Panels.LoginPanel;
import net.spotifei.Views.MainFrame;
import net.spotifei.Views.Panels.RegisterPanel;

import javax.swing.*;

import static net.spotifei.Infrastructure.Logger.LoggerRepository.*;

public class AuthController {
    private final MainFrame mainFrame;
    private final AuthService authService;
    private final JPanel view;
    private final UserController userController;

    public AuthController(JPanel view, MainFrame mainFrame, AuthService authService, UserController userController) {
        this.mainFrame = mainFrame;
        this.view = view;
        this.authService = authService;
        this.userController = userController;
    }

    public void loginUsuario() {
        LoginPanel loginFrame = (LoginPanel) view;
        String email = loginFrame.getTxt_email_login().getText();
        String password = loginFrame.getTxt_senha_login().getText();
        Response<Boolean> response = authService.validateUserLogin(email, password);
        if (!response.isSuccess()){
            JOptionPane.showMessageDialog(view, response.getMessage());
            if (response.getClass() == ErrorResponse.class){
                logError("Erro ao validar login!", response.getException());
            }
            return;
        }
        boolean isLoginValid = response.getData();
        if (isLoginValid){
            logInfo("Usuário com email " + email + " logou com sucesso!");
            userController.handleLoginSucess(email);
        } else {
            JOptionPane.showMessageDialog(view, "Senha incorreta!");
         }
    }

    public void createUser(){
        RegisterPanel registerPanel = (RegisterPanel) view;
        User user = new User();
        user.setEmail(registerPanel.getTxt_email_cadastro().getText());
        user.setNome(registerPanel.getTxt_nome_cadastro().getText());
        user.setSobrenome(registerPanel.getTxt_sob_cadastro().getText());
        user.setTelefone(registerPanel.getTxt_telefone_cadastro().getText());
        user.setSenha(registerPanel.getTxt_senha_cadastro().getText());
        user.setIdUsuario(1);

        Response<Void> response = authService.createUser(user);
        if (!response.isSuccess()){
            logError("Erro ao criar usuário!", response.getException());
            return;
        }
        logDebug("Usuário criado com sucesso!");
        registerPanel.getMainframe().setPanel(MainFrame.LOGIN_PANEL);
    }

}
