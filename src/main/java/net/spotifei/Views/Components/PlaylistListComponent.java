package net.spotifei.Views.Components;

//imports
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.Playlist;
import net.spotifei.Views.MainFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class PlaylistListComponent extends JPanel{
    private static final Logger log = LoggerFactory.getLogger(PlaylistListComponent.class);
    private List<Playlist> playlists = new ArrayList<>();
    private JPanel playlistInfoComponent;
    private final AppContext appContext;
    private final MainFrame mainframe;

    /**
     * Construtor padrão do componente `PlaylistListComponent`.
     * Inicializa o componente sem uma lista inicial de playlists.
     *
     * @param appContext O contexto da aplicação.
     * @param mainframe A instância do `MainFrame`.
     */
    public PlaylistListComponent(AppContext appContext, MainFrame mainframe){
        this.appContext = appContext;
        this.mainframe = mainframe;
        initComponents();
    }

    /**
     * Construtor do componente `PlaylistListComponent` que aceita uma lista inicial de playlists.
     *
     * @param playlists A lista inicial de objetos `Playlist` a serem exibidos.
     * @param appContext O contexto da aplicação.
     * @param mainframe A instância do `MainFrame`.
     */
    public PlaylistListComponent(List<Playlist> playlists, AppContext appContext, MainFrame mainframe){
        this.appContext = appContext;
        this.mainframe = mainframe;
        initComponents();
        setPlaylists(playlists);
    }

    /**
     * Método privado para inicializar e configurar os componentes visuais do painel.
     */
    private void initComponents(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        playlistInfoComponent = new JPanel();
        playlistInfoComponent.setLayout(new BoxLayout(playlistInfoComponent, BoxLayout.Y_AXIS));
        playlistInfoComponent.setBackground(Color.decode("#121212"));

        JScrollPane playlistsScrollPanel = new JScrollPane(playlistInfoComponent);
        playlistsScrollPanel.getViewport().setBackground(Color.decode("#121212"));

        JScrollBar verticalScrollBar = playlistsScrollPanel.getVerticalScrollBar();
        verticalScrollBar.setUI(new SpotifyLikeScrollBarUI());
        verticalScrollBar.setUnitIncrement(20);

        add(playlistsScrollPanel);

        renderPlaylists();
    }

    /**
     * Renderiza a lista de playlists no painel.
     */
    public void renderPlaylists(){
        playlistInfoComponent.removeAll();

        if(playlists == null || playlists.isEmpty()){
            JLabel label = new JLabel("Nenhuma playlist encontrada!");
            label.setFont(new Font("Arial", Font.BOLD, 14));
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setForeground(Color.decode("#aeaeae"));
            playlistInfoComponent.add(label);
        } else{
            for(Playlist playlist : playlists){
                playlistInfoComponent.add(new PlaylistInfoComponent(playlist, appContext, mainframe));
            }
        }
        updateUI();
    }

    /**
     * Retorna a lista atual de playlists sendo exibida pelo componente.
     *
     * @return A `List<Playlist>` atual.
     */
    public List<Playlist> getPlaylists(){
        return playlists;
    }

    /**
     * Define uma nova lista de playlists para o componente.
     *
     * @param playlists A nova `List<Playlist>` a ser exibida.
     */
    public void setPlaylists(List<Playlist> playlists){
        if (playlists == null) return;
        this.playlists = playlists;
        renderPlaylists();
    }

    /**
     * Adiciona uma única playlist à lista existente.
     *
     * @param playlist A playlist a ser adicionada.
     */
    public void addPlaylist(Playlist playlist){
        if(playlist == null) return;
        this.playlists.add(playlist);
        renderPlaylists();
    }

    /**
     * Remove uma única playlist da lista existente.
     *
     * @param playlist A playlist a ser removida.
     */
    public void removePlaylist(Playlist playlist){
        if(playlist == null) return;
        this.playlists.remove(playlist);
        renderPlaylists();
    }
}
