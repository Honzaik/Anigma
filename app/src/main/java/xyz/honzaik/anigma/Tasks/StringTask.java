package xyz.honzaik.anigma.Tasks;

import android.os.Message;
import android.util.Log;
import android.os.Handler;

import xyz.honzaik.anigma.Algorithm;
import xyz.honzaik.anigma.MainActivity;
import xyz.honzaik.anigma.Runnables.StringRunnable;

public class StringTask {

    public Algorithm algo;
    public String input;
    public String password;
    public String IV;
    private boolean encrypting = true;
    public String result = null;
    private Handler mainHandler;
    private StringTaskState state;

    public StringTask(Handler mainHandler){
        this.mainHandler = mainHandler;
        this.state = StringTaskState.START;
    }

    public void setAlgo(Algorithm algo){
        this.algo = algo;
    }

    public void encryptString(String plaintext, String password, String IV){
        Thread thread = new Thread(new StringRunnable(this));
        this.input = plaintext;
        this.password = password;
        this.IV = IV;
        encrypting = true;
        thread.start();
    }

    public void decryptString(String ciphertext, String password){
        Thread thread = new Thread(new StringRunnable(this));
        this.input = ciphertext;
        this.password = password;
        encrypting = false;
        thread.start();
    }

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

    public void setState(StringTaskState state){
        this.state = state;
    }

    public StringTaskState getState(){
        return this.state;
    }

    public void setResult(String result){
        this.result = result;
    }

    public boolean isEncrypting(){
        return this.encrypting;
    }

}
