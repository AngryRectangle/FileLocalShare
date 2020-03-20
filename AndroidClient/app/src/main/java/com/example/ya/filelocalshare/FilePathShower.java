package com.example.ya.filelocalshare;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;

public class FilePathShower {
    int pathElementView;
    Resources res;
    LinearLayout parent;
    public FilePathShower(Context con, int pathElement, LinearLayout parent){
        res = con.getResources();
        pathElementView = pathElement;
        this.parent = parent;
    }
    public void showPath(MainActivity activity, String path, FileExplorer explorer){
        Log.d("DEB", path);
        parent.removeAllViews();
        path = path.replaceAll(
                res.getString(R.string.default_path),
                res.getString(R.string.default_path_name)+"/");
        String[] parts = getPathParts(path);
        String fullPath = res.getString(R.string.default_path);
        for(int i =0; i<parts.length; i++){
            View view =  inflateView(activity).findViewById(R.id.pathTextView);
            applyPathElement(fullPath, parts[i]+"/", view, explorer);
            parent.addView((View)view.getParent());
            if(i<parts.length-2)
                fullPath+=parts[i+1]+"/";
        }
        final HorizontalScrollView scrollview =  (activity.findViewById(R.id.pathScroller));
        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_RIGHT);
            }
        });
    }
    private String[] getPathParts(String path){
        return path.split("[/]");
    }
    private void applyPathElement(String fullPath, String name, View view, final FileExplorer explorer){
        ((TextView)view).setText(name);
        setPathOnClickAction(explorer, view, fullPath);
    }
    private View inflateView(Activity activity){
        LayoutInflater ltInflater = activity.getLayoutInflater();
        return ltInflater.inflate(pathElementView, null, false);
    }

    private void setPathOnClickAction(final FileExplorer explorer, final View view, final String path){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                explorer.openDirectory(path);
            }
        });
    }
}
