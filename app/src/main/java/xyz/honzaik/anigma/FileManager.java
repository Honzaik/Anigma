package xyz.honzaik.anigma;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class FileManager {

    private static final String MAIN_FOLDER_NAME = "Anigma";
    private static final String ENCRYPTION_SUFFIX = ".encrypted.";
    private static final String DECRYPTION_SUFFIX = ".decrypted.";

    public FileManager(){
    }

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
