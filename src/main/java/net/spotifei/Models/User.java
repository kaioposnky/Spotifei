package net.spotifei.Models;

import net.spotifei.Controller.AuthController;
import net.spotifei.Views.Panels.LoginPanel;


public class User extends Person implements Auth {

    /**
     * Construtor completo para criar uma instância de `User`.
     * Chama o construtor da superclasse `Person` para inicializar os atributos básicos do usuário.
     *
     * @param nome O nome do usuário.
     * @param sobrenome O sobrenome do usuário.
     * @param email O endereço de e-mail do usuário, usado para login.
     * @param telefone O número de telefone do usuário.
     * @param senha A senha do usuário.
     * @param idUsuario O ID único do usuário.
     * @param musicTocando A música que está tocando atualmente para o usuário
     */
    public User(String nome, String sobrenome, String email, String telefone, String senha, int idUsuario, Music musicTocando) {
        super(nome, sobrenome, email, telefone, senha, idUsuario);
    }

    /**
     * Construtor padrão/vazio para a classe `User`.
     * Chama o construtor padrão da superclasse `Person`.
     */
    public User(){
        super();
    }

    /**
     * Implementa o método `login` da interface `Auth`.
     * Define como um usuário se autentica no sistema.
     *
     * @param authController O controlador de autenticação responsável por lidar com a lógica de login.
     * @param panel O painel de login da interface do usuário que contém as credenciais.
     */
    @Override
    public void login(AuthController authController, LoginPanel panel) {
        authController.loginUsuario(panel);
    }

}
