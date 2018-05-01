package xyz.honzaik.anigma;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor {

    public TreeMap<String, Algorithm> algorithmList;
    private static final String algoPattern = "(AES|DES|RSA)";
    private static final String excludePattern = "(WRAP|PBE|OAEPWITHSHA)";
    private Algorithm currentAlgorithm;
    private Cipher currentCipher;
    private SecureRandom random;

    public Encryptor(){
        random = new SecureRandom();
        algorithmList = new TreeMap<String, Algorithm>();
        Set<String> algos = Security.getAlgorithms("Cipher");
        Pattern p1 = Pattern.compile(algoPattern);
        Pattern p2 = Pattern.compile(excludePattern);
        for(String s : algos){
            if(p1.matcher(s).find() && !p2.matcher(s).find() && s.length() > 3 && s.length() != 6){
                algorithmList.put(s, new Algorithm(s));
            }
        }
    }

    public void printAvailableAlgos(){
        for(Map.Entry<String, Algorithm> e : algorithmList.entrySet()){
            Log.d(MainActivity.TAG, e.getKey());
        }

    }

    public void setCurrentAlgoritm(String name){
        currentAlgorithm = algorithmList.get(name);
        try {
            currentCipher = Cipher.getInstance(currentAlgorithm.id);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public Algorithm getCurrentAlgorithm(){
        return currentAlgorithm;
    }

    public String getRandomIV(){
        int size = currentCipher.getBlockSize();
        Log.d(MainActivity.TAG, "Generating " + size + " bytes for IV");
        byte[] newIV = new byte[size];
        random.nextBytes(newIV);
        return Base64.encodeToString(newIV, Base64.DEFAULT);
    }

    public String encryptString(String plainext, String password, String IV){
        String result = null;
        try {
            byte[] base64Plaintext = Base64.encode(plainext.getBytes("UTF-8"), Base64.DEFAULT);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2withHmacSHA1");
            byte[] salt = new byte[20];
            random.nextBytes(salt);
            SecretKey temp = factory.generateSecret(new PBEKeySpec(password.toCharArray(), salt, 10000, 256));
            SecretKey keySpec = new SecretKeySpec(temp.getEncoded(), currentCipher.getAlgorithm());
            IvParameterSpec ivSpec = new IvParameterSpec(Base64.decode(IV,Base64.DEFAULT));
            currentCipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            result = Base64.encodeToString(currentCipher.doFinal(base64Plaintext), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return result;
    }
}
