package net.spotifei.Views.Panels;

import net.spotifei.Controller.MusicController;
import net.spotifei.Views.Components.SpotifyLikeButton;
import net.spotifei.Views.Components.SpotifyLikeSlider;
import net.spotifei.Views.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MusicPlayerPanel extends JPanel {

    private MusicController musicController;
    private SpotifyLikeSlider musicSlider;
    private SpotifyLikeSlider audioSlider;
    private JLabel musicTimeNowLabel;
    private JLabel musicTimeTotalLabel;
    private SpotifyLikeButton btnPause;
    private JLabel musicTitle;
    private JLabel musicArtist;

    public MusicPlayerPanel(MainFrame mainframe) {
        this.musicController = new MusicController(this, mainframe, this);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 15, 10));
        setBackground(Color.black);

        add(createLeftPanel(), BorderLayout.WEST);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createRightPanel(), BorderLayout.EAST);
    }

    private JPanel createLeftPanel(){
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        leftPanel.setOpaque(true);

        JPanel songInfoPanel = new JPanel();
        songInfoPanel.setOpaque(false);
        songInfoPanel.setLayout(new BoxLayout(songInfoPanel, BoxLayout.Y_AXIS));

        musicTitle = new JLabel("Music Title");
        musicTitle.setFont(new Font("Arial", Font.BOLD, 14));
        musicTitle.setForeground(Color.WHITE);
        
        musicArtist = new JLabel("Music Artist");
        musicArtist.setFont(new Font("Arial", Font.PLAIN, 12));
        musicArtist.setForeground(Color.GRAY);

        songInfoPanel.add(musicTitle);
        songInfoPanel.add(musicArtist);

        leftPanel.add(songInfoPanel, BorderLayout.WEST);

        return leftPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalGlue());


        JPanel musicProgressPanel = new JPanel();
        musicProgressPanel.setOpaque(false);

        musicSlider = new SpotifyLikeSlider(0, 100, 20, 400, 20);
        musicSlider.addMouseListener(getMusicSliderMouseListeners(musicSlider));

        musicTimeNowLabel = new JLabel("0:00");
        musicTimeNowLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        musicTimeNowLabel.setForeground(Color.GRAY);

        musicTimeTotalLabel = new JLabel("1:00");
        musicTimeTotalLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        musicTimeTotalLabel.setForeground(Color.GRAY);

        musicProgressPanel.add(musicTimeNowLabel, BorderLayout.WEST);
        musicProgressPanel.add(musicSlider, BorderLayout.CENTER);
        musicProgressPanel.add(musicTimeTotalLabel, BorderLayout.EAST);

        JPanel musicControlPanel = new JPanel();
        musicControlPanel.setOpaque(false);
        musicControlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

        SpotifyLikeButton btnSkip = new SpotifyLikeButton("<html>&#x23ED;</html>", 16);
        btnSkip.addActionListener(event -> handleNextMusicButton());

        JPanel pausebtnWrapper = new JPanel();
        pausebtnWrapper.setOpaque(false);
        pausebtnWrapper.setPreferredSize(new Dimension(50, 50));
        pausebtnWrapper.setMinimumSize(new Dimension(50, 50));
        pausebtnWrapper.setMaximumSize(new Dimension(50, 50));
        pausebtnWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        btnPause = new SpotifyLikeButton("<html>⏸</html>", 20);
        btnPause.addActionListener(event -> handlePauseButton());

        pausebtnWrapper.add(btnPause);

        SpotifyLikeButton btnPrevious = new SpotifyLikeButton("<html>&#x23EE;</html>", 16);

        musicControlPanel.add(btnPrevious, BorderLayout.WEST);
        musicControlPanel.add(pausebtnWrapper, BorderLayout.CENTER);
        musicControlPanel.add(btnSkip, BorderLayout.EAST);

        centerPanel.add(musicControlPanel, BorderLayout.NORTH);
        centerPanel.add(musicProgressPanel, BorderLayout.SOUTH);

        return centerPanel;
    }

    private JPanel createRightPanel(){
        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));

        JPanel muteButtonWrapper = new JPanel();
        muteButtonWrapper.setOpaque(false);
        muteButtonWrapper.setPreferredSize(new Dimension(25, 25));
        muteButtonWrapper.setMinimumSize(new Dimension(25, 25));
        muteButtonWrapper.setMaximumSize(new Dimension(25, 25));
        muteButtonWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        SpotifyLikeButton btnMute = new SpotifyLikeButton("<html> &#x1F50A </html>", 17);
        btnMute.setFocusPainted(false);
        btnMute.setBorder(new EmptyBorder(-7,8,0,0));

        muteButtonWrapper.add(btnMute);

        audioSlider = new SpotifyLikeSlider(0, 100, 15);
        audioSlider.addMouseListener(getAudioSliderMouseListeners(audioSlider));

        rightPanel.add(muteButtonWrapper);
        rightPanel.add(audioSlider);

        return rightPanel;
    }

    private MouseAdapter getMusicSliderMouseListeners(SpotifyLikeSlider musicSlider) {
        return new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                musicController.setMusicTime(musicSlider.getValue());
            }
        };
    }

    private MouseAdapter getAudioSliderMouseListeners(SpotifyLikeSlider audioSlider) {
        return new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                musicController.setAudioVolume(audioSlider.getValue());
            }
        };
    }

    public void handleNextMusicButton(){
        musicController.playMusic();
    }

    public void handlePauseButton(){
        musicController.pauseMusic();
    }

    public JLabel getMusicTimeNowLabel() {
        return musicTimeNowLabel;
    }

    public JLabel getMusicTimeTotalLabel() {
        return musicTimeTotalLabel;
    }

    public SpotifyLikeSlider getMusicSlider() {
        return musicSlider;
    }

    public SpotifyLikeButton getBtnPause() {
        return btnPause;
    }

    public JLabel getMusicTitle() {
        return musicTitle;
    }

    public void setMusicTitle(JLabel musicTitle) {
        this.musicTitle = musicTitle;
    }

    public JLabel getMusicArtist() {
        return musicArtist;
    }

    public void setMusicArtist(JLabel musicArtist) {
        this.musicArtist = musicArtist;
    }
}
