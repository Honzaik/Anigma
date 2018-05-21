package xyz.honzaik.anigma.Ciphers;

import org.spongycastle.crypto.InvalidCipherTextException;
import org.spongycastle.crypto.engines.DESedeEngine;
import org.spongycastle.crypto.modes.CBCBlockCipher;
import org.spongycastle.crypto.paddings.PaddedBufferedBlockCipher;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import xyz.honzaik.anigma.CipherList;
import xyz.honzaik.anigma.CipherModes.AlgoPaddedBufferedBlockCipher;
import xyz.honzaik.anigma.Tasks.FileTask;

/**
 * Třída pro algoritmus Triple DES (3-DES, DESede)
 */
public class Algo3DES extends AlgoPaddedBufferedBlockCipher{

    /**
     * Konstruktor Algo3DES. Vytvoří instanci DESedeEngine v CBC módu.
     * @param algo
     */
    public Algo3DES(CipherList algo) {
        super(algo);
        this.cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new DESedeEngine()));
        this.KEY_SIZE = 24; //Velikost klíče - 24 bytů
        this.FILE_BLOCK_SIZE = 1024*1024*4; //Velikost bloku, který se šifruje v DESedeEngine. 4 MB
    }

    /**
     * Zavolá funkci encryptStringWithPaddedBufferedBlockCipher se správnými parametry.
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
        return encryptStringWithPaddedBufferedBlockCipher(cipher, plaintext, password, IV);
    }

    /**
     * Zavolá funkci decryptStringWithPaddedBufferedBlockCipher se správnými parametry.
     * @param ciphertext Ciphertext v Base64 kódování
     * @param password Heslo, které se použije k odvození klíče
     * @return
     * @throws UnsupportedEncodingException
     * @throws InvalidCipherTextException
     * @throws IllegalArgumentException
     */
    @Override
    public String decryptString(String ciphertext, String password) throws UnsupportedEncodingException, InvalidCipherTextException, IllegalArgumentException{
       return decryptStringWithPaddedBufferedBlockCipher(cipher, ciphertext, password);
    }

    /**
     * Zavolá funkci encryptFileWithPaddedBufferedBlockCipher se správnými parametry.
     * @param task Objekt obsahující všechny potřebné parametry (vstupní soubor, výstupní, heslo apod.)
     * @throws IOException
     * @throws InvalidCipherTextException
     */
    @Override
    public void encryptFile(FileTask task) throws IOException, InvalidCipherTextException {
        encryptFileWithPaddedBufferedBlockCipher(cipher, task);
    }

    /**
     * Zavolá funkci decryptFileWithPaddedBufferedBlockCipher se správnými parametry.
     * @param task Objekt obsahující všechny potřebné parametry (vstupní soubor, výstupní, heslo apod.)
     * @throws IOException
     * @throws InvalidCipherTextException
     */
    @Override
    public void decryptFile(FileTask task) throws IOException, InvalidCipherTextException {
        decryptFileWithPaddedBufferedBlockCipher(cipher, task);
    }

    /**
     * Neimplementováno. CBC režim používá IV.
     * @param plaintext Text, který se bude šifrovat
     * @param password Heslo, které se použije k odvození klíče
     * @return
     */
    @Override
    public String encryptString(String plaintext, String password) {
        return null;
    }


}
