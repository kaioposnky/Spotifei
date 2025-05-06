package net.spotifei.Controller;

import net.spotifei.Models.Responses.ErrorResponse;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Services.AuthService;
import net.spotifei.Views.LoginPanel;
import net.spotifei.Views.MainFrame;

import javax.swing.*;

import static net.spotifei.Infrastructure.Logger.LoggerRepository.logError;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logInfo;

public class AuthController {
    private final AuthService authService;
    private final JPanel view;

    public AuthController(JPanel view) {
        this.view = view;
        this.authService = new AuthService();
    }

    public void loginUsuario() {
        LoginPanel loginFrame = (LoginPanel) view;
        String email = loginFrame.getTxt_email_login().getText();
        String password = loginFrame.getTxt_senha_login().getText();
        Response<Boolean> response = authService.validateUserLogin(email, password);
        System.out.println(response.isSuccess());
        System.out.println();
        if (!response.isSuccess()){
            JOptionPane.showMessageDialog(view, response.getMessage());
            if (response.getClass() == ErrorResponse.class){
                logError("Erro ao validar login!", response.getException());
            }
            return;
        }
        boolean isLoginValid = response.getData();
        if (isLoginValid){
            logInfo("Usuário com email" + email + " logou com sucesso!");
            loginFrame.getMainframe().setPanel(MainFrame.HOME_PANEL);
        } else {
            JOptionPane.showMessageDialog(view, "Senha incorreta!");
         }
    }

}
