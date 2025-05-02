package net.spotifei.Infrastructure.Repository;

import net.spotifei.Infrastructure.JDBC.JDBCRepository;
import net.spotifei.Models.Person;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.List;

public class PersonRepository {

    public Person getUsuarioByEmail(String email) throws Exception{
        try{
            String sql = JDBCRepository.getInstance().getQueryNamed("GetUserByEmail");
            List<Person> users = JDBCRepository.getInstance().queryProcedure(sql, email, new BeanListHandler<>(Person.class));

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
