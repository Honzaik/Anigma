package xyz.honzaik.anigma.Ciphers;

import org.spongycastle.crypto.InvalidCipherTextException;
import org.spongycastle.crypto.engines.DESedeEngine;
import org.spongycastle.crypto.modes.CBCBlockCipher;
import org.spongycastle.crypto.paddings.PaddedBufferedBlockCipher;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import xyz.honzaik.anigma.Algorithm;
import xyz.honzaik.anigma.Algorithms;
import xyz.honzaik.anigma.Tasks.FileTask;

public class Algo3DES extends Algorithm{

    private PaddedBufferedBlockCipher cipher;

    public Algo3DES(Algorithms algo, SecureRandom random) {
        super(algo, random);
        this.cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new DESedeEngine()));
        KEY_SIZE = 24;
        FILE_BLOCK_SIZE = 1024*1024*8;
    }

    @Override
    public int getBlockSize() {
        return this.cipher.getUnderlyingCipher().getBlockSize();
    }

    @Override
    public String encryptString(String plaintext, String password, String IV) throws UnsupportedEncodingException, InvalidCipherTextException, IllegalArgumentException{
        return encryptStringWithPaddedBufferedBlockCipher(cipher, plaintext, password, IV);
    }

    @Override
    public String decryptString(String ciphertext, String password) throws UnsupportedEncodingException, InvalidCipherTextException, IllegalArgumentException{
       return decryptStringWithPaddedBufferedBlockCipher(cipher, ciphertext, password);
    }

    @Override
    public void encryptFile(FileTask task) throws IOException, InvalidCipherTextException {
        encryptFileWithPaddedBufferedBlockCipher(cipher, task);
    }

    @Override
    public void decryptFile(FileTask task) throws IOException, InvalidCipherTextException {
        decryptFileWithPaddedBufferedBlockCipher(cipher, task);
    }

    @Override
    public String encryptString(String plaintext, String password) {
        return null;
    }


}
