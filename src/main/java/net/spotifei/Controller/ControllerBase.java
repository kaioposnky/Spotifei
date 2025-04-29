package net.spotifei.Controller;

import net.spotifei.Models.User;

import javax.swing.*;

public abstract class ControllerBase {
    private User userLogado;
    private JFrame view;

    public User getUsuarioLogado() {
        return userLogado;
    }

    public void setUsuarioLogado(User userLogado) {
        this.userLogado = userLogado;
    }

    public JFrame getView() {
        return view;
    }

    public void setView(JFrame view) {
        this.view = view;
    }
}
