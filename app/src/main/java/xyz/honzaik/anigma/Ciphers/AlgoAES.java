package xyz.honzaik.anigma.Ciphers;

import org.spongycastle.crypto.InvalidCipherTextException;
import org.spongycastle.crypto.engines.AESEngine;
import org.spongycastle.crypto.modes.GCMBlockCipher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.IllegalArgumentException;
import java.security.SecureRandom;

import xyz.honzaik.anigma.Algorithms;
import xyz.honzaik.anigma.CipherModes.AlgoGCMBlockCipher;
import xyz.honzaik.anigma.Tasks.FileTask;

public class AlgoAES extends AlgoGCMBlockCipher{

    private GCMBlockCipher cipher;

    public AlgoAES(Algorithms algo, SecureRandom random) {
        super(algo, random);
        this.cipher = new GCMBlockCipher(new AESEngine());
        KEY_SIZE = 16; //keysize in bytes
        FILE_BLOCK_SIZE = 1024*1024*8; //8 MB
    }

    @Override
    public int getBlockSize() {
        return cipher.getUnderlyingCipher().getBlockSize();
    }

    @Override
    public String encryptString(String plaintext, String password, String IV) throws UnsupportedEncodingException, InvalidCipherTextException, IllegalArgumentException{
        return encryptStringWithGCMBlockCipher(cipher, plaintext, password, IV);
    }

    @Override
    public String decryptString(String ciphertext, String password) throws UnsupportedEncodingException, InvalidCipherTextException, IllegalArgumentException{
        return decryptStringWithGCMBlockCipher(cipher, ciphertext, password);
    }

    @Override
    public void encryptFile(FileTask task) throws IOException, InvalidCipherTextException {
        encryptFileWithGCMBlockCipher(cipher, task);
    }

    @Override
    public void decryptFile(FileTask task) throws IOException, InvalidCipherTextException {
        decryptFileWithGCMBlockCipher(cipher, task);
    }


    @Override
    public String encryptString(String plaintext, String password) throws UnsupportedEncodingException, InvalidCipherTextException, IllegalArgumentException {
        return null;
    }
}
