package net.spotifei.Views.Panels.Admin;

import javax.swing.*;
import javax.swing.border.MatteBorder;

import net.spotifei.Controller.AdminController;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Infrastructure.Factories.MusicInfoComponent.MusicInfoPanelBuilder;
import net.spotifei.Models.Music;
import net.spotifei.Views.Components.MusicListComponent;
import net.spotifei.Views.MainFrame;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;

public class ADMEstatisticasPanel extends javax.swing.JPanel {

    private final MainFrame mainframe;
    private final AppContext appContext;
    private final AdminController adminController;
    private JLabel totalMusicsNumber;
    private JLabel totalUsersNumber;
    private List<Music> mostLikedMusics = new ArrayList<>();
    private List<Music> mostDislikedMusics = new ArrayList<>();

    public ADMEstatisticasPanel(MainFrame mainframe, AppContext appContext) {
        this.appContext = appContext;
        this.mainframe = mainframe;
        this.adminController = appContext.getAdminController(this);
        initComponents();
    }
    
    private void initComponents() {

        setBackground(new java.awt.Color(35, 35, 35));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//        setBorder(new EmptyBorder(10,10,10,10));

        JLabel titleLabel = new JLabel("Estatísticas do sistema");
        titleLabel.setFont(new java.awt.Font("Segoe UI Black", 1, 36));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new BoxLayout(totalPanel, BoxLayout.X_AXIS));
        totalPanel.setOpaque(false);

        totalPanel.add(createTotalMusicsPanel());
        totalPanel.add(createTotalUsersPanel());

        this.add(titleLabel);
        this.add(totalPanel);
        this.add(createMostLikedAndDislikedMusicsPanel());
    }

    private JPanel createTotalMusicsPanel(){
        JPanel totalMusics = new JPanel();
        totalMusics.setLayout(new BoxLayout(totalMusics, BoxLayout.Y_AXIS));
        totalMusics.setBorder(new MatteBorder(5,5,5,5, Color.decode("#343a40")));

        JLabel totalMusicsText = new JLabel("Total de músicas no sistema");
        totalMusicsText.setFont(new java.awt.Font("Segoe UI Black", 1, 16));
        totalMusicsText.setAlignmentX(CENTER_ALIGNMENT);

        totalMusicsNumber = new JLabel("0");
        totalMusicsNumber.setFont(new java.awt.Font("Segoe UI Black", 1, 26));
        totalMusicsNumber.setAlignmentX(CENTER_ALIGNMENT);

        totalMusics.add(totalMusicsText);
        totalMusics.add(totalMusicsNumber);

        return totalMusics;
    }

    private JPanel createTotalUsersPanel(){
        JPanel totalMusics = new JPanel();
        totalMusics.setLayout(new BoxLayout(totalMusics, BoxLayout.Y_AXIS));
        totalMusics.setBorder(new MatteBorder(5,5,5,5, Color.decode("#343a40")));

        JLabel totalUsersText = new JLabel("Total de usuários no sistema");
        totalUsersText.setFont(new java.awt.Font("Segoe UI Black", 1, 16));
        totalUsersText.setAlignmentX(CENTER_ALIGNMENT);

        totalUsersNumber = new JLabel("0");
        totalUsersNumber.setFont(new java.awt.Font("Segoe UI Black", 1, 26));
        totalUsersNumber.setAlignmentX(CENTER_ALIGNMENT);

        totalMusics.add(totalUsersText);
        totalMusics.add(totalUsersNumber);

        return totalMusics;
    }

    private JPanel createMostLikedAndDislikedMusicsPanel(){
        JPanel musicsPanel = new JPanel();
        musicsPanel.setLayout(new BoxLayout(musicsPanel, BoxLayout.X_AXIS));
        musicsPanel.setOpaque(false);

        MusicInfoPanelBuilder panelBuilder = new MusicInfoPanelBuilder(appContext, mainframe);
        panelBuilder.selectLikedOrDislikedMusicInfoPanel();
        MusicListComponent likedMusicsList = new MusicListComponent(
                appContext, mainframe, mostLikedMusics, panelBuilder
        );

        MusicListComponent dislikedMusicsList = new MusicListComponent(
                appContext, mainframe, mostDislikedMusics, panelBuilder
        );


        musicsPanel.add(likedMusicsList);
        musicsPanel.add(Box.createHorizontalStrut(20));
        musicsPanel.add(dislikedMusicsList);

        return musicsPanel;
    }

    private void addListeners(){
        this.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentShown(ComponentEvent e) {
                adminController.loadSystemStatistics();
                super.componentShown(e);
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                mostDislikedMusics.clear();
                mostLikedMusics.clear();
                super.componentHidden(e);
            }
        });
    }

    public void setTotalMusics(int total){
        totalMusicsNumber.setText(String.valueOf(total));
    }

    public void setTotalUsers(int total){
        totalUsersNumber.setText(String.valueOf(total));
    }

    public MainFrame getMainframe() {
        return mainframe;
    }

    public void setMostLikedMusics(List<Music> mostLikedMusics) {
        this.mostLikedMusics = mostLikedMusics;
        repaint();
        revalidate();
    }

    public void setMostDislikedMusics(List<Music> mostDislikedMusics) {
        this.mostDislikedMusics = mostDislikedMusics;

        repaint();
        revalidate();
    }
}
