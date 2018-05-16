package xyz.honzaik.anigma;

import android.os.Message;
import android.util.Log;
import android.os.Handler;

import xyz.honzaik.anigma.Algorithm;
import xyz.honzaik.anigma.Runnables.StringRunnable;

public class StringTask {

    public Algorithm algo;
    private Thread thread;
    public String input;
    public String password;
    public String IV;
    private boolean encrypting = true;
    public String result = null;
    private Handler mainHandler;

    public StringTask(Handler mainHandler){
        this.mainHandler = mainHandler;
        thread = new Thread(new StringRunnable(this));
    }

    public void setAlgo(Algorithm algo){
        this.algo = algo;
    }

    public void encryptString(String plaintext, String password, String IV){
        this.input = plaintext;
        this.password = password;
        this.IV = IV;
        encrypting = true;
        thread.start();
    }

    public void decryptString(String ciphertext, String password){
        this.input = ciphertext;
        this.password = password;
        encrypting = false;
        thread.start();
    }

    public void setResult(String result){
        this.result = result;
        Log.d(MainActivity.TAG, "DONE");
        Message completeMessage = mainHandler.obtainMessage(1, this);
        completeMessage.sendToTarget();
    }

    public boolean isEncrypting(){
        return this.encrypting;
    }

}
