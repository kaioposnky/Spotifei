package net.spotifei.Infrastructure.Factories.MusicInfoComponent;

import net.spotifei.Models.Music;

import javax.swing.*;

//Interface de funções para as informação das musicas.
public interface MusicInfoBuild {
    void selectSearchMusicInfoPanel();
    void selectMostPlayedMusicInfoPanel();
    void selectLikedOrDislikedMusicInfoPanel();
    void selectMusicsFromPlaylist();
    void selectMusicDeleted();
    JPanel buildPanel(Music music);
}
