package xyz.honzaik.anigma.Ciphers;

import org.spongycastle.crypto.AsymmetricBlockCipher;

import java.security.SecureRandom;

import xyz.honzaik.anigma.Algorithm;
import xyz.honzaik.anigma.Algorithms;

public class AlgoRSA extends Algorithm{

    private AsymmetricBlockCipher cipher;

    public AlgoRSA(Algorithms algo, SecureRandom random) {
        super(algo, random);
    }

    @Override
    public int getBlockSize() {
        return cipher.getInputBlockSize();
    }

    @Override
    public String encryptString(String plaintext, String password, String IV) {
        return null;
    }

    @Override
    public String encryptString(String plaintext, String password) {
        return null;
    }

    @Override
    public String decryptString(String plaintext, String password, String IV) {
        return null;
    }

    @Override
    public String decryptString(String plaintext, String password) {
        return null;
    }


}
