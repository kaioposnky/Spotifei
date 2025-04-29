package net.spotifei.Controller;

import net.spotifei.Models.User;
import net.spotifei.Services.UserService;

public class AuthController extends ControllerBase {

    public void loginUsuario(){
        UserService userServices = new UserService();
        User user = new User(
                view.getName(), view.getemail, view.getpassword
        );
        userServices.getUsuario();
    }
}
