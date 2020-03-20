package com.example.ya.filelocalshare;

import java.io.File;

public class FileExplorer {
    String currentPath;
    public  FileExplorer(String currentPath){
        this.currentPath = currentPath;
    }
    public File[] GetFiles(){
        File root = new File(currentPath);
        return  root.listFiles();
    }
    public void SetCurrentPath(String path){
        currentPath = path;
    }

}
