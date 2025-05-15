package net.spotifei.Controller;

import net.spotifei.Models.Responses.ErrorResponse;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Models.User;
import net.spotifei.Services.AuthService;
import net.spotifei.Services.UserService;
import net.spotifei.Views.Panels.LoginPanel;
import net.spotifei.Views.MainFrame;
import net.spotifei.Views.Panels.RegisterPanel;

import javax.swing.*;

import static net.spotifei.Helpers.ResponseHelper.handleDefaultResponseIfError;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.*;

public class AuthController {
    private final MainFrame mainFrame;
    private final AuthService authService;
    private final JPanel view;
    private final UserController userController;

    public AuthController(JPanel view, MainFrame mainFrame, AuthService authService, UserController userController) {
        this.mainFrame = mainFrame;
        this.view = view;
        this.authService = authService;
        this.userController = userController;
    }

    public void loginUsuario(LoginPanel loginFrame) {
        String email = loginFrame.getTxt_email_login().getText();
        String password = loginFrame.getTxt_senha_login().getText();
        
        Response<Boolean> response = authService.validateUserLogin(email, password);
        if(handleDefaultResponseIfError(response)) return;

        boolean isLoginValid = response.getData();
        if (isLoginValid){
            logDebug("Usuário com email " + email + " logou com sucesso!");
            userController.handleLoginSucess(email);
        } else {
            logDebug("Login incorreto para usuário com email " + email + "!");
            JOptionPane.showMessageDialog(view, "Senha incorreta!");
         }
    }

    public void createUser(){
        RegisterPanel registerPanel = (RegisterPanel) view;
        String email = registerPanel.getTxt_email_cadastro().getText();
        String nome = registerPanel.getTxt_nome_cadastro().getText();
        String sobrenome = registerPanel.getTxt_sob_cadastro().getText();
        String telefone = registerPanel.getTxt_telefone_cadastro().getText();
        String senha = registerPanel.getTxt_senha_cadastro().getText();

        if (email.isEmpty() || nome.isEmpty() || sobrenome.isEmpty() || telefone.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Todos os campos são obrigatórios!", "Erro de Cadastro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        User user = new User();
        user.setEmail(email);
        user.setNome(nome);
        user.setSobrenome(sobrenome);
        user.setTelefone(telefone);
        user.setSenha(senha);
        user.setIdUsuario(1);

        Response<Void> response = authService.createUser(user);
        if(handleDefaultResponseIfError(response)) return;

        logDebug("Usuário criado com sucesso!");
        registerPanel.getMainframe().setPanel(MainFrame.LOGIN_PANEL);

    }
    }
