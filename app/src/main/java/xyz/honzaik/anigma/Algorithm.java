package xyz.honzaik.anigma;

import android.util.Log;

import org.spongycastle.crypto.InvalidCipherTextException;
import org.spongycastle.crypto.generators.SCrypt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.IllegalArgumentException;
import java.security.SecureRandom;

import xyz.honzaik.anigma.Tasks.FileTask;

/**
 * Abstraktní třída sdružující vlastnosti všech použitých šifer
 */
public abstract class Algorithm {

    protected static final int SCRYPT_SALT_LENGTH = 16;
    protected static final int SCRYPT_N = 2^16;
    protected static final int SCRYPT_R = 8;
    protected static final int SCRYPT_P = 1;
    protected int KEY_SIZE;
    protected int FILE_BLOCK_SIZE;
    public String name;
    public boolean hasIV;
    protected SecureRandom random;
    protected byte[] salt;
    protected byte[] key;

    /**
     * Konstruktor abstraktné třídy Algorithm
     * @param algo Typ algoritmu z CipherList třídy
     */
    public Algorithm(CipherList algo){
        this.name = algo.getName();
        this.hasIV = algo.hasIV();
        this.random = new SecureRandom();
        this.salt = new byte[SCRYPT_SALT_LENGTH];
    }

    /**
     * Vrátí velikost bloku pro danou šifru. Tato hodnota se používá pro zjištění velikosti IV
     * @return Velikost bloku
     */
    public abstract int getBlockSize();

    /**
     * Zašifruje plaintext pomocí passwordu a použije IV
     * @param plaintext Text, který se bude šifrovat
     * @param password Heslo, které se použije k odvození klíče
     * @param IV Inicializační vektor
     * @return
     * @throws UnsupportedEncodingException Nepodařilo se přečíst plaintext nebo password
     * @throws InvalidCipherTextException Nepodařilo se zašifrovat plaintext
     * @throws IllegalArgumentException Nastala chyba při inicializaci šifry pomocí parametrů
     */
    public abstract String encryptString(String plaintext, String password, String IV) throws UnsupportedEncodingException, InvalidCipherTextException, IllegalArgumentException;

    /**
     * Zašifruje plaintext pomocí passwordu. Nepoužívá se IV
     * @param plaintext Text, který se bude šifrovat
     * @param password Heslo, které se použije k odvození klíče
     * @return
     * @throws UnsupportedEncodingException Nepodařilo se přečíst plaintext nebo password
     * @throws InvalidCipherTextException Nepodařilo se zašifrovat plaintext
     * @throws IllegalArgumentException Nastala chyba při inicializaci šifry pomocí parametrů
     */
    public abstract String encryptString(String plaintext, String password) throws UnsupportedEncodingException, InvalidCipherTextException, IllegalArgumentException;

    /**
     * Dešifruje ciphertext pomocí hesla. IV je součástí ciphertextu
     * @param ciphertext Ciphertext v Base64 kódování
     * @param password Heslo, které se použije k odvození klíče
     * @return
     * @throws UnsupportedEncodingException Nepodařilo se přečíst ciphertext nebo password
     * @throws InvalidCipherTextException Nepodařilo se dešifrovat ciphertext. Pravděpodobně špatné heslo.
     * @throws IllegalArgumentException Nastala chyba při inicializaci šifry pomocí parametrů
     */
    public abstract String decryptString(String ciphertext, String password) throws UnsupportedEncodingException, InvalidCipherTextException, IllegalArgumentException;

    /**
     * Vytvoří zašifrovanou kopii souboru definovaném v "task"
     * @param task Objekt obsahující všechny potřebné parametry (vstupní soubor, výstupní, heslo apod.)
     * @throws IOException Nastala chyba při práci se soubory
     * @throws InvalidCipherTextException Nepodařilo se zašifrovat soubor.
     */
    public abstract void encryptFile(FileTask task) throws IOException, InvalidCipherTextException;

    /**
     * Vytvoří dešifrovanou kopii souboru definovaném v "task"
     * @param task Objekt obsahující všechny potřebné parametry (vstupní soubor, výstupní, heslo apod.)
     * @throws IOException Nastala chyba při práci se soubory
     * @throws InvalidCipherTextException Nepodařilo se zašifrovat soubor. Pravděpodobně špatné heslo.
     */
    public abstract void decryptFile(FileTask task) throws IOException, InvalidCipherTextException;

    /**
     * Vygeneruje klíč z hesla pomocí algoritmu SCrypt.
     * @param password Heslo, ze kterého se bude odvozovat klíč.
     * @param salt Sůl
     * @return
     * @throws UnsupportedEncodingException Nepodařilo se dekódovat heslo
     */
    protected byte[] getKey(String password, byte[] salt) throws UnsupportedEncodingException {
        Log.d(MainActivity.TAG, "KEY " + KEY_SIZE + " " + name);
        return SCrypt.generate(password.getBytes("UTF-8"), salt, SCRYPT_N, SCRYPT_R, SCRYPT_P, KEY_SIZE);
    }

}
