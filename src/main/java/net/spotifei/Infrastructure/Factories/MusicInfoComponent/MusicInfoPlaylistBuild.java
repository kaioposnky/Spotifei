package net.spotifei.Infrastructure.Factories.MusicInfoComponent;

import net.spotifei.Models.Playlist;

//Interface de funções para as musicas dentro de uma playlist
public interface MusicInfoPlaylistBuild {
    void selectMusicFromPlaylistEditorPanel(Playlist playlist);
    void selectMusicToPlaylistEditorPanel(Playlist playlist);
}
