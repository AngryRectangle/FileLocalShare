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

import androidx.annotation.Nullable;

import java.io.File;

public class FilePathShower {
    int pathElementView;
    Resources res;
    LinearLayout parent;
    Activity activity;
    public SelectPathHandler handler;
    public FilePathShower(Activity activity, int pathElement, LinearLayout parent){
        res = activity.getApplicationContext().getResources();
        pathElementView = pathElement;
        this.parent = parent;
        this.activity = activity;
    }
    public void showPath(String path, FileExplorer explorer){
        parent.removeAllViews();
        String[] parts = new Path(path).getPathElements();
        String fullPath = "/"+parts[0]+"/";
        parts[0] = res.getString(R.string.default_path_name);
        addElement(parts[0]+"/", fullPath, explorer, true);
        for(int i =1; i<parts.length; i++){
            fullPath+=parts[i]+"/";
            addElement(parts[i]+"/", fullPath, explorer, true);
        }
        final HorizontalScrollView scrollview =  (activity.findViewById(R.id.pathScroller));
        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_RIGHT);
            }
        });
    }
    public void addElement(String name, @Nullable String fullPath, @Nullable FileExplorer explorer, boolean isClickable){
        View view = inflateView(activity).findViewById(R.id.pathTextView);
        ((TextView)view).setText(name);
        if(isClickable){
            setPathOnClickAction(view, fullPath);
        }
        parent.addView((View) view.getParent());
    }
    private String[] getPathParts(String path){
        path = path.replaceAll(res.getString(R.string.default_path),
                res.getString(R.string.default_path_name)+"/");
        String[] out = path.split("[/]");
        out[0] = res.getString(R.string.default_path).split("[/]")[0];
        return out;
    }
    private void applyPathElement(String fullPath, String name, View view, final FileExplorer explorer){
        ((TextView)view).setText(name);
        setPathOnClickAction(view, fullPath);
    }
    private View inflateView(Activity activity){
        LayoutInflater ltInflater = activity.getLayoutInflater();
        return ltInflater.inflate(pathElementView, null, false);
    }

    private void setPathOnClickAction(final View view, final String path){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(handler!=null)
                    handler.execute(path);
            }
        });
    }

    public static abstract class SelectPathHandler{
        public abstract void execute(String fullpath);
    }
}
