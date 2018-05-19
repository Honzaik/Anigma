package xyz.honzaik.anigma.Ciphers;

import android.util.Base64;
import android.util.Log;

import org.spongycastle.crypto.BlockCipher;
import org.spongycastle.crypto.CipherParameters;
import org.spongycastle.crypto.InvalidCipherTextException;
import org.spongycastle.crypto.engines.DESedeEngine;
import org.spongycastle.crypto.generators.BCrypt;
import org.spongycastle.crypto.modes.CBCBlockCipher;
import org.spongycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.spongycastle.crypto.params.KeyParameter;
import org.spongycastle.crypto.params.ParametersWithIV;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import xyz.honzaik.anigma.Algorithm;
import xyz.honzaik.anigma.Algorithms;
import xyz.honzaik.anigma.MainActivity;

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
    public String encryptString(String plaintext, String password, String IV) throws UnsupportedEncodingException, InvalidCipherTextException, IllegalArgumentException{
        byte[] salt = new byte[BCRYPT_SALT_LENGTH];
        random.nextBytes(salt);
        byte[] key = BCrypt.generate(password.getBytes("UTF-8"), salt, BCRYPT_COST);
        byte[] plaintextBytes = plaintext.getBytes("UTF-8");
        Log.d(MainActivity.TAG, "ecrypting " + plaintextBytes.length + " bytes");
        byte[] IVBytes = Base64.decode(IV, Base64.DEFAULT);
        CipherParameters params = new ParametersWithIV(new KeyParameter(key), IVBytes);
        PaddedBufferedBlockCipher padded3DES = new PaddedBufferedBlockCipher(new CBCBlockCipher(cipher));
        padded3DES.init(true, params);
        byte[] outputBuffer = new byte[padded3DES.getOutputSize(plaintextBytes.length)];
        int bytesEncrypted = padded3DES.processBytes(plaintextBytes, 0, plaintextBytes.length, outputBuffer, 0);
        int additionalInfo = padded3DES.doFinal(outputBuffer, bytesEncrypted);
        int headerLength = BCRYPT_SALT_LENGTH+cipher.getBlockSize();
        byte[] result = new byte[headerLength+bytesEncrypted+additionalInfo];
        System.arraycopy(salt, 0, result, 0, salt.length);
        System.arraycopy(IVBytes, 0, result, BCRYPT_SALT_LENGTH, IVBytes.length);
        System.arraycopy(outputBuffer, 0, result, headerLength, outputBuffer.length);
        Log.d(MainActivity.TAG, "encrypted result has: " + result.length + " bytes");
        return Base64.encodeToString(result, Base64.DEFAULT);
    }

    @Override
    public String decryptString(String ciphertext, String password) throws UnsupportedEncodingException, InvalidCipherTextException, IllegalArgumentException{
        byte[] ciphertextBytes = Base64.decode(ciphertext, Base64.DEFAULT);
        if(ciphertextBytes.length <= BCRYPT_SALT_LENGTH + cipher.getBlockSize()){
            throw new IllegalArgumentException("Invalid ciphertext");
        }
        byte[] salt = new byte[BCRYPT_SALT_LENGTH];
        System.arraycopy(ciphertextBytes,0, salt, 0, salt.length);
        byte[] IVBytes = new byte[cipher.getBlockSize()];
        System.arraycopy(ciphertextBytes, BCRYPT_SALT_LENGTH, IVBytes, 0, IVBytes.length);
        byte[] strippedCiphertextBytes = new byte[ciphertextBytes.length-BCRYPT_SALT_LENGTH-IVBytes.length];
        System.arraycopy(ciphertextBytes, BCRYPT_SALT_LENGTH+IVBytes.length, strippedCiphertextBytes, 0, strippedCiphertextBytes.length);

        byte[] key = BCrypt.generate(password.getBytes("UTF-8"), salt, BCRYPT_COST);
        CipherParameters params = new ParametersWithIV(new KeyParameter(key), IVBytes);
        PaddedBufferedBlockCipher padded3DES = new PaddedBufferedBlockCipher(new CBCBlockCipher(cipher));
        padded3DES.init(false, params);
        byte[] outputBuffer = new byte[padded3DES.getOutputSize(strippedCiphertextBytes.length)];
        int bytesDecrypted = padded3DES.processBytes(strippedCiphertextBytes, 0, strippedCiphertextBytes.length, outputBuffer, 0);
        int additionalInfo = padded3DES.doFinal(outputBuffer, bytesDecrypted);
        Log.d(MainActivity.TAG, "decrypted result has " + outputBuffer.length + " bytes");
        return new String(outputBuffer, StandardCharsets.UTF_8);
    }

    @Override
    public void encryptFile(File input, File output, String password, String IV) throws FileNotFoundException, UnsupportedEncodingException, IOException, InvalidCipherTextException {

    }

    @Override
    public void encryptFile(File input, File output, String password) {

    }

    @Override
    public String encryptString(String plaintext, String password) {
        return null;
    }


}
