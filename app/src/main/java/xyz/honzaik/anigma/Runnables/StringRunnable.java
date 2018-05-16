package xyz.honzaik.anigma.Runnables;

import android.os.Process;
import android.util.Log;

import xyz.honzaik.anigma.MainActivity;
import xyz.honzaik.anigma.StringTask;

public class StringRunnable implements Runnable {

    public StringTask task;

    public StringRunnable(StringTask task){
        this.task = task;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        String result = null;
        if(task.isEncrypting()){
            if(task.algo.hasIV){
                result = task.algo.encryptString(task.input, task.password, task.IV);
            }else{
                result = task.algo.encryptString(task.input, task.password);
            }
        }else{
            result = task.algo.decryptString(task.input, task.password);
        }
        task.setResult(result);
    }
}
