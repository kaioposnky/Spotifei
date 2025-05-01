package net.spotifei.Services;

import net.spotifei.Helpers.ResponseHelper;
import net.spotifei.Infrastructure.Repository.UserRepository;
import net.spotifei.Models.Responses.BadResponse;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Models.User;

import static net.spotifei.Infrastructure.Cryptograph.CriptographRepository.compareHash;

public class AuthServices {

    private final UserRepository userRepository = new UserRepository();

    public Response<Boolean> validateUserLogin(String email, String senha){
        try{
            User user = userRepository.getUsuarioByEmail(email);

            if (user == null){
                return ResponseHelper.GenerateBadResponse("Usuário não encontrado!");
            }

            return ResponseHelper.GenerateSuccessResponse(
                    "Usuário validado com sucesso!", compareHash(senha, user.getSenha())); // Retorna se a senha encriptada era igual à senha inserida
        } catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse(ex.getMessage(), ex);
        }
    }
}
