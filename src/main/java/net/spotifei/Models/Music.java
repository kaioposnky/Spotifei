package net.spotifei.Models;

public class Music {
    private String nome;
    private Artist autor;
    private Genre genre;
    private int milissegundos;
    private int id;

    public Music(String nome, Artist autor, Genre genre, int milissegundos, int id) {
        this.nome = nome;
        this.autor = autor;
        this.genre = genre;
        this.milissegundos = milissegundos;
        this.id = id;
    }

    public Artist getAutor() {
        return autor;
    }

    public void setAutor(Artist autor) {
        this.autor = autor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getMilissegundos() {
        return milissegundos;
    }

    public void setMilissegundos(int milissegundos) {
        this.milissegundos = milissegundos;
    }

    public Genre getGenero() {
        return genre;
    }

    public void setGenero(Genre genre) {
        this.genre = genre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
