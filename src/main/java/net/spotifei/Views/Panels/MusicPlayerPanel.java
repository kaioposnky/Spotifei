package net.spotifei.Views.Panels;

import net.spotifei.Controller.MusicController;
import net.spotifei.Infrastructure.AudioPlayer.AudioUpdateListener;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Views.Components.SpotifyLikeButton;
import net.spotifei.Views.Components.SpotifyLikeSlider;
import net.spotifei.Views.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;

public class MusicPlayerPanel extends JPanel implements AudioUpdateListener {

    private final MusicController musicController;
    private SpotifyLikeSlider musicSlider;
    private SpotifyLikeSlider audioSlider;
    private JLabel musicTimeNowLabel;
    private JLabel musicTimeTotalLabel;
    private SpotifyLikeButton btnPause;
    private JLabel musicTitle;
    private JLabel musicArtist;

    private final MainFrame mainframe;
    private final AppContext appContext;

    public MusicPlayerPanel(MainFrame mainframe, AppContext appContext) {
        this.mainframe = mainframe;
        this.appContext = appContext;
        this.musicController = appContext.getMusicController(this, mainframe);
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(5, 5, 15, 10));
        setBackground(Color.black);

        JPanel leftPanel = createLeftPanel();
        JPanel centerPanel = createCenterPanel();
        JPanel rightPanel = createRightPanel();

        GridBagConstraints gbc;
        gbc = createPanelConstraints(0,0, GridBagConstraints.WEST);
        add(leftPanel, gbc);

        gbc = createPanelConstraints(1, 0.7, GridBagConstraints.WEST);
        add(Box.createHorizontalGlue(), gbc);

        gbc = createPanelConstraints(2, 0.5, GridBagConstraints.CENTER);
        add(centerPanel, gbc);

        gbc = createPanelConstraints(3, 0.7, GridBagConstraints.EAST);
        add(Box.createHorizontalGlue(), gbc);

        gbc = createPanelConstraints(4, 0, GridBagConstraints.EAST);
        add(rightPanel, gbc);
    }

    public GridBagConstraints createPanelConstraints(
            int gridx, double weightx, int anchor
    ){
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = 0; // nao mexer na altura pra nao bugar tudo
        constraints.weightx = weightx;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = anchor;
        return constraints;
    }

    private JPanel createLeftPanel(){
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS) );
        leftPanel.setOpaque(false);

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

        // as box deixam as infos centralizadas (é meio gambis mas funciona)
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(songInfoPanel);
        leftPanel.add(Box.createVerticalGlue());

        return leftPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel musicProgressPanel = new JPanel();
        musicProgressPanel.setLayout(new BorderLayout( 0, 0));
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
        musicControlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

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

        centerPanel.add(musicControlPanel);
        centerPanel.add(musicProgressPanel);

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

    @Override
    public void onMusicProgressUpdate(long musicCurrentLength, long musicTotalLength) {

        float musicPercentage = (float) musicCurrentLength / (float) musicTotalLength;
        musicPercentage = Math.max(0.0f, Math.min(1.0f, musicPercentage));

        this.musicSlider.setValue((int) (musicPercentage * 100));

        long currentSeconds = musicCurrentLength / 1_000_000;
        long minutes = currentSeconds / 60;
        long seconds = currentSeconds % 60;
        this.musicTimeNowLabel.setText(String.format("%1d:%02d", minutes, seconds));

        long totalSeconds = musicTotalLength / 1_000_000;
        minutes = totalSeconds / 60;
        seconds = totalSeconds % 60;
        this.musicTimeTotalLabel.setText(String.format("%1d:%02d", minutes, seconds));

    }

    @Override
    public void onMusicPlayingStatusUpdate(boolean isPlaying) {
        logDebug("Play update acionado! " + isPlaying);
        if (isPlaying) {
            this.btnPause.setText("<html>⏸</html>");
        } else{
            this.btnPause.setText("<html>&#x25B6;</html>");
        }
    }

    @Override
    public void onEndOfMusic() {

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
