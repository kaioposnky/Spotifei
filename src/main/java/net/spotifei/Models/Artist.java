package net.spotifei.Models;

public class Artist extends Person {

    private String nomeArtistico;
    private int idArtista;

    /**
     * Construtor completo para criar uma instância de `Artist`.
     * Chama o construtor da superclasse `Person` para inicializar os atributos básicos do usuário.
     *
     * @param nome O nome real do artista.
     * @param sobrenome O sobrenome real do artista.
     * @param email O endereço de e-mail do artista.
     * @param telefone O número de telefone do artista.
     * @param senha A senha associada à conta do artista.
     * @param idUsuario O ID único do usuário (Person) associado a este artista.
     */
    public Artist(String nome, String sobrenome, String email, String telefone, String senha, int idUsuario) {
        super(nome, sobrenome, email, telefone, senha, idUsuario);
    }

    /**
     * Chama o construtor padrão da superclasse `Person`.
     */
    public Artist(){
        super();
    }

    /**
     * Getters and setters do artista.
     */
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
