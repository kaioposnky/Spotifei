package net.spotifei.Controller;

import net.spotifei.Models.Requests.UserRequest;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Models.User;
import net.spotifei.Services.UserService;

import javax.swing.*;

public class UserController {

    private final UserService _userServices;
    private final JFrame view;

    public UserController(JFrame view){
        this.view = view;
        _userServices = new UserService();
    }

    public void getUserInfo(){
        String email = "kposansky@gmail.com";
        UserRequest userRequest = new UserRequest();
        userRequest.email = email;
        Response<User> response = _userServices.getUsuarioByEmail(userRequest);
        System.out.println(response.getMessage());
        System.out.println(response.getData().getNome());
        System.out.println(response.getData().getEmail());
        System.out.println(response.getData().getTelefone());
        System.out.println(response.getData().getSenha());
    }
}
