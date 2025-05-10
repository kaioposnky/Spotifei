package net.spotifei.Infrastructure.Cryptograph;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class CriptographRepository {
    private final Argon2 argon2 = Argon2Factory.create(32, 16);

    /**
     * @param string Recebe a string a ser encriptada
     * @return Retorna a string encriptada
     */
    public String generateHash(String string){
        return argon2.hash(1, 8196, 1, string);
    }

    /**
     *
     * @param string Recebe a string a ser checada
     * @param hash Recebe a string com hash
     * @return Retorna se a string pertence ao hash
     */
    public boolean compareHash(String string, String hash){
        return argon2.verify(hash, string);
    }
}
