package net.spotifei.Infrastructure.Repository;

import net.spotifei.Infrastructure.JDBC.JDBCRepository;
import net.spotifei.Models.Person;
import net.spotifei.Models.User;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.ResultSet;
import java.util.List;

public class UserRepository {

    public User getUsuarioByEmail(String email) throws Exception{
        try{
            String sql = JDBCRepository.getInstance().getQueryNamed("GetUserByEmail");
            List<User> users = JDBCRepository.getInstance().queryProcedure(sql, email, new BeanListHandler<>(User.class));

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
