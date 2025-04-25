package net.spotifei.Models;

public class Musica {
    private String nome;
    private Artista autor;
    private Genero genero;
    private int milissegundos;
    private int id;

    public Musica(String nome, Artista autor, Genero genero, int milissegundos, int id) {
        this.nome = nome;
        this.autor = autor;
        this.genero = genero;
        this.milissegundos = milissegundos;
        this.id = id;
    }

    public Artista getAutor() {
        return autor;
    }

    public void setAutor(Artista autor) {
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

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
