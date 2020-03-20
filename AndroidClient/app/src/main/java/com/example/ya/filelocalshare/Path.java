package com.example.ya.filelocalshare;

import android.util.Log;

public class Path {
    String[] pathElements;
    public Path(String path){
        String[] parts = path.split("[/]");
        pathElements = new String[parts.length-3];
        pathElements[0]=parts[1]+"/"+parts[2]+"/"+parts[3];
        Log.d("PATH",  parts[0]+"");
        for(int i =4; i<parts.length; i++)
            pathElements[i-3]=parts[i];

    }
    public String[] getPathElements(){
        return pathElements;
    }
}
