package net.spotifei.Infrastructure.Factories.MusicInfoComponent;

import net.spotifei.Models.Music;
import net.spotifei.Models.Playlist;

import javax.swing.*;

public interface MusicInfoBuild {
    void selectSearchMusicInfoPanel();
    void selectMostPlayedMusicInfoPanel();
    void selectLikedOrDislikedMusicInfoPanel();
    JPanel buildPanel(Music music);
}
