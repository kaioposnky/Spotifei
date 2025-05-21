package net.spotifei.Views.Components;

import net.spotifei.Models.Playlist;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Views.MainFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class PlaylistListComponent extends JPanel{
    private static final Logger log = LoggerFactory.getLogger(PlaylistListComponent.class);
    private List<Playlist> playlists = new ArrayList<>();
    private JPanel playlistInfoComponent;
    private final AppContext appContext;
    private final MainFrame mainframe;

    public PlaylistListComponent(AppContext appContext, MainFrame mainframe){
        this.appContext = appContext;
        this.mainframe = mainframe;
        initComponents();
    }

    public PlaylistListComponent(List<Playlist> playlists, AppContext appContext, MainFrame mainframe){
        this.appContext = appContext;
        this.mainframe = mainframe;
        initComponents();
        setPlaylists(playlists);
    }

    private void initComponents(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        playlistInfoComponent = new JPanel();
        playlistInfoComponent.setLayout(new BoxLayout(playlistInfoComponent, BoxLayout.Y_AXIS));
        playlistInfoComponent.setBackground(Color.decode("#121212"));

        JScrollPane playlistsScrollPanel = new JScrollPane(playlistInfoComponent);
        playlistsScrollPanel.getViewport().setBackground(Color.decode("#121212"));

        JScrollBar verticalScrollBar = playlistsScrollPanel.getVerticalScrollBar();
        verticalScrollBar.setUI(new SpotifyLikeScrollBarUI());
        verticalScrollBar.setUnitIncrement(20);

        add(playlistsScrollPanel);

        renderPlaylists();
    }

    public void renderPlaylists(){
        playlistInfoComponent.removeAll();

        if(playlists == null || playlists.isEmpty()){
            JLabel label = new JLabel("Nenhuma playlist encontrada!");
            label.setFont(new Font("Arial", Font.BOLD, 14));
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setForeground(Color.decode("#aeaeae"));
            playlistInfoComponent.add(label);
        } else{
            for(Playlist playlist : playlists){
                playlistInfoComponent.add(new PlaylistInfoComponent(playlist, appContext, mainframe));
            }
        }
        updateUI();
    }

    public List<Playlist> getPlaylists(){
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists){
        if (playlists == null) return;
        this.playlists = playlists;
        renderPlaylists();
    }

    public void addPlaylist(Playlist playlist){
        if(playlist == null) return;
        this.playlists.add(playlist);
        renderPlaylists();
    }

    public void removePlaylist(Playlist playlist){
        if(playlist == null) return;
        this.playlists.remove(playlist);
        renderPlaylists();
    }
}
