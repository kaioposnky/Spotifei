package net.spotifei.Infrastructure.AudioPlayer;

public interface AudioUpdateListener {
    void onMusicProgressUpdate(long musicTime, long musicDuration);
    void onMusicPlayingStatusUpdate(boolean isPlaying);
    void onEndOfMusic();
}
