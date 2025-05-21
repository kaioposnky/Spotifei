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

/**
 *
 * @author fengl
 */

public class QueueMusicInfoPanel extends JPanel {
    private final MainFrame mainframe;
    private final AppContext appContext;
    private int userId;

    /**
     * Construtor da classe `QueueMusicInfoPanel`.
     *
     * @param mainframe A instância da janela principal.
     * @param appContext O contexto da aplicação.
     */
    public QueueMusicInfoPanel(MainFrame mainframe, AppContext appContext){
        this.mainframe = mainframe;
        this.appContext = appContext;
        initComponents();
        this.addStartListener();
    }

    private JLabel jLabel;

    /**
     * Este método inicializa e configura todos os componentes visuais do painel.
     */
    private void initComponents() {
        setBackground(new Color(0, 0, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(null);
        setAlignmentX(CENTER_ALIGNMENT);

        JLabel jLabel = new JLabel("> Sua fila de músicas: ");
        jLabel.setFont(new Font("Segoe UI", 1, 18));
        jLabel.setForeground(new Color(250, 250, 250));
        jLabel.setAlignmentX(CENTER_ALIGNMENT);

        MusicInfoPanelBuilder musicInfoPanelBuilder = new MusicInfoPanelBuilder(appContext, mainframe);
        musicInfoPanelBuilder.selectMusicsFromPlaylist();

        userId = 0;

//        List<Music> queueMusics = appContext.getMusicController(this, mainframe).getUserQueueMusics(userId);

        MusicListComponent musicListComponent = new MusicListComponent(musicInfoPanelBuilder);
//        musicListComponent.setMusics(queueMusics);
        musicListComponent.setAlignmentX(CENTER_ALIGNMENT);
        musicListComponent.renderMusics();

        this.add(Box.createVerticalGlue());
        this.add(jLabel);
        this.add(Box.createVerticalStrut(30));
        this.add(musicListComponent);
        this.add(Box.createVerticalGlue());


        setMaximumSize(new Dimension(300, 600));
        setMinimumSize(new Dimension(300, 600));
        setPreferredSize(new Dimension(300, 600));
    }

    /**
     * Adiciona um listener de componente que é ativado quando o painel é mostrado.
     * Quando mostrado, ele obtém o ID do usuário logado.
     */
    private void addStartListener(){
        this.addComponentListener(new ComponentAdapter(){
            @Override
            public void componentShown(ComponentEvent e) {
                userId = appContext.getPersonContext().getIdUsuario();
                super.componentShown(e);
            }
        });
    }

}
