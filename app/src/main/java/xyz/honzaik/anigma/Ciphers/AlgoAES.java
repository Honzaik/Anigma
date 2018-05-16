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
    public String encryptString(String plaintext, String password, String IV) {
        byte[] salt = new byte[BCRYPT_SALT_LENGTH];
        random.nextBytes(salt);
        try{
            byte[] key = BCrypt.generate(password.getBytes("UTF-8"), salt, BCRYPT_COST);
            byte[] plaintextBytes = plaintext.getBytes("UTF-8");
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
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
        }
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

    @Override
    public String encryptString(String plaintext, String password) {
        return null;
    }
}
