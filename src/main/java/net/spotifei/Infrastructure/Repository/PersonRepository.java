package net.spotifei.Infrastructure.Repository;

//imports
import net.spotifei.Infrastructure.JDBC.JDBCRepository;
import net.spotifei.Models.Music;
import net.spotifei.Models.User;
import net.spotifei.Models.UserSearch;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonRepository {

    private final JDBCRepository jdbcRepository;

    /**
     * Construtor da classe.
     * Injeta a dependência do JDBCRepository, que é a ferramenta de acesso ao banco de dados.
     *
     * @param jdbcRepository A instância do JDBCRepository a ser utilizada.
     */
    public PersonRepository(JDBCRepository jdbcRepository) {
        this.jdbcRepository = jdbcRepository;
    }

    /**
     * Obtém um usuário do banco de dados pelo seu endereço de e-mail.
     *
     * @param email O endereço de e-mail do usuário a ser pesquisado.
     * @return O objeto `User` correspondente ao e-mail, ou `null` se não for encontrado.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public User getUsuarioByEmail(String email) throws Exception{
        try{
            String sql = jdbcRepository.getQueryNamed("GetUserByEmail");
            User user = jdbcRepository.queryProcedure(sql, email, new BeanHandler<>(User.class));

            return user;
        } catch (Exception ex){
            ex.getCause();
            throw ex;
        }
    }

    /**
     * Obtém um usuário do banco de dados pesquisando pelo seu ID.
     *
     * @param userId O ID do usuário a ser pesquisado.
     * @return O objeto `User` correspondente ao ID, ou `null` se não for encontrado.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public User getUserById(int userId) throws Exception{
        try{
            String sql = jdbcRepository.getQueryNamed("GetUserById");
            User user = jdbcRepository.queryProcedure(
                    sql, String.valueOf(userId), new BeanHandler<>(User.class));

            return user;
        } catch (Exception ex){
            ex.getCause();
            throw ex;
        }
    }

    /**
     * Cria um novo registro de usuário no banco de dados.
     *
     * @param user O objeto `User` contendo os dados do novo usuário a ser criado.
     * @throws Exception Se ocorrer um erro durante a execução da procedure no banco de dados.
     */
    public void createUser(User user) throws Exception{
        try{

            String sql = jdbcRepository.getQueryNamed("CreateUser");
            jdbcRepository.executeProcedure(sql, user);

        } catch (Exception ex){
            ex.getCause();
            throw ex;
        }
    }

    /**
     * Obtém uma lista das músicas mais curtidas por um usuário.
     *
     * @param userId O ID do usuário para o qual se deseja obter as músicas mais curtidas.
     * @return Uma lista de objetos `Music` que o usuário mais curtiu.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public List<Music> getMostLikedUserMusics(int userId) throws Exception{
        try{
            String sql = jdbcRepository.getQueryNamed("GetMostLikedUserMusics");
            List<Music> musics = jdbcRepository.queryProcedure(sql, userId, new BeanListHandler<>(Music.class));

            return musics;
        } catch (Exception ex){
            ex.getCause();
            throw ex;
        }
    }

    /**
     * Obtém uma lista das músicas mais descurtidas por um usuário.
     *
     * @param userId O ID do usuário para o qual se deseja obter as músicas mais descurtidas.
     * @return Uma lista de objetos `Music` que o usuário mais descurtiu.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public List<Music> getMostDislikedUserMusics(int userId) throws Exception{
        try{
            String sql = jdbcRepository.getQueryNamed("GetMostDislikedUserMusics");
            List<Music> musics = jdbcRepository.queryProcedure(sql, userId, new BeanListHandler<>(Music.class));

            return musics;
        } catch (Exception ex){
            ex.getCause();
            throw ex;
        }
    }

    /**
     * Obtém o histórico das últimas 10 músicas pesquisadas por um usuário.
     *
     * @param userId O ID do usuário para o qual se deseja obter o histórico de pesquisa.
     * @return Uma lista de objetos `UserSearch` contendo os termos de pesquisa e metadados.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public List<UserSearch> getUserLast10MusicsSearched(int userId) throws Exception{
        try{
            String sql = jdbcRepository.getQueryNamed("GetUserLast10MusicsSearched");
            List<UserSearch> searches = jdbcRepository.queryProcedure(sql, userId, new BeanListHandler<>(UserSearch.class));

            return searches;
        } catch (Exception ex){
            ex.getCause();
            throw ex;
        }
    }

    /**
     * Obtém o número total de usuários registrados no sistema.
     *
     * @return O número total de usuários como um valor `long`.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public long getTotalUsers() throws Exception{
        try{
            ScalarHandler<Long> handler = new ScalarHandler<>();

            String sql = jdbcRepository.getQueryNamed("GetTotalUsers");
            long totalUsers = jdbcRepository.queryProcedure(sql, handler);

            return totalUsers;
        } catch (Exception ex){
            ex.getCause();
            throw ex;
        }
    }

    /**
     * Exclui um usuário do banco de dados pelo seu ID.
     *
     * @param userId O ID do usuário a ser excluído.
     * @throws Exception Se ocorrer um erro durante a execução da procedure no banco de dados.
     */
    public void deleteUser(int userId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idUser", userId);

            String sql = jdbcRepository.getQueryNamed("DeleteUser");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception ex){
            throw ex;
        }
    }
}
