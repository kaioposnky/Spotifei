package net.spotifei.Infrastructure.Factories.MusicInfoComponent;

//imports
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.Music;
import net.spotifei.Models.Playlist;
import net.spotifei.Views.MainFrame;

import javax.swing.*;

public class MusicInfoPanelBuilder extends MusicInfoFactory implements MusicInfoBuild, MusicInfoPlaylistBuild{

    // Enumeração para representar os diferentes tipos de painéis que podem ser construídos.
    private enum PanelType {
        SEARCH,
        MOSTPLAYED,
        LIKEDORDISLIKED,
        PLAYLISTEDIT,
        PLAYLISTADD,
        MUSICSPLAYLIST,
        DELMUSIC,
        QUEUEMUSIC
    }
    private PanelType panelType;

    /**
     * Construtor do `MusicInfoPanelBuilder`. Ele chama o construtor da classe pai (`MusicInfoFactory`),
     * que é responsável por inicializar as dependências dos controladores.
     *
     * @param appContext O contexto da aplicação.
     * @param mainframe O frame principal da aplicação.
     */
    public MusicInfoPanelBuilder(AppContext appContext, MainFrame mainframe) {
        super(appContext, mainframe);
    }

    /**
     * Seleciona o tipo de painel para resultados de busca.
     */
    @Override
    public void selectSearchMusicInfoPanel() {this.panelType = PanelType.SEARCH;}

    /**
     * Seleciona o tipo de painel para músicas mais tocadas.
     */
    @Override
    public void selectMostPlayedMusicInfoPanel() {this.panelType = PanelType.MOSTPLAYED;}

    /**
     * Seleciona o tipo de painel para músicas curtidas ou descurtidas pelo usuário.
     */
    @Override
    public void selectLikedOrDislikedMusicInfoPanel(){this.panelType = PanelType.LIKEDORDISLIKED;}

    /**
     * Constrói e retorna o JPanel de informações da música com base no tipo de painel selecionado.
     *
     * @param music A música cujas informações serão exibidas no painel.
     * @return O JPanel construído.
     * @throws IllegalStateException Se o tipo de painel não foi selecionado antes da chamada.
     */
    @Override
    public JPanel buildPanel(Music music){
        if (panelType == null){
            throw new IllegalStateException("Você deve selecionar o tipo de painel para construir!");
        }
        return generatePanel(music);
    }

    /**
     * Seleciona o tipo de painel para músicas dentro da edição de playlist.
     *
     * @param playlist A playlist que está sendo editada.
     */
    @Override
    public void selectMusicFromPlaylistEditorPanel(Playlist playlist) {
        if(playlist == null) return;
        super.setPlaylist(playlist);
        this.panelType = PanelType.PLAYLISTEDIT;
    }

    /**
     * Seleciona o tipo de painel para músicas que podem ser adicionadas a uma playlist.
     *
     * @param playlist A playlist à qual a música será adicionada.
     */
    @Override
    public void selectMusicToPlaylistEditorPanel(Playlist playlist) {
        if (playlist == null) return;
        super.setPlaylist(playlist);
        this.panelType = PanelType.PLAYLISTADD;
    }

    /**
     * Seleciona o tipo de painel para exibição de músicas em uma playlist.
     */
    @Override
    public void selectMusicsFromPlaylist(){
        this.panelType = PanelType.MUSICSPLAYLIST;
    }

    /**
     * Seleciona o tipo de painel para que músicas podem ser deletadas.
     */
    @Override
    public void selectMusicInfoFromDeletePanel(){
        this.panelType = PanelType.DELMUSIC;
    }

    @Override
    public void selectMusicInfoFromQueuePanel() {
        this.panelType = PanelType.QUEUEMUSIC;
    }

    /**
     * Chama o método correspondente na classe pai (`MusicInfoFactory`) para gerar o JPanel.
     *
     * @param music A música para a qual o painel será gerado.
     * @return O JPanel configurado para o tipo selecionado.
     * @throws IllegalStateException Se um `panelType` inesperado for encontrado (deveria ser coberto pelos `case`s).
     */
    private JPanel generatePanel(Music music){
        switch (panelType){
            case SEARCH -> {
                return getSearchMusicInfoPanel(music);
            }
            case MOSTPLAYED -> {
                return getMostViewedMusicInfoPanel(music);
            }
            case LIKEDORDISLIKED -> {
                return getUserLikedOrDislikedMusicInfoPanel(music);
            }
            case PLAYLISTEDIT -> {
                return getMusicInfoFromPlaylistEditorPanel(music);
            }
            case PLAYLISTADD -> {
                return getSearchMusicInfoForPlaylistPanel(music);
            }
            case MUSICSPLAYLIST -> {
                return getMusicsFromPlaylist(music);
            }
            case DELMUSIC ->{
                return getMusicInfoFromDeletePanel(music);
            }
            case QUEUEMUSIC ->{
                return getMusicInfoFromQueuePanel(music);
            }
            default -> throw new IllegalStateException("Valor inesperado: " + panelType);
        }
    }

}
