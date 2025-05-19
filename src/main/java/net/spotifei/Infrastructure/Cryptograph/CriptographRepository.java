package net.spotifei.Infrastructure.Cryptograph;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CriptographRepository {
    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();


    /**
     * @param string Recebe a string a ser encriptada
     * @return Retorna a string encriptada
     */
    public String generateHash(String string){
        return bcrypt.encode(string);
    }

    /**
     *
     * @param string Recebe a string a ser checada
     * @param hash Recebe a string com hash
     * @return Retorna se a string pertence ao hash
     */
    public boolean compareHash(String string, String hash){
        return bcrypt.matches(string, hash);
    }
}
