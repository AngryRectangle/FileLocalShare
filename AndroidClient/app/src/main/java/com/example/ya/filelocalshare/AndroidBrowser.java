package com.example.ya.filelocalshare;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class AndroidBrowser implements FileBrowser {
    Activity activity;
    TableLayout fileView;
    FileViewer fileViewer;
    FilePathShower pathViewer;
    Map<String,Integer> icons;
    HashSet<String> thumbnailExtensions;
    FileViewer.FileViewOptions options;
    AsyncSearch asyncSearch;
    FileExplorer explorer;

    public AndroidBrowser(final Activity activity, TableLayout fileView, LinearLayout pathLayout, final FileExplorer explorer, @Nullable Map<String, Integer> icons, @Nullable HashSet<String> thumbnailExtensions, @Nullable FileViewer.FileViewOptions options) {
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
        this.explorer = explorer;
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(asyncSearch!=null&&asyncSearch.getStatus().equals(AsyncTask.Status.RUNNING)) {
                    asyncSearch.cancel(false);
                    clearFileView();
                    displayFiles(explorer.getFiles());
                }else {
                    
                }
            }
        };
        ((MainActivity)activity).getOnBackPressedDispatcher().addCallback((MainActivity)activity, callback);
        FileExplorer.ExplorerCallback viewFilesCallback = new FileExplorer.ExplorerCallback(){
            @Override
            public void execute(String path){
                displayFiles(explorer.getFiles());
                displayPath(path);
            }
        };
        explorer.addOpenDirListener(viewFilesCallback);
        FileExplorer.ExplorerCallback startSearchingCallback = new FileExplorer.ExplorerCallback(){
            @Override
            public void execute(String target){
                startSearching(new File(explorer.currentPath), target);
                pathViewer.addElement("Search", null, null, false);
            }
        };
        explorer.addStartSearchingListener(startSearchingCallback);

        FilePathShower.SelectPathHandler selectPathHandler = new FilePathShower.SelectPathHandler() {
            @Override
            public void execute(String fullpath) {
                explorer.openDirectory(fullpath);
            }
        };
        pathViewer.handler = selectPathHandler;
    }

    public void displayFiles(File[] files){
        fileViewer.viewFiles(activity, files, explorer, options);
    }
    public void displayFile(File file){
        fileViewer.viewFile(activity, file, explorer, options);
    }
    public void displayPath(String path){
        pathViewer.showPath(path, explorer);
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
            displayFile(file[0]);
        }
    }
}
