package net.spotifei.Views.Components;

import net.spotifei.Controller.MusicController;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.Music;
import net.spotifei.Views.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

import static net.spotifei.Helpers.AssetsLoader.loadImageIcon;

public class FeedBackComponent extends JPanel {
    private JLabel lblDisLikeNumber = null;
    private JLabel lblLikeNumber= null;
    private JButton btnDislike= null;
    private JButton btnLike= null;
    private Boolean isMusicLiked = null;
    private final MusicController musicController;
    private final Music music;
    private final boolean showPlayButton;

    public FeedBackComponent(AppContext appContext, MainFrame mainframe, Music music) {
        this.music = music;
        if (music == null) {
            this.isMusicLiked = null;
        } else{
            this.isMusicLiked = music.isGostou();
        }
        this.showPlayButton = false;
        this.musicController = appContext.getMusicController(this, mainframe);
        initComponents();
    }
    public FeedBackComponent(AppContext appContext, MainFrame mainframe, Music music, boolean showPlayButton) {
        this.music = music;
        this.isMusicLiked = music.isGostou();
        this.showPlayButton = showPlayButton;
        this.musicController = appContext.getMusicController(this, mainframe);
        initComponents();
    }

    public void initComponents(){
        this.setOpaque(false);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        btnLike = new JButton();
        btnLike.setIcon(loadImageIcon("feedback/thumbsup_empty.png", 20, 20));
        btnLike.addActionListener(event -> {handleLikeMusic();});
        btnLike.setBorder(new EmptyBorder(0,0,0,5));
        btnLike.setFocusPainted(false);
        btnLike.setContentAreaFilled(false);
        btnLike.setOpaque(false);

        lblLikeNumber = new JLabel("0");
        lblLikeNumber.setFont(new Font("Arial", Font.PLAIN, 14));

        btnDislike = new JButton();
        btnDislike.setIcon(loadImageIcon("feedback/thumbsdown_empty.png", 20, 20));
        btnDislike.addActionListener(event -> {handleDislikeMusic();});
        btnDislike.setBorder(new EmptyBorder(0,0,0,5));
        btnDislike.setFocusPainted(false);
        btnDislike.setContentAreaFilled(false);
        btnDislike.setOpaque(false);

        lblDisLikeNumber = new JLabel("0");
        lblDisLikeNumber.setFont(new Font("Arial", Font.PLAIN, 14));

        this.add(btnLike);
        this.add(lblLikeNumber);
        this.add(Box.createHorizontalStrut(20));
        this.add(btnDislike);
        this.add(lblDisLikeNumber);
    }

    public void handleLikeMusic(){
        if (isMusicLiked == null){
            isMusicLiked = true;
            btnLike.setIcon(loadImageIcon("feedback/thumbsup_filled.png", 20, 20));
            lblLikeNumber.setText((""+ (Integer.parseInt(lblLikeNumber.getText()) + 1) ));
        }
        else if(!isMusicLiked){
            isMusicLiked = true;
            btnLike.setIcon(loadImageIcon("feedback/thumbsup_filled.png", 20, 20));
            lblLikeNumber.setText((""+ (Integer.parseInt(lblLikeNumber.getText()) + 1) ));

            btnDislike.setIcon(loadImageIcon("feedback/thumbsdown_empty.png", 20, 20));
            lblDisLikeNumber.setText((""+ (Integer.parseInt(lblDisLikeNumber.getText()) - 1) ));
        }
        else if(isMusicLiked){
            isMusicLiked = null;
            btnLike.setIcon(loadImageIcon("feedback/thumbsup_empty.png", 20, 20));
            lblLikeNumber.setText((""+ (Integer.parseInt(lblLikeNumber.getText()) - 1) ));
        }
        musicController.insertUserRating();
    }

    public void handleDislikeMusic(){
        if (isMusicLiked == null){
            isMusicLiked = false;
            btnDislike.setIcon(loadImageIcon("feedback/thumbsdown_filled.png", 20, 20));
            lblDisLikeNumber.setText((""+ (Integer.parseInt(lblDisLikeNumber.getText()) + 1) ));
        }
        else if(!isMusicLiked){
            isMusicLiked = null;
            btnDislike.setIcon(loadImageIcon("feedback/thumbsdown_empty.png", 20, 20));
            lblDisLikeNumber.setText((""+ (Integer.parseInt(lblDisLikeNumber.getText()) - 1) ));
        }
        else if(isMusicLiked){
            isMusicLiked = false;
            btnDislike.setIcon(loadImageIcon("feedback/thumbsdown_filled.png", 20, 20));
            lblDisLikeNumber.setText((""+ (Integer.parseInt(lblDisLikeNumber.getText()) + 1) ));

            btnLike.setIcon(loadImageIcon("feedback/thumbsup_empty.png", 20, 20));
            lblLikeNumber.setText((""+ (Integer.parseInt(lblLikeNumber.getText()) - 1) ));
        }
        musicController.insertUserRating();
    }

    public JLabel getLblDisLikeNumber() {
        return lblDisLikeNumber;
    }

    public void setLblDisLikeNumber(JLabel lblDisLikeNumber) {
        this.lblDisLikeNumber = lblDisLikeNumber;
    }

    public JLabel getLblLikeNumber() {
        return lblLikeNumber;
    }

    public void setLblLikeNumber(JLabel lblLikeNumber) {
        this.lblLikeNumber = lblLikeNumber;
    }

    public JButton getBtnDislike() {
        return btnDislike;
    }

    public void setBtnDislike(JButton btnDislike) {
        this.btnDislike = btnDislike;
    }

    public JButton getBtnLike() {
        return btnLike;
    }

    public void setBtnLike(JButton btnLike) {
        this.btnLike = btnLike;
    }

    public Boolean isMusicLiked() {
        return isMusicLiked;
    }

    public Music getMusic() {
        return music;
    }
}
