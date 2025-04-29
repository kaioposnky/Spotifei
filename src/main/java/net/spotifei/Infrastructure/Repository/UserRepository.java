package net.spotifei.Infrastructure.Repository;

import net.spotifei.Models.User;

public class UserRepository extends RepositoryBase {

    public UserRepository() {
        super();
    }

    public User getUsuario(String email) throws Exception{
        try{
            get_jdbcRepository().getQueryNamed("GetUserByEmail");
        } catch (Exception ex){
            throw ex;
        }
    }
}
