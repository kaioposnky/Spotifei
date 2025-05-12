package net.spotifei.Services;

import net.spotifei.Helpers.ResponseHelper;
import net.spotifei.Infrastructure.Cryptograph.CriptographRepository;
import net.spotifei.Infrastructure.Repository.PersonRepository;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Models.User;

public class AuthService {

    private final PersonRepository personRepository;
    private final CriptographRepository criptographRepository;

    public AuthService(PersonRepository personRepository, CriptographRepository criptographRepository) {
        this.personRepository = personRepository;
        this.criptographRepository = criptographRepository;
    }

    public Response<Boolean> validateUserLogin(String email, String senha){
        try{
            User user = personRepository.getUsuarioByEmail(email);
            if (user == null){
                return ResponseHelper.GenerateBadResponse("Usuário não encontrado!");
            }

            // Retorna se a senha encriptada era igual à senha inserida
            boolean passwordMatch = validatePassword(senha, user.getSenha());

            return ResponseHelper.GenerateSuccessResponse(
                    "Usuário validado com sucesso!",
                    passwordMatch);
        } catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse(ex.getMessage(), ex);
        }
    }

    public Response<Void> createUser(User user){
        try{
            // Deixa a senha criptografada antes de salvar no banco
            user.setSenha(criptographPassword(user.getSenha()));

            personRepository.createUser(user);

            return ResponseHelper.GenerateSuccessResponse("Usuário criado com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse(ex.getMessage(), ex);
        }
    }

    public boolean validatePassword(String password, String hash){
        return criptographRepository.compareHash(password, hash);
    }

    public String criptographPassword(String password){
        return criptographRepository.generateHash(password);
    }
}
