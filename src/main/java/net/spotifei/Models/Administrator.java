package net.spotifei.Models;

import net.spotifei.Controller.AuthController;
import net.spotifei.Views.Panels.LoginPanel;


public class Administrator extends Person implements Auth {

    /**
     * Construtor completo para criar uma instância de `Administrator`.
     * Chama o construtor da superclasse `Person` para inicializar os atributos básicos do usuário.
     *
     * @param nome O nome do administrador.
     * @param sobrenome O sobrenome do administrador.
     * @param email O endereço de e-mail do administrador, usado para login.
     * @param telefone O número de telefone do administrador.
     * @param senha A senha do administrador.
     * @param idUsuario O ID único do usuário administrador.
     */
    public Administrator(String nome, String sobrenome, String email, String telefone, String senha, int idUsuario) {
        super(nome, sobrenome, email, telefone, senha, idUsuario);
    }

    /**
     * Chama o construtor padrão da superclasse `Person`.
     */
    public Administrator(){
        super();
    }

    /**
     * Este método define como um administrador se autentica no sistema.
     *
     * @param authController O controlador de autenticação responsável por lidar com a lógica de login.
     * @param panel O painel de login da interface do usuário que contém as credenciais.
     */
    @Override
    public void login(AuthController authController, LoginPanel panel) {
        authController.loginUsuario(panel);
    }
}
