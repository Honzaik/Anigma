package xyz.honzaik.anigma;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Třída pro práci se soubory v aplikaci.
 */
public class FileManager {

    private static final String MAIN_FOLDER_NAME = "Anigma";
    private static final String ENCRYPTION_SUFFIX = ".encrypted.";
    private static final String DECRYPTION_SUFFIX = ".decrypted.";

    /**
     * Kontruktor třídy FileManager. Aktuálně prázdný.
     */
    public FileManager(){
    }

    /**
     * Vratí odkaz hlavní složku, ze které bude program číst a do které bude zapisovat. Pokud složka neexistuje, tak jí vytvoří.
     * @return
     * @throws Exception Pokud se nepodařilo vytvořit složku
     */
    public File getMainFolder() throws Exception {
        File mainFolder = new File(Environment.getExternalStorageDirectory() + "/" + MAIN_FOLDER_NAME);
        if(mainFolder.exists() && mainFolder.isDirectory()){
            return mainFolder;
        }else{
            if(mainFolder.mkdir()){
                return mainFolder;
            }else{
                throw new Exception("Error while creating main directory.");
            }
        }
    }

    /**
     * Vratí seznam souborů ve hlavní složce. Vrací seznam objektů File
     * @return
     */
    public ArrayList<File> getFiles(){
        ArrayList<File> fileList = new ArrayList<>();
        try{
            File mainFolder = getMainFolder();
            Log.d(MainActivity.TAG, mainFolder.getAbsolutePath());
            File[] files = mainFolder.listFiles();
            for(File f : files){
                if(f.isFile()){
                    fileList.add(f);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileList;
    }

    /**
     * Vrátí cesty k souborům ve hlavní složce. Vrací seznam objektů String
     * @return
     */
    private ArrayList<String> getFilesPath(){
        ArrayList<String> fileList = new ArrayList<>();
        try{
            File mainFolder = getMainFolder();
            File[] files = mainFolder.listFiles();
            for(File f : files){
                if(f.isFile()){
                    fileList.add(f.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileList;
    }

    /**
     * Vrátí ještě nepoužité jméno pro daný soubor. Zabraňuje přepisu souborů.
     * @param encrypting Zda-li chceme vrátit jméno pro dešifrovaný soubor nebo naopak.
     * @param path Cesta k souboru od kterého chceme vytvořit kopii (šifrovanou nebo dešifrovanou).
     * @return
     */
    public String getUniqueFileExtension(boolean encrypting, String path){
        String suffix = (encrypting) ? ENCRYPTION_SUFFIX : DECRYPTION_SUFFIX;
        ArrayList<String> fileList = getFilesPath();
        int counter = -1;
        for (String filePath : fileList){
            if(filePath.contains(path + suffix)){
                int numberStartIndex = filePath.lastIndexOf(suffix)+suffix.length();
                int numberEndIndex = filePath.indexOf(".", numberStartIndex);
                Log.d(MainActivity.TAG, filePath + " " + numberStartIndex + " " + numberEndIndex);
                int temp = Integer.parseInt(filePath.substring(numberStartIndex,(numberEndIndex != -1) ? numberEndIndex : filePath.length()));
                if(temp > counter){
                    counter = temp;
                }
            }
        }
        counter++;
        return suffix + counter;
    }

    /**
     * Vratí odkaz na nový soubor, do kterého se bude zapisovat.
     * @param encrypting Zda-li je nový soubor zašifrovaná kopie nebo dešifrovaná.
     * @param input Odkaz na soubor od kterého chceme vytvořit kopii.
     * @return
     */
    public File getOutputFile(boolean encrypting, File input){
        String inputPath = input.getAbsolutePath();
        String newInputPath = inputPath;
        int extensionIndexStart = inputPath.lastIndexOf(".");
        String extension = "";
        if(extensionIndexStart != -1){
            extension = inputPath.substring(extensionIndexStart);
            newInputPath = newInputPath.substring(0, extensionIndexStart);
        }
        Log.d(MainActivity.TAG, "extension: " + extension + " unique extension: " + getUniqueFileExtension(encrypting, newInputPath));
        String outputPath = newInputPath + getUniqueFileExtension(encrypting, newInputPath) + extension;
        Log.d(MainActivity.TAG, outputPath);
        return new File(outputPath);
    }

}
