package xyz.honzaik.anigma;

import org.spongycastle.crypto.InvalidCipherTextException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.IllegalArgumentException;
import java.security.SecureRandom;

import xyz.honzaik.anigma.Tasks.FileTask;

public abstract class Algorithm {

    protected static final int BCRYPT_COST = 10;
    protected static final int BCRYPT_SALT_LENGTH = 16;
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
    public abstract void encryptFile(FileTask task) throws FileNotFoundException, UnsupportedEncodingException, IOException, InvalidCipherTextException;
    public abstract void decryptFile(FileTask task) throws FileNotFoundException, UnsupportedEncodingException, IOException, InvalidCipherTextException;

}
