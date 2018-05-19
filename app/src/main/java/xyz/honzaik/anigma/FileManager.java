package xyz.honzaik.anigma;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class FileManager {

    private String mainFolderName;
    private static final String ENCRYPTION_SUFFIX = ".encrypted.";
    private static final String DECRYPTION_SUFFIX = ".decrypted.";

    public FileManager(String mainFolderName){
        this.mainFolderName = mainFolderName;
    }

    private File getMainFolder() throws Exception {
        File mainFolder = new File(Environment.getExternalStorageDirectory() + "/Anigma");
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

    public ArrayList<String> getFilesPath(){
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
            if(filePath.contains(path + ".encrypted.")){
                int temp = Integer.parseInt(filePath.substring(filePath.lastIndexOf(suffix)+suffix.length(),filePath.length()));
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
        String outputPath = inputPath + getUniqueFileExtension(encrypting, inputPath);
        Log.d(MainActivity.TAG, outputPath);
        return new File(outputPath);
    }

}
