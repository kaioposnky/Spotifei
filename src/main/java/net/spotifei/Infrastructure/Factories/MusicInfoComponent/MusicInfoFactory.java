package net.spotifei.Infrastructure.Factories.MusicInfoComponent;

import net.spotifei.Controller.MusicController;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.Music;
import net.spotifei.Views.Components.FeedBackComponent;
import net.spotifei.Views.MainFrame;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;

import static net.spotifei.Helpers.AssetsLoader.loadImageIcon;

public abstract class MusicInfoFactory {
    private Music music;
    private final AppContext appContext;
    private final MainFrame mainframe;
    private MusicController musicController;

    protected MusicInfoFactory(AppContext appContext, MainFrame mainframe){
        this.appContext = appContext;
        this.mainframe = mainframe;
    }

    protected JPanel getSearchMusicInfoPanel(){
        JPanel basePanel = createContainerPanel();

        basePanel.add(createMusicFeedbackPanel());
        basePanel.add(Box.createHorizontalStrut(20));
        basePanel.add(createMusicPlayButtonPanel());

        basePanel.add(Box.createHorizontalStrut(20));
        basePanel.add(createMusicTimePanel());
        basePanel.add(Box.createHorizontalStrut(20));

//        musicPanel.add(basePanel);

        return basePanel;
    }

    protected JPanel getHistoryMusicInfoPanel(){

        JPanel mainPanel = createContainerPanel();

        mainPanel.add(createMusicFeedbackPanel());
        mainPanel.add(Box.createHorizontalStrut(50));
        mainPanel.add(createPlayCountPanel());
        mainPanel.add(Box.createHorizontalStrut(40));
        mainPanel.add(createMusicTimePanel());
        mainPanel.add(Box.createHorizontalStrut(20));

        addHoverListeners(mainPanel);

        this.musicController = appContext.getMusicController(mainPanel, mainframe);

        return mainPanel;
    }

    private JPanel createContainerPanel(){
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.X_AXIS));
        containerPanel.setBackground(Color.decode("#121212"));
        containerPanel.setOpaque(true);

        Border borderLine = new MatteBorder(0, 0, 1, 0, new Color(50, 50, 50));
        Border borderInside = BorderFactory.createEmptyBorder(5, 5, 5, 5);

        containerPanel.setBorder(BorderFactory.createCompoundBorder(borderLine, borderInside));

        containerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        containerPanel.add(getMusicInfoPanel());
        containerPanel.add(Box.createHorizontalGlue()); // joga os proximos elementos pra direita

        return containerPanel;
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

        musicInfoPanel.add(infoWrapperPanel);
        musicInfoPanel.add(Box.createHorizontalStrut(40));

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
        musicPlayButton.setBorder(new EmptyBorder(0,0,0,5));
        musicPlayButton.setFocusPainted(false);
        musicPlayButton.setContentAreaFilled(false);
        musicPlayButton.setOpaque(false);
        musicPlayButton.addActionListener(event -> handlePlayButton());

        musicPlayButtonPanel.add(musicPlayButton);

        return musicPlayButtonPanel;
    }

    private JPanel createMusicFeedbackPanel(){
        JPanel feedbackPanel = new JPanel();
        feedbackPanel.setLayout(new BoxLayout(feedbackPanel, BoxLayout.X_AXIS));
        feedbackPanel.setOpaque(false);

        FeedBackComponent feedBackComponent = new
                FeedBackComponent(appContext, mainframe, music);

        feedbackPanel.add(feedBackComponent);
        return feedbackPanel;
    }

    private JPanel createPlayCountPanel(){
        JPanel playCountPanel = new JPanel();
        playCountPanel.setLayout(new BoxLayout(playCountPanel, BoxLayout.X_AXIS));
        playCountPanel.setOpaque(false);

        JLabel viewCount = new JLabel(String.valueOf(music.getVezesTocada()) + " reproduções");
        viewCount.setFont(new Font("Arial", Font.BOLD, 12));
        viewCount.setForeground(Color.decode("#aeaeae"));

        playCountPanel.add(viewCount);

        return playCountPanel;
    }

    private String getMusicTimeTotal(){
        long musicMicrosseconds = music.getDuracaoMs();
        long musicInSeconds = musicMicrosseconds / 1_000_000;
        long musicMinutes = musicInSeconds / 60;
        long musicSeconds = musicInSeconds % 60;
        return String.format("%01d:%02d", musicMinutes, musicSeconds);
    }

    private void handlePlayButton(){
        musicController.playMusicById(music.getIdMusica());
    }

    private void addHoverListeners(JPanel panel){
        addActionListeners(panel, new MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBackground(new Color(35, 35, 35)); // Cor ao passar o mouse
                super.mouseEntered(evt);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel.setBackground(Color.decode("#121212")); // Cor original ao sair
                super.mouseExited(evt); // Corrigido para mouseExited
            }
        });
    }

    private void addActionListeners(JPanel panel, MouseAdapter mouseAdapterListeners){
        panel.addMouseListener(mouseAdapterListeners);
    }

    public void setMusic(Music music) {
        if (music == null) {
            return;
        }
        this.music = music;
    }

}
