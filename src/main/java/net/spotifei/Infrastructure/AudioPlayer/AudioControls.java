package net.spotifei.Infrastructure.AudioPlayer;

import net.spotifei.Models.Music;

public interface AudioControls {
    void selectMusic(Music music);
    void playMusic(byte[] musicAudioByteArray) throws InterruptedException;
    void pause() throws InterruptedException;
    void resume() throws InterruptedException;
    void seek(float musicTimePercentage) throws InterruptedException;
    void setVolume(float volume) throws InterruptedException;
    void shutdown() throws InterruptedException;
    boolean isPlaying();
}
