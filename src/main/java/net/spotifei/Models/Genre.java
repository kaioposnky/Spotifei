package net.spotifei.Models;

public class Genre {
    private String name;
    private int idGenre;

    /**
     * Construtor para criar uma instância de `Genre`.
     *
     * @param name O nome do gênero musical.
     */
    public Genre(String name) {
        this.name = name;
    }

    /**
     * Construtor padrão/vazio para a classe `Genre`.
     */
    public Genre(){
    }

    /**
     * Getters e Setters da classe `Genre`.
     */
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
