package net.spotifei.Services;

import net.spotifei.Helpers.ResponseHelper;
import net.spotifei.Infrastructure.Repository.PersonRepository;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Models.User;

import static net.spotifei.Infrastructure.Cryptograph.CriptographRepository.generateHash;

public class UserService {

    private final PersonRepository _personRepository;

    public UserService(){
        _personRepository = new PersonRepository();
    }

    public Response<User> getUsuarioByEmail(User user){
        try{
            User usuario = _personRepository.getUsuarioByEmail(user.getEmail());

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

    public Response<Void> createUser(User user){
        try{
            // Deixa a senha criptografada antes de salvar no banco
            user.setSenha(generateHash(user.getSenha()));

            _personRepository.createUser(user);

            return ResponseHelper.GenerateSuccessResponse("Usuário criado com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse(ex.getMessage(), ex);
        }
    }
}
