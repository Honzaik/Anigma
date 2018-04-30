package xyz.honzaik.anigma;

import android.util.Base64;
import android.util.Log;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

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
}
