package net.spotifei.Models;

public class Artist extends Person {

    private String nomeArtistico;
    private int idArtista;

    public Artist(String nome, String sobrenome, String email, String telefone, String senha, int idUsuario) {
        super(nome, sobrenome, email, telefone, senha, idUsuario);
    }

    public Artist(){
        super();
    }

    public String getNomeArtistico() {
        return nomeArtistico;
    }

    public void setNomeArtistico(String nomeArtistico) {
        this.nomeArtistico = nomeArtistico;
    }

    public int getIdArtista() {
        return idArtista;
    }

    public void setIdArtista(int idArtista) {
        this.idArtista = idArtista;
    }
}
