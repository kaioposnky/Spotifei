package net.spotifei.Models;

import net.spotifei.Controller.AuthController;
import net.spotifei.Views.Panels.LoginPanel;
//import net.spotifei.Views.Panels.LoginPanel;


public class User extends Person implements Auth {

    public User(String nome, String sobrenome, String email, String telefone, String senha, int idUsuario, Music musicTocando) {
        super(nome, sobrenome, email, telefone, senha, idUsuario);
    }

    public User(){
        super();
    }

    @Override
    public void login(AuthController authController, LoginPanel panel) {
        authController.loginUsuario(panel);
    }

}
