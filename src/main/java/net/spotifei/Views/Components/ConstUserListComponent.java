
package net.spotifei.Views.Components;

import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Views.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import net.spotifei.Models.User;

public class ConstUserListComponent extends JPanel {
    private List<User> users = new ArrayList<>();
    private JPanel ConstUserInfoPanel;
    private final AppContext appContext;
    private final MainFrame mainframe;


    public ConstUserListComponent(AppContext appContext, MainFrame mainframe){
        this.appContext = appContext;
        this.mainframe = mainframe;
        initComponents();
    }

    public ConstUserListComponent(List<User> users, AppContext appContext, MainFrame mainframe){
        this.appContext = appContext;
        this.mainframe = mainframe;
        initComponents();
        setUser(users);
    }

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

    public List<User> getUsers() {
        return users;
    }

    public void setUser(List<User> users) {
        if (users == null) return; // return para evitar nullpointerexception la na frente
        this.users = users;
        renderUser();
    }

}

