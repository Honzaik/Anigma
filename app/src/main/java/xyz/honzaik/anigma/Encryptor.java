package xyz.honzaik.anigma;

import android.util.Log;

import java.security.Security;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;

public class Encryptor {

    private Cipher cipher;
    public TreeSet<String> algorithmList;
    private static final String algoPattern = "(AES|DES|RSA)";
    private static final String excludePattern = "(WRAP|PBE)";

    public Encryptor(){
        algorithmList = new TreeSet<String>();
        Set<String> algos = Security.getAlgorithms("Cipher");
        Pattern p1 = Pattern.compile(algoPattern);
        Pattern p2 = Pattern.compile(excludePattern);
        for(String s : algos){
            if(p1.matcher(s).find() && !p2.matcher(s).find() && s.length() > 3){
                algorithmList.add(s);
            }
        }
    }

    public void printAvailableAlgos(){
        for(String name : algorithmList){
            Log.d(MainActivity.TAG, name);
        }

    }

}
