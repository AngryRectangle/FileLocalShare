package com.example.ya.filelocalshare;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;

import com.example.ya.filelocalshare.sort.FileSorter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
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
                if(asyncSearch!=null&&(asyncSearch.getStatus().equals(AsyncTask.Status.RUNNING)||asyncSearch.getStatus().equals(AsyncTask.Status.FINISHED))) {
                    cancelSearching();
                }else {
                    String[] path = explorer.currentPath.split("[/]");
                    if(path.length>3) {
                        String fullPath = "";
                        for (int i = 0; i < path.length-1; i++)
                            fullPath+=path[i]+"/";
                        explorer.openDirectory(fullPath);
                    }
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
                pathViewer.showPath(explorer.currentPath, explorer);
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
        explorer.addSortChangeListener(new FileExplorer.SortChangeCallback() {
            @Override
            public void execute(FileSorter.SortType type) {
                sortViewedFiles(type);
            }
        });
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
    public void cancelSearching(){
        asyncSearch.cancel(false);
        asyncSearch=null;
        clearFileView();
        displayFiles(explorer.getFiles());
        ((EditText)activity.findViewById(R.id.searchText)).setText(null);
        activity.findViewById(R.id.searchText).clearFocus();
        pathViewer.showPath(explorer.currentPath, explorer);
    }

    private void sortViewedFiles(FileSorter.SortType type){
        fileViewer.viewedFiles = FileSorter.sort(fileViewer.viewedFiles, type);
        File[] fileToView = fileViewer.viewedFiles.toArray(new File[0]);
        fileViewer.clear();
        fileViewer.viewFiles(activity, fileToView, explorer, options);
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
            if(!isCancelled())displayFile(file[0]);
        }
    }
}
