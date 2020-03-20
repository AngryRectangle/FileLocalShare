package com.example.ya.filelocalshare;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import java.io.File;

public class FileViewer {
    Map<String,Integer> iconResources;
    HashSet<String> imageExtensions;
    static final int FileView = R.layout.file_view;

    public FileViewer( Map<String,Integer> iconResources, HashSet<String> imageExtensions){
        this.iconResources = iconResources;
        this.imageExtensions = imageExtensions;
    }

    /*private boolean isMediaContent(String extension){
        if( MimeTypeMap.getMimeTypeFromExtension(extension))

    }*/
    public View GetFileView(Activity activity, File file, FileExplorer explorer) {
        LayoutInflater ltInflater = activity.getLayoutInflater();
        View output = ltInflater.inflate(FileView, null, false);
        ((TextView) output.findViewById(R.id.fileName)).setText(file.getName());
        int iconId = iconResources.get("unknown");
        if (file.isDirectory()) {
            iconId = iconResources.get("folder");
            setOnClickActionForFolder(explorer, output, file);
        }
        else {
            String extension = GetFileExtension(file.getName());
            if (iconResources.containsKey(extension))
                iconId = iconResources.get(extension);
        }
        ((ImageView)output.findViewById(R.id.fileIcon)).setImageResource(iconId);
        if(imageExtensions.contains(GetFileExtension(file.getName()))){
            //bitmapHolders.add(new BitmapHolder(null, (ImageView)output.findViewById(R.id.fileIcon), file));
            Glide.with(activity)
                    .load(file)
                    .centerCrop()
                    .placeholder(iconId)
                    .into((ImageView)output.findViewById(R.id.fileIcon));
        }
        return output;
    }
    public void viewFiles(Activity activity, TableLayout parent, File[] files, FileExplorer explorer, FileViewOptions opt){
        parent.removeAllViews();
        View[] fileViews = new View[files.length];
        for(int i =0; i< files.length; i++)
            fileViews[i] = GetFileView(activity, files[i], explorer);
        TableRow row;
        for(int r = 0; r<Math.ceil((float)files.length/opt.columns); r++){
            row = new TableRow(activity);
            for(int i = 0; i<opt.columns&&r*opt.columns+i<fileViews.length; i++){
                row.addView(fileViews[r*opt.columns+i]);
            }
            //row.setGravity(Gravity.START);
            parent.addView(row);
        }
    }
    public void viewFile(Activity activity, TableLayout parent, File file, FileExplorer explorer, FileViewOptions opt) {
        View fileView = GetFileView(activity, file, explorer);
        int rowCount = parent.getChildCount();
        if (rowCount == 0) {
            parent.addView(new TableRow(activity));
        }
        TableRow row = (TableRow) parent.getChildAt(rowCount - 1);
        int columnCount = row.getChildCount();
        if (columnCount >= opt.columns) {
            row = new TableRow(activity);
            parent.addView(row);
        }
        row.addView(fileView);
    }

    public static class FileViewOptions{
        int columns;

        public FileViewOptions(int columns) {
            this.columns = columns;
        }
    }
    private String GetFileExtension(String extension){
        char[] textArray = extension.toCharArray();
        for(int i = extension.length()-1; i>=0; i--){
            if(textArray[i]=='.'){
                char[] output = new char[extension.length()-i-1];
                extension.getChars(i+1, extension.length(), output, 0);
                return new String(output);
            }
        }
        return extension;
    }
    public static void SortFilesByAlphabetAndFolders(File[] input){
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

    private void setOnClickActionForFolder(final FileExplorer explorer, final View view, final File file){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                explorer.openDirectory(file);
            }
        });
    }

    private File[] GetSpecificFiles(File[] files, HashSet<String> extensions){
        ArrayList<File> output = new ArrayList<>();
        for(int i =0; i < files.length; i++)
            if(extensions.contains(GetFileExtension(files[i].getName())))
                output.add(files[i]);
            return output.toArray(new File[0]);
    }
}
