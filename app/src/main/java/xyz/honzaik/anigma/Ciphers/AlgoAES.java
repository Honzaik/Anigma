package xyz.honzaik.anigma.Ciphers;

import android.util.Base64;
import android.util.Log;

import org.spongycastle.crypto.BlockCipher;
import org.spongycastle.crypto.CipherParameters;
import org.spongycastle.crypto.InvalidCipherTextException;
import org.spongycastle.crypto.engines.AESEngine;
import org.spongycastle.crypto.generators.BCrypt;
import org.spongycastle.crypto.modes.GCMBlockCipher;
import org.spongycastle.crypto.params.KeyParameter;
import org.spongycastle.crypto.params.ParametersWithIV;

import java.io.UnsupportedEncodingException;
import java.lang.IllegalArgumentException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import xyz.honzaik.anigma.Algorithm;
import xyz.honzaik.anigma.Algorithms;
import xyz.honzaik.anigma.MainActivity;

public class AlgoAES extends Algorithm{

    private BlockCipher cipher;
    private static final int BCRYPT_COST = 10;
    private static final int BCRYPT_SALT_LENGTH = 16;

    public AlgoAES(Algorithms algo, SecureRandom random) {
        super(algo, random);
        this.cipher = new AESEngine();
    }

    @Override
    public int getBlockSize() {
        return cipher.getBlockSize();
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
        GCMBlockCipher gcmAES = new GCMBlockCipher(cipher);
        gcmAES.init(true, params);
        byte[] outputBuffer = new byte[gcmAES.getOutputSize(plaintextBytes.length)];
        int bytesEncrypted = gcmAES.processBytes(plaintextBytes, 0, plaintextBytes.length, outputBuffer, 0);
        int additionalInfo = gcmAES.doFinal(outputBuffer, bytesEncrypted);
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
        GCMBlockCipher gcmAES = new GCMBlockCipher(cipher);
        gcmAES.init(false, params);
        byte[] outputBuffer = new byte[gcmAES.getOutputSize(strippedCiphertextBytes.length)];
        int bytesDecrypted = gcmAES.processBytes(strippedCiphertextBytes, 0, strippedCiphertextBytes.length, outputBuffer, 0);
        int additionalInfo = gcmAES.doFinal(outputBuffer, bytesDecrypted);
        Log.d(MainActivity.TAG, "decrypted result has " + outputBuffer.length + " bytes");
        return new String(outputBuffer, StandardCharsets.UTF_8);
    }

    @Override
    public String encryptString(String plaintext, String password) throws UnsupportedEncodingException, InvalidCipherTextException, IllegalArgumentException{
        return null;
    }
}
