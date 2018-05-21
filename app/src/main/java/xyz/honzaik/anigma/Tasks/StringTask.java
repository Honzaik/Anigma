package xyz.honzaik.anigma.Tasks;

import android.os.Message;
import android.util.Log;
import android.os.Handler;

import xyz.honzaik.anigma.Algorithm;
import xyz.honzaik.anigma.MainActivity;
import xyz.honzaik.anigma.Runnables.StringRunnable;

/**
 * Třída spravující parametry pro šifrování/dešifrovaní textu.
 */
public class StringTask {

    public Algorithm algo;
    public String input;
    public String password;
    public String IV;
    private boolean encrypting = true;
    public String result = null;
    private Handler mainHandler;
    private StringTaskState state;

    /**
     * Konstruktor třídy StringTask.
     * @param mainHandler Odkaz na handler v MainActivity
     */
    public StringTask(Handler mainHandler){
        this.mainHandler = mainHandler;
        this.state = StringTaskState.START;
    }

    /**
     * Nastaví aktuální algoritmus pro daný úkol
     * @param algo
     */
    public void setAlgo(Algorithm algo){
        this.algo = algo;
    }


    /**
     * Vytvoří nové vlákno s StringRunnable šifrující text.
     * @param plaintext Text, který se šifruje
     * @param password Heslo
     * @param IV Inicializační vektor.
     */
    public void encryptString(String plaintext, String password, String IV){
        Thread thread = new Thread(new StringRunnable(this));
        this.input = plaintext;
        this.password = password;
        this.IV = IV;
        encrypting = true;
        thread.start();
    }

    /**
     * Vytvoří nové vlákno s StringRunnable dešifrující text.
     * @param ciphertext Text, který se dešifuje
     * @param password Heslo
     */
    public void decryptString(String ciphertext, String password){
        Thread thread = new Thread(new StringRunnable(this));
        this.input = ciphertext;
        this.password = password;
        encrypting = false;
        thread.start();
    }

    /**
     * Funkce, která se volá po skončení vlákna s StringRunnable. Posílá informaci o změně stavu MainActivity
     */
    public void finish(){
        switch(state){
            case START:
                Log.d(MainActivity.TAG, "NOTHING HAPPENED ON THREAD");
                break;
            case SUCCESS:
                Log.d(MainActivity.TAG, "DONE");

        }
        Message completeMessage = mainHandler.obtainMessage(0, this);
        completeMessage.sendToTarget();
    }

    /**
     *  Aktualizuje stav vlákna.
     * @param state
     */
    public void setState(StringTaskState state){
        this.state = state;
    }

    /**
     * Vrátí aktuální stav
     * @return
     */
    public StringTaskState getState(){
        return this.state;
    }

    /**
     * Nastaví výsledek aktuálního úkolu.
     * @param result
     */
    public void setResult(String result){
        this.result = result;
    }

    /**
     * Zda tato třída šifruje text.
     * @return
     */
    public boolean isEncrypting(){
        return this.encrypting;
    }

}
