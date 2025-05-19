package net.spotifei.Models;

public class Genre {
    private String name;
    private int idGenre;

    public Genre(String name) {
        this.name = name;
    }

    public Genre(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdGenre() {
        return idGenre;
    }

    public void setIdGenre(int idGenre) {
        this.idGenre = idGenre;
    }
}
