package net.spotifei.Models;

import java.util.ArrayList;

public class Playlist {
    private String nome;
    private Artist autor;
    private long salvamentos;
    private boolean isPublic;
    private int idPlaylist;
    private ArrayList<Music> musicas;
    private long qntMusicas;

    /**
     * Construtor para criar uma instância de `Playlist` com detalhes essenciais.
     *
     * @param nome O nome da playlist.
     * @param autor O artista que criou a playlist.
     * @param musicas Uma `ArrayList` de objetos `Music` que pertencem à playlist.
     * @param salvamentos O número inicial de salvamentos da playlist.
     */
    public Playlist(String nome, Artist autor, ArrayList<Music> musicas, long salvamentos) {
        this.nome = nome;
        this.autor = autor;
        this.musicas = musicas;
        this.salvamentos = salvamentos;
    }

    /**
     * Construtor padrão/vazio para a classe `Playlist`.
     */
    public Playlist(){}

    /**
     * Getters e Setters da classe `Playlist`.
     */
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
        return musicas;
    }

    public void setMusicas(ArrayList<Music> music) {
        this.musicas = music;
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

    public int getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(int idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

}
