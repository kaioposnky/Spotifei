package net.spotifei.Services;

//imports
import net.spotifei.Helpers.ResponseHelper;
import net.spotifei.Infrastructure.Cryptograph.CriptographRepository;
import net.spotifei.Infrastructure.Repository.PersonRepository;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Models.User;

public class AuthService {

    private final PersonRepository personRepository;
    private final CriptographRepository criptographRepository;

    /**
     * Construtor da classe `AuthService`.
     * Injeta as dependências do `PersonRepository` e `CriptographRepository`.
     *
     * @param personRepository A instância de `PersonRepository` a ser utilizada.
     * @param criptographRepository A instância de `CriptographRepository` a ser utilizada.
     */
    public AuthService(PersonRepository personRepository, CriptographRepository criptographRepository) {
        this.personRepository = personRepository;
        this.criptographRepository = criptographRepository;
    }

    /**
     * Valida as credenciais de login de um usuário.
     * Procura o usuário pelo e-mail e compara a senha fornecida com a senha hashada armazenada.
     *
     * @param email O e-mail do usuário tentando logar.
     * @param senha A senha em texto claro fornecida pelo usuário.
     * @return Um objeto `Response<Boolean>` indicando o sucesso da validação e se a senha corresponde.
     * Retorna `true` no payload se a senha estiver correta, `false` se não.
     */
    public Response<Boolean> validateUserLogin(String email, String senha){
        try{
            User user = personRepository.getUsuarioByEmail(email);
            if (user == null){
                return ResponseHelper.generateBadResponse("Usuário não encontrado!");
            }

            // Retorna se a senha encriptada era igual à senha inserida
            boolean passwordMatch = validatePassword(senha, user.getSenha());

            return ResponseHelper.generateSuccessResponse(
                    "Usuário validado com sucesso!",
                    passwordMatch);
        } catch (Exception ex){
            return ResponseHelper.generateErrorResponse(ex.getMessage(), ex);
        }
    }

    /**
     * Cria um novo usuário no sistema.
     * Antes de salvar, a senha do usuário é criptografada (hashada) para segurança.
     *
     * @param user O objeto `User` contendo os dados do novo usuário (com a senha em texto claro).
     * @return Um objeto `Response<Void>` indicando o sucesso ou falha da operação.
     */
    public Response<Void> createUser(User user){
        try{
            // Deixa a senha criptografada antes de salvar no banco
            user.setSenha(criptographPassword(user.getSenha()));

            personRepository.createUser(user);

            return ResponseHelper.generateSuccessResponse("Usuário criado com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.generateErrorResponse(ex.getMessage(), ex);
        }
    }

    /**
     * Valida uma senha em texto claro comparando-a com um hash de senha existente.
     *
     * @param password A senha em texto claro a ser verificada.
     * @param hash O hash da senha armazenado.
     * @return `true` se a senha corresponder ao hash, `false` caso contrário.
     */
    public boolean validatePassword(String password, String hash){
        return criptographRepository.compareHash(password, hash);
    }

    /**
     * Gera um hash criptográfico para uma senha em texto claro.
     *
     * @param password A senha em texto claro a ser hashada.
     * @return Uma `String` contendo o hash criptográfico da senha.
     */
    public String criptographPassword(String password){
        return criptographRepository.generateHash(password);
    }
}
