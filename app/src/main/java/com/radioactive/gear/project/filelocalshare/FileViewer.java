package com.radioactive.gear.project.filelocalshare;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;

public class FileViewer {
    public FileViewHandler fileViewer;
    public ClearViewHandler clearViewHandler;
    private static final int FileView = R.layout.file_view;
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

    private static void setFileThumbnail(
            @NonNull ImageView view,
            @NonNull File file,
            @NonNull Activity activity
    ) {
        if (FileExtensionsHandler.isMediaFile(getFileExtension(file.getName()))) {
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
        int iconId = FileExtensionsHandler.fileIcons.get("unknown");
        if (file.isDirectory()) {
            iconId = FileExtensionsHandler.fileIcons.get("folder");
            setOnClickActionForFolder(explorer, (View) view.getParent(), file);
            //TODO убрать логику установки функции при тапе отсюда
        } else {
            String extension = getFileExtension(file.getName());
            if (FileExtensionsHandler.fileIcons.containsKey(extension))
                iconId = FileExtensionsHandler.fileIcons.get(extension);
        }

        view.setImageResource(iconId);
        setFileThumbnail(view, file, activity);
    }
    public static void setFileIcon(
            @NonNull File file,
            @NonNull ImageView view,
            @NonNull Activity activity
    ) {
        int iconId = FileExtensionsHandler.fileIcons.get("unknown");
        if (file.isDirectory()) {
            iconId = FileExtensionsHandler.fileIcons.get("folder");
        } else {
            String extension = getFileExtension(file.getName());
            if (FileExtensionsHandler.fileIcons.containsKey(extension))
                iconId = FileExtensionsHandler.fileIcons.get(extension);
        }

        view.setImageResource(iconId);
        setFileThumbnail(view, file, activity);
    }
}