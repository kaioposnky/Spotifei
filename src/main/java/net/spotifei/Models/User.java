package net.spotifei.Models;

public class User extends Person {

    public User(String nome, String sobrenome, String email, String telefone, String senha, int idUsuario, Music musicTocando) {
        super(nome, sobrenome, email, telefone, senha, idUsuario);
    }

    public User(){
        super();
    }
}
