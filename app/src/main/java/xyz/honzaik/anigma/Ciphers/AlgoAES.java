package xyz.honzaik.anigma.Ciphers;

import org.spongycastle.crypto.InvalidCipherTextException;
import org.spongycastle.crypto.engines.AESEngine;
import org.spongycastle.crypto.modes.GCMBlockCipher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.IllegalArgumentException;

import xyz.honzaik.anigma.CipherList;
import xyz.honzaik.anigma.CipherModes.AlgoGCMBlockCipher;
import xyz.honzaik.anigma.Tasks.FileTask;

/**
 * Třída pro algoritmus AES (AES-128, Rijndael-128)
 */
public class AlgoAES extends AlgoGCMBlockCipher{

    /**
     * Konstruktor AlgoAES. Vytvoří instanci AESEngine v GCM módu.
     * @param algo
     */
    public AlgoAES(CipherList algo) {
        super(algo);
        this.cipher = new GCMBlockCipher(new AESEngine());
        this.KEY_SIZE = 16; //Velikost klíče - 16 bytů (128 bit)
        this.FILE_BLOCK_SIZE = 1024*1024*4; //Velikost bloku, který se šifruje v AESEngine. 4 MB
    }

    /**
     * Zavolá funkci encryptStringWithGCMBlockCipher se správnými parametry.
     * @param plaintext Text, který se bude šifrovat
     * @param password Heslo, které se použije k odvození klíče
     * @param IV Inicializační vektor
     * @return
     * @throws UnsupportedEncodingException
     * @throws InvalidCipherTextException
     * @throws IllegalArgumentException
     */
    @Override
    public String encryptString(String plaintext, String password, String IV) throws UnsupportedEncodingException, InvalidCipherTextException, IllegalArgumentException{
        return encryptStringWithGCMBlockCipher(cipher, plaintext, password, IV);
    }

    /**
     * Zavolá funkci decryptStringWithGCMBlockCipher se správnými parametry.
     * @param ciphertext Ciphertext v Base64 kódování
     * @param password Heslo, které se použije k odvození klíče
     * @return
     * @throws UnsupportedEncodingException
     * @throws InvalidCipherTextException
     * @throws IllegalArgumentException
     */
    @Override
    public String decryptString(String ciphertext, String password) throws UnsupportedEncodingException, InvalidCipherTextException, IllegalArgumentException{
        return decryptStringWithGCMBlockCipher(cipher, ciphertext, password);
    }

    /**
     * Zavolá funkci encryptFileWithGCMBlockCipher se správnými parametry.
     * @param task Objekt obsahující všechny potřebné parametry (vstupní soubor, výstupní, heslo apod.)
     * @throws IOException
     * @throws InvalidCipherTextException
     */
    @Override
    public void encryptFile(FileTask task) throws IOException, InvalidCipherTextException {
        encryptFileWithGCMBlockCipher(cipher, task);
    }

    /**
     * Zavolá funkci decryptFileWithGCMBlockCipher se správnými parametry.
     * @param task Objekt obsahující všechny potřebné parametry (vstupní soubor, výstupní, heslo apod.)
     * @throws IOException
     * @throws InvalidCipherTextException
     */
    @Override
    public void decryptFile(FileTask task) throws IOException, InvalidCipherTextException {
        decryptFileWithGCMBlockCipher(cipher, task);
    }

    /**
     * Neimplementováno. GCM režim používá IV.
     * @param plaintext Text, který se bude šifrovat
     * @param password Heslo, které se použije k odvození klíče
     * @return
     * @throws UnsupportedEncodingException
     * @throws InvalidCipherTextException
     * @throws IllegalArgumentException
     */
    @Override
    public String encryptString(String plaintext, String password) throws UnsupportedEncodingException, InvalidCipherTextException, IllegalArgumentException {
        return null;
    }
}
