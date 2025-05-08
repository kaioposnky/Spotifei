package net.spotifei.Infrastructure.Repository;

import net.spotifei.Infrastructure.JDBC.JDBCRepository;
import net.spotifei.Models.Music;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicRepository {

    private final JDBCRepository jdbcRepository = JDBCRepository.getInstance();

    private List<Music> searchMusicByName(String name) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("name", name);

            String sql = jdbcRepository.getQueryNamed("SearchMusicByName");
            List<Music> musics = jdbcRepository.queryProcedure(sql, params, new BeanListHandler<>(Music.class));

            return  musics;
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    public Music getNextMusicOnUserQueue(int userId) throws Exception {
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);

            String sql = jdbcRepository.getQueryNamed("GetNextMusicOnUserQueueByUserId");
            Music music = jdbcRepository.queryProcedure(sql, params, new BeanHandler<>(Music.class));

            return music;
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    public byte[] getMusicAsByteArray(int musicId) throws Exception {
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idMusica", musicId);

            String sql = jdbcRepository.getQueryNamed("GetMusicAudioByMusicId");
            byte[] musicAudio = jdbcRepository.queryProcedure(sql, params, new ScalarHandler<>());

            return musicAudio;
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    public List<Music> getMostLikedMusics() throws Exception{
        try{
            String sql = jdbcRepository.getQueryNamed("GetMostLikedMusics");
            List<Music> musics = jdbcRepository.queryProcedure(sql, new BeanListHandler<>(Music.class));

            return musics;
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    public List<Music> getMostDislikedMusics() throws Exception{
        try{
            String sql = jdbcRepository.getQueryNamed("GetMostDislikedMusics");
            List<Music> musics = jdbcRepository.queryProcedure(sql, new BeanListHandler<>(Music.class));

            return musics;
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    public int getTotalMusics() throws Exception{
        try{
            String sql = jdbcRepository.getQueryNamed("GetTotalMusics");
            int totalMusics = jdbcRepository.queryProcedure(sql, new BeanHandler<>(Integer.class));

            return totalMusics;
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }
}
