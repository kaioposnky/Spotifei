package net.spotifei.Models;

import net.spotifei.Controller.AuthController;
import net.spotifei.Views.Panels.LoginPanel;

// Interface de autenticação no login
public interface Auth {
    void login(AuthController authController, LoginPanel panel);
}
