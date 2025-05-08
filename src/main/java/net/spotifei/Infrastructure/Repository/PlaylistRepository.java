package net.spotifei.Infrastructure.Repository;

import net.spotifei.Infrastructure.JDBC.JDBCRepository;
import net.spotifei.Models.Music;
import net.spotifei.Models.Playlist;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistRepository {

    private final JDBCRepository jdbcRepository = JDBCRepository.getInstance();

    public void createPlaylist(Playlist playlist) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("nome", playlist.getNome());
            params.put("isPublic", playlist.isPublic());

            String sql = jdbcRepository.getQueryNamed("CreatePlaylist");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    public void deletePlaylist(int playlistId) throws Exception{
        try{
            String sql = jdbcRepository.getQueryNamed("DeletePlaylist");
            jdbcRepository.executeProcedure(sql, String.valueOf(playlistId));
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    public void updatePlaylist(Playlist playlist) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("nome", playlist.getNome());
            params.put("isPublic", playlist.isPublic());

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

    public void removeMusicFromPlaylist(Music music, int playlistId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idPlaylist", playlistId);
            params.put("idMusica", music.getIdMusica());

            String sql = jdbcRepository.getQueryNamed("RemoveMusicFromPlaylist");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    public List<Music> getMusicsFromPlaylist(int playlistId) throws Exception {
        try{
            String sql = jdbcRepository.getQueryNamed("GetMusicsFromPlaylist");
            List<Music> musics = jdbcRepository.queryProcedure(sql,
                    String.valueOf(playlistId), new BeanListHandler<>(Music.class));

            return musics;
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    public Playlist getPlaylistById(int playlistId) throws Exception {
        try {
            String sql = jdbcRepository.getQueryNamed("GetPlaylistById");
            Playlist playlist = jdbcRepository.queryProcedure(
                    sql, String.valueOf(playlistId), new BeanHandler<>(Playlist.class));

            return playlist;
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

}
