package com.example.ya.filelocalshare;

import android.app.Activity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;

import java.io.File;

public class FileViewer {
    Map<String,Integer> iconResources;
    static final int FileView = R.layout.file_view;

    public  FileViewer( Map<String,Integer> iconResources){
        this.iconResources = iconResources;
    }

    public  View GetFileView(Activity activity, File file){
        LayoutInflater ltInflater = activity.getLayoutInflater();
        View output = ltInflater.inflate(FileView, null, false);
        ((TextView)output.findViewById(R.id.fileName)).setText(file.getName());
        int iconId =iconResources.get("unknown");
        if(file.isDirectory())
            iconId = iconResources.get("folder");
        else {
            String extension = GetFileExtension(file.getName());
            if (iconResources.containsKey(extension))
                iconId = iconResources.get(extension);
        }
        ((ImageView)output.findViewById(R.id.fileIcon)).setImageResource(iconId);
        return  output;
    }
    public String GetFileExtension(String extension){
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
}
