package com.example.ya.filelocalshare;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import java.io.File;

public class FileViewer {
    Map<String,Integer> iconResources;
    static final int FileView = R.layout.file_view;

    public FileViewer( Map<String,Integer> iconResources){
        this.iconResources = iconResources;
    }

    public View GetFileView(Activity activity, File file) {
        LayoutInflater ltInflater = activity.getLayoutInflater();
        View output = ltInflater.inflate(FileView, null, false);
        ((TextView) output.findViewById(R.id.fileName)).setText(file.getName());
        int iconId = iconResources.get("unknown");
        if (file.isDirectory()) {
            iconId = iconResources.get("folder");
            setOnClickActionForFolder((MainActivity)activity, output, file);
        }
        else {
            String extension = GetFileExtension(file.getName());
            if (iconResources.containsKey(extension))
                iconId = iconResources.get(extension);
        }
        ((ImageView)output.findViewById(R.id.fileIcon)).setImageResource(iconId);
        if(GetFileExtension(file.getName()).equals("png")||GetFileExtension(file.getName()).equals("jpeg")||GetFileExtension(file.getName()).equals("jpg")||GetFileExtension(file.getName()).equals("gif"))
            ((ImageView)output.findViewById(R.id.fileIcon)).setImageBitmap(getBitmap(file));
        return output;
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

    private void setOnClickActionForFolder(final MainActivity activity, final View view, final File file){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.explorer.SetCurrentPath(file.getAbsolutePath());
                activity.ViewPath(file.getAbsolutePath());
                activity.ViewFiles(file.getAbsolutePath()+"/");
            }
        });
    }
    private Bitmap getBitmap(File file){
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        float ratioWidth = (float)bitmap.getWidth()/Math.max(bitmap.getWidth(), bitmap.getHeight());
        float ratioHeight = (float)bitmap.getHeight()/Math.max(bitmap.getWidth(), bitmap.getHeight());
        return Bitmap.createScaledBitmap(
                bitmap,
                (int)(ratioWidth*120),
                (int)(ratioHeight*120),
                false
        );
    }
}
