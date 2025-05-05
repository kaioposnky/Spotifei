package net.spotifei.Services;

import net.spotifei.Helpers.ResponseHelper;
import net.spotifei.Infrastructure.Repository.PersonRepository;
import net.spotifei.Models.Requests.UserRequest;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Models.User;

public class UserService {

    private PersonRepository _personRepository;

    public UserService(){
        _personRepository = new PersonRepository();
    }

    public Response<User> getUsuarioByEmail(UserRequest user){
        try{
//            User usuari2o = _userRepository.getUsuarioByEmail(user.email);
            return ResponseHelper.GenerateSuccessResponse(
                    "usuarios obtidos com sucesso");
        } catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse(ex.getMessage(), ex);
        }
    }
}
