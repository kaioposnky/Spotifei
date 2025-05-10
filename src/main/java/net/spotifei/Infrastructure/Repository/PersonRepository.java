package net.spotifei.Infrastructure.Repository;

import net.spotifei.Infrastructure.JDBC.JDBCRepository;
import net.spotifei.Models.Music;
import net.spotifei.Models.User;
import net.spotifei.Models.UserSearch;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.List;

public class PersonRepository {

    private final JDBCRepository jdbcRepository;

    public PersonRepository(JDBCRepository jdbcRepository) {
        this.jdbcRepository = jdbcRepository;
    }

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

    public void createUser(User user) throws Exception{
        try{

            String sql = jdbcRepository.getQueryNamed("CreateUser");
            jdbcRepository.executeProcedure(sql, user);

        } catch (Exception ex){
            ex.getCause();
            throw ex;
        }
    }

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

    public int getTotalUsers() throws Exception{
        try{
            String sql = jdbcRepository.getQueryNamed("GetTotalUsers");
            int totalUsers = jdbcRepository.queryProcedure(sql, new BeanHandler<>(Integer.class));

            return totalUsers;
        } catch (Exception ex){
            ex.getCause();
            throw ex;
        }
    }
}
