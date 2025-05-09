package net.spotifei.Views;

import net.spotifei.Infrastructure.AudioPlayer.AudioPlayerWorker;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Views.Panels.*;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{

    public static final String LOGIN_PANEL = "Login";
    public static final String REGISTER_PANEL = "Register";
    public static final String HOME_PANEL = "Home";
    public static final String SEARCH_PANEL = "Search";
    public static final String HISTORY_PANEL = "History";
    public static final String PLAYLIST_PANEL = "Playlist";

    private CardLayout cardLayout;
    private JPanel cards;
    private final AppContext appContext;
    private final AudioPlayerWorker audioPlayerWorker;
    private final MusicPlayerPanel musicPlayerPanel;

    public MainFrame(AppContext appContext, AudioPlayerWorker audioPlayerWorker){
        this.appContext = appContext;
        this.audioPlayerWorker = audioPlayerWorker;
        this.musicPlayerPanel = new MusicPlayerPanel(this);

        audioPlayerWorker.execute();
        audioPlayerWorker.setMusicPlayerPanel(musicPlayerPanel);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        cards.add(new LoginPanel(this), LOGIN_PANEL);
        cards.add(new SearchPanel(this), SEARCH_PANEL);
        cards.add(new HomePanel(this), HOME_PANEL);
        cards.add(new RegisterPanel(this), REGISTER_PANEL);
        cards.add(new HistoryPanel(this), HISTORY_PANEL);
        cards.add(new PlaylistPanel(this), PLAYLIST_PANEL);

//        musicPlayerPanel.setVisible(false);
        add(musicPlayerPanel, BorderLayout.SOUTH);
        add(cards);


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

    public AppContext getAppContext() {
        return appContext;
    }

    public AudioPlayerWorker getAudioPlayerWorker() {
        return audioPlayerWorker;
    }

    public MusicPlayerPanel getMusicPlayerPanel() {
        return musicPlayerPanel;
    }
}
