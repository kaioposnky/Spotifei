package net.spotifei.Views.Panels;

//imports
import net.spotifei.Infrastructure.AudioPlayer.AudioUpdateListener;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Views.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import net.spotifei.Controller.MusicController;
import net.spotifei.Models.Music;

import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logError;


public class QueueMusicInfoPanel extends JPanel implements AudioUpdateListener {
    private final MainFrame mainframe;
    private final AppContext appContext;
    private JLabel musicTitle;
    private JLabel musicAuthors;
    private JPanel queuePanel;
    private final MusicController musicController;
    private SwingWorker<Void, Void> updaterWorker;

    /**
     * Construtor da classe `QueueMusicInfoPanel`.
     *
     * @param mainframe A instância da janela principal.
     * @param appContext O contexto da aplicação.
     */
    public QueueMusicInfoPanel(MainFrame mainframe, AppContext appContext){
        this.mainframe = mainframe;
        this.appContext = appContext;
        this.musicController = appContext.getMusicController(this, mainframe);
        initComponents();
        this.addStartListener();
        appContext.getAudioPlayerWorker().addListener(this);
    }

    /**
     * Este método inicializa e configura todos os componentes visuais do painel.
     */
    private void initComponents() {
        setBackground(new Color(23, 22, 22));
        setMaximumSize(new Dimension(200, Integer.MAX_VALUE ));
        setPreferredSize(new Dimension(200, Integer.MAX_VALUE ));
        setMinimumSize(new Dimension(200, Integer.MAX_VALUE ));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        JPanel titleWrapper = new JPanel();
        titleWrapper.setLayout(new BoxLayout(titleWrapper, BoxLayout.Y_AXIS));
        titleWrapper.setOpaque(false);

        JLabel title = new JLabel("Fila");
        title.setFont(new Font("Segoe UI", 1, 24));
        title.setForeground(new Color(250, 250, 250));
        title.setAlignmentX(CENTER_ALIGNMENT);

        titleWrapper.add(title);
        titleWrapper.setAlignmentX(CENTER_ALIGNMENT);

        this.add(Box.createVerticalStrut(30));
        this.add(titleWrapper);
        this.add(Box.createVerticalStrut(30));
        this.add(createMusicPlayingPanel());
        this.add(Box.createVerticalStrut(30));
        this.add(createMusicsQueue());
    }

    private JPanel createMusicPlayingPanel(){
        JPanel musicPlayingPanel = new JPanel();
        musicPlayingPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        musicPlayingPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        musicPlayingPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        musicPlayingPanel.setOpaque(false);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel musicPlaying = new JLabel("Tocando agora");
        musicPlaying.setFont(new Font("Segoe UI", 1, 16));
        musicPlaying.setForeground(new Color(255, 255, 255));
        musicPlaying.setAlignmentX(LEFT_ALIGNMENT);

        musicTitle = new JLabel("Carregando...");
        musicTitle.setFont(new Font("Arial", Font.BOLD, 14));
        musicTitle.setForeground(Color.WHITE);

        musicAuthors = new JLabel("Carregando...");
        musicAuthors.setFont(new Font("Arial", Font.BOLD, 12));
        musicAuthors.setForeground(Color.GRAY);

        JPanel musicInfoPanel = new JPanel();
        musicInfoPanel.setLayout(new BoxLayout(musicInfoPanel, BoxLayout.Y_AXIS));
        musicInfoPanel.setOpaque(false);
        musicInfoPanel.setAlignmentX(LEFT_ALIGNMENT);

        musicInfoPanel.add(musicTitle);
        musicInfoPanel.add(musicAuthors);

        contentPanel.add(musicPlaying);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(musicInfoPanel);

        musicPlayingPanel.add(contentPanel);

        return musicPlayingPanel;
    }

    /**
     * Cria o painel que contêm a lista de músicas na fila.
     * @return O JPanel com a lista de músicas na fila.
     */
    private JPanel createMusicsQueue() {
        JPanel musicsQueuePanel = new JPanel();
        musicsQueuePanel.setLayout(new BoxLayout(musicsQueuePanel, BoxLayout.Y_AXIS));
        musicsQueuePanel.setOpaque(false);
        musicsQueuePanel.setBorder(null);

        JLabel queueMusicsLabel = new JLabel("Próximas");
        queueMusicsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        queueMusicsLabel.setForeground(new Color(255, 255, 255));
        queueMusicsLabel.setBorder(new EmptyBorder(0, 5, 10, 0));

        musicsQueuePanel.add(queueMusicsLabel);

        JScrollPane scrollPane = new JScrollPane(queueMusicListPanel());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        musicsQueuePanel.add(scrollPane);
        musicsQueuePanel.add(Box.createVerticalGlue());

        return musicsQueuePanel;
    }

