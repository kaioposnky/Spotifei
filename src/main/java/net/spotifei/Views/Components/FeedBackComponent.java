package net.spotifei.Views.Components;

//imports
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
    private JLabel lblLikeNumber = null;
    private JButton btnDislike = null;
    private JButton btnLike = null;
    private Boolean isMusicLiked = null;
    private final MusicController musicController;
    private Music music;
    private boolean feedBackActionEnabled = true;

    /**
     * Construtor para o componente de feedback, pegando a música do contexto da aplicação.
     *
     * @param appContext O contexto da aplicação.
     * @param mainframe A instância do `MainFrame` (janela principal).
     */
    public FeedBackComponent(AppContext appContext, MainFrame mainframe) {
        this.music = appContext.getMusicContext();
        if (music == null) {
            this.isMusicLiked = null;
        } else{
            this.isMusicLiked = music.isGostou();
        }
        this.musicController = appContext.getMusicController(this, mainframe);
        initComponents();
    }

    /**
     * Construtor para o componente de feedback com uma música específica.
     *
     * @param appContext O contexto da aplicação.
     * @param mainframe A instância do `MainFrame` (janela principal).
     * @param music A música para a qual o feedback será exibido.
     */
    public FeedBackComponent(AppContext appContext, MainFrame mainframe, Music music) {
        this.music = music;
        this.isMusicLiked = music.isGostou();
        this.musicController = appContext.getMusicController(this, mainframe);
        this.feedBackActionEnabled = false;
        initComponents();
    }

    /**
     * Construtor completo para o componente de feedback, com música específica e controle de ativação das ações.
     *
     * @param appContext O contexto da aplicação.
     * @param mainframe A instância do `MainFrame` (janela principal).
     * @param music A música para a qual o feedback será exibido.
     * @param feedBackActionEnabled `true` para habilitar as ações de like/dislike, `false` para desabilitar.
     */
    public FeedBackComponent(AppContext appContext, MainFrame mainframe, Music music, boolean feedBackActionEnabled) {
        this.music = music;
        this.isMusicLiked = music.isGostou();
        this.musicController = appContext.getMusicController(this, mainframe);
        this.feedBackActionEnabled = feedBackActionEnabled;
        initComponents();
    }

    /**
     * Método para inicializar e configurar os componentes visuais do painel.
     */
    public void initComponents(){
        this.setOpaque(false);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        btnLike = new JButton();
        if(isMusicLiked != null && isMusicLiked){
            btnLike.setIcon(loadImageIcon("feedback/thumbsup_filled.png", 20, 20));
        } else{
            btnLike.setIcon(loadImageIcon("feedback/thumbsup_empty.png", 20, 20));
        }
        btnLike.setBorder(new EmptyBorder(0,0,0,5));
        btnLike.setFocusPainted(false);
        btnLike.setContentAreaFilled(false);
        btnLike.setOpaque(false);

        String likes = music != null ? String.valueOf(music.getLikes()) : "0";
        lblLikeNumber = new JLabel(likes);
        lblLikeNumber.setFont(new Font("Arial", Font.PLAIN, 14));

        btnDislike = new JButton();
        if (isMusicLiked != null && !isMusicLiked){
            btnDislike.setIcon(loadImageIcon("feedback/thumbsdown_filled.png", 20, 20));
        } else {
            btnDislike.setIcon(loadImageIcon("feedback/thumbsdown_empty.png", 20, 20));
        }
        btnDislike.setBorder(new EmptyBorder(0,0,0,5));
        btnDislike.setFocusPainted(false);
        btnDislike.setContentAreaFilled(false);
        btnDislike.setOpaque(false);

        if (feedBackActionEnabled){
            btnLike.addActionListener(event -> {handleLikeMusic();});
            btnDislike.addActionListener(event -> {handleDislikeMusic();});
        }

        String dislikes = music != null ? String.valueOf(music.getDislikes()) : "0";
        lblDisLikeNumber = new JLabel(dislikes);
        lblDisLikeNumber.setFont(new Font("Arial", Font.PLAIN, 14));

        this.add(btnLike);
        this.add(lblLikeNumber);
        this.add(Box.createHorizontalStrut(20));
        this.add(btnDislike);
        this.add(lblDisLikeNumber);
    }

    /**
     * Lida com a ação de "curtir" uma música.
     * Atualiza o estado visual do botão e os contadores, e notifica o controlador.
     */
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

    /**
     * Lida com a ação de "não curtir" uma música.
     * Atualiza o estado visual do botão e os contadores, e notifica o controlador.
     */
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

    /**
     * Getters e Setters.
     */
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

    /**
     * Atualiza a música associada a este componente de feedback e redesenha a UI.
     *
     * @param music A nova música a ser exibida.
     */
    public void setMusic(Music music) {
        if (music == null) {
            return;
        }

        this.music = music;
        this.isMusicLiked = music.isGostou();

        if (isMusicLiked != null && !isMusicLiked){
            btnDislike.setIcon(loadImageIcon("feedback/thumbsdown_filled.png", 20, 20));
        } else {
            btnDislike.setIcon(loadImageIcon("feedback/thumbsdown_empty.png", 20, 20));
        }

        if(isMusicLiked != null && isMusicLiked){
            btnLike.setIcon(loadImageIcon("feedback/thumbsup_filled.png", 20, 20));
        } else{
            btnLike.setIcon(loadImageIcon("feedback/thumbsup_empty.png", 20, 20));
        }

        lblLikeNumber.setText(String.valueOf(music.getLikes()));
        lblDisLikeNumber.setText(String.valueOf(music.getDislikes()));

        revalidate();
        repaint();
    }

}
