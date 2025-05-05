package net.spotifei.Infrastructure.Repository;

import net.spotifei.Infrastructure.JDBC.JDBCRepository;
import net.spotifei.Models.User;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.List;

public class PersonRepository {

    private final JDBCRepository jdbcRepository = JDBCRepository.getInstance();

    public User getUsuarioByEmail(String email) throws Exception{
        try{
            String sql = jdbcRepository.getQueryNamed("GetUserByEmail");
            List<User> users = jdbcRepository.queryProcedure(sql, email, new BeanListHandler<>(User.class));

            if (users.isEmpty()){
                return null;
            }

            return users.get(0);
        } catch (Exception ex){
            ex.getCause();
            throw ex;
        }
    }
}
