package net.spotifei.Models;

import java.util.ArrayList;

public class Playlist {
    private String nome;
    private Artist autor;
    private long salvamentos;
    private boolean isPublic;
    private ArrayList<Music> music;

    public Playlist(String nome, Artist autor, ArrayList<Music> music, long salvamentos) {
        this.nome = nome;
        this.autor = autor;
        this.music = music;
        this.salvamentos = salvamentos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Person getAutor() {
        return autor;
    }

    public void setAutor(Artist autor) {
        this.autor = autor;
    }

    public ArrayList<Music> getMusicas() {
        return music;
    }

    public void setMusicas(ArrayList<Music> music) {
        this.music = music;
    }

    public long getSalvamentos() {
        return salvamentos;
    }

    public void setSalvamentos(long salvamentos) {
        this.salvamentos = salvamentos;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
