package net.spotifei.Models;

public class User extends Person {
    private Music musicTocando;

    public User(String nome, String sobrenome, String email, String telefone, String senha, int idUsuario, Music musicTocando) {
        super(nome, sobrenome, email, telefone, senha, idUsuario);
        this.musicTocando = musicTocando;
    }

    public User(){
        super();
    }

    public Music getMusicaTocando() {
        return musicTocando;
    }

    public void setMusicaTocando(Music musicTocando) {
        this.musicTocando = musicTocando;
    }
}
