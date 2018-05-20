package xyz.honzaik.anigma;

import org.spongycastle.crypto.InvalidCipherTextException;
import org.spongycastle.crypto.generators.SCrypt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.IllegalArgumentException;
import java.security.SecureRandom;

import xyz.honzaik.anigma.Tasks.FileTask;

public abstract class Algorithm {

    protected static final int SCRYPT_SALT_LENGTH = 16;
    protected static final int SCRYPT_N = 2^16;
    protected static final int SCRYPT_R = 8;
    protected static final int SCRYPT_P = 1;
    protected static int KEY_SIZE;
    protected static int FILE_BLOCK_SIZE;
    public String name;
    public boolean hasIV;
    protected SecureRandom random;
    protected byte[] salt;
    protected byte[] key;

    public Algorithm(Algorithms algo, SecureRandom random){
        this.name = algo.getName();
        this.hasIV = algo.hasIV();
        this.random = random;
        this.salt = new byte[SCRYPT_SALT_LENGTH];
    }

    public abstract int getBlockSize();
    public abstract String encryptString(String plaintext, String password, String IV) throws UnsupportedEncodingException, InvalidCipherTextException, IllegalArgumentException;
    public abstract String encryptString(String plaintext, String password) throws UnsupportedEncodingException, InvalidCipherTextException, IllegalArgumentException;
    public abstract String decryptString(String ciphertext, String password) throws UnsupportedEncodingException, InvalidCipherTextException, IllegalArgumentException;
    public abstract void encryptFile(FileTask task) throws IOException, InvalidCipherTextException;
    public abstract void decryptFile(FileTask task) throws IOException, InvalidCipherTextException;

    protected byte[] getKey(String password, byte[] salt) throws UnsupportedEncodingException {
        return SCrypt.generate(password.getBytes("UTF-8"), salt, SCRYPT_N, SCRYPT_R, SCRYPT_P, KEY_SIZE);
    }

}
