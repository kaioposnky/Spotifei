package net.spotifei.Views.Panels;

import javax.swing.*;

import net.spotifei.Controller.HistoryController;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Views.MainFrame;
import net.spotifei.Views.PopUps.MusicsPopUp;

import java.awt.*;

/**
 *
 * @author fengl
 */
public class HistoryPanel extends javax.swing.JPanel {
    private final MainFrame mainframe;
    private final AppContext appContext;
    private final HistoryController historyController;
    private MusicsPopUp musicsPopUp;

    public HistoryPanel(MainFrame mainframe, AppContext appContext) {
        this.appContext = appContext;
        this.mainframe = mainframe;
        this.historyController = appContext.getHistoryController(this);
        initComponents();
    }

    private void initComponents(){
        setBackground(new Color(35, 35, 35));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("HISTÓRICO");
        titleLabel.setFont(new java.awt.Font("Segoe UI Black", 1, 36));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel greetingLabel = new JLabel("Aqui está seu histórico!");
        greetingLabel.setFont(new java.awt.Font("Segoe UI Black", 1, 28));
        greetingLabel.setForeground(Color.white);
        greetingLabel.setAlignmentX(CENTER_ALIGNMENT);

        JButton bt_sair = new JButton("Deslogar");
        bt_sair.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        bt_sair.setAlignmentX(CENTER_ALIGNMENT);
        bt_sair.addActionListener(this::bt_sairActionPerformed);
        bt_sair.setMaximumSize(new Dimension(200, 50));
        bt_sair.setBackground(Color.BLACK);
        bt_sair.setForeground(Color.white);

        this.add(Box.createVerticalGlue());
        this.add(titleLabel);
        this.add(Box.createVerticalStrut(50));
        this.add(greetingLabel);
        this.add(Box.createVerticalStrut(30));
        JPanel buttonsPanel = historybuttons();
        this.add(buttonsPanel);
        this.add(Box.createVerticalStrut(30));
        this.add(bt_sair);
        this.add(Box.createVerticalGlue());


        setMaximumSize(new Dimension(1920, 1080));
        setMinimumSize(new Dimension(800, 600));
    }

    private JPanel historybuttons(){
        JPanel historyButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        historyButton.setBackground(new Color(35, 35, 35));

        Dimension buttonSize = new Dimension(150, 150);

        JButton bt_top10 = new JButton("TOP 10");
        bt_top10.setFont(new java.awt.Font("Segoe UI Black", 1, 14));
        bt_top10.setAlignmentX(CENTER_ALIGNMENT);
        bt_top10.addActionListener(this::bt_top10ActionPerformed);
        bt_top10.setPreferredSize(buttonSize);

        JButton bt_curtidas = new JButton("CURTIDAS");
        bt_curtidas.setFont(new java.awt.Font("Segoe UI Black", 1, 14));
        bt_curtidas.setAlignmentX(CENTER_ALIGNMENT);
        bt_curtidas.addActionListener(this::bt_curtidasActionPerformed);
        bt_curtidas.setPreferredSize(buttonSize);

        JButton bt_deslikes = new JButton("DESCURTIDAS");
        bt_deslikes.setFont(new java.awt.Font("Segoe UI Black", 1, 14));
        bt_deslikes.setAlignmentX(CENTER_ALIGNMENT);
        bt_deslikes.addActionListener(this::bt_deslikesActionPerformed);
        bt_deslikes.setPreferredSize(buttonSize);

        historyButton.add(bt_top10);
        historyButton.add(Box.createHorizontalStrut(20));
        historyButton.add(bt_curtidas);
        historyButton.add(Box.createHorizontalStrut(30));
        historyButton.add(bt_deslikes);
        historyButton.add(Box.createHorizontalStrut(20));

        return historyButton;
    }

    private void bt_top10ActionPerformed(java.awt.event.ActionEvent evt) {
        historyController.showUserMostSearchedMusics();

    }

    private void bt_curtidasActionPerformed(java.awt.event.ActionEvent evt) {
        historyController.showUserLikedMusics();

    }
    private void bt_deslikesActionPerformed(java.awt.event.ActionEvent evt) {
        historyController.showUserDislikedMusics();

    }
    private void bt_sairActionPerformed(java.awt.event.ActionEvent evt) {
        mainframe.setHUDVisible(false);
        if (appContext.getAudioPlayerWorker().isPlaying()) {
            appContext.getMusicService().pauseMusic();
        }
        mainframe.setPanel(MainFrame.LOGIN_PANEL);

    }


    private javax.swing.JButton bt_top10;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel greetingLabel;
    private javax.swing.JButton bt_curtidas;
    private javax.swing.JButton bt_deslikes;


    public MainFrame getMainframe() {
        return mainframe;
    }

    public AppContext getAppContext() {
        return appContext;
    }

    public HistoryController getHistoryController() {
        return historyController;
    }

    public MusicsPopUp getMusicsPopUp() {
        return musicsPopUp;
    }

    public void setMusicsPopUp(MusicsPopUp musicsPopUp) {
        this.musicsPopUp = musicsPopUp;
    }

    public JButton getBt_top10() {
        return bt_top10;
    }

    public void setBt_top10(JButton bt_top10) {
        this.bt_top10 = bt_top10;
    }

    public JLabel getTitleLabel() {
        return titleLabel;
    }

    public void setTitleLabel(JLabel titleLabel) {
        this.titleLabel = titleLabel;
    }

    public JLabel getGreetingLabel() {
        return greetingLabel;
    }

    public void setGreetingLabel(JLabel greetingLabel) {
        this.greetingLabel = greetingLabel;
    }

    public JButton getBt_curtidas() {
        return bt_curtidas;
    }

    public void setBt_curtidas(JButton bt_curtidas) {
        this.bt_curtidas = bt_curtidas;
    }

    public JButton getBt_deslikes() {
        return bt_deslikes;
    }

    public void setBt_deslikes(JButton bt_deslikes) {
        this.bt_deslikes = bt_deslikes;
    }
}
