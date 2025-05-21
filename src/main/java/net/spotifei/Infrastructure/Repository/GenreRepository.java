package net.spotifei.Infrastructure.Repository;

//imports
import net.spotifei.Infrastructure.JDBC.JDBCRepository;
import net.spotifei.Models.Genre;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenreRepository {

    private final JDBCRepository jdbcRepository;

    /**
     * Construtor da classe.
     * Injeta a dependência do JDBCRepository, que é a ferramenta de acesso ao banco de dados.
     *
     * @param jdbcRepository A instância do JDBCRepository a ser utilizada para as operações de banco de dados.
     */
    public GenreRepository(JDBCRepository jdbcRepository) {
        this.jdbcRepository = jdbcRepository;
    }

    /**
     * Cria um novo registro de gênero musical no banco de dados.
     *
     * @param name O nome do gênero a ser criado.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
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

    /**
     * Obtém um gênero do banco de dados pelo seu ID.
     *
     * @param genreId O ID do gênero a ser pesquisado.
     * @return O objeto Genre correspondente ao ID, ou null se nenhum gênero for encontrado.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
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

    /**
     * Retorna uma lista contendo todos os gêneros musicais registrados no banco de dados.
     *
     * @return Uma lista de objetos Genre, que pode ser vazia se não houver gêneros cadastrados.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public List<Genre> getAllGenres() throws Exception {
        try {
            String sql = jdbcRepository.getQueryNamed("GetAllGenres");
            List<Genre> genres = jdbcRepository.queryProcedure(sql, new BeanListHandler<>(Genre.class));
            return genres;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Atualiza o nome de um gênero musical existente no banco de dados.
     *
     * @param genreId O ID do gênero a ser atualizado.
     * @param name O novo nome para o gênero.
     * @throws Exception Se ocorrer um erro durante a execução da query de atualização no banco de dados.
     */
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

    /**
     * Exclui um gênero musical do banco de dados pelo seu ID.
     *
     * @param genreId O ID do gênero a ser excluído.
     * @throws Exception Se ocorrer um erro durante a execução da query de exclusão no banco de dados.
     */
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

    /**
     * Obtém um gênero do banco de dados pelo seu nome.
     *
     * @param name O nome do gênero a ser pesquisado.
     * @return O objeto Genre correspondente ao nome, ou null se nenhum gênero for encontrado.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
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

    /**
     * Obtém o gênero associado a uma música pelo seu ID.
     *
     * @param musicId O ID da música cujo gênero será buscado.
     * @return O objeto Genre associado à música, ou null se não houver um gênero encontrado.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public Genre getGenreByMusicId(int musicId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idMusic", musicId);

            String sql = jdbcRepository.getQueryNamed("GetGenreByMusicId");
            Genre genre = jdbcRepository.queryProcedure(sql, params, new BeanHandler<>(Genre.class));

            return genre;
        } catch (Exception ex){
            throw ex;
        }
    }
}
