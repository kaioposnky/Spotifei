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

        cards.add(new Login_Panel(this), LOGIN_PANEL);;
        cards.add(new Search_Panel(this), SEARCH_PANEL);;
        cards.add(new Home_Panel(this), HOME_PANEL);;
        cards.add(new Register_Panel(this), REGISTER_PANEL);

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

