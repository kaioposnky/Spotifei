package net.spotifei.Services;

import net.spotifei.Helpers.SQL.SQLResponseHelper;
import net.spotifei.Infrastructure.Repository.UserRepository;
import net.spotifei.Models.SQL.Response;
import net.spotifei.Models.User;

public class UserService {

    private UserRepository _userRepository;

    public UserService(){
        _userRepository = new UserRepository();
    }

    public Response getUsuario(User user){
        try{
            User usuari2o = _userRepository.getUsuario(user.getNome());
            return SQLResponseHelper.GenerateSuccessResponse("usuarios obtidos com sucesso",
                    DataHelper.generateJson(usuari2o))
        } catch (Exception ex){

        }
    }
}
