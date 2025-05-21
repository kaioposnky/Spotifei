package net.spotifei.Views;

//imports
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Views.Panels.Admin.*;
import net.spotifei.Views.Panels.Artist.RegisterMusicPanel;
import net.spotifei.Views.Panels.*;

import javax.swing.*;
import java.awt.*;

import static net.spotifei.Helpers.AssetsLoader.loadImageIcon;

public class MainFrame extends JFrame{

    public static final String LOGIN_PANEL = "Login";
    public static final String REGISTER_PANEL = "Register";
    public static final String SEARCH_PANEL = "Search";
    public static final String HISTORY_PANEL = "History";
    public static final String PLAYLIST_PANEL = "Playlist";
    public static final String ARTISTREGMUSIC_PANEL = "ArtistRegMusic";
    public static final String ADMHOME_PANEL = "ADMHome";
    public static final String ADMCADGENRE_PANEL = "ADMCadGenre";
    public static final String ADMREGMUSIC_PANEL = "ADMRegMusic";
    public static final String ADMESTATISTICS_PANEL = "ADMStatistics";
    public static final String ADMCONUSER_PANEL = "ADMConUser";
    public static final String ADMDELMUSIC_PANEL = "ADMDelMusic";
    public static final String ADMCADARTIST_PANEL = "ADMCadArtist";

    private CardLayout cardLayout;
    private JPanel cards;

    private final AppContext appContext;
    private final MusicPlayerPanel musicPlayerPanel;
    private final RedirectPanel redirectPanel;
    private final QueueMusicInfoPanel queueMusicInfoPanel;

    /**
     * Construtor da classe `MainFrame`.
     *
     * @param appContext O contexto da aplicação, que é usado para inicializar os painéis e registradores.
     */
    public MainFrame(AppContext appContext){
        this.appContext = appContext;
        this.musicPlayerPanel = new MusicPlayerPanel(this, this.appContext);
        this.appContext.registerMusicPlayerPanelListener(this.musicPlayerPanel);
        this.redirectPanel= new RedirectPanel(this,this.appContext);
        this.queueMusicInfoPanel = new QueueMusicInfoPanel(this, appContext);

        musicPlayerPanel.setVisible(false);
        redirectPanel.setVisible(false);
        queueMusicInfoPanel.setVisible(false);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        cards.add(new LoginPanel(this, this.appContext), LOGIN_PANEL);
        cards.add(new SearchPanel(this, this.appContext), SEARCH_PANEL);
        cards.add(new RegisterPanel(this, this.appContext), REGISTER_PANEL);
        cards.add(new HistoryPanel(this, this.appContext), HISTORY_PANEL);
        cards.add(new PlaylistPanel(this, this.appContext), PLAYLIST_PANEL);
        cards.add(new RegisterMusicPanel(this, this.appContext), ARTISTREGMUSIC_PANEL);
        cards.add(new ADMHomePanel(this, this.appContext), ADMHOME_PANEL);
        cards.add(new ADMCadGenre(this, this.appContext), ADMCADGENRE_PANEL);
        cards.add(new ADMRegisterMusicPanel(this, this.appContext), ADMREGMUSIC_PANEL);
        cards.add(new ADMEstatisticasPanel(this, this.appContext), ADMESTATISTICS_PANEL);
        cards.add(new ADMConsultUserPanel(this, this.appContext), ADMCONUSER_PANEL);
        cards.add(new ADMDelMusicPanel(this, this.appContext), ADMDELMUSIC_PANEL);
        cards.add(new ADMCadArtistPanel(this, this.appContext), ADMCADARTIST_PANEL);

        add(musicPlayerPanel, BorderLayout.SOUTH);
        add(redirectPanel,BorderLayout.WEST);
        add(queueMusicInfoPanel, BorderLayout.EAST);
        add(cards);

        setIconImage(loadImageIcon("spotifeiIcon.png").getImage());
        setMinimumSize(new Dimension(1000, 600));
        setPreferredSize(new Dimension(1000, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.DARK_GRAY);
        pack(); // tem que estar antes do setLocationRelativeTo
        setLocationRelativeTo(null);
        setPanel(LOGIN_PANEL);
    }

    /**
     * Alterna para o painel especificado pelo nome.
     *
     * @param panel O nome (constante String) do painel a ser exibido.
     */
    public void setPanel(String panel){
        cardLayout.show(cards, panel);
    }

    /**
     * Controla a visibilidade dos painéis de "HUD".
     *
     * @param visible `true` para tornar os painéis visíveis, `false` para torná-los invisíveis.
     */
    public void setHUDVisible(boolean visible){
        musicPlayerPanel.setVisible(visible);
        redirectPanel.setVisible(visible);
        queueMusicInfoPanel.setVisible(visible);
    }
   
}
