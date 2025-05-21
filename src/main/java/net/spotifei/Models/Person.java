package net.spotifei.Models;

public class Person {
    private String nome;
    private String sobrenome;
    private String email;
    private String telefone;
    private String senha;
    private int idUsuario;


    /**
     * Construtor completo para criar uma instância de `Person` com todos os seus dados.
     *
     * @param nome O nome da pessoa.
     * @param sobrenome O sobrenome da pessoa.
     * @param email O endereço de e-mail da pessoa.
     * @param telefone O número de telefone da pessoa.
     * @param senha A senha da pessoa.
     * @param idUsuario O ID único do usuário.
     */
    public Person(String nome, String sobrenome, String email, String telefone, String senha, int idUsuario) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
        this.idUsuario = idUsuario;
    }

    /**
     * Construtor padrão/vazio para a classe `Person`.
     */
    public Person(){

    }

    /**
     * Getters e Setters da classe `Person`.
     */
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Sobrescreve o método `toString()` da Pessoa.
     *
     * @return Uma `String` que representa o estado do objeto `Person`.
     */
    @Override
    public String toString() {
        return "Person{" +
                "nome='" + nome + '\'' +
                ", sobrenome='" + sobrenome + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", senha='" + senha + '\'' +
                ", idUsuario=" + idUsuario +
                '}';
    }
}
