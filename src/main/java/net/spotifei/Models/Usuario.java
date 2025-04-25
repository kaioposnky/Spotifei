package net.spotifei.Models;

public class Usuario extends Pessoa {
    private Musica musicaTocando;

    public Usuario(String nome, String sobrenome, String email, String telefone, String senha, int idUsuario, Musica musicaTocando) {
        super(nome, sobrenome, email, telefone, senha, idUsuario);
        this.musicaTocando = musicaTocando;
    }

    public Musica getMusicaTocando() {
        return musicaTocando;
    }

    public void setMusicaTocando(Musica musicaTocando) {
        this.musicaTocando = musicaTocando;
    }
}
