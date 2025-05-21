package net.spotifei.Views.Panels;

//imports
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Infrastructure.Factories.MusicInfoComponent.MusicInfoPanelBuilder;
import net.spotifei.Views.Components.MusicListComponent;
import net.spotifei.Views.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import net.spotifei.Controller.MusicController;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Infrastructure.Factories.MusicInfoComponent.MusicInfoPanelBuilder;
import net.spotifei.Models.Music;
import net.spotifei.Views.Components.MusicListComponent;
import net.spotifei.Views.MainFrame;

import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logError;


public class QueueMusicInfoPanel extends JPanel {
    private final MainFrame mainframe;
    private final AppContext appContext;
    private MusicListComponent musicsQueue;
    private MusicInfoPanelBuilder musicInfoPanelBuilder;
    private JLabel musicTitle;
    private JLabel musicAuthors;
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
        musicPlayingPanel.setAlignmentX(CENTER_ALIGNMENT);
        musicPlayingPanel.setOpaque(false);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel musicPlaying = new JLabel("Tocando agora");
        musicPlaying.setFont(new Font("Segoe UI", 1, 16));
        musicPlaying.setForeground(new Color(250, 250, 250));
        musicPlaying.setAlignmentX(LEFT_ALIGNMENT);

        Music music = new Music();
        music.setNome("Carregando...");
        music.setArtistsNames("Carregando...");
        JPanel musicInfoPanel;
        musicInfoPanel = createMusicInfoPanel(music);
        musicInfoPanel.setAlignmentX(LEFT_ALIGNMENT);
        musicInfoPanel.setOpaque(false);

        contentPanel.add(musicPlaying);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(musicInfoPanel);

        musicPlayingPanel.add(contentPanel);

        return musicPlayingPanel;
    }

    private JPanel createMusicsQueue(){
        JPanel musicsQueuePanel = new JPanel();
        musicsQueuePanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        musicsQueuePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        musicsQueuePanel.setAlignmentX(CENTER_ALIGNMENT);
        musicsQueuePanel.setOpaque(false);
        musicsQueuePanel.setPreferredSize(new Dimension(140, Integer.MAX_VALUE));
        musicsQueuePanel.setMaximumSize(new Dimension(140, Integer.MAX_VALUE));
        musicsQueuePanel.setMinimumSize(new Dimension(140, Integer.MAX_VALUE));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel queueMusicsLabel = new JLabel("Próximas");
        queueMusicsLabel.setFont(new Font("Segoe UI", 1, 16));
        queueMusicsLabel.setForeground(new Color(250, 250, 250));
        queueMusicsLabel.setAlignmentX(LEFT_ALIGNMENT);

        musicInfoPanelBuilder = new MusicInfoPanelBuilder(appContext, mainframe);
        musicInfoPanelBuilder.selectMusicInfoFromQueuePanel();

        musicsQueue = new MusicListComponent(musicInfoPanelBuilder);
        musicsQueue.setOpaque(false);
        musicsQueue.setBackground(null);
        musicsQueue.setPreferredSize(new Dimension(140, Integer.MAX_VALUE));
        musicsQueue.setMaximumSize(new Dimension(140, Integer.MAX_VALUE));
        musicsQueue.setMinimumSize(new Dimension(140, Integer.MAX_VALUE));
        musicsQueue.setAlignmentX(LEFT_ALIGNMENT);

        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(queueMusicsLabel);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(musicsQueue);

        musicsQueuePanel.add(contentPanel);

        return musicsQueuePanel;
    }

    private JPanel createMusicInfoPanel(Music music) {
        JPanel musicInfoPanel = new JPanel();
        musicInfoPanel.setLayout(new BoxLayout(musicInfoPanel, BoxLayout.X_AXIS));
        musicInfoPanel.setOpaque(false);

        JPanel infoWrapperPanel = new JPanel();
        infoWrapperPanel.setLayout(new BoxLayout(infoWrapperPanel, BoxLayout.Y_AXIS));
        infoWrapperPanel.setOpaque(false);

        musicTitle = new JLabel(music.getNome());
        musicTitle.setFont(new Font("Arial", Font.BOLD, 14));
        musicTitle.setForeground(Color.white);

        musicAuthors = new JLabel(music.getArtistsNames());
        musicAuthors.setFont(new Font("Arial", Font.BOLD, 12));
        musicAuthors.setForeground(Color.gray);

        infoWrapperPanel.add(musicTitle);
        infoWrapperPanel.add(musicAuthors);

        musicInfoPanel.add(infoWrapperPanel);
        return musicInfoPanel;
    }

    private void startPlaylistUpdateWorker(){
        updaterWorker = new SwingWorker<>() {
            @Override
            protected Void doInBackground(){
                try{
                    while(true){
                        try{
                            Thread.sleep(2000);
                            if(appContext.getMusicContext() == null || appContext.getPersonContext() == null) continue;
                            musicController.loadUserMusicQueue();
                            musicTitle.setText(appContext.getMusicContext().getNome());
                            musicAuthors.setText(appContext.getMusicContext().getArtistsNames());
                            updateUI();
                            repaint();
                            revalidate();
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

    public MusicListComponent getMusicsQueue() {
        return musicsQueue;
    }
}
