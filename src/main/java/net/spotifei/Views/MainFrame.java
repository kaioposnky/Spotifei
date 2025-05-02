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

//        cards.add(new JanelaLogin(), LOGIN_PANEL);
//        cards.add(new JanelaPesquisa(), SEARCH_PANEL);
//        cards.add(new JanelaHome(), HOME_PANEL);
        cards.add(new JanelaCadastro(), REGISTER_PANEL);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(cards);
        pack();
        setPanel(REGISTER_PANEL);
    }

    public void setPanel(String panel){
        cardLayout.show(cards, panel);
    }
}

