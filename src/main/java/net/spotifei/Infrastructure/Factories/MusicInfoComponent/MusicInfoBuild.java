package net.spotifei.Infrastructure.Factories.MusicInfoComponent;

import net.spotifei.Models.Music;

import javax.swing.*;

public interface MusicInfoBuild {
    void selectSearchMusicInfoPanel();
    void selectHistoryMusicInfoPanel();
    JPanel buildPanel(Music music);
}
