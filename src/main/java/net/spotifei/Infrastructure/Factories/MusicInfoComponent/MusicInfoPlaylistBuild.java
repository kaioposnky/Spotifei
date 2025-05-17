package net.spotifei.Infrastructure.Factories.MusicInfoComponent;

import net.spotifei.Models.Playlist;

public interface MusicInfoPlaylistBuild {
    void selectMusicFromPlaylistEditorPanel(Playlist playlist);
    void selectMusicToPlaylistEditorPanel(Playlist playlist);
}
