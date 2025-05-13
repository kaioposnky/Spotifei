package net.spotifei.Views.Components;

import net.spotifei.Controller.MusicController;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.Music;
import net.spotifei.Views.MainFrame;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;

import static net.spotifei.Helpers.AssetsLoader.loadImageIcon;

public class MusicInfoComponent extends JPanel{
    private final Music music;
    private final boolean showLikeDislikeButtons;
    private final AppContext appContext;
    private final MainFrame mainframe;
    private final MusicController musicController;

    public MusicInfoComponent(Music music, boolean showLikeDislikeButtons, AppContext appContext, MainFrame mainframe){
        this.music = music;
        this.showLikeDislikeButtons = showLikeDislikeButtons;
        this.appContext = appContext;
        this.mainframe = mainframe;
        this.musicController = appContext.getMusicController(this, mainframe);
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

        Border borderLine = new MatteBorder(0, 0, 1, 0, new Color(50, 50, 50));
        Border borderInside = BorderFactory.createEmptyBorder(5, 5, 5, 5);

        boxPanel.setBorder(BorderFactory.createCompoundBorder(borderLine, borderInside));

        boxPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        boxPanel.add(getMusicInfoPanel(), BorderLayout.WEST);
        boxPanel.add(Box.createHorizontalGlue()); // joga os proximos elementos pra direita
        boxPanel.add(createMusicPlayButtonPanel());
        boxPanel.add(Box.createHorizontalStrut(20));
        boxPanel.add(createMusicTimePanel(), BorderLayout.EAST);

        setAlignmentY(Component.CENTER_ALIGNMENT);
        this.add(boxPanel);
    }

    private JPanel getMusicInfoPanel() {
        JPanel musicInfoPanel = new JPanel();
        musicInfoPanel.setLayout(new BoxLayout(musicInfoPanel, BoxLayout.X_AXIS));
        musicInfoPanel.setOpaque(false);

        JPanel infoWrapperPanel = new JPanel();
        infoWrapperPanel.setLayout(new BoxLayout(infoWrapperPanel, BoxLayout.Y_AXIS));
        infoWrapperPanel.setOpaque(false);

        JLabel musicTitle = new JLabel(music.getNome());
        musicTitle.setFont(new Font("Arial", Font.BOLD, 14));
        musicTitle.setForeground(Color.white);

        JLabel musicAuthors = new JLabel(music.getArtistsNames());
        musicAuthors.setFont(new Font("Arial", Font.BOLD, 12));
        musicAuthors.setForeground(Color.gray);

        infoWrapperPanel.add(musicTitle);
        infoWrapperPanel.add(musicAuthors);

        FeedBackComponent feedBackComponent = new
                FeedBackComponent(appContext, mainframe, music);

        musicInfoPanel.add(infoWrapperPanel);
        musicInfoPanel.add(Box.createHorizontalStrut(40));
        musicInfoPanel.add(feedBackComponent);

        return musicInfoPanel;
    }

    private JPanel createMusicTimePanel() {
        JPanel musicTimePanel = new JPanel();
        musicTimePanel.setLayout(new BoxLayout(musicTimePanel, BoxLayout.X_AXIS));
        musicTimePanel.setOpaque(false);

        musicTimePanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        musicTimePanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel musicTimeTotal = new JLabel(getMusicTimeTotal());
        musicTimeTotal.setFont(new Font("Arial", Font.BOLD, 12));
        musicTimeTotal.setForeground(Color.decode("#aeaeae"));

        long musicSeconds = music.getDuracaoMs() / 1_000_000;
        long musicMinutes = musicSeconds / 60;
        musicTimeTotal.setText(String.format("%01d:%02d", musicMinutes, musicSeconds % 60));

        musicTimePanel.add(musicTimeTotal);

        return musicTimePanel;
    }

    private JPanel createMusicPlayButtonPanel() {
        JPanel musicPlayButtonPanel = new JPanel();
        musicPlayButtonPanel.setLayout(new BoxLayout(musicPlayButtonPanel, BoxLayout.X_AXIS));
        musicPlayButtonPanel.setOpaque(false);

        JButton musicPlayButton = new JButton();
        musicPlayButton.setIcon(loadImageIcon("musicIcons/play.png", 20, 20));
        musicPlayButton.addActionListener(event -> {
            appContext.getMusicController(this, mainframe).playMusic();
        });
        musicPlayButton.setBorder(new EmptyBorder(0,0,0,5));
        musicPlayButton.setFocusPainted(false);
        musicPlayButton.setContentAreaFilled(false);
        musicPlayButton.setOpaque(false);
        musicPlayButton.addActionListener(event -> {handlePlayButton();});

        musicPlayButtonPanel.add(musicPlayButton);

        return musicPlayButtonPanel;
    }

    private String getMusicTimeTotal(){
        long musicMicrosseconds = music.getDuracaoMs();
        long musicInSeconds = musicMicrosseconds / 1_000_000;
        long musicMinutes = musicInSeconds / 60;
        long musicSeconds = musicInSeconds % 60;
        return String.format("%01d:%02d", musicMinutes, musicSeconds);
    }

    private void handlePlayButton(){
        musicController.playMusic();
    }

    private void addHoverListeners(){
        addActionListeners(new MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(new Color(35, 35, 35));
                super.mouseEntered(evt);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(Color.decode("#121212"));
                super.mouseEntered(evt);
            }
        });
    }

    private void addActionListeners(MouseAdapter mouseAdapterListeners){
        this.addMouseListener(mouseAdapterListeners);
    }
}
