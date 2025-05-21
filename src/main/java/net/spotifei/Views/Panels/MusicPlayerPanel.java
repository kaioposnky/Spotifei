package net.spotifei.Views.Panels;

//imports
import net.spotifei.Controller.MusicController;
import net.spotifei.Infrastructure.AudioPlayer.AudioUpdateListener;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.Music;
import net.spotifei.Views.Components.FeedBackComponent;
import net.spotifei.Views.Components.SpotifyLikeButton;
import net.spotifei.Views.Components.SpotifyLikeSlider;
import net.spotifei.Views.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MusicPlayerPanel extends JPanel implements AudioUpdateListener {

    private final MusicController musicController;
    private FeedBackComponent feedbackPanel;
    private SpotifyLikeSlider musicSlider;
    private SpotifyLikeSlider audioSlider;
    private JLabel musicTimeNowLabel;
    private JLabel musicTimeTotalLabel;
    private SpotifyLikeButton btnPause;
    private JLabel musicTitle;
    private JLabel musicArtist;

    private final MainFrame mainframe;
    private final AppContext appContext;

    /**
     * Construtor da classe `MusicPlayerPanel`.
     *
     * @param mainframe A instância da janela principal.
     * @param appContext O contexto da aplicação.
     */
    public MusicPlayerPanel(MainFrame mainframe, AppContext appContext) {
        this.mainframe = mainframe;
        this.appContext = appContext;
        this.musicController = appContext.getMusicController(this, mainframe);
        appContext.getAudioPlayerWorker().addListener(musicController);
        initComponents();
    }

    /**
     * Este método inicializa e configura todos os componentes visuais do painel.
     */
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

    /**
     * Cria e retorna um objeto `GridBagConstraints` com as configurações para posicionar
     * um painel no `GridBagLayout`.
     *
     * @param gridx A coluna na grade.
     * @param weightx O peso horizontal (para espaçamento flexível).
     * @param anchor O ponto de ancoragem dentro da célula (e.g., WEST, CENTER, EAST).
     * @return Um objeto `GridBagConstraints` configurado.
     */
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

    /**
     * Cria e retorna o painel esquerdo do player, contendo o título da música, o artista
     * e o componente de feedback.
     *
     * @return Um JPanel configurado para o lado esquerdo do player.
     */
    private JPanel createLeftPanel(){
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS) );
        leftPanel.setOpaque(false);

        JPanel songInfoPanel = new JPanel();
        songInfoPanel.setOpaque(false);
        songInfoPanel.setLayout(new BoxLayout(songInfoPanel, BoxLayout.Y_AXIS));

        musicTitle = new JLabel("Carregando...");
        musicTitle.setFont(new Font("Arial", Font.BOLD, 14));
        musicTitle.setForeground(Color.WHITE);

        musicArtist = new JLabel("Carregando...");
        musicArtist.setFont(new Font("Arial", Font.PLAIN, 12));
        musicArtist.setForeground(Color.GRAY);

        feedbackPanel = new FeedBackComponent(appContext, mainframe);

        if(appContext.getMusicContext() != null){
            feedbackPanel.setMusic(appContext.getMusicContext());
        } else{
            Music music = new Music();
            music.setGostou(false);
            feedbackPanel.setMusic(music);
        }

        // as box deixam as infos centralizadas (é meio gambis mas funciona)
        songInfoPanel.add(Box.createVerticalGlue());
        songInfoPanel.add(musicTitle);
        songInfoPanel.add(musicArtist);
        songInfoPanel.add(Box.createVerticalGlue());

        leftPanel.add(songInfoPanel);
        leftPanel.add(Box.createHorizontalStrut(50));
        leftPanel.add(feedbackPanel);

        return leftPanel;
    }

    /**
     * Cria e retorna o painel central do player, contendo os controles de reprodução
     * e o slider de progresso da música.
     *
     * @return Um JPanel configurado para o centro do player.
     */
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel musicProgressPanel = new JPanel();
        musicProgressPanel.setLayout(new BorderLayout( 0, 0));
        musicProgressPanel.setOpaque(false);

        musicSlider = new SpotifyLikeSlider(0, 100, 0, 400, 20);
        musicSlider.addMouseListener(getMusicSliderMouseListeners(musicSlider));

        musicTimeNowLabel = new JLabel("-/-");
        musicTimeNowLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        musicTimeNowLabel.setForeground(Color.GRAY);

        musicTimeTotalLabel = new JLabel("-/-");
        musicTimeTotalLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        musicTimeTotalLabel.setForeground(Color.GRAY);

        musicProgressPanel.add(musicTimeNowLabel, BorderLayout.WEST);
        musicProgressPanel.add(musicSlider, BorderLayout.CENTER);
        musicProgressPanel.add(musicTimeTotalLabel, BorderLayout.EAST);

        JPanel musicControlPanel = new JPanel();
        musicControlPanel.setOpaque(false);
        musicControlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

        SpotifyLikeButton btnSkip = new SpotifyLikeButton("<html>&#x23ED;</html>", 16);
        btnSkip.addActionListener(event -> handleSkipButton());

        JPanel pausebtnWrapper = new JPanel();
        pausebtnWrapper.setOpaque(false);
        pausebtnWrapper.setPreferredSize(new Dimension(50, 50));
        pausebtnWrapper.setMinimumSize(new Dimension(50, 50));
        pausebtnWrapper.setMaximumSize(new Dimension(50, 50));
        pausebtnWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        btnPause = new SpotifyLikeButton("<html>&#x25B6;</html>", 20);
        btnPause.addActionListener(event -> handlePauseButton());

        pausebtnWrapper.add(btnPause);

        SpotifyLikeButton btnPrevious = new SpotifyLikeButton("<html>&#x23EE;</html>", 16);
        btnPrevious.addActionListener(event -> handlePreviousButton());

        musicControlPanel.add(btnPrevious, BorderLayout.WEST);
        musicControlPanel.add(pausebtnWrapper, BorderLayout.CENTER);
        musicControlPanel.add(btnSkip, BorderLayout.EAST);

        centerPanel.add(musicControlPanel);
        centerPanel.add(musicProgressPanel);

        return centerPanel;
    }

    /**
     * Cria e retorna o painel direito do player, contendo o botão de mudo e o slider de volume.
     *
     * @return Um JPanel configurado para o lado direito do player.
     */
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

        audioSlider = new SpotifyLikeSlider(0, 100, 50);
        audioSlider.addMouseListener(getAudioSliderMouseListeners(audioSlider));

        rightPanel.add(muteButtonWrapper);
        rightPanel.add(audioSlider);

        return rightPanel;
    }

    /**
     * Retorna um `MouseAdapter` para o slider de progresso da música.
     *
     * @param musicSlider O slider de progresso da música.
     * @return Um `MouseAdapter` para o slider.
     */
    private MouseAdapter getMusicSliderMouseListeners(SpotifyLikeSlider musicSlider) {
        return new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                musicController.setMusicTime(musicSlider.getValue());
            }
        };
    }

    /**
     * Retorna um `MouseAdapter` para o slider de volume de áudio.
     *
     * @param audioSlider O slider de volume.
     * @return Um `MouseAdapter` para o slider.
     */
    private MouseAdapter getAudioSliderMouseListeners(SpotifyLikeSlider audioSlider) {
        return new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                musicController.setAudioVolume(audioSlider.getValue());
            }
        };
    }

    /**
     * Implementação do método `onSelectMusic` da interface `AudioUpdateListener`.
     *
     * @param music A música que foi selecionada.
     */
    @Override
    public void onSelectMusic(Music music) {
        // atualiza os textos para os textos da nova música
        musicTitle.setText(music.getNome());
        musicArtist.setText(music.getArtistsNames());

        musicTimeNowLabel.setText("0:00");
        long musicTotalLength = music.getDuracaoMs();
        long totalSeconds = musicTotalLength / 1_000_000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        this.musicTimeTotalLabel.setText(String.format("%1d:%02d", minutes, seconds));

        feedbackPanel.setMusic(music);
    }

    /**
     * Implementação do método `onMusicProgressUpdate` da interface `AudioUpdateListener`.
     *
     * @param musicCurrentLength O tempo atual da música em microssegundos.
     * @param musicTotalLength O tempo total da música em microssegundos.
     */
    @Override
    public void onMusicProgressUpdate(long musicCurrentLength, long musicTotalLength) {

        float musicPercentage = (float) musicCurrentLength / (float) musicTotalLength;
        musicPercentage = Math.max(0.0f, Math.min(1.0f, musicPercentage));

        this.musicSlider.setValue((int) (musicPercentage * 100));

        long currentSeconds = musicCurrentLength / 1_000_000;
        long minutes = currentSeconds / 60;
        long seconds = currentSeconds % 60;
        this.musicTimeNowLabel.setText(String.format("%1d:%02d", minutes, seconds));
    }

    /**
     * Implementação do método `onMusicPlayingStatusUpdate` da interface `AudioUpdateListener`.
     *
     * @param isPlaying `true` se a música estiver tocando, `false` caso contrário.
     */
    @Override
    public void onMusicPlayingStatusUpdate(boolean isPlaying) {
        if (isPlaying) {
            this.btnPause.setText("<html>⏸</html>");
        } else{
            this.btnPause.setText("<html>&#x25B6;</html>");
        }
    }

    /**
     * Implementação do método `onEndOfMusic` da interface `AudioUpdateListener`.
     */
    @Override
    public void onEndOfMusic() {

    }

    /**
     * Método para lidar com o clique no botão "Pular".
     */
    public void handleSkipButton(){
        musicController.skipMusic();
    }

    /**
     * Método para lidar com o clique no botão "Voltar".
     */
    public void handlePreviousButton(){
        musicController.previousMusic();
    }

    /**
     * Método para lidar com o clique no botão de pausa/play.
     */
    public void handlePauseButton(){
        musicController.togglePauseMusic();
    }

    // Métodos Getters e Setters para as variáveis de instância dos componentes
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

    public FeedBackComponent getFeedbackPanel() {
        return feedbackPanel;
    }
}
