/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package net.spotifei.Views.Panels;

import javax.swing.*;

import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.Playlist;
import net.spotifei.Views.Components.PlaylistListComponent;
import net.spotifei.Views.MainFrame;

import java.awt.*;

/**
 *
 * @author fengl
 */
public class PlaylistPanel extends javax.swing.JPanel {

    private final MainFrame mainframe;
    private final AppContext appContext;
    private PlaylistListComponent playlistListComponent;
    private JTextField txt_criar;


    public PlaylistPanel(MainFrame mainframe, AppContext appContext) {
        this.appContext = appContext;
        this.mainframe = mainframe;
        initComponents();
    }

    private void initComponents(){
        setBackground(new java.awt.Color(35, 35, 35));
        setMaximumSize(new java.awt.Dimension(1920, 1080));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Playlist");
        titleLabel.setFont(new java.awt.Font("Segoe UI Black", 1, 36));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel greetingLabel = new JLabel("Vamos criar sua playlist?");
        greetingLabel.setFont(new java.awt.Font("Segoe UI Black", 1, 28));
        greetingLabel.setForeground(Color.white);
        greetingLabel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel createPlaylistPanel = new JPanel();
        createPlaylistPanel.setLayout(new BoxLayout(createPlaylistPanel, BoxLayout.X_AXIS));

        JLabel textPlaylistName = new JLabel("Digite o nome da playlist:");
        textPlaylistName.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        textPlaylistName.setForeground(Color.white);
        textPlaylistName.setOpaque(false);

        txt_criar = new JTextField();
        txt_criar.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        txt_criar.setMinimumSize(new Dimension(300, 30));
        txt_criar.setPreferredSize(new Dimension(300, 30));
        txt_criar.setMaximumSize(new Dimension(300, 30));
        txt_criar.setAlignmentX(CENTER_ALIGNMENT);

        createPlaylistPanel.add(textPlaylistName);
        createPlaylistPanel.add(Box.createHorizontalStrut(10));
        createPlaylistPanel.add(txt_criar);

        JButton bt_criar = new JButton("Criar!");
        bt_criar.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        bt_criar.setAlignmentX(CENTER_ALIGNMENT);
        bt_criar.addActionListener(this::bt_criarActionPerformed);

        JButton bt_mostrar = new JButton("Ver playlists");
        bt_mostrar.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        bt_mostrar.setAlignmentX(CENTER_ALIGNMENT);
        bt_mostrar.addActionListener(this::bt_mostrarActionPerformed);

        playlistListComponent = new PlaylistListComponent(appContext, mainframe);
        playlistListComponent.setBackground(new java.awt.Color(35, 35, 35));
        playlistListComponent.setPreferredSize(new Dimension(800, 600));
        playlistListComponent.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));
        playlistListComponent.setAlignmentX(CENTER_ALIGNMENT);

        this.add(Box.createVerticalStrut(10));
        this.add(titleLabel);
        this.add(Box.createVerticalStrut(10));
        this.add(greetingLabel);
        this.add(Box.createVerticalStrut(20));
        this.add(createPlaylistPanel);
        this.add(Box.createVerticalStrut(30));
        this.add(bt_criar);
        this.add(Box.createVerticalStrut(20));
        this.add(bt_mostrar);
        this.add(Box.createVerticalStrut(20));
        this.add(playlistListComponent);
    }

    private void bt_criarActionPerformed(java.awt.event.ActionEvent evt) {
        appContext.getPlayListController(this).createPlaylist();

    }

    private void bt_mostrarActionPerformed(java.awt.event.ActionEvent evt){
        appContext.getPlayListController(this).getUserPlaylists();
    }


    private javax.swing.JButton bt_criar;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txt_criar_playlist;
    private javax.swing.JButton bt_mostrar;

    public MainFrame getMainframe() {
        return mainframe;
    }

    public JTextField getTxt_criar() {
        return txt_criar;
    }

    public void setTxt_criar(JTextField txt_criar) {
        this.txt_criar = txt_criar;
    }

    public PlaylistListComponent getPlaylistListComponent() {
        return playlistListComponent;
    }

    public void setPlaylistListComponent(PlaylistListComponent playlistListComponent) {
        this.playlistListComponent = playlistListComponent;
    }

}