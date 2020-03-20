package com.example.ya.filelocalshare;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.ya.filelocalshare.sort.FileSorter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FileExplorer {
    String currentPath;
    ArrayList<ExplorerCallback> openDirListeners = new ArrayList<>();
    ArrayList<ExplorerCallback> startSearchingListeners = new ArrayList<>();
    ArrayList<SortChangeCallback> sortChangeListeners = new ArrayList<>();
    FileSorter.SortType sortType = FileSorter.SortType.BY_NAME;
    public File[] getFiles(){
        File root = new File(currentPath);
        return FileSorter.sort(root.listFiles(), sortType);
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
    public void search(String target){
        for(int i = 0; i< startSearchingListeners.size(); i++)
            startSearchingListeners.get(i).execute(target);
    }
    public void setSortType(FileSorter.SortType type){
        if(sortType!=type) {
            sortType = type;
            for (int i = 0; i < sortChangeListeners.size(); i++)
                sortChangeListeners.get(i).execute(type);
        }
    }
    public void addOpenDirListener(ExplorerCallback listener){
        openDirListeners.add(listener);
    }
    public void addStartSearchingListener(ExplorerCallback listener){
        startSearchingListeners.add(listener);
    }
    public void addSortChangeListener(SortChangeCallback listener){
        sortChangeListeners.add(listener);
    }

    static abstract class ExplorerCallback {
        public abstract void execute(String path);
    }
    static abstract class SortChangeCallback {
        public abstract void execute(FileSorter.SortType type);
    }
}
