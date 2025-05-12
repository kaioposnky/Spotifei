package net.spotifei.Views;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import net.spotifei.Infrastructure.AudioPlayer.AudioPlayerWorker;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Views.Panels.*;
import net.spotifei.Views.Panels.Admin.*;

import javax.swing.*;
import java.awt.*;

import static net.spotifei.Helpers.AssetsLoader.loadImageIcon;

public class MainFrame extends JFrame{

    public static final String LOGIN_PANEL = "Login";
    public static final String REGISTER_PANEL = "Register";
    public static final String SEARCH_PANEL = "Search";
    public static final String HISTORY_PANEL = "History";
    public static final String PLAYLIST_PANEL = "Playlist";
    public static final String ADMHOME_PANEL = "ADMHome";
    public static final String ADMREGMUSIC_PANEL = "ADMRegMusic";
    public static final String ADMSISTEMA_PANEL = "ADMSistema";
    public static final String ADMCONUSER_PANEL = "ADMConUser";
    public static final String ADMDELMUSIC_PANEL = "ADMDelMusic";
    public static final String ADMCADARTIST_PANEL = "ADMCadArtist";

    private CardLayout cardLayout;
    private JPanel cards;

    private final AppContext appContext;
    private final MusicPlayerPanel musicPlayerPanel;
    private final RedirectPanel redirectPanel;

    public MainFrame(AppContext appContext){
        this.appContext = appContext;
        this.musicPlayerPanel = new MusicPlayerPanel(this, this.appContext);
        this.appContext.registerMusicPlayerPanelListener(this.musicPlayerPanel);
        this.redirectPanel= new RedirectPanel(this,this.appContext);

        musicPlayerPanel.setVisible(false);
        redirectPanel.setVisible(false);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        cards.add(new LoginPanel(this, this.appContext), LOGIN_PANEL);
        cards.add(new SearchPanel(this, this.appContext), SEARCH_PANEL);
        cards.add(new RegisterPanel(this, this.appContext), REGISTER_PANEL);
        cards.add(new HistoryPanel(this, this.appContext), HISTORY_PANEL);
        cards.add(new PlaylistPanel(this, this.appContext), PLAYLIST_PANEL);
        cards.add(new ADMHomePanel(this, this.appContext), ADMHOME_PANEL);
        cards.add(new ADMRegisterMusicPanel(this, this.appContext), ADMREGMUSIC_PANEL);
        cards.add(new ADMSistemaPanel(this, this.appContext), ADMSISTEMA_PANEL);
        cards.add(new ADMConsultaUsuarioPanel(this, this.appContext), ADMCONUSER_PANEL);
        cards.add(new ADMDelMusicPanel(this, this.appContext), ADMDELMUSIC_PANEL);
        cards.add(new ADMCadArtistPanel(this, this.appContext), ADMCADARTIST_PANEL);

        add(musicPlayerPanel, BorderLayout.SOUTH);
        add(redirectPanel,BorderLayout.EAST);
        add(cards);

        setIconImage(loadImageIcon("spotifeiIcon.png").getImage());
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.DARK_GRAY);
        pack(); // tem que estar antes do setLocationRelativeTo
        setLocationRelativeTo(null);
        setPanel(LOGIN_PANEL);
    }

    public void setPanel(String panel){
        cardLayout.show(cards, panel);
    }

    public void setHUDVisible(boolean visible){
        musicPlayerPanel.setVisible(visible);
        redirectPanel.setVisible(visible);
    }
   
}
