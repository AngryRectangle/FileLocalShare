package com.example.ya.filelocalshare;

import android.annotation.SuppressLint;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FileExplorer {
    String currentPath;
    ArrayList<ExplorerCallback> openDirListeners = new ArrayList<>();
    ArrayList<ExplorerCallback> startSearchingListeners = new ArrayList<>();
    public File[] getFiles(){
        File root = new File(currentPath);
        File[] files = root.listFiles();
        sort(files);
        return files;
    }
    public static File[] getFiles(String path){
        File root = new File(path);
        return root.listFiles();
    }
    public void openDirectory(String path){
        currentPath = path;
        for(int i = 0; i< openDirListeners.size(); i++)
            openDirListeners.get(i).execute(path);
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
        for(int i = 0; i< startSearchingListeners.size(); i++)
            startSearchingListeners.get(i).execute(target);
    }
    public void addOpenDirListener(ExplorerCallback listener){
        openDirListeners.add(listener);
    }
    public void addStartSearchingListener(ExplorerCallback listener){
        startSearchingListeners.add(listener);
    }

    static abstract class ExplorerCallback {
        public abstract void execute(String path);
    }
}
