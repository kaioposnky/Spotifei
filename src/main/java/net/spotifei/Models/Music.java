package net.spotifei.Models;

import javax.sound.sampled.AudioInputStream;

public class Music {
    private String nome;
    private Artist autor;
    private Genre genre;
    private int duracaoMs;
    private int idMusica;
    private long likes;
    private long dislikes;

    public Music(String nome, Artist autor, Genre genre, int duracaoMs, int idMusica) {
        this.nome = nome;
        this.autor = autor;
        this.genre = genre;
        this.duracaoMs = duracaoMs;
        this.idMusica = idMusica;
    }

    public Music(){

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

    public int getDuracaoMs() {
        return duracaoMs;
    }

    public void setDuracaoMs(int duracaoMs) {
        this.duracaoMs = duracaoMs;
    }

    public int getIdMusica() {
        return idMusica;
    }

    public void setIdMusica(int idMusica) {
        this.idMusica = idMusica;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getDislikes() {
        return dislikes;
    }

    public void setDislikes(long dislikes) {
        this.dislikes = dislikes;
    }
}
