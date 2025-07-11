package net.spotifei.Infrastructure.Factories.MusicInfoComponent;

import net.spotifei.Models.Music;

import javax.swing.*;

//Interface de funções para as informação das musicas.
public interface MusicInfoBuild {
    void selectSearchMusicInfoPanel();
    void selectMostPlayedMusicInfoPanel();
    void selectLikedOrDislikedMusicInfoPanel();
    void selectMusicsFromPlaylist();
    void selectMusicInfoFromDeletePanel();
    void selectMusicInfoFromQueuePanel();
    JPanel buildPanel(Music music);
}
