package xyz.honzaik.anigma;

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
    public abstract String encryptString(String plaintext, String password, String IV);
    public abstract String encryptString(String plaintext, String password);
    public abstract String decryptString(String plaintext, String password, String IV);
    public abstract String decryptString(String plaintext, String password);

}
