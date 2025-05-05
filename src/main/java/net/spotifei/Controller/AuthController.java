package net.spotifei.Controller;

import net.spotifei.Models.Responses.ErrorResponse;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Services.AuthServices;
import net.spotifei.Views.LoginPanel;
import net.spotifei.Views.MainFrame;

import javax.swing.*;

public class AuthController  {
    private final MainFrame mainframe;
    private final JPanel view;
    private final AuthServices authServices;

    public AuthController(MainFrame mainframe, JPanel view) {
        this.mainframe = mainframe;
        this.view = view;
        this.authServices = new AuthServices();
    }

    public void loginUsuario() {
        LoginPanel loginFrame = (LoginPanel) view;
        String email = loginFrame.getTxt_email_login().getText();
        String password = loginFrame.getTxt_senha_login().getText();
        Response<Boolean> response = authServices.validateUserLogin(email, password);
        System.out.println(response.isSuccess());
        System.out.println();
        if (!response.isSuccess()){
            JOptionPane.showMessageDialog(view, response.getMessage());
            if (response.getClass() == ErrorResponse.class){
//             mais tarde colocar um logger com error aqui
                response.getException().printStackTrace();
            }
            return;
        }
        boolean isLoginValid = response.getData();
        if (isLoginValid){
            System.out.println("Login válido!"); // lógica de mandar para o painel home
            mainframe.setPanel(MainFrame.HOME_PANEL);
        } else {
            System.out.println("Login inválido!");
         }
    }

}
