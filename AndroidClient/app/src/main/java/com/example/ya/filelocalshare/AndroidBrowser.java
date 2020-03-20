package com.example.ya.filelocalshare;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AndroidBrowser implements FileBrowser {
    Activity activity;
    TableLayout fileView;
    FileViewer fileViewer;
    FilePathShower pathViewer;
    Map<String,Integer> icons;
    HashSet<String> thumbnailExtensions;
    FileViewer.FileViewOptions options;
    AsyncSearch asyncSearch;

    public AndroidBrowser(final Activity activity, TableLayout fileView, LinearLayout pathLayout, @Nullable Map<String, Integer> icons, @Nullable HashSet<String> thumbnailExtensions, @Nullable FileViewer.FileViewOptions options) {
        this.activity = activity;
        this.fileView = fileView;
        if(icons==null) {
            icons = new HashMap<>();
            icons.put("unknown", R.drawable.ic_file_icon_txt);
        }
        this.icons = icons;
        if(thumbnailExtensions==null)
            thumbnailExtensions = new HashSet<>();
        this.thumbnailExtensions = thumbnailExtensions;
        fileViewer = new FileViewer(icons, thumbnailExtensions, fileView);
        pathViewer = new FilePathShower(activity, R.layout.path_view, pathLayout);
        if(options==null)
            options = new FileViewer.FileViewOptions(3);
        this.options = options;

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(asyncSearch!=null&&asyncSearch.getStatus().equals(AsyncTask.Status.RUNNING))
                    asyncSearch.cancel(false);
                clearFileView();

            }
        };
        ((MainActivity)activity).getOnBackPressedDispatcher().addCallback((MainActivity)activity, callback);
    }

    public void displayFiles(File[] files, FileExplorer explorer){
        fileViewer.viewFiles(activity, files, explorer, options);
    }
    public void displayFile(File file, FileExplorer explorer){
        fileViewer.viewFile(activity, file, explorer, options);
    }
    public void displayPath(String path, FileExplorer explorer){
        pathViewer.showPath((MainActivity) activity, path, explorer);
    }
    public void clearFileView(){
        fileViewer.clear();
    }
    public void startSearching(File dir, String target){
        clearFileView();
        if(asyncSearch!=null&&asyncSearch.getStatus().equals(AsyncTask.Status.RUNNING))
            asyncSearch.cancel(false);
        asyncSearch = new AsyncSearch();
        asyncSearch.dir = dir;
        asyncSearch.execute(target);
    }

    private class AsyncSearch extends AsyncTask<String, File, Void> {
        File dir;
        FileExplorer explorer;
        @Override
        protected Void doInBackground(String... targetString) {
            ArrayList<File> dirs = new ArrayList<>();
            dirs.add(dir);
            File[] files;
            for(int i =0; i<dirs.size()&&!isCancelled(); i++){
                files = dirs.get(i).listFiles();
                for(int j = 0; j<files.length; j++){
                    if(files[j].getName().contains(targetString[0]))
                        publishProgress(files[j]);
                    if(files[j].isDirectory())
                        dirs.add(files[j]);
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(File... file) {
            displayFile(file[0], explorer);
        }
    }
}
