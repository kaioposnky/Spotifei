package net.spotifei.Models;

import java.util.List;

public class Music {
    private String nome;
    private List<Artist> autores;
    private Genre genre;
    private long duracaoMs;
    private int idMusica;
    private int idMusicaFila;
    private long likes;
    private long dislikes;
    private long vezesTocada;
    private Boolean gostou;
    private String artistsNames; // maneira forçada de colocar os nomes dos artistas, para não precisar encher de objetos artistas

    /**
     * Construtor completo para criar uma instância de `Music`.
     *
     * @param nome O nome da música.
     * @param autores Uma lista de objetos Artist que são os autores da música.
     * @param genre O gênero musical da música.
     * @param duracaoMs A duração da música em milissegundos.
     * @param idMusica O ID único da música.
     */
    public Music(String nome, List<Artist> autores, Genre genre, long duracaoMs, int idMusica) {
        this.nome = nome;
        this.autores = autores;
        this.genre = genre;
        this.duracaoMs = duracaoMs;
        this.idMusica = idMusica;
    }

    /**
     * Construtor padrão/vazio para a classe `Music`.
     */
    public Music(){

    }

    /**
     * Getters e Setters da classe `Music`.
     */
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

    public Boolean isGostou() {
        return gostou;
    }

    public void setGostou(Boolean gostou) {
        this.gostou = gostou;
    }

    public int getIdMusicaFila() {
        return idMusicaFila;
    }

    public void setIdMusicaFila(int idMusicaFila) {
        this.idMusicaFila = idMusicaFila;
    }

    /**
     * Retorna os nomes dos artistas da música como uma única string concatenada.
     *
     * @return Uma `String` contendo os nomes artísticos dos autores, separados por vírgula.
     */
    public String getArtistsNames(){
        if((autores == null || autores.isEmpty()) && artistsNames != null){
            return artistsNames;
        }

        String names = "";
        for(int i = 0; i < autores.size(); i++){
            if(i != 0 || (i+1 != autores.size())){
                names += ",";
            }
            names += autores.get(i).getNomeArtistico() + " ";
        }
        return names;
    }

    public void setArtistsNames(String artistsNames) {
        this.artistsNames = artistsNames;
    }

    /**
     * Sobrescreve o método `toString()` da música.
     *
     * @return Uma `String` que representa o estado do objeto `Music`.
     */
    @Override
    public String toString() {
        return "Music{" +
                "nome='" + nome + '\'' +
                ", genre=" + genre +
                ", duracaoMs=" + duracaoMs +
                ", idMusica=" + idMusica +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", gostou=" + gostou +
                ", artistsNames='" + artistsNames + '\'' +
                ", autores=" + autores +
                '}';
    }

    public long getVezesTocada() {
        return vezesTocada;
    }

    public void setVezesTocada(long vezesTocada) {
        this.vezesTocada = vezesTocada;
    }
}
