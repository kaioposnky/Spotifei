package net.spotifei.Controller;

import net.spotifei.Models.Responses.ErrorResponse;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Services.AuthServices;
import net.spotifei.Views.JanelaLogin;

import javax.swing.*;

public class AuthController  {
    private final JFrame view;
    private final AuthServices authServices;

    public AuthController(JFrame view) {
        this.view = view;
        this.authServices = new AuthServices();
    }

    public void loginUsuario() {
        JanelaLogin loginFrame = (JanelaLogin) view;
        String email = loginFrame.getTxt_email_login().getText();
        String password = loginFrame.getTxt_senha_login().getText();
        Response<Boolean> response = authServices.validateUserLogin(email, password);
        System.out.println(response.isSuccess());
        System.out.println();
        if (!response.isSuccess()){
//             mais tarde colocar um logger com error aqui
            JOptionPane.showMessageDialog(view, response.getMessage());
            if (response.getClass() == ErrorResponse.class){
                response.getException().printStackTrace();
            }
            return;
        }
        boolean isLoginValid = response.getData();
        if (isLoginValid){
            System.out.println("Login válido!"); // lógica de mandar para o painel home
        } else {
            System.out.println("Login inválido!");
         }
    }
}
