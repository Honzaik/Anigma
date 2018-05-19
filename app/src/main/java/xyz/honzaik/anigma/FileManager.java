package xyz.honzaik.anigma;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

public class FileManager {

    private String mainFolderName;

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



}
