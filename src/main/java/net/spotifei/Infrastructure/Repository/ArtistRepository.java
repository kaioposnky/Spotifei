package net.spotifei.Infrastructure.Repository;

import net.spotifei.Infrastructure.JDBC.JDBCRepository;
import net.spotifei.Models.Artist;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.bytedeco.libfreenect._freenect_context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArtistRepository {

    private final JDBCRepository jdbcRepository;

    public ArtistRepository(JDBCRepository jdbcRepository) {
        this.jdbcRepository = jdbcRepository;
    }

    public void createArtist(Artist artist) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("nome", artist.getNome());
            params.put("sobrenome", artist.getSobrenome());
            params.put("email", artist.getEmail());
            params.put("telefone", artist.getTelefone());
            params.put("senha", artist.getSenha());
            params.put("nomeArtistico", artist.getNomeArtistico());

            String sql = jdbcRepository.getQueryNamed("CreateArtist");
            jdbcRepository.executeProcedure(sql, params);

        } catch (Exception e){
            throw e;
        }
    }

    public void deleteArtist(int artistId) throws Exception{
        try {
            String sql = jdbcRepository.getQueryNamed("DeleteArtist");
            jdbcRepository.executeProcedure(sql, String.valueOf(artistId));
        } catch (Exception e){
            throw e;
        }
    }

    public void setUserArtist(int userId, String artistName) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idUser", userId);
            params.put("artisticName", artistName);

            String sql = jdbcRepository.getQueryNamed("SetUserArtist");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception e){
            throw e;
        }
    }

    public Artist getArtistById(int artistId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idArtista", artistId);

            String sql = jdbcRepository.getQueryNamed("GetArtistById");
            Artist artist = jdbcRepository.queryProcedure(sql, params, new BeanHandler<>(Artist.class));

            return artist;
        } catch (Exception e){
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
            throw e;
        }
    }

    public Artist getArtistByName(String artistName) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("artisticName", artistName);

            String sql = jdbcRepository.getQueryNamed("GetArtistByName");
            Artist artist = jdbcRepository.queryProcedure(sql, params, new BeanHandler<>(Artist.class));

            return artist;
        } catch (Exception e){
            throw e;
        }
    }
}
