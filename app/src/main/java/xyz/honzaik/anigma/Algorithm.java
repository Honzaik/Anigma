package xyz.honzaik.anigma;

import org.spongycastle.crypto.InvalidCipherTextException;
import java.io.UnsupportedEncodingException;
import java.lang.IllegalArgumentException;
import java.security.SecureRandom;

public abstract class Algorithm {

    public String name;
    public boolean hasIV;
    protected SecureRandom random;

    public Algorithm(Algorithms algo, SecureRandom random){
        this.name = algo.getName();
        this.hasIV = algo.hasIV();
        this.random = random;

    }

    public abstract int getBlockSize();
    public abstract String encryptString(String plaintext, String password, String IV) throws UnsupportedEncodingException, InvalidCipherTextException, IllegalArgumentException;
    public abstract String encryptString(String plaintext, String password) throws UnsupportedEncodingException, InvalidCipherTextException, IllegalArgumentException;
    public abstract String decryptString(String ciphertext, String password) throws UnsupportedEncodingException, InvalidCipherTextException, IllegalArgumentException;

}
