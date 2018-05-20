package xyz.honzaik.anigma.Ciphers;

import android.util.Log;

import org.spongycastle.crypto.InvalidCipherTextException;
import org.spongycastle.crypto.engines.AESEngine;
import org.spongycastle.crypto.modes.GCMBlockCipher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.IllegalArgumentException;

import xyz.honzaik.anigma.CipherList;
import xyz.honzaik.anigma.CipherModes.AlgoGCMBlockCipher;
import xyz.honzaik.anigma.MainActivity;
import xyz.honzaik.anigma.Tasks.FileTask;

public class AlgoAES extends AlgoGCMBlockCipher{

    public AlgoAES(CipherList algo) {
        super(algo);
        this.cipher = new GCMBlockCipher(new AESEngine());
        this.KEY_SIZE = 16; //keysize in bytes
        this.FILE_BLOCK_SIZE = 1024*1024*8; //8 MB
        Log.d(MainActivity.TAG, "here " + KEY_SIZE);
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
