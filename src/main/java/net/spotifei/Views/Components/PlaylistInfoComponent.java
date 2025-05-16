package net.spotifei.Views.Components;

import net.spotifei.Controller.PlaylistController;
import net.spotifei.Helpers.AssetsLoader;
import net.spotifei.Models.Playlist;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Services.MusicService;
import net.spotifei.Views.MainFrame;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
public class PlaylistInfoComponent extends JPanel {
    private final Playlist playlist;
    private final AppContext appContext;
    private final MainFrame mainframe;
    private final PlaylistController playlistController;

    public PlaylistInfoComponent(Playlist playlist, AppContext appContext, MainFrame mainframe){
        this.playlist = playlist;
        this.appContext = appContext;
        this.mainframe = mainframe;
        this.playlistController = appContext.getPlayListController(this);
        initComponents();
    }

    private void initComponents(){
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(Color.decode("#121212"));
        setOpaque(true);
        addHoverListeners();

        Border borderLine = new MatteBorder(0,0,1,0, new Color(50,50,50));
        Border borderInside = BorderFactory.createEmptyBorder(5,5,5,5);

        this.setBorder(BorderFactory.createCompoundBorder(borderLine, borderInside));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE,50));

        this.add(createPlaylistInfoComponent());
        this.add(Box.createHorizontalGlue());
        this.add(Box.createHorizontalStrut(20));
        this.add(createPlaylistButtonsPanel());
        this.add(Box.createHorizontalStrut(20));

        setAlignmentY(Component.CENTER_ALIGNMENT);
    }
    private JPanel createPlaylistButtonsPanel(){
        JPanel playlistButtonsPanel = new JPanel();
        playlistButtonsPanel.setLayout(new BoxLayout(playlistButtonsPanel, BoxLayout.X_AXIS));
        playlistButtonsPanel.setOpaque(false);

        playlistButtonsPanel.add(createPlaylistDeleteButtonPanel());
        playlistButtonsPanel.add(Box.createHorizontalStrut(30));
        playlistButtonsPanel.add(createPlaylistEditButtonPanel());
        playlistButtonsPanel.add(Box.createHorizontalStrut(30));
        playlistButtonsPanel.add(createPlaylistPlayButtonPanel());

        return playlistButtonsPanel;
    }

    private JPanel createPlaylistInfoComponent(){
        JPanel playlistInfoComponent = new JPanel();
        playlistInfoComponent.setLayout(new BoxLayout(playlistInfoComponent, BoxLayout.X_AXIS));
        playlistInfoComponent.setOpaque(false);

        JLabel playlistName = new JLabel(playlist.getNome());
        playlistName.setFont(new Font("Arial", Font.BOLD, 14));
        playlistName.setForeground(Color.white);

        playlistInfoComponent.add(playlistName);

        return playlistInfoComponent;
    }

    private JPanel createPlaylistPlayButtonPanel(){
        JPanel playlistActionButtonPanel = new JPanel();
        playlistActionButtonPanel.setLayout(new BoxLayout(playlistActionButtonPanel, BoxLayout.X_AXIS));
        playlistActionButtonPanel.setOpaque(false);

        JButton playButton = new JButton();
        playButton.setIcon(AssetsLoader.loadImageIcon("play_icon.png", 20, 20));
        playButton.addActionListener(event -> {
            playlistController.playPlaylistMusics();
        });
        playButton.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
        playButton.setFocusPainted(false);
        playButton.setContentAreaFilled(false);
        playButton.setOpaque(false);
        playButton.setAlignmentY(Component.CENTER_ALIGNMENT);

        playlistActionButtonPanel.add(playButton);

        return playlistActionButtonPanel;
    }

    private JPanel createPlaylistDeleteButtonPanel(){
        JPanel playlistDeleteButtonPanel = new JPanel();
        playlistDeleteButtonPanel.setLayout(new BoxLayout(playlistDeleteButtonPanel, BoxLayout.X_AXIS));
        playlistDeleteButtonPanel.setOpaque(false);

        JButton deleteButton = new JButton();
        deleteButton.setIcon(AssetsLoader.loadImageIcon("trashcan_icon.png", 20, 20));
        deleteButton.addActionListener(event -> playlistController.deletePlaylist());
        deleteButton.setFocusPainted(false);
        deleteButton.setContentAreaFilled(false);
        deleteButton.setOpaque(false);
        deleteButton.setAlignmentY(Component.CENTER_ALIGNMENT);

        playlistDeleteButtonPanel.add(deleteButton);
        return playlistDeleteButtonPanel;
    }

    private JPanel createPlaylistEditButtonPanel(){
        JPanel playlistDeleteButtonPanel = new JPanel();
        playlistDeleteButtonPanel.setLayout(new BoxLayout(playlistDeleteButtonPanel, BoxLayout.X_AXIS));
        playlistDeleteButtonPanel.setOpaque(false);

        JButton deleteButton = new JButton();
        deleteButton.setIcon(AssetsLoader.loadImageIcon("pencil_icon.png", 20, 20));
        deleteButton.addActionListener(event -> playlistController.editPlaylist());
        deleteButton.setFocusPainted(false);
        deleteButton.setContentAreaFilled(false);
        deleteButton.setOpaque(false);
        deleteButton.setAlignmentY(Component.CENTER_ALIGNMENT);

        playlistDeleteButtonPanel.add(deleteButton);
        return playlistDeleteButtonPanel;
    }

    private void addHoverListeners(){
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt){
                setBackground(new Color(35,35,35));
                super.mouseEntered(evt);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt){
                setBackground(Color.decode("#121212"));
                super.mouseEntered(evt);
            }
        });
    }

    public Playlist getPlaylist() {
        return playlist;
    }
}
