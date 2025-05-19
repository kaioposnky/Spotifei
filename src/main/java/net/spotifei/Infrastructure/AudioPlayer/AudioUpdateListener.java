package net.spotifei.Infrastructure.AudioPlayer;

import net.spotifei.Models.Music;

public interface AudioUpdateListener {
    void onSelectMusic(Music music);
    void onMusicProgressUpdate(long musicTime, long musicDuration);
    void onMusicPlayingStatusUpdate(boolean isPlaying);
    void onEndOfMusic();
}
