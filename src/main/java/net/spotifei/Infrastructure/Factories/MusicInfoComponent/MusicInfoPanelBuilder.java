package net.spotifei.Infrastructure.Factories.MusicInfoComponent;

import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.Music;
import net.spotifei.Models.Playlist;
import net.spotifei.Views.MainFrame;

import javax.swing.*;

public class MusicInfoPanelBuilder extends MusicInfoFactory implements MusicInfoBuild, MusicInfoPlaylistBuild{

    private enum PanelType {
        SEARCH,
        MOSTPLAYED,
        LIKEDORDISLIKED,
        PLAYLISTEDIT,
        PLAYLISTADD
    }
    private PanelType panelType;

    public MusicInfoPanelBuilder(AppContext appContext, MainFrame mainframe) {
        super(appContext, mainframe);
    }

    @Override
    public void selectSearchMusicInfoPanel() {this.panelType = PanelType.SEARCH;}

    @Override
    public void selectMostPlayedMusicInfoPanel() {this.panelType = PanelType.MOSTPLAYED;}

    @Override
    public void selectLikedOrDislikedMusicInfoPanel(){this.panelType = PanelType.LIKEDORDISLIKED;}

    @Override
    public JPanel buildPanel(Music music){
        super.setMusic(music);
        if (panelType == null){
            throw new IllegalStateException("Você deve selecionar o tipo de painel para construir!");
        }
        return generatePanel();
    }

    @Override
    public void selectMusicFromPlaylistEditorPanel(Playlist playlist) {
        if(playlist == null) return;
        super.setPlaylist(playlist);
        this.panelType = PanelType.PLAYLISTEDIT;
    }

    @Override
    public void selectMusicToPlaylistEditorPanel(Playlist playlist) {
        if (playlist == null) return;
        super.setPlaylist(playlist);
        this.panelType = PanelType.PLAYLISTADD;
    }

    private JPanel generatePanel(){
        switch (panelType){
            case SEARCH -> {
                return getSearchMusicInfoPanel();
            }
            case MOSTPLAYED -> {
                return getMostViewedMusicInfoPanel();
            }
            case LIKEDORDISLIKED -> {
                return getUserLikedOrDislikedMusicInfoPanel();
            }
            case PLAYLISTEDIT -> {
                return getMusicInfoFromPlaylistEditorPanel();
            }
            case PLAYLISTADD -> {
                return getSearchMusicInfoForPlaylistPanel();
            }
            default -> throw new IllegalStateException("Valor inesperado: " + panelType);
        }
    }

}
