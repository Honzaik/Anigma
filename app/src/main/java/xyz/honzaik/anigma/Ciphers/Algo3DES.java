package xyz.honzaik.anigma.Ciphers;

import org.spongycastle.crypto.BlockCipher;
import org.spongycastle.crypto.engines.DESedeEngine;

import java.security.SecureRandom;

import xyz.honzaik.anigma.Algorithm;
import xyz.honzaik.anigma.Algorithms;

public class Algo3DES extends Algorithm{

    private BlockCipher cipher;

    public Algo3DES(Algorithms algo, SecureRandom random) {
        super(algo, random);

        this.cipher = new DESedeEngine();
    }

    @Override
    public int getBlockSize() {
        return this.cipher.getBlockSize();
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
