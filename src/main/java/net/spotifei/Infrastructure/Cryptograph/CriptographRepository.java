package net.spotifei.Infrastructure.Cryptograph;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class CriptographRepository {
    private static Argon2 argon2 = Argon2Factory.create(32, 16);

    public static String genererateHash(String string){
        return argon2.hash(1, 8196, 1, string);
    }

    public static boolean compareHash(String string, String hash){
        return argon2.verify(string, hash);
    }
}
