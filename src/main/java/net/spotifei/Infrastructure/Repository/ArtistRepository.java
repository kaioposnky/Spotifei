package net.spotifei.Infrastructure.Repository;

import net.spotifei.Infrastructure.JDBC.JDBCRepository;
import net.spotifei.Models.Artist;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.spotifei.Infrastructure.Logger.LoggerRepository.logInfo;

public class ArtistRepository {

    private final JDBCRepository jdbcRepository;

    public ArtistRepository(JDBCRepository jdbcRepository) {
        this.jdbcRepository = jdbcRepository;
    }


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

    public List<Artist> getArtistsByMusicId(int musicId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idMusica", musicId);

            String sql = jdbcRepository.getQueryNamed("GetArtistsByMusicId");
            List<Artist> artists = jdbcRepository.queryProcedure(sql, params, new BeanListHandler<>(Artist.class));

            return artists;
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }
}
