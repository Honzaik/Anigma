package xyz.honzaik.anigma.CipherModes;

import android.util.Base64;
import android.util.Log;

import org.spongycastle.crypto.CipherParameters;
import org.spongycastle.crypto.InvalidCipherTextException;
import org.spongycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.spongycastle.crypto.params.KeyParameter;
import org.spongycastle.crypto.params.ParametersWithIV;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import xyz.honzaik.anigma.Algorithm;
import xyz.honzaik.anigma.CipherList;
import xyz.honzaik.anigma.MainActivity;
import xyz.honzaik.anigma.Tasks.FileTask;
import xyz.honzaik.anigma.Tasks.FileTaskState;

/**
 * Abstraktní třída, která implimenetuje algoritmy definované v tříde Algorithm pro šifry v CBC módu s paddingem.
 */
public abstract class AlgoPaddedBufferedBlockCipher extends Algorithm {

    protected PaddedBufferedBlockCipher cipher;


    /**
     * Konstruktor třídy AlgoPaddedBufferedBlockCipher
     * @param algo Jaký to je algoritmus
     */
    public AlgoPaddedBufferedBlockCipher(CipherList algo) {
        super(algo);
    }

    /**
     * Implementuje funkci z Algorithm
     * @return
     */
    @Override
    public int getBlockSize() {
        return cipher.getUnderlyingCipher().getBlockSize();
    }

    /**
     * Funkce, která pro jakoukoliv šifru v CBC módu zašifuje plaintext
     * @param cipher Odkaz na šifru
     * @param plaintext Plaintext, který chceme šifrovat
     * @param password Heslo, které se použije k odvození klíče
     * @param IV Inicializační vektor
     * @return
     * @throws UnsupportedEncodingException Nepodařilo se přečíst plaintext nebo password
     * @throws InvalidCipherTextException Nepodařilo se zašifrovat plaintext
     * @throws IllegalArgumentException Nastala chyba při inicializaci šifry pomocí parametrů
     */
    protected String encryptStringWithPaddedBufferedBlockCipher(PaddedBufferedBlockCipher cipher, String plaintext, String password, String IV) throws UnsupportedEncodingException, InvalidCipherTextException, IllegalArgumentException{
        random.nextBytes(salt);
        key = getKey(password, salt);
        byte[] plaintextBytes = plaintext.getBytes("UTF-8");
        byte[] IVBytes = Base64.decode(IV, Base64.DEFAULT);
        CipherParameters params = new ParametersWithIV(new KeyParameter(key), IVBytes);
        cipher.init(true, params);
        byte[] outputBuffer = new byte[cipher.getOutputSize(plaintextBytes.length)];
        int bytesEncrypted = cipher.processBytes(plaintextBytes, 0, plaintextBytes.length, outputBuffer, 0);
        int additionalInfo = cipher.doFinal(outputBuffer, bytesEncrypted);
        int headerLength = SCRYPT_SALT_LENGTH + getBlockSize(); //salt + IV
        byte[] result = new byte[headerLength+bytesEncrypted+additionalInfo];
        System.arraycopy(salt, 0, result, 0, salt.length);
        System.arraycopy(IVBytes, 0, result, SCRYPT_SALT_LENGTH, IVBytes.length);
        System.arraycopy(outputBuffer, 0, result, headerLength, outputBuffer.length);
        return Base64.encodeToString(result, Base64.DEFAULT);
    }

    /**
     * Funkce, která pro jakoukoliv šifru v CBC módu dešifuje ciphertext.
     * @param cipher Odkaz na šifru
     * @param ciphertext Ciphertext v Base64 kódování
     * @param password Heslo, které se použije k odvození klíče
     * @return
     * @throws UnsupportedEncodingException Nepodařilo se přečíst ciphertext nebo password
     * @throws InvalidCipherTextException Nepodařilo se dešifrovat ciphertext. Pravděpodobně špatné heslo.
     * @throws IllegalArgumentException Nastala chyba při inicializaci šifry pomocí parametrů
     */
    protected String decryptStringWithPaddedBufferedBlockCipher(PaddedBufferedBlockCipher cipher, String ciphertext, String password) throws UnsupportedEncodingException, InvalidCipherTextException, IllegalArgumentException {
        byte[] ciphertextBytes = Base64.decode(ciphertext, Base64.DEFAULT);
        if(ciphertextBytes.length <= SCRYPT_SALT_LENGTH + getBlockSize()){
            throw new IllegalArgumentException("Invalid ciphertext");
        }
        System.arraycopy(ciphertextBytes,0, salt, 0, salt.length);
        byte[] IVBytes = new byte[getBlockSize()];
        System.arraycopy(ciphertextBytes, SCRYPT_SALT_LENGTH, IVBytes, 0, IVBytes.length);
        byte[] strippedCiphertextBytes = new byte[ciphertextBytes.length-SCRYPT_SALT_LENGTH-IVBytes.length];
        System.arraycopy(ciphertextBytes, SCRYPT_SALT_LENGTH+IVBytes.length, strippedCiphertextBytes, 0, strippedCiphertextBytes.length);

        key = getKey(password, salt);
        CipherParameters params = new ParametersWithIV(new KeyParameter(key), IVBytes);
        cipher.init(false, params);
        byte[] outputBuffer = new byte[cipher.getOutputSize(strippedCiphertextBytes.length)];
        int bytesDecrypted = cipher.processBytes(strippedCiphertextBytes, 0, strippedCiphertextBytes.length, outputBuffer, 0);
        int additionalInfo = cipher.doFinal(outputBuffer, bytesDecrypted);
        return new String(outputBuffer, StandardCharsets.UTF_8);
    }

