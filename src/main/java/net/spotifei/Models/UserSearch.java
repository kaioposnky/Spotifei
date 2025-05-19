package net.spotifei.Models;

import java.util.Date;

public class UserSearch {
    private String busca;
    private int idUsuario;
    private Date data;

    public UserSearch(String busca, int idUsuario, Date data) {
        this.busca = busca;
        this.idUsuario = idUsuario;
        this.data = data;
    }

    public UserSearch(){
    }

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
