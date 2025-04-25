package net.spotifei.Models;

import java.util.ArrayList;

public class Playlist {
    private String nome;
    private Pessoa autor;
    private ArrayList<Musica> musicas;
    private long salvamentos;

    public Playlist(String nome, Pessoa autor, ArrayList<Musica> musicas, long salvamentos) {
        this.nome = nome;
        this.autor = autor;
        this.musicas = musicas;
        this.salvamentos = salvamentos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Pessoa getAutor() {
        return autor;
    }

    public void setAutor(Pessoa autor) {
        this.autor = autor;
    }

    public ArrayList<Musica> getMusicas() {
        return musicas;
    }

    public void setMusicas(ArrayList<Musica> musicas) {
        this.musicas = musicas;
    }

    public long getSalvamentos() {
        return salvamentos;
    }

    public void setSalvamentos(long salvamentos) {
        this.salvamentos = salvamentos;
    }
}