    /**
     * Cria o painel interno que irá conter os itens da fila de músicas.
     * @return O JPanel que servirá como contêiner para os itens da fila.
     */
    private JPanel queueMusicListPanel(){
        queuePanel = new JPanel();
        queuePanel.setLayout(new BoxLayout(queuePanel, BoxLayout.Y_AXIS));
        queuePanel.setOpaque(false);
        queuePanel.setBorder(new EmptyBorder(0, 5, 0, 0));
        return queuePanel;
    }

    /**
     * Atualiza o painel da lista de músicas da fila.
     * @param musics A lista de objetos Music para exibir na fila.
     */
    public void updateMusicListPanel(List<Music> musics){
        queuePanel.removeAll();
        if (musics != null && !musics.isEmpty()) {
            for (Music music : musics){
                queuePanel.add(createMusicInfoPanel(music));
                queuePanel.add(Box.createVerticalStrut(8));
            }
        } else {
            JLabel emptyQueueLabel = new JLabel("A fila está vazia.");
            emptyQueueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            emptyQueueLabel.setForeground(Color.GRAY);
            emptyQueueLabel.setAlignmentX(CENTER_ALIGNMENT);
            queuePanel.add(emptyQueueLabel);
        }
        queuePanel.revalidate();
        queuePanel.repaint();
    }

    public void updateCurrentMusic(){
        if(appContext.getMusicContext() != null){
            musicTitle.setText(appContext.getMusicContext().getNome());
            musicAuthors.setText(appContext.getMusicContext().getArtistsNames());
            repaint();
            revalidate();
        }
    }

    private JPanel createMusicInfoPanel(Music music) {
        JPanel musicInfoPanel = new JPanel();
        musicInfoPanel.setLayout(new BoxLayout(musicInfoPanel, BoxLayout.X_AXIS));
        musicInfoPanel.setOpaque(false);
        musicInfoPanel.setAlignmentX(LEFT_ALIGNMENT);

        JPanel infoWrapperPanel = new JPanel();
        infoWrapperPanel.setLayout(new BoxLayout(infoWrapperPanel, BoxLayout.Y_AXIS));
        infoWrapperPanel.setOpaque(false);

        JLabel musicInfoTitle = new JLabel(music.getNome());
        musicInfoTitle.setFont(new Font("Arial", Font.BOLD, 14));
        musicInfoTitle.setForeground(Color.white);

        JLabel musicInfoAuthors = new JLabel(music.getArtistsNames());
        musicInfoAuthors.setFont(new Font("Arial", Font.BOLD, 12));
        musicInfoAuthors.setForeground(Color.gray);

        infoWrapperPanel.add(musicInfoTitle);
        infoWrapperPanel.add(musicInfoAuthors);

        musicInfoPanel.add(infoWrapperPanel);
        return musicInfoPanel;
    }

    private void startPlaylistUpdateWorker(){
        updaterWorker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground(){
                try{
                    while(true){
                        try{
                            Thread.sleep(2000);
                            if(appContext.getMusicContext() == null || appContext.getPersonContext() == null) continue;

                            musicController.loadUserMusicQueue();

                        } catch (Exception e){
                            logError(e.getMessage(), e);
                        }
                    }
                } catch (Exception e){
                    return null;
                }
            }
        };
        updaterWorker.execute();
    }

    /**
     * Adiciona um listener de componente que é ativado quando o painel é mostrado.
     * Quando mostrado, ele obtém o ID do usuário logado.
     */
    private void addStartListener(){
        this.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentShown(ComponentEvent e) {
                if (updaterWorker != null){
                    updaterWorker.cancel(true);
                }
                startPlaylistUpdateWorker();
                super.componentShown(e);
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                if (updaterWorker == null) return;
                updaterWorker.cancel(true);
                super.componentHidden(e);
            }
        });
    }

    @Override
    public void onSelectMusic(Music music) {
        SwingUtilities.invokeLater(() -> {
            updateCurrentMusic();
        });
    }

    @Override
    public void onMusicProgressUpdate(long musicTime, long musicDuration) {

    }

    @Override
    public void onMusicPlayingStatusUpdate(boolean isPlaying) {

    }

    @Override
    public void onEndOfMusic() {

    }
}