    /**
     * Funkce, která pro jakoukoliv šifru v CBC módu zašifuje soubor v "task"
     * @param cipher Odkaz na šifru
     * @param task Objekt obsahující všechny potřebné parametry (vstupní soubor, výstupní, heslo apod.)
     * @throws IOException Nastala chyba při práci se soubory
     * @throws InvalidCipherTextException Nepodařilo se zašifrovat soubor.
     */
    protected void encryptFileWithPaddedBufferedBlockCipher(PaddedBufferedBlockCipher cipher, FileTask task) throws IOException, InvalidCipherTextException{
        random.nextBytes(salt);
        key = getKey(task.password, salt);
        FileInputStream inputStream = new FileInputStream(task.input);
        FileOutputStream outputStream = new FileOutputStream(task.output);
        byte[] IVBytes = Base64.decode(task.IV, Base64.DEFAULT);
        outputStream.write(salt);
        outputStream.write(IVBytes);

        CipherParameters params = new ParametersWithIV(new KeyParameter(key), IVBytes);
        cipher.init(true, params);
        byte[] inputBuffer = new byte[FILE_BLOCK_SIZE];
        byte[] outputBuffer = new byte[cipher.getOutputSize(inputBuffer.length)];
        long bytesReadTotal = 0;
        int bytesRead = 0;
        int bytesProcessed = 0;
        while(bytesReadTotal < task.input.length()){
            bytesRead = inputStream.read(inputBuffer);
            if(bytesRead == -1){
                throw new IOException("nothing to read anymore");
            }
            bytesProcessed = cipher.processBytes(inputBuffer,0, bytesRead, outputBuffer, 0);
            bytesReadTotal += bytesRead;
            outputStream.write(outputBuffer, 0, bytesProcessed);
            int newProgress = Math.round(((float)bytesReadTotal / task.input.length())*100);
            Log.d(MainActivity.TAG, newProgress + " " + task.progress + " " + bytesReadTotal + " " + task.input.length());
            if(newProgress != task.progress){
                task.progress = newProgress;
                task.setState(FileTaskState.UPDATE_PROGRESS);
            }
        }
        bytesProcessed = cipher.doFinal(outputBuffer, 0);
        outputStream.write(outputBuffer, 0, bytesProcessed);
    }

    /**
     * Funkce, která pro jakoukoliv šifru v CBC módu dešifruje soubor v "task"
     * @param cipher Odkaz na šifru
     * @param task Objekt obsahující všechny potřebné parametry (vstupní soubor, výstupní, heslo apod.)
     * @throws IOException Nastala chyba při práci se soubory
     * @throws InvalidCipherTextException Nepodařilo se zašifrovat soubor. Pravděpodobně špatné heslo.
     */
    protected void decryptFileWithPaddedBufferedBlockCipher(PaddedBufferedBlockCipher cipher, FileTask task) throws IOException, InvalidCipherTextException{
        FileInputStream inputStream = new FileInputStream(task.input);
        FileOutputStream outputStream = new FileOutputStream(task.output);
        byte[] IVBytes = new byte[getBlockSize()];
        inputStream.read(salt);
        inputStream.read(IVBytes);
        long bytesToDecrypt = task.input.length()- (SCRYPT_SALT_LENGTH + getBlockSize());
        key = getKey(task.password, salt);
        CipherParameters params = new ParametersWithIV(new KeyParameter(key), IVBytes);

        cipher.init(false, params);

        byte[] inputBuffer = new byte[FILE_BLOCK_SIZE];
        byte[] outputBuffer = new byte[cipher.getOutputSize(inputBuffer.length)+16];
        long bytesReadTotal = 0;
        int bytesRead = 0;
        int bytesProcessed = 0;
        while(bytesReadTotal < bytesToDecrypt){
            bytesRead = inputStream.read(inputBuffer);
            if(bytesRead == -1){
                throw new IOException("nothing to read anymore");
            }
            bytesProcessed = cipher.processBytes(inputBuffer,0, bytesRead, outputBuffer, 0);
            bytesReadTotal += bytesRead;
            outputStream.write(outputBuffer, 0, bytesProcessed);
            int newProgress = Math.round(((float)bytesReadTotal / bytesToDecrypt)*100);
            Log.d(MainActivity.TAG, newProgress + " " + task.progress + " " + bytesReadTotal + " " + task.input.length());
            if(newProgress != task.progress){
                task.progress = newProgress;
                task.setState(FileTaskState.UPDATE_PROGRESS);
            }
        }
        bytesProcessed = cipher.doFinal(outputBuffer, 0);
        outputStream.write(outputBuffer, 0, bytesProcessed);
    }

}
