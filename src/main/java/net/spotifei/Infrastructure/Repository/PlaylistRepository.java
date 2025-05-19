package net.spotifei.Infrastructure.Repository;

import net.spotifei.Infrastructure.JDBC.JDBCRepository;
import net.spotifei.Models.Music;
import net.spotifei.Models.Playlist;
import net.spotifei.Models.User;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistRepository {

    private final JDBCRepository jdbcRepository;

    public PlaylistRepository(JDBCRepository jdbcRepository) {
        this.jdbcRepository = jdbcRepository;
    }

    public void createPlaylist(Playlist playlist, int userId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("nome", playlist.getNome());
            params.put("userId", userId);

            String sql = jdbcRepository.getQueryNamed("CreatePlaylist");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    public void deletePlaylist(int playlistId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idPlaylist", playlistId);

            String sql = jdbcRepository.getQueryNamed("DeletePlaylist");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    public void updatePlaylist(Playlist playlist) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("nome", playlist.getNome());
            params.put("idPlaylist", playlist.getIdPlaylist());

            String sql = jdbcRepository.getQueryNamed("UpdatePlaylist");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    public void addMusicToPlaylist(Music music, int playlistId, int position) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idPlaylist", playlistId);
            params.put("idMusica", music.getIdMusica());
            params.put("position", position);

            String sql = jdbcRepository.getQueryNamed("AddMusicToPlaylist");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    public void removeMusicFromPlaylist(int musicId, int playlistId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idPlaylist", playlistId);
            params.put("idMusica", musicId);

            String sql = jdbcRepository.getQueryNamed("RemoveMusicFromPlaylist");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    public List<Music> getMusicsFromPlaylist(int playlistId) throws Exception {
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idPlaylist", playlistId);

            String sql = jdbcRepository.getQueryNamed("GetMusicsFromPlaylist");
            List<Music> musics = jdbcRepository.queryProcedure(sql,
                    params, new BeanListHandler<>(Music.class));

            return musics;
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    public Playlist getPlaylistById(int playlistId) throws Exception {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("idPlaylist", playlistId);

            String sql = jdbcRepository.getQueryNamed("GetPlaylistById");
            Playlist playlist = jdbcRepository.queryProcedure(
                    sql, params, new BeanHandler<>(Playlist.class));

            return playlist;
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    public int getLastPlaylistPosition(int playlistId) throws Exception{
        try {
            ScalarHandler<Integer> handler = new ScalarHandler<>();
            Map<String, Object> params = new HashMap<>();
            params.put("idPlaylist", playlistId);

            String sql = jdbcRepository.getQueryNamed("GetMaxPlaylistPosition");
            int lastPosition = jdbcRepository.queryProcedure(sql, params, handler);

            return lastPosition;
        } catch (Exception e){
            throw e;
        }
    }

    public List<Playlist> getPlaylistUser(int userId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);

            String sql = jdbcRepository.getQueryNamed("GetPlaylistUser");
            List<Playlist> playlists = jdbcRepository.queryProcedure(sql,
                    params, new BeanListHandler<>(Playlist.class));
            return playlists;

        } catch(Exception ex){
            throw ex;
        }
    }

    public void setPlaylistAsQueueForUser(int playlistId, int userId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idPlaylist", playlistId);
            params.put("idUser", userId);

            String sql = jdbcRepository.getQueryNamed("SetPlaylistAsQueueForUser");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception ex){
            throw ex;
        }
    }

}
