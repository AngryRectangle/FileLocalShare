package com.radioactive.gear.project.filelocalshare;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FilePathShower {
    public SelectPathHandler handler;
    private int pathElementView;
    private Resources res;
    private LinearLayout parent;
    private Activity activity;

    public static abstract class SelectPathHandler {
        public abstract void execute(String fullpath);
    }

    public FilePathShower(Activity activity, int pathElement, LinearLayout parent) {
        res = activity.getApplicationContext().getResources();
        pathElementView = pathElement;
        this.parent = parent;
        this.activity = activity;
    }

    public void showPath(String path, FileExplorer explorer) {
        clear();
        addElements(path, explorer);
        scrollToEnd((HorizontalScrollView) activity.findViewById(R.id.pathScroller));
    }

    public void addElement(
            @NonNull String name,
            @Nullable String fullPath,
            @Nullable FileExplorer explorer,
            boolean isClickable
    ) {
        TextView view = inflateView(activity).findViewById(R.id.pathTextView);
        view.setText(name);
        if (isClickable)
            setPathOnClickAction(view, fullPath);
        parent.addView((View) view.getParent());
    }

    private void addElements(String path, FileExplorer explorer) {
        String[] parts = new Path(path).getPathElements();
        StringBuilder fullPath = new StringBuilder("/");
        fullPath.insert(1, parts[0]);
        parts[0] = res.getString(R.string.default_path_name);
        addElement(parts[0], fullPath.toString(), explorer, true);
        for (int i = 1; i < parts.length; i++) {
            fullPath.append(parts[i]);
            addElement(parts[i], fullPath.toString(), explorer, true);
        }
    }

    private View inflateView(@NonNull Activity activity) {
        LayoutInflater ltInflater = activity.getLayoutInflater();
        return ltInflater.inflate(pathElementView, null, false);
    }

    private void clear() {
        parent.removeAllViews();
    }

    private void scrollToEnd(final HorizontalScrollView view) {
        view.post(new Runnable() {
            @Override
            public void run() {
                view.fullScroll(ScrollView.FOCUS_RIGHT);
            }
        });
    }

    private void setPathOnClickAction(@NonNull final View view, @NonNull final String path) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (handler != null)
                    handler.execute(path);
            }
        });
    }
}