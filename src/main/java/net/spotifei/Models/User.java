package net.spotifei.Models;

import java.awt.event.ActionEvent;

public class User extends Person implements Auth {

    public User(String nome, String sobrenome, String email, String telefone, String senha, int idUsuario, Music musicTocando) {
        super(nome, sobrenome, email, telefone, senha, idUsuario);
    }

    public User(){
        super();
    }

    @Override
    public void login(ActionEvent evt) {

    }
}
