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
    public boolean encrypting = true;
    public String result = null;
    private Handler mainHandler;
    private FileTaskState state;
    public int progress;

    public FileTask(Handler mainHandler){
        this.mainHandler = mainHandler;
        this.state = FileTaskState.START;
        this.progress = 0;
    }

    public void setAlgo(Algorithm algo){
        this.algo = algo;
    }

    public void encryptFile(File input, File output, String password, String IV){
        this.progress = 0;
        Thread thread = new Thread(new FileRunnable(this));
        this.input = input;
        this.output = output;
        this.password = password;
        this.IV = IV;
        encrypting = true;
        thread.start();
    }

    public void decryptFile(File input, File output, String password){
        this.progress = 0;
        Thread thread = new Thread(new FileRunnable(this));
        this.input = input;
        this.output = output;
        this.password = password;
        encrypting = false;
        thread.start();
    }

    public void setState(FileTaskState state){
        this.state = state;
        Message completeMessage = mainHandler.obtainMessage(0, this);
        completeMessage.sendToTarget();
    }

    public FileTaskState getState(){
        return this.state;
    }

    public boolean isEncrypting(){
        return this.encrypting;
    }

}
