package net.spotifei.Views.Components;

import net.spotifei.Infrastructure.Factories.MusicInfoComponent.MusicInfoPanelBuilder;
import net.spotifei.Models.Music;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MusicListComponent extends JPanel {
    private List<Music> musics = new ArrayList<>();
    private JPanel musicsInfoPanel;
    private final MusicInfoPanelBuilder panelBuilder;

    public MusicListComponent(MusicInfoPanelBuilder panelBuilder){
        this.panelBuilder = panelBuilder;
        initComponents();
    }

    public MusicListComponent(List<Music> musics, MusicInfoPanelBuilder panelBuilder){
        this.panelBuilder = panelBuilder;
        initComponents();
        setMusics(musics);
    }

    private void initComponents(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        musicsInfoPanel = new JPanel();
        musicsInfoPanel.setLayout(new BoxLayout(musicsInfoPanel, BoxLayout.Y_AXIS));
        musicsInfoPanel.setBackground(Color.decode("#121212"));

        JScrollPane musicsScrollPanel = new JScrollPane(musicsInfoPanel);
        musicsScrollPanel.getViewport().setBackground(Color.decode("#121212"));

        JScrollBar verticalScrollBar = musicsScrollPanel.getVerticalScrollBar();
        verticalScrollBar.setUI(new SpotifyLikeScrollBarUI());
        verticalScrollBar.setUnitIncrement(20);

        add(musicsScrollPanel);

        renderMusics();
    }

    public void renderMusics(){
        musicsInfoPanel.removeAll();

        if(musics == null || musics.isEmpty()){
            JLabel label = new JLabel("Nenhuma música encontrada!");
            label.setFont(new Font("Arial", Font.BOLD, 14));
            label.setForeground(Color.decode("#aeaeae"));
            label.setHorizontalAlignment(JLabel.CENTER);
            musicsInfoPanel.add(label);
        } else{
            for (Music music : musics) {
                musicsInfoPanel.add(panelBuilder.buildPanel(music));
            }
        }
    }

    public List<Music> getMusics() {
        return musics;
    }

    public void setMusics(List<Music> musics) {
        if (musics == null) return; // return para evitar nullpointerexception la na frente
        this.musics = musics;
        renderMusics();
    }

    public void addMusic(Music music){
        if (music == null) return;
        this.musics.add(music);
        renderMusics();
    }

    public void removeMusic(Music music){
        if (music == null) return;
        this.musics.remove(music);
        renderMusics();
    }
}
