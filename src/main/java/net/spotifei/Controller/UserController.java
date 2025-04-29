package net.spotifei.Controller;

import net.spotifei.Services.UserService;

public class UserController {

    private UserService _userServices;

    public UserController(){
        _userServices = new UserService();
    }

}
