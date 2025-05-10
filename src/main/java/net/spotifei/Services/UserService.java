package net.spotifei.Services;

import net.spotifei.Helpers.ResponseHelper;
import net.spotifei.Infrastructure.Repository.PersonRepository;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Models.User;

public class UserService {

    private final PersonRepository personRepository;

    public UserService(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    public Response<User> getUsuarioByEmail(User user){
        try{
            User usuario = personRepository.getUsuarioByEmail(user.getEmail());

            if (usuario == null){
                return ResponseHelper.GenerateBadResponse("Nenhum usuário foi encontrado com o email: "
                        + user.getEmail());
            }

            return ResponseHelper.GenerateSuccessResponse(
                    "usuarios obtidos com sucesso", usuario);

        } catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse(ex.getMessage(), ex);
        }
    }

    public Response<User> getUsuarioByEmail(String email){
        try{
            User usuario = personRepository.getUsuarioByEmail(email);

            if (usuario == null){
                return ResponseHelper.GenerateBadResponse("Nenhum usuário foi encontrado com o email: "
                        + email);
            }

            return ResponseHelper.GenerateSuccessResponse(
                    "usuarios obtidos com sucesso", usuario);

        } catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse(ex.getMessage(), ex);
        }
    }
}
