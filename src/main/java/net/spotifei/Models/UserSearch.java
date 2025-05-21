package net.spotifei.Models;

import java.util.Date;

public class UserSearch {
    private String busca;
    private int idUsuario;
    private Date data;

    /**
     * Construtor para criar uma instância de `UserSearch` com todos os seus dados.
     *
     * @param busca O termo de pesquisa.
     * @param idUsuario O ID do usuário que fez a busca.
     * @param data A data e hora da busca.
     */
    public UserSearch(String busca, int idUsuario, Date data) {
        this.busca = busca;
        this.idUsuario = idUsuario;
        this.data = data;
    }

    /**
     * Construtor padrão/vazio para a classe `UserSearch`.
     */
    public UserSearch(){
    }

    /**
     * Getters e Setters da classe `UserSearch`.
     */
    public String getBusca() {
        return busca;
    }

    public void setBusca(String busca) {
        this.busca = busca;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
