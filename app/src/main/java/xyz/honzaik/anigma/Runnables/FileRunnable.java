package xyz.honzaik.anigma.Runnables;

import android.os.Process;

import org.spongycastle.crypto.InvalidCipherTextException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import xyz.honzaik.anigma.Tasks.FileTask;
import xyz.honzaik.anigma.Tasks.FileTaskState;
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
                task.algo.encryptFile(task);
                task.setState(FileTaskState.SUCCESS);
            } catch (UnsupportedEncodingException e) {
                task.setState(FileTaskState.ERROR_DECODING);
                e.printStackTrace();
            } catch (InvalidCipherTextException e) {
                task.setState(FileTaskState.ERROR_ENCRYPTION);
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                task.setState(FileTaskState.ERROR_DECODING);
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
