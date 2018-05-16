package xyz.honzaik.anigma;

import android.util.Base64;
import android.util.Log;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.SecureRandom;
import java.util.Map;
import java.util.TreeMap;

import xyz.honzaik.anigma.Ciphers.Algo3DES;
import xyz.honzaik.anigma.Ciphers.AlgoAES;
import xyz.honzaik.anigma.Ciphers.AlgoRSA;


public class Encryptor {

    public TreeMap<String, Algorithm> algorithmList;
    private Algorithm currentAlgorithm;
    private SecureRandom random;
    private StringTask stringTask;
    private MainActivity mainActivity;

    public Encryptor(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        random = new SecureRandom();
        stringTask = new StringTask(mainActivity.getHandler());
        algorithmList = new TreeMap<String, Algorithm>();
        for(Algorithms algo : Algorithms.values()){
            switch (algo){
                case AES: algorithmList.put(algo.getName(), new AlgoAES(algo, random));
                break;
                case RSA: algorithmList.put(algo.getName(), new AlgoRSA(algo, random));
                break;
                case TRIPLEDES: algorithmList.put(algo.getName(), new Algo3DES(algo, random));
                break;
            }

        }
        BouncyCastleProvider prov = new BouncyCastleProvider();

        System.out.println("---------------------------");
        for (BouncyCastleProvider.Service s: prov.getServices()){
            if (s.getType().equals("Cipher") || s.getType().equals("SecretKeyFactory")){
                System.out.println("\t"+s.getType()+" "+ s.getAlgorithm());
            }

        }
        System.out.println("##########################");

    }

    public void printAvailableAlgos(){
        for(Map.Entry<String, Algorithm> e : algorithmList.entrySet()){
            Log.d(MainActivity.TAG, e.getKey());
        }

    }

    public void setCurrentAlgoritm(String name){
        currentAlgorithm = algorithmList.get(name);
        stringTask.setAlgo(currentAlgorithm);
    }

    public Algorithm getCurrentAlgorithm(){
        return currentAlgorithm;
    }

    public String getRandomIV() throws Exception{
        if(!currentAlgorithm.hasIV){
            throw new Exception("error getting iv for algorithm that doesnt have it");
        }
        int size = currentAlgorithm.getBlockSize();
        Log.d(MainActivity.TAG, "Generating " + size + " bytes for IV");
        byte[] newIV = new byte[size];
        random.nextBytes(newIV);
        return Base64.encodeToString(newIV, Base64.DEFAULT);
    }

    public StringTask getStringTask() {
        return stringTask;
    }
}
