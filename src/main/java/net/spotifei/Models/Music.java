package net.spotifei.Models;

import javax.sound.sampled.AudioInputStream;

public class Music {
    private String nome;
    private Artist autor;
    private Genre genre;
    private int duracao_ms;
    private AudioInputStream audio;
    private int id_musica;

    public Music(String nome, Artist autor, Genre genre, int duracao_ms, int id_musica) {
        this.nome = nome;
        this.autor = autor;
        this.genre = genre;
        this.duracao_ms = duracao_ms;
        this.id_musica = id_musica;
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

    public int getDuracao_ms() {
        return duracao_ms;
    }

    public void setDuracao_ms(int duracao_ms) {
        this.duracao_ms = duracao_ms;
    }

    public int getId_musica() {
        return id_musica;
    }

    public void setId_musica(int id_musica) {
        this.id_musica = id_musica;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public AudioInputStream getAudio() {
        return audio;
    }

    public void setAudio(AudioInputStream audio) {
        this.audio = audio;
    }
}
