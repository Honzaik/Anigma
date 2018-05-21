package xyz.honzaik.anigma.Runnables;

import android.os.Process;

import org.spongycastle.crypto.InvalidCipherTextException;

import java.io.UnsupportedEncodingException;

import xyz.honzaik.anigma.Tasks.StringTask;
import xyz.honzaik.anigma.Tasks.StringTaskState;

/**
 * Třída implementující rozhraní runnable. Používá se pro šifrování/dešifrování textu v jiném vlákně.
 */
public class StringRunnable implements Runnable {

    public StringTask task;

    /**
     * Kontruktor třídy StringRunnable. Dostává odkaz na objekt "task". Který obsahuje všechny potřebné informace.
     * @param task
     */
    public StringRunnable(StringTask task){
        this.task = task;
    }

    /**
     * Tato funkce spustí šifrování/dešifrování souborů v "task" v novém vlákně. Také zpracovává možné chyby.
     */
    @Override
    public void run() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        String result = null;
        if(task.isEncrypting()){
            try {
                if (task.algo.hasIV) {
                    result = task.algo.encryptString(task.input, task.password, task.IV);
                } else {
                    result = task.algo.encryptString(task.input, task.password);
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
            }
        }else{
            try {
                result = task.algo.decryptString(task.input, task.password);
                task.setState(StringTaskState.SUCCESS);
            } catch (UnsupportedEncodingException e) {
                task.setState(StringTaskState.ERROR_DECODING);
                e.printStackTrace();
            } catch (InvalidCipherTextException e) {
                task.setState(StringTaskState.ERROR_DECRYPTION);
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                task.setState(StringTaskState.ERROR_DECODING);
                e.printStackTrace();
            }
        }
        task.setResult(result);
        task.finish();
    }
}
