package net.spotifei.Models;

import java.util.List;

public class Music {
    private String nome;
    private List<Artist> autores;
    private Genre genre;
    private long duracaoMs;
    private int idMusica;
    private long likes;
    private long dislikes;
    private boolean gostou;

    public Music(String nome, List<Artist> autores, Genre genre, long duracaoMs, int idMusica) {
        this.nome = nome;
        this.autores = autores;
        this.genre = genre;
        this.duracaoMs = duracaoMs;
        this.idMusica = idMusica;
    }

    public Music(){

    }

    public List<Artist> getAutores() {
        return autores;
    }

    public void setAutores(List<Artist> autores) {
        this.autores = autores;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getDuracaoMs() {
        return duracaoMs;
    }

    public void setDuracaoMs(long duracaoMs) {
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

    public boolean isGostou() {
        return gostou;
    }

    public void setGostou(boolean gostou) {
        this.gostou = gostou;
    }

    public String getAuthorNames(){
        String names = "";
        for(int i = 0; i < autores.size(); i++){
            if(i != 0 || (i+1 != autores.size())){
                names += ",";
            }
            names += autores.get(i).getNomeArtistico() + " ";
        }
        return names;
    }
}
