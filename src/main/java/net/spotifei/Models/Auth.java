package net.spotifei.Models;

import net.spotifei.Controller.AuthController;
import net.spotifei.Views.Panels.LoginPanel;

public interface Auth {
    void login(AuthController authController, LoginPanel panel);
}
