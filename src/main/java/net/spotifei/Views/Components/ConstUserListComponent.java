
package net.spotifei.Views.Components;

//imports
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.User;
import net.spotifei.Views.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ConstUserListComponent extends JPanel {
    private List<User> users = new ArrayList<>();
    private JPanel ConstUserInfoPanel;
    private final AppContext appContext;
    private final MainFrame mainframe;


    /**
     * Construtor padrão do componente `ConstUserListComponent`.
     * Inicializa o componente sem uma lista inicial de usuários.
     *
     * @param appContext O contexto da aplicação.
     * @param mainframe A instância do `MainFrame`.
     */
    public ConstUserListComponent(AppContext appContext, MainFrame mainframe){
        this.appContext = appContext;
        this.mainframe = mainframe;
        initComponents();
    }

    /**
     * Construtor do componente `ConstUserListComponent` que aceita uma lista inicial de usuários.
     *
     * @param users A lista inicial de objetos `User` a serem exibidos.
     * @param appContext O contexto da aplicação.
     * @param mainframe A instância do `MainFrame`.
     */
    public ConstUserListComponent(List<User> users, AppContext appContext, MainFrame mainframe){
        this.appContext = appContext;
        this.mainframe = mainframe;
        initComponents();
        setUser(users);
    }

    /**
     * Método privado para inicializar e configurar os componentes visuais do painel.
     */
    private void initComponents(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        ConstUserInfoPanel = new JPanel();
        ConstUserInfoPanel.setLayout(new BoxLayout(ConstUserInfoPanel, BoxLayout.Y_AXIS));
        ConstUserInfoPanel.setBackground(Color.decode("#121212"));

        JScrollPane musicsScrollPanel = new JScrollPane(ConstUserInfoPanel);
        musicsScrollPanel.getViewport().setBackground(Color.decode("#121212"));

        JScrollBar verticalScrollBar = musicsScrollPanel.getVerticalScrollBar();
        verticalScrollBar.setUI(new SpotifyLikeScrollBarUI());
        verticalScrollBar.setUnitIncrement(20);

        add(musicsScrollPanel);

        renderUser();
    }

    /**
     * Renderiza a lista de usuários no painel.
     */
    public void renderUser(){
        ConstUserInfoPanel.removeAll();

        if(users == null || users.isEmpty()){
            JLabel label = new JLabel("Nenhuma usuário encontrado!");
            label.setFont(new Font("Arial", Font.BOLD, 14));
            label.setForeground(Color.decode("#aeaeae"));
            ConstUserInfoPanel.add(label);
        } else{
            for (User users : users) {
                ConstUserInfoPanel.add(new ConstUserInfoComponent(users, appContext, mainframe));
            }
        }
    }

    /**
     * Retorna a lista atual de usuários sendo exibida pelo componente.
     *
     * @return A `List<User>` atual.
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Define uma nova lista de usuários para o componente.
     *
     * @param users A nova `List<User>` a ser exibida.
     */
    public void setUser(List<User> users) {
        if (users == null) return; // return para evitar nullpointerexception la na frente
        this.users = users;
        renderUser();
    }

}

