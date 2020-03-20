package com.example.ya.filelocalshare;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class FileExplorer {
    String currentPath;
    FileBrowser fileBrowser;
    public  FileExplorer(FileBrowser fileBrowser, String currentPath){
        this.fileBrowser = fileBrowser;
        this.currentPath = currentPath;
    }
    public File[] getFiles(){
        File root = new File(currentPath);
        return root.listFiles();
    }
    public static File[] getFiles(String path){
        File root = new File(path);
        return root.listFiles();
    }
    public void openDirectory(String path){
        currentPath = path;
        File[] files = getFiles();
        sort(files);
        fileBrowser.displayFiles(files, this);
        fileBrowser.displayPath(path, this);
    }
    public void openDirectory(File dir){
        openDirectory(dir.getAbsolutePath());
    }
    public static void sort(File[] input){
        ArrayList<File> outputFiles = new ArrayList<>();
        ArrayList<File> outputDirs = new ArrayList<>();
        for(int i =0; i<input.length; i++)
            if(input[i].isDirectory())
                outputDirs.add(input[i]);
            else
                outputFiles.add(input[i]);
        Collections.sort(outputFiles);
        Collections.sort(outputDirs);
        outputDirs.addAll(outputFiles);
        input = outputDirs.toArray(input);
    }
    public void search(String target){
        fileBrowser.startSearching(new File(currentPath), target);
    }
}
