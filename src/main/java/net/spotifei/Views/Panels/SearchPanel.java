/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package net.spotifei.Views.Panels;

import javax.swing.*;

import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Views.Components.MusicListComponent;
import net.spotifei.Views.MainFrame;

import java.awt.*;

/**
 *
 * @author fengl
 */
public class SearchPanel extends javax.swing.JPanel {

    private final MainFrame mainframe;
    private final AppContext appContext;
    private MusicListComponent musicListComponent;
    private JTextField txt_pesquisar;

    public SearchPanel(MainFrame mainframe, AppContext appContext) {
        this.appContext = appContext;
        this.mainframe = mainframe;
        initComponents();
    }


    private void initComponents() {
        setBackground(new java.awt.Color(35, 35, 35));
        setMaximumSize(new java.awt.Dimension(1920, 1080));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Pesquisa");
        titleLabel.setFont(new java.awt.Font("Segoe UI Black", 1, 36));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel greetingLabel = new JLabel("O que vamos ouvir hoje?");
        greetingLabel.setFont(new java.awt.Font("Segoe UI Black", 1, 28));
        greetingLabel.setForeground(Color.white);
        greetingLabel.setAlignmentX(CENTER_ALIGNMENT);

        txt_pesquisar = new JTextField();
        txt_pesquisar.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        txt_pesquisar.setMinimumSize(new Dimension(300, 30));
        txt_pesquisar.setPreferredSize(new Dimension(300, 30));
        txt_pesquisar.setMaximumSize(new Dimension(300, 30));
        txt_pesquisar.setAlignmentX(CENTER_ALIGNMENT);

        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        btnPesquisar.setAlignmentX(CENTER_ALIGNMENT);
        btnPesquisar.addActionListener(this::bt_pesquisarActionPerformed);

        musicListComponent = new MusicListComponent(appContext, mainframe);
        musicListComponent.setBackground(new java.awt.Color(35, 35, 35));
        musicListComponent.setPreferredSize(new Dimension(800, 600));
        musicListComponent.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));
        musicListComponent.setAlignmentX(CENTER_ALIGNMENT);

        this.add(titleLabel);
        this.add(Box.createVerticalStrut(10));
        this.add(greetingLabel);
        this.add(Box.createVerticalStrut(20));
        this.add(txt_pesquisar);
        this.add(Box.createVerticalStrut(30));
        this.add(btnPesquisar);
        this.add(Box.createVerticalStrut(20));
        this.add(musicListComponent);
    }

//    private JPanel createLowerButtonsPanel(){
//        JPanel lowerButtonsPanel = new JPanel();
//        lowerButtonsPanel.setLayout(new );
//
//        JButton btn_search = new JButton("Pessquisar");
//
//    }

    private void bt_pesquisarActionPerformed(java.awt.event.ActionEvent evt) {
        appContext.getMusicController(this, mainframe).searchMusic();
    }

    private javax.swing.JButton bt_pesquisar;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txt_pesquisar_musica;

    public MainFrame getMainframe() {
        return mainframe;
    }

    public JTextField getTxt_pesquisar() {
        return txt_pesquisar;
    }

    public void setTxt_pesquisar(JTextField txt_pesquisar) {
        this.txt_pesquisar = txt_pesquisar;
    }

    public MusicListComponent getMusicListComponent() {
        return musicListComponent;
    }

    public void setMusicListComponent(MusicListComponent musicListComponent) {
        this.musicListComponent = musicListComponent;
    }
}
