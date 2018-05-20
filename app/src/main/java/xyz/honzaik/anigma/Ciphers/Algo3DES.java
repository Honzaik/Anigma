package xyz.honzaik.anigma.Ciphers;

import org.spongycastle.crypto.InvalidCipherTextException;
import org.spongycastle.crypto.engines.DESedeEngine;
import org.spongycastle.crypto.modes.CBCBlockCipher;
import org.spongycastle.crypto.paddings.PaddedBufferedBlockCipher;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import xyz.honzaik.anigma.CipherList;
import xyz.honzaik.anigma.CipherModes.AlgoPaddedBufferedBlockCipher;
import xyz.honzaik.anigma.Tasks.FileTask;

public class Algo3DES extends AlgoPaddedBufferedBlockCipher{


    public Algo3DES(CipherList algo) {
        super(algo);
        this.cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new DESedeEngine()));
        this.KEY_SIZE = 24;
        this.FILE_BLOCK_SIZE = 1024*1024*4;
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
