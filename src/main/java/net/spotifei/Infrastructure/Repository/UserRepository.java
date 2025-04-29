package net.spotifei.Infrastructure.Repository;

import net.spotifei.Infrastructure.JDBC.JDBCRepository;
import net.spotifei.Models.User;

public class UserRepository {

    public User getUsuarioByEmail(String email) throws Exception{
        try{
            String sql = JDBCRepository.getInstance().getQueryNamed("GetUserByEmail");
            return new User("kaio", "santos", "kposansky@gmail.com",
                    "1124242424", "12345", 1, null);
        } catch (Exception ex){
            throw ex;
        }
    }
}
