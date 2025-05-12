package net.spotifei.Services;

import net.spotifei.Helpers.ResponseHelper;
import net.spotifei.Infrastructure.Cryptograph.CriptographRepository;
import net.spotifei.Infrastructure.Repository.AdministratorRepository;
import net.spotifei.Infrastructure.Repository.ArtistRepository;
import net.spotifei.Infrastructure.Repository.PersonRepository;
import net.spotifei.Models.Artist;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Models.User;

public class UserService {

    private final PersonRepository personRepository;
    private final AdministratorRepository administratorRepository;
    private final ArtistRepository artistRepository;
    private final AuthService authService;

    public UserService(PersonRepository personRepository, AdministratorRepository administratorRepository, ArtistRepository artistRepository, AuthService authService){
        this.personRepository = personRepository;
        this.administratorRepository = administratorRepository;
        this.artistRepository = artistRepository;
        this.authService = authService;
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

    public Response<Boolean> checkUserAdmin(User user){
        try{
            if (user == null || user.getIdUsuario() == 0){
                return ResponseHelper.GenerateBadResponse("Os campos user e idUsuario não podem ser nulos ou zero!");
            }

            int isUserAdmin = administratorRepository.checkUserAdminById(user.getIdUsuario());
            boolean isAdmin = isUserAdmin == 1;

            return ResponseHelper.GenerateSuccessResponse("Usuário checado com sucesso!", isAdmin);

        } catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse(ex.getMessage(), ex);
        }
    }

    public Response<Void> updateAdminLastLoginByEmail(String email){
        try{
            int adminId = administratorRepository.getAdminIdByEmail(email);

            administratorRepository.updateAdminLastLoginById(adminId);

            return ResponseHelper.GenerateSuccessResponse("Última data de login do administrador atualizada com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse(ex.getMessage(), ex);
        }
    }

    public Response<Void> createArtist(Artist artist){
        try{
            if (artist == null || artist.getNome().isBlank()
            || artist.getNomeArtistico().isBlank() || artist.getSenha().isBlank() ||
            artist.getEmail().isBlank() || artist.getTelefone().isBlank() || artist.getSobrenome().isBlank()){
                return ResponseHelper.GenerateBadResponse("O artista fornecida tem dados faltando!");
            }

            artist.setSenha(authService.criptographPassword(artist.getSenha())); // criptografa a senha dããã

            artistRepository.createArtist(artist);

            return ResponseHelper.GenerateSuccessResponse("Artista criado com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse(ex.getMessage(), ex);
        }
    }

    public Response<Artist> getArtistById(int artistId){
        try{
            if (artistId <= 0){
                return ResponseHelper.GenerateBadResponse("O id do artista deve ser >= 0!");
            }
            Artist artist = artistRepository.getArtistById(artistId);

            return ResponseHelper.GenerateSuccessResponse("Artista obtido com sucesso!", artist);
        } catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse(ex.getMessage(), ex);
        }
    }

    public Response<Void> deleteArtist(int artistId){
        try {
            if (artistId <= 0){
                return ResponseHelper.GenerateBadResponse("O id do artista deve ser >= 0!");
            }

            artistRepository.deleteArtist(artistId);

            return ResponseHelper.GenerateSuccessResponse("Artista deletado com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse(ex.getMessage(), ex);
        }

    }
}
