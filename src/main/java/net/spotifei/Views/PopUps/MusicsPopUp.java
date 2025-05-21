package net.spotifei.Views.PopUps;

//imports
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Infrastructure.Factories.MusicInfoComponent.MusicInfoPanelBuilder;
import net.spotifei.Models.Music;
import net.spotifei.Views.Components.MusicListComponent;
import net.spotifei.Views.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MusicsPopUp extends JDialog {
    private final MusicListComponent musicListComponent;
    private final String title;

    /**
     * Construtor da classe `MusicsPopUp`.
     *
     * @param appContext O contexto da aplicação (não usado diretamente aqui, mas passado para o builder).
     * @param mainFrame A janela principal que será a proprietária deste diálogo (tornando-o modal).
     * @param title O título a ser exibido na janela pop-up.
     * @param musics Uma lista de objetos `Music` a serem exibidos.
     * @param panelBuilder O builder usado para criar os painéis de informação para cada música na lista.
     */
    public MusicsPopUp(AppContext appContext, MainFrame mainFrame, String title, List<Music> musics, MusicInfoPanelBuilder panelBuilder){
        super(mainFrame, true);
        musicListComponent = new MusicListComponent(
                musics, panelBuilder);
        this.title = title;

        initComponents();
    }

    /**
     * Este método inicializa e configura todos os componentes visuais dentro do pop-up.
     */
    private void initComponents() {
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new java.awt.Color(48, 45, 45));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new java.awt.Font("Segoe UI Black", Font.BOLD, 26));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        musicListComponent.setBackground(new java.awt.Color(76, 59, 59));
        musicListComponent.setPreferredSize(new Dimension(800, 600));
        musicListComponent.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));
        musicListComponent.setAlignmentX(CENTER_ALIGNMENT);
        musicListComponent.renderMusics();

        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(musicListComponent);
        contentPanel.add(Box.createVerticalStrut(20));

        getContentPane().add(contentPanel);

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Getter para o `MusicListComponent`.
     *
     * @return A instância de `MusicListComponent` contida no pop-up.
     */
    public MusicListComponent getMusicListComponent() {
        return musicListComponent;
    }

}
