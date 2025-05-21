package net.spotifei.Infrastructure.Repository;

//imports
import net.spotifei.Infrastructure.JDBC.JDBCRepository;
import net.spotifei.Models.Artist;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArtistRepository {

    private final JDBCRepository jdbcRepository;

    /**
     * Construtor da classe.
     * Injeta a dependência do JDBCRepository, que é a ferramenta de acesso ao banco de dados.
     *
     * @param jdbcRepository A instância do JDBCRepository a ser utilizada para as operações de banco de dados.
     */
    public ArtistRepository(JDBCRepository jdbcRepository) {
        this.jdbcRepository = jdbcRepository;
    }

    /**
     * Cria um novo registro de artista no banco de dados.
     *
     * @param artist O objeto Artist contendo os dados do novo artista a ser criado.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
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

    /**
     * Exclui um artista do banco de dados pelo seu ID.
     *
     * @param artistId O ID do artista a ser excluído.
     * @throws Exception Se ocorrer um erro durante a execução da query de exclusão no banco de dados.
     */
    public void deleteArtist(int artistId) throws Exception{
        try {
            String sql = jdbcRepository.getQueryNamed("DeleteArtist");
            jdbcRepository.executeProcedure(sql, String.valueOf(artistId));
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Associa um usuário existente a um perfil de artista.
     *
     * @param userId O ID do usuário a ser associado.
     * @param artistName O nome artístico a ser vinculado ao usuário.
     * @throws Exception Se ocorrer um erro durante a execução da procedure no banco de dados.
     */
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

    /**
     * Obtém um artista do banco de dados pelo seu ID.
     *
     * @param artistId O ID do artista a ser pesquisado.
     * @return O objeto Artist correspondente ao ID, ou null se não for encontrado.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
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

    /**
     * Obtém uma lista de artistas que estão associados a uma música.
     *
     * @param musicId O ID da música cujos artistas associados serão buscados.
     * @return Uma lista de objetos Artist associados à música. Retorna uma lista vazia se nenhum artista for encontrado.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
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

    /**
     * Obtém um artista do banco de dados pelo seu nome.
     *
     * @param artistName O nome artístico do artista a ser pesquisado.
     * @return O objeto Artist correspondente ao nome artístico, ou null se não for encontrado.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
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

    /**
     * Verifica se um usuário com um determinado ID é um artista.
     *
     * @param userId O ID do usuário a ser verificado.
     * @return Retorna 1 (um inteiro) se o usuário for um artista, 0 (zero) caso contrário.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public int checkUserArtistById(int userId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idUser", userId);

            ScalarHandler<Integer> scalar = new ScalarHandler<>();

            String sql = jdbcRepository.getQueryNamed("CheckUserArtistById");
            Integer isUserAdmin = jdbcRepository.queryProcedure(sql, params, scalar);

            return isUserAdmin == null ? 0 : isUserAdmin;
        } catch (Exception e){
            throw e;
        }
    }
}
