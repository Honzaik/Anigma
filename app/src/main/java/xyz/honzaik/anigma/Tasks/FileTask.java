package xyz.honzaik.anigma.Tasks;

import android.os.Handler;
import android.os.Message;

import java.io.File;

import xyz.honzaik.anigma.Algorithm;
import xyz.honzaik.anigma.Runnables.FileRunnable;

/**
 * Třída spravující parametry pro šifrování/dešifrovaní souborů.
 */
public class FileTask {

    public Algorithm algo;
    public File input;
    public File output;
    public String password;
    public String IV;
    public boolean encrypting = true;
    public String result = null;
    private Handler mainHandler;
    private FileTaskState state;
    public int progress;

    /**
     * Konstruktor třídy FileTask.
     * @param mainHandler Odkaz na handler v MainActivity
     */
    public FileTask(Handler mainHandler){
        this.mainHandler = mainHandler;
        this.state = FileTaskState.START;
        this.progress = 0;
    }

    /**
     * Nastaví aktuální algoritmus pro daný úkol.
     * @param algo
     */
    public void setAlgo(Algorithm algo){
        this.algo = algo;
    }

    /**
     * Vytvoří nové vlákno s FileRunnable šifrující soubor.
     * @param input Odkaz na soubor, jenž se šifruje
     * @param output Odkaz na zašifrovaný soubor.
     * @param password Heslo
     * @param IV Inicializační vektor.
     */
    public void encryptFile(File input, File output, String password, String IV){
        this.progress = 0;
        this.input = input;
        this.output = output;
        this.password = password;
        this.IV = IV;
        encrypting = true;
        Thread thread = new Thread(new FileRunnable(this));
        thread.start();
    }

    /**
     * Vytvoří nové vlákno s FileRunnable dešifrující soubor.
     * @param input Odkaz na soubor, jenž se dešifruje
     * @param output Odkaz na dešifrovaný soubor.
     * @param password Heslo
     */
    public void decryptFile(File input, File output, String password){
        this.progress = 0;
        this.input = input;
        this.output = output;
        this.password = password;
        encrypting = false;
        Thread thread = new Thread(new FileRunnable(this));
        thread.start();
    }

    /**
     * Aktualizuje stav vlákna. Posílá informace hlavní aktivitě.
     * @param state Aktuální stav vlákna.
     */
    public void setState(FileTaskState state){
        this.state = state;
        Message completeMessage = mainHandler.obtainMessage(0, this);
        completeMessage.sendToTarget();
    }

    /**
     * Vrátí aktuální stav
     * @return
     */
    public FileTaskState getState(){
        return this.state;
    }

    /**
     * Zda tato třída šifruje soubor.
     * @return
     */
    public boolean isEncrypting(){
        return this.encrypting;
    }

}
