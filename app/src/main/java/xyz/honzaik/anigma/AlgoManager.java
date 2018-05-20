package xyz.honzaik.anigma;

import android.util.Base64;
import android.util.Log;

import java.security.SecureRandom;
import java.util.Map;
import java.util.TreeMap;

import xyz.honzaik.anigma.Ciphers.Algo3DES;
import xyz.honzaik.anigma.Ciphers.AlgoAES;
import xyz.honzaik.anigma.Ciphers.AlgoAES256;
import xyz.honzaik.anigma.Tasks.FileTask;
import xyz.honzaik.anigma.Tasks.StringTask;


public class AlgoManager {

    public TreeMap<String, Algorithm> algorithmList;
    private Algorithm currentAlgorithm;
    private SecureRandom random;
    private StringTask stringTask;
    private FileTask fileTask;
    private MainActivity mainActivity;

    public AlgoManager(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        random = new SecureRandom();
        stringTask = new StringTask(mainActivity.getHandler());
        fileTask = new FileTask(mainActivity.getHandler());
        algorithmList = new TreeMap<String, Algorithm>();
        for(CipherList algo : CipherList.values()){
            switch (algo){
                case AES: algorithmList.put(algo.getName(), new AlgoAES(algo));
                break;
                case TRIPLEDES: algorithmList.put(algo.getName(), new Algo3DES(algo));
                break;
                case AES256: algorithmList.put(algo.getName(), new AlgoAES256(algo));
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
        stringTask.setAlgo(currentAlgorithm);
        fileTask.setAlgo(currentAlgorithm);
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

    public FileTask getFileTask() {
        return fileTask;
    }
}
