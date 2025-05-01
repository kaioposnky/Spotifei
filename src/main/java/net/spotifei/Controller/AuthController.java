package net.spotifei.Controller;

import net.spotifei.Models.Responses.Response;
import net.spotifei.Services.AuthServices;
import net.spotifei.Views.JanelaLogin;
import net.spotifei.Views.MainFrame;

import javax.swing.*;

public class AuthController  {
    private final JPanel view;
    private final AuthServices authServices;

    public AuthController(JPanel view) {
        this.view = view;
        this.authServices = new AuthServices();
    }

    public void loginUsuario(){
        JanelaLogin loginFrame = (JanelaLogin) view;
        String email = loginFrame.getTxt_email_login().getText();
        String password = loginFrame.getTxt_senha_login().getText();
        Response<Boolean> response = authServices.validateUserLogin(email, password);
        if (!response.isSuccess()){
            // mais tarde colocar um logger com error aqui
            JOptionPane.showMessageDialog(view, response.getMessage());
            return;
        }
        boolean isLoginValid = response.getData();
        if (isLoginValid){
            // lógica de mandar para o painel home
        }
    }
}
