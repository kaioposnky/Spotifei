package net.spotifei.Services;

import net.spotifei.Helpers.ResponseHelper;
import net.spotifei.Infrastructure.Repository.UserRepository;
import net.spotifei.Models.Requests.UserRequest;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Models.User;

public class UserService {

    private UserRepository _userRepository;

    public UserService(){
        _userRepository = new UserRepository();
    }

    public Response<User> getUsuarioByEmail(UserRequest user){
        try{
            User usuari2o = _userRepository.getUsuarioByEmail(user.email);
            return ResponseHelper.GenerateSuccessResponse(
                    "usuarios obtidos com sucesso", usuari2o);
        } catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse(ex.getMessage(), ex);
        }
    }
}
