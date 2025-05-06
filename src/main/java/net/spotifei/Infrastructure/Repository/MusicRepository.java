package net.spotifei.Infrastructure.Repository;

import net.spotifei.Infrastructure.JDBC.JDBCRepository;
import net.spotifei.Models.Music;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import javax.sound.sampled.AudioInputStream;
import java.util.List;

public class MusicRepository {

    private final JDBCRepository jdbcRepository = JDBCRepository.getInstance();

    private List<Music> getMusicByName(String name) throws Exception{
        try{
            String sql = jdbcRepository.getQueryNamed("GetMusicByName");
            List<Music> musics = jdbcRepository.queryProcedure(sql, name, new BeanListHandler<>(Music.class));

            return  musics;
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    public Music getNextMusicOnUserQueue(int userId) throws Exception {
        try{
            String sql = jdbcRepository.getQueryNamed("GetNextMusicOnUserQueueByUserId");
            Music music = jdbcRepository.queryProcedure(sql, userId, new BeanHandler<>(Music.class));

            return music;
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    public byte[] getMusicAsByteArray(int musicId) throws Exception {
        try{
            String sql = jdbcRepository.getQueryNamed("GetMusicAudioByMusicId");
            byte[] musicAudio = jdbcRepository.queryProcedure(sql, musicId, new BeanHandler<>(byte[].class));

            return musicAudio;
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }
}
