package net.spotifei.Controller;

import net.spotifei.Models.Usuario;

import javax.swing.*;

public abstract class ControllerBase {
    private Usuario usuarioLogado;
    private JFrame view;

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public JFrame getView() {
        return view;
    }

    public void setView(JFrame view) {
        this.view = view;
    }
}
