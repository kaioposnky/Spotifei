package net.spotifei.Views.Panels.Admin;

//imports
import net.spotifei.Controller.AdminController;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Infrastructure.Factories.MusicInfoComponent.MusicInfoPanelBuilder;
import net.spotifei.Models.Music;
import net.spotifei.Views.Components.MusicListComponent;
import net.spotifei.Views.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
    private MusicListComponent likedMusicsList;
    private MusicListComponent dislikedMusicsList;

    /**
     * Construtor da classe `ADMEstatisticasPanel`.
     *
     * @param mainframe A instância da janela principal.
     * @param appContext O contexto da aplicação.
     */
    public ADMEstatisticasPanel(MainFrame mainframe, AppContext appContext) {
        this.appContext = appContext;
        this.mainframe = mainframe;
        this.adminController = appContext.getAdminController(this);
        initComponents();
    }


    /**
     * Método privado para inicializar e configurar os componentes visuais do painel.
     */
    private void initComponents() {

        setBackground(new java.awt.Color(35, 35, 35));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Estatísticas do sistema");
        titleLabel.setFont(new java.awt.Font("Segoe UI Black", 1, 36));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new BoxLayout(totalPanel, BoxLayout.X_AXIS));
        totalPanel.setOpaque(false);

        totalPanel.add(createTotalMusicsPanel());
        totalPanel.add(Box.createHorizontalStrut(30));
        totalPanel.add(createTotalUsersPanel());

        JButton btnGoBack = new JButton("Voltar");
        btnGoBack.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        btnGoBack.setAlignmentX(CENTER_ALIGNMENT);
        btnGoBack.setBackground(new Color(0, 109, 170));
        btnGoBack.setForeground(new Color(0,0,0));
        btnGoBack.addActionListener(event -> btn_voltar() );

        this.add(titleLabel);
        this.add(totalPanel);
        this.add(createMostLikedAndDislikedMusicsPanel());
        this.add(Box.createVerticalStrut(30));
        this.add(btnGoBack);
        this.add(Box.createVerticalStrut(30));
        addListeners();
    }

    /**
     * Cria e retorna um JPanel para exibir o total de músicas no sistema.
     *
     * @return JPanel contendo o rótulo e o número total de músicas.
     */
    private JPanel createTotalMusicsPanel(){
        JPanel totalMusics = new JPanel();
        totalMusics.setLayout(new BoxLayout(totalMusics, BoxLayout.Y_AXIS));
        MatteBorder lineBorder = new MatteBorder(5,5,5,5, Color.decode("#343a40"));
        EmptyBorder spaceBorder = new EmptyBorder(0,5,5,5);

        totalMusics.setBorder(BorderFactory.createCompoundBorder(lineBorder, spaceBorder));

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

    /**
     * Cria e retorna um JPanel para exibir o total de usuários no sistema.
     *
     * @return JPanel contendo o rótulo e o número total de usuários.
     */
    private JPanel createTotalUsersPanel(){
        JPanel totalUsers = new JPanel();
        totalUsers.setLayout(new BoxLayout(totalUsers, BoxLayout.Y_AXIS));
        MatteBorder lineBorder = new MatteBorder(5,5,5,5, Color.decode("#343a40"));
        EmptyBorder spaceBorder = new EmptyBorder(0,5,5,5);

        totalUsers.setBorder(BorderFactory.createCompoundBorder(lineBorder, spaceBorder));

        JLabel totalUsersText = new JLabel("Total de usuários no sistema");
        totalUsersText.setFont(new java.awt.Font("Segoe UI Black", 1, 16));
        totalUsersText.setAlignmentX(CENTER_ALIGNMENT);

        totalUsersNumber = new JLabel("0");
        totalUsersNumber.setFont(new java.awt.Font("Segoe UI Black", 1, 26));
        totalUsersNumber.setAlignmentX(CENTER_ALIGNMENT);

        totalUsers.add(totalUsersText);
        totalUsers.add(totalUsersNumber);

        return totalUsers;
    }

    /**
     * Cria e retorna um JPanel que contém as listas de músicas mais curtidas e menos curtidas.
     *
     * @return JPanel com as listas de músicas.
     */
    private JPanel createMostLikedAndDislikedMusicsPanel(){
        JPanel musicsPanel = new JPanel();
        musicsPanel.setLayout(new BoxLayout(musicsPanel, BoxLayout.X_AXIS));
        musicsPanel.setOpaque(false);
        musicsPanel.setBorder(new EmptyBorder(0,30,30,30));

        JPanel mostLikedWrapper = new JPanel();
        mostLikedWrapper.setLayout(new BoxLayout(mostLikedWrapper, BoxLayout.Y_AXIS));
        mostLikedWrapper.setOpaque(false);

        JLabel mostLikedMusicsLabel = new JLabel("Músicas com mais likes");
        mostLikedMusicsLabel.setFont(new java.awt.Font("Segoe UI Black", 1, 24));
        mostLikedMusicsLabel.setAlignmentX(CENTER_ALIGNMENT);

        MusicInfoPanelBuilder panelBuilder = new MusicInfoPanelBuilder(appContext, mainframe);
        panelBuilder.selectLikedOrDislikedMusicInfoPanel();
        likedMusicsList = new MusicListComponent(
                mostLikedMusics, panelBuilder
        );

        mostLikedWrapper.add(mostLikedMusicsLabel);
        mostLikedWrapper.add(likedMusicsList);

        JPanel mostDislikedWrapper = new JPanel();
        mostDislikedWrapper.setLayout(new BoxLayout(mostDislikedWrapper, BoxLayout.Y_AXIS));
        mostDislikedWrapper.setOpaque(false);

        JLabel mostDislikedMusicsLabel = new JLabel("Músicas com mais dislikes");
        mostDislikedMusicsLabel.setFont(new java.awt.Font("Segoe UI Black", 1, 24));
        mostDislikedMusicsLabel.setAlignmentX(CENTER_ALIGNMENT);

        dislikedMusicsList = new MusicListComponent(
                mostDislikedMusics, panelBuilder
        );

        mostDislikedWrapper.add(mostDislikedMusicsLabel);
        mostDislikedWrapper.add(dislikedMusicsList);

        musicsPanel.add(mostLikedWrapper);
        musicsPanel.add(Box.createHorizontalStrut(20));
        musicsPanel.add(mostDislikedWrapper);

        return musicsPanel;
    }

    /**
     * Adiciona um `ComponentListener` ao painel para carregar as estatísticas.
     */
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

    //Getters e Setters
    public void setTotalMusics(long total){
        totalMusicsNumber.setText(String.valueOf(total));
    }

    public void setTotalUsers(long total){
        totalUsersNumber.setText(String.valueOf(total));
    }

    public MainFrame getMainframe() {
        return mainframe;
    }

    /**
     * Define a lista de músicas mais curtidas.
     *
     * @param mostLikedMusics A lista de músicas mais curtidas.
     */
    public void setMostLikedMusics(List<Music> mostLikedMusics) {
        this.mostLikedMusics = mostLikedMusics;
        likedMusicsList.setMusics(mostLikedMusics);
        likedMusicsList.renderMusics();
    }

    /**
     * Define a lista de músicas menos curtidas.
     *
     * @param mostDislikedMusics A lista de músicas menos curtidas.
     */
    public void setMostDislikedMusics(List<Music> mostDislikedMusics) {
        this.mostDislikedMusics = mostDislikedMusics;
        dislikedMusicsList.setMusics(mostDislikedMusics);
        dislikedMusicsList.renderMusics();
    }

    /**
     * Método acionado pelo botão "Voltar".
     */
    public void btn_voltar(){
        mainframe.setPanel(MainFrame.ADMHOME_PANEL);
    }
}
