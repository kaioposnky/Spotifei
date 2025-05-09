package net.spotifei.Models;

public class Genre {
    private String nome;

    public Genre(String nome) {
        this.nome = nome;
    }

    public Genre(){
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
