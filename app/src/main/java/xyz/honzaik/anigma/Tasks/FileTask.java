package xyz.honzaik.anigma.Tasks;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;

import xyz.honzaik.anigma.Algorithm;
import xyz.honzaik.anigma.MainActivity;
import xyz.honzaik.anigma.Runnables.FileRunnable;
import xyz.honzaik.anigma.Runnables.StringRunnable;

public class FileTask {

    public Algorithm algo;
    public File input;
    public File output;
    public String password;
    public String IV;
    private boolean encrypting = true;
    public String result = null;
    private Handler mainHandler;
    private StringTaskState state;

    public FileTask(Handler mainHandler){
        this.mainHandler = mainHandler;
        this.state = StringTaskState.START;
    }

    public void setAlgo(Algorithm algo){
        this.algo = algo;
    }

    public void encryptFile(File input, File output, String password, String IV){
        Thread thread = new Thread(new FileRunnable(this));
        this.input = input;
        this.output = output;
        this.password = password;
        this.IV = IV;
        encrypting = true;
        thread.start();
    }

    public void decryptFile(File input, File output, String password){
        Thread thread = new Thread(new FileRunnable(this));
        this.input = input;
        this.output = output;
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
