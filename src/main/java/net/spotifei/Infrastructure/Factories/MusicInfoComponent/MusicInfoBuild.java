package net.spotifei.Infrastructure.Factories.MusicInfoComponent;

import net.spotifei.Models.Music;

import javax.swing.*;

public interface MusicInfoBuild {
    void selectSearchMusicInfoPanel();
    void selectMostPlayedMusicInfoPanel();
    void selectLikedOrDislikedMusicInfoPanel();
    void selectMusicsFromPlaylist();
    void selectMusicInfoFromDeletePanel();
    void selectMusicInfoFromQueuePanel();
    JPanel buildPanel(Music music);
}
