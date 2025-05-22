package net.spotifei.Infrastructure.Container;

import java.util.Arrays;
import java.util.LinkedHashMap;

public class MusicCache {
    private final LinkedHashMap<Integer, byte[]> musicCache;

    /**
     * Construtor que gera a lista de hash
     */
    public MusicCache(){
        this.musicCache = new LinkedHashMap<>(10, 0.90f, true);
    }

    /**
     * Add music bytes to cache
     * @param musicId Id da música
     * @param musicBytes Bytes da música
     */
    public void addMusicBytesToCache(int musicId, byte[] musicBytes){
        musicCache.put(musicId, Arrays.copyOf(musicBytes, musicBytes.length));
    }

    /**
     * Get bytes from cache music
     * @param musicId Id da música
     * @return Bytes da música
     */
    public byte[] getMusicBytesFromCache(int musicId){
        return musicCache.get(musicId);
    }

    /**
     * Checa se o musicId está no cache
     * @param musicId Id da música
     * @return true se tiver no cache false se não tiver
     */
    public boolean cacheContains(int musicId){
        return musicCache.containsKey(musicId);
    }
}
