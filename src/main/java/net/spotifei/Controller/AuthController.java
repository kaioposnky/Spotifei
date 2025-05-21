package net.spotifei.Controller;

// Importes Otimizados
import net.spotifei.Models.Responses.Response;
import net.spotifei.Models.User;
import net.spotifei.Services.AuthService;
import net.spotifei.Views.MainFrame;
import net.spotifei.Views.Panels.LoginPanel;
import net.spotifei.Views.Panels.RegisterPanel;

import javax.swing.*;

import static net.spotifei.Helpers.ResponseHelper.handleDefaultResponseIfError;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;

public class AuthController {
    private final MainFrame mainFrame;
    private final AuthService authService;
    private final JPanel view;
    private final UserController userController;

    /**
     * Construtor da classe.
     * Inicializa o controlador de autenticação com as dependências necessárias.
     *
     * @param view O painel da interface gráfica atual.
     * @param mainFrame A janela principal da aplicação para navegação entre painéis.
     * @param authService O serviço responsável pela lógica de autenticação (login e registro).
     * @param userController O controlador de usuário para lidar com o sucesso do login.
     */
    public AuthController(JPanel view, MainFrame mainFrame, AuthService authService, UserController userController) {
        this.mainFrame = mainFrame;
        this.view = view;
        this.authService = authService;
        this.userController = userController;
    }

    /**
     * Realiza o processo de login de um usuário.
     * Obtém o email e a senha digitados no painel de login.
     * Se o login for bem-sucedido, registra um log e chama o userController para lidar com o sucesso do login.
     * Caso contrário, exibe uma mensagem de erro ao usuário.
     *
     * @param loginFrame O painel de login de onde os dados são obtidos.
     */
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

    /**
     * Cria um novo usuário no sistema.
     * Valida se todos os campos obrigatórios foram preenchidos.
     * Em caso de sucesso, registra um log, redireciona para o painel de login e exibe uma mensagem de sucesso.
     * Em caso de erro, exibe uma mensagem de erro ao usuário.
     */
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
        JOptionPane.showMessageDialog(view, "Cadastro realizado com sucesso!");

    }
}
