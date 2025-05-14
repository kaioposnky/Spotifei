package net.spotifei.Views.Components;

import net.spotifei.Helpers.AssetsLoader;
import net.spotifei.Models.Playlist;
import net.spotifei.Infrastructure.Container.AppContext;
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

    public PlaylistInfoComponent(Playlist playlist, AppContext appContext, MainFrame mainframe){
        this.playlist = playlist;
        this.appContext = appContext;
        this.mainframe = mainframe;
        initComponents();
    }

    private void initComponents(){
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(Color.decode("#121212"));
        setOpaque(true);
        addHoverListeners();

        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.X_AXIS));
        boxPanel.setOpaque(false);

        Border borderLine = new MatteBorder(0,0,1,0, new Color(50,50,50));
        Border borderInside = BorderFactory.createEmptyBorder(5,5,5,5);

        boxPanel.setBorder(BorderFactory.createCompoundBorder(borderLine, borderInside));
        boxPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,50));

        boxPanel.add(getPlaylistInfoComponent(), BorderLayout.WEST);
        boxPanel.add(Box.createHorizontalGlue());
        boxPanel.add(createPlaylistActionButtonPanel());
        boxPanel.add(Box.createHorizontalStrut(20));

        setAlignmentY(Component.CENTER_ALIGNMENT);
        this.add(boxPanel);
    }

    private JPanel getPlaylistInfoComponent(){
        JPanel playlistInfoComponent = new JPanel();
        playlistInfoComponent.setLayout(new BoxLayout(playlistInfoComponent, BoxLayout.X_AXIS));
        playlistInfoComponent.setOpaque(false);

        JPanel infoWrapperPanel = new JPanel();
        infoWrapperPanel.setLayout(new BoxLayout(infoWrapperPanel, BoxLayout.Y_AXIS));
        infoWrapperPanel.setOpaque(false);

        JLabel playlistName = new JLabel(playlist.getNome());
        playlistName.setFont(new Font("Arial", Font.BOLD, 14));
        playlistName.setForeground(Color.white);

        playlistInfoComponent.add(playlistName);
        playlistInfoComponent.add(Box.createHorizontalStrut(20));

        return playlistInfoComponent;
    }

    private JPanel createPlaylistActionButtonPanel(){
        JPanel playlistActionButtonPanel = new JPanel();
        playlistActionButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,0,0));
        playlistActionButtonPanel.setOpaque(false);

//        JButton playButton = new JButton();
//        playButton.setIcon(AssetsLoader.loadImageIcon("musicIcons/play.png", 20, 20));
//        playButton.addActionListener(event -> {
//            appContext.getPlayListController(this).createPlaylist();
//        });
//        playButton.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
//        playButton.setFocusPainted(false);
//        playButton.setContentAreaFilled(false);
//        playButton.setOpaque(false);
//
//        playlistActionButtonPanel.add(playButton);

        return playlistActionButtonPanel;
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


}
