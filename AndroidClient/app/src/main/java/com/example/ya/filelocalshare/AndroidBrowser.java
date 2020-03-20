package com.example.ya.filelocalshare;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import androidx.annotation.Nullable;

import java.io.File;
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
        fileViewer = new FileViewer(icons, thumbnailExtensions);
        pathViewer = new FilePathShower(activity, R.layout.path_view, pathLayout);
        if(options==null)
            options = new FileViewer.FileViewOptions(3);
        this.options = options;
    }

    public void displayFiles(File[] files, FileExplorer explorer){
        fileViewer.viewFiles(activity, fileView, files, explorer, options);
    }
    public void displayPath(String path, FileExplorer explorer){
        pathViewer.showPath((MainActivity) activity, path, explorer);
    }
}
