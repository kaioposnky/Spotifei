package net.spotifei.Models;

import net.spotifei.Controller.AuthController;
import net.spotifei.Views.Panels.LoginPanel;


public class Administrator extends Person implements Auth {
    public Administrator(String nome, String sobrenome, String email, String telefone, String senha, int idUsuario) {
        super(nome, sobrenome, email, telefone, senha, idUsuario);
    }

    public Administrator(){
        super();
    }

    @Override
    public void login(AuthController authController, LoginPanel panel) {
        authController.loginUsuario(panel);
    }
}
