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
        PLAYLISTADD,
        MUSICSPLAYLIST,
        DELMUSIC
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
        if (panelType == null){
            throw new IllegalStateException("Você deve selecionar o tipo de painel para construir!");
        }
        return generatePanel(music);
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

    @Override
    public void selectMusicsFromPlaylist(){
        this.panelType = PanelType.MUSICSPLAYLIST;
    }

    @Override
    public void selectMusicDeleted(){
        this.panelType = PanelType.DELMUSIC;
    }

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
                return getMusicDeleted(music);
            }
            default -> throw new IllegalStateException("Valor inesperado: " + panelType);
        }
    }

}
