package net.spotifei.Controller;

import net.spotifei.Models.Responses.Response;
import net.spotifei.Models.User;
import net.spotifei.Services.UserService;
import net.spotifei.Views.MainFrame;
import net.spotifei.Views.RegisterPanel;

import javax.swing.*;

import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logError;

public class UserController {

    private final UserService _userServices;
    private final JPanel view;

    public UserController(JPanel view){
        this.view = view;
        _userServices = new UserService();
    }

    public void getUserInfo(){
        String email = "kposansky@gmail.com";
        User user = new User();
        user.setEmail(email);
        Response<User> response = _userServices.getUsuarioByEmail(user);
        System.out.println(response.getMessage());
        System.out.println(response.getData().getNome());
        System.out.println(response.getData().getEmail());
        System.out.println(response.getData().getTelefone());
        System.out.println(response.getData().getSenha());
    }

    public void createUser(){
        RegisterPanel registerPanel = (RegisterPanel) view;
        User user = new User();
        user.setEmail(registerPanel.getTxt_email_cadastro().getText());
        user.setNome(registerPanel.getTxt_nome_cadastro().getText());
        user.setSobrenome(registerPanel.getTxt_sob_cadastro().getText());
        user.setTelefone(registerPanel.getTxt_telefone_cadastro().getText());
        user.setSenha(registerPanel.getTxt_senha_cadastro().getText());
        user.setIdUsuario(1);

        Response<Void> response = _userServices.createUser(user);
        if (!response.isSuccess()){
            logError("Erro ao criar usuário!", response.getException());
            return;
        }
        logDebug("Usuário criado com sucesso!");
        registerPanel.getMainframe().setPanel(MainFrame.LOGIN_PANEL);
    }
}
