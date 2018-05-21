package xyz.honzaik.anigma;

import android.util.Base64;
import android.util.Log;

import java.security.SecureRandom;
import java.util.TreeMap;

import xyz.honzaik.anigma.Ciphers.Algo3DES;
import xyz.honzaik.anigma.Ciphers.AlgoAES;
import xyz.honzaik.anigma.Ciphers.AlgoAES256;
import xyz.honzaik.anigma.Tasks.FileTask;
import xyz.honzaik.anigma.Tasks.StringTask;

/**
 * Tato třída se stará o seznam algoritmů použitých v programu.
 */
public class AlgoManager {

    public TreeMap<String, Algorithm> algorithmList;
    private Algorithm currentAlgorithm;
    private SecureRandom random;
    private StringTask stringTask;
    private FileTask fileTask;

    /**
     * Konstruktor třídy AlgoManager
     * @param mainActivity odkaz na hlavní aktivitu programu kvůli předání odkazu na Handler
     */
    public AlgoManager(MainActivity mainActivity){
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

    /**
     * Nastaví aktuálně používaný algoritmus dle jména
     * @param name Jméno algoritmu
     */
    public void setCurrentAlgoritm(String name){
        currentAlgorithm = algorithmList.get(name);
        stringTask.setAlgo(currentAlgorithm);
        fileTask.setAlgo(currentAlgorithm);
    }

    /**
     * Vrátí aktuální algoritmus
     * @return
     */
    public Algorithm getCurrentAlgorithm(){
        return currentAlgorithm;
    }

    /**
     * Vygeneruje náhodný IV pro aktuální algoritmus
     * @return IV zakódovaný v Base64
     * @throws Exception Výjimka, pokud algoritmus IV nepoužívá
     */
    public String getRandomIV() throws Exception{
        if(!currentAlgorithm.hasIV){
            throw new Exception("Error getting iv for algorithm that does not use it");
        }
        int size = currentAlgorithm.getBlockSize();
        Log.d(MainActivity.TAG, "Generating " + size + " bytes for IV");
        byte[] newIV = new byte[size];
        random.nextBytes(newIV);
        return Base64.encodeToString(newIV, Base64.DEFAULT);
    }

    /**
     * Vratí odkaz na StringTask
     * @return
     */
    public StringTask getStringTask() {
        return stringTask;
    }

    /**
     * Vrátí odkaz na FileTask
     * @return
     */
    public FileTask getFileTask() {
        return fileTask;
    }
}
