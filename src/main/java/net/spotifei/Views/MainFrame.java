package net.spotifei.Views;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{

    public static final String LOGIN_PANEL = "Login";
    public static final String REGISTER_PANEL = "Register";
    public static final String HOME_PANEL = "Home";
    public static final String SEARCH_PANEL = "Search";
    private CardLayout cardLayout;
    private JPanel cards;

    public MainFrame(){
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        cards.add(new LoginPanel(this), LOGIN_PANEL);;
        cards.add(new SearchPanel(this), SEARCH_PANEL);;
        cards.add(new HomePanel(this), HOME_PANEL);;
        cards.add(new RegisterPanel(this), REGISTER_PANEL);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(cards);
        pack();
        setPanel(LOGIN_PANEL);
    }

    public void setPanel(String panel){
        cardLayout.show(cards, panel);
    }
}

