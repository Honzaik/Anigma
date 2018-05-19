package xyz.honzaik.anigma.Runnables;

import android.os.Process;

import org.spongycastle.crypto.InvalidCipherTextException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import xyz.honzaik.anigma.Tasks.FileTask;
import xyz.honzaik.anigma.Tasks.StringTask;
import xyz.honzaik.anigma.Tasks.StringTaskState;

public class FileRunnable implements Runnable {

    public FileTask task;

    public FileRunnable(FileTask task){
        this.task = task;
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        if(task.isEncrypting()){
            try {
                if (task.algo.hasIV) {
                    task.algo.encryptFile(task.input, task.output, task.password, task.IV);
                } else {
                    task.algo.encryptFile(task.input, task.output, task.password, task.IV);
                }
                task.setState(StringTaskState.SUCCESS);
            } catch (UnsupportedEncodingException e) {
                task.setState(StringTaskState.ERROR_DECODING);
                e.printStackTrace();
            } catch (InvalidCipherTextException e) {
                task.setState(StringTaskState.ERROR_ENCRYPTION);
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                task.setState(StringTaskState.ERROR_DECODING);
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        task.finish();
    }
}
