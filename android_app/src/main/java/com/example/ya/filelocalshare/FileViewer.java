package com.example.ya.filelocalshare;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import java.io.File;

public class FileViewer {
    public FileViewHandler fileViewer;
    public ClearViewHandler clearViewHandler;
    private static final int FileView = R.layout.file_view;
    private final Map<String, Integer> iconResources;
    private final HashSet<String> imageExtensions;
    private ArrayList<File> viewedFiles = new ArrayList<>();

    public static abstract class FileViewHandler {
        public abstract void execute(File file, FileViewOptions options);
    }

    public static abstract class ClearViewHandler {
        public abstract void execute();
    }

    public static class FileViewOptions {
        int columns;

        public FileViewOptions(int columns) {
            this.columns = columns;
        }
    }

    public FileViewer(
            @NonNull Map<String, Integer> iconResources,
            @NonNull HashSet<String> imageExtensions
    ) {
        this.iconResources = iconResources;
        this.imageExtensions = imageExtensions;
    }

    public ArrayList<File> getViewedFiles() {
        return viewedFiles;
    }

    public View getFileView(
            @NonNull final Activity activity,
            @NonNull final File file,
            @NonNull FileExplorer explorer
    ) {

        LayoutInflater ltInflater = activity.getLayoutInflater();
        View output = ltInflater.inflate(FileView, null, false);
        ImageView view = output.findViewById(R.id.fileIcon);

        setFileName((TextView) output.findViewById(R.id.fileName), file);
        setFileIcon(file, view, explorer, activity);


        //TODO refactoring
        output.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)activity).sendFile(file);
                //((MainActivity)activity).startListeningProgress();
            }
        });
        return output;
    }

    public void viewFiles(@NonNull File[] files, @NonNull FileViewOptions opt) {
        clear();
        for (int i = 0; i < files.length; i++)
            viewFile(files[i], opt);
    }

    public void viewFile(@NonNull File file, @NonNull FileViewOptions opt) {
        viewedFiles.add(file);
        fileViewer.execute(file, opt);
    }

    public void clear() {
        clearViewHandler.execute();
        viewedFiles.clear();
    }

    private static void setFileName(@NonNull TextView view, @NonNull File file) {
        view.setText(file.getName());
    }

    private static RequestBuilder requestMediaIcon(
            @NonNull File file,
            @NonNull Activity activity
    ) {
        return Glide.with(activity)
                .load(file)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .centerCrop();
    }

    private static Drawable getApkIcon(@NonNull File file, @NonNull Activity activity) {
        PackageManager pm = activity.getPackageManager();
        PackageInfo pi = pm.getPackageArchiveInfo(file.getAbsolutePath(), 0);
        if (pi != null && pm != null) {
            pi.applicationInfo.sourceDir = file.getAbsolutePath();
            pi.applicationInfo.publicSourceDir = file.getAbsolutePath();
            return pi.applicationInfo.loadIcon(pm);
        }
        return null;
    }

    private static String getFileExtension(@NonNull String extension) {
        char[] textArray = extension.toCharArray();
        for (int i = extension.length() - 1; i >= 0; i--) {
            if (textArray[i] == '.') {
                char[] output = new char[extension.length() - i - 1];
                extension.getChars(i + 1, extension.length(), output, 0);
                return new String(output).toLowerCase();
            }
        }
        return extension;
    }

    private void setOnClickActionForFolder(
            @NonNull final FileExplorer explorer,
            @NonNull final View view,
            @NonNull final File file
    ) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                explorer.openDirectory(file);
            }
        });
    }

    private void setFileThumbnail(
            @NonNull ImageView view,
            @NonNull File file,
            @NonNull Activity activity
    ) {
        if (imageExtensions.contains(getFileExtension(file.getName()))) {
            requestMediaIcon(file, activity).into(view);
        } else if (getFileExtension(file.getName()).equals("apk")) {
            Drawable drawable = getApkIcon(file, activity);
            if (drawable != null)
                view.setImageDrawable(drawable);
        }
    }

    private void setFileIcon(
            @NonNull File file,
            @NonNull ImageView view,
            @NonNull FileExplorer explorer,
            @NonNull Activity activity
    ) {
        int iconId = iconResources.get("unknown");
        if (file.isDirectory()) {
            iconId = iconResources.get("folder");
            setOnClickActionForFolder(explorer, (View) view.getParent(), file);
        } else {
            String extension = getFileExtension(file.getName());
            if (iconResources.containsKey(extension))
                iconId = iconResources.get(extension);
        }

        view.setImageResource(iconId);
        setFileThumbnail(view, file, activity);
    }
}