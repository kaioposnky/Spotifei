package net.spotifei.Infrastructure.AudioPlayer;

import net.spotifei.Models.Music;

//Interface das funções de update de audio do AudioPlayer
public interface AudioUpdateListener {
    void onSelectMusic(Music music);
    void onMusicProgressUpdate(long musicTime, long musicDuration);
    void onMusicPlayingStatusUpdate(boolean isPlaying);
    void onEndOfMusic();
}
