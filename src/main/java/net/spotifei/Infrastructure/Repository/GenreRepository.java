package net.spotifei.Infrastructure.Repository;

import net.spotifei.Infrastructure.JDBC.JDBCRepository;
import net.spotifei.Models.Genre; // Assumindo que você tem um modelo Genre
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenreRepository {

    private final JDBCRepository jdbcRepository;

    public GenreRepository(JDBCRepository jdbcRepository) {
        this.jdbcRepository = jdbcRepository;
    }

    public void createGenre(String name) throws Exception {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("name", name);

            String sql = jdbcRepository.getQueryNamed("CreateGenre");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception e) {
            throw e;
        }
    }

    public Genre getGenreById(int genreId) throws Exception {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("idGenre", genreId);

            String sql = jdbcRepository.getQueryNamed("GetGenreById");
            Genre genre = jdbcRepository.queryProcedure(sql, params, new BeanHandler<>(Genre.class));
            return genre;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Genre> getAllGenres() throws Exception {
        try {
            String sql = jdbcRepository.getQueryNamed("GetAllGenres");
            List<Genre> genres = jdbcRepository.queryProcedure(sql, new BeanListHandler<>(Genre.class));
            return genres;
        } catch (Exception e) {
            throw e;
        }
    }

    public void updateGenre(int genreId, String name) throws Exception {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("idGenre", genreId);
            params.put("name", name);

            String sql = jdbcRepository.getQueryNamed("UpdateGenre");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception e) {
            throw e;
        }
    }

    public void deleteGenre(int genreId) throws Exception {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("idGenre", genreId);

            String sql = jdbcRepository.getQueryNamed("DeleteGenre");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception e) {
            throw e;
        }
    }

    public Genre getGenreByName(String name) throws Exception {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("name", name);

            String sql = jdbcRepository.getQueryNamed("GetGenreByName");
            Genre genre = jdbcRepository.queryProcedure(sql, params, new BeanHandler<>(Genre.class));

            return genre;
        } catch (Exception e) {
            throw e;
        }
    }
}
