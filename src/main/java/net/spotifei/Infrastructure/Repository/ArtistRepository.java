package net.spotifei.Infrastructure.Repository;

import net.spotifei.Infrastructure.JDBC.JDBCRepository;
import net.spotifei.Models.Artist;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArtistRepository {

    private final JDBCRepository jdbcRepository = JDBCRepository.getInstance();

    public Artist getArtistById(int artistId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idArtista", artistId);

            String sql = jdbcRepository.getQueryNamed("GetArtistById");
            Artist artist = jdbcRepository.queryProcedure(sql, params, new BeanHandler<>(Artist.class));

            return artist;
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    public List<Artist> getArtistByMusicId(int musicId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idMusica", musicId);

            String sql = jdbcRepository.getQueryNamed("GetArtistById");
            List<Artist> artists = jdbcRepository.queryProcedure(sql, params, new BeanListHandler<>(Artist.class));

            return artists;
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }
}
