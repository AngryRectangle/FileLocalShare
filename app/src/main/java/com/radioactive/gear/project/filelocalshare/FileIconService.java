package com.radioactive.gear.project.filelocalshare;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
public class FileIconService {
    private static FileIconService _instance;

    public static FileIconService get() {
        if (_instance == null)
            _instance = new FileIconService();
        return _instance;
    }

    public void setIcon(File file, final ImageView view) {
        Activity activity = (Activity) FileLocalShare.getActivityContext();
        Glide.with(activity).clear(view);
        if (file.isDirectory()) {
            view.setImageDrawable(FileIconProvider.getFolderIcon());
            return;
        }

        String extension = getExtension(file.getName());
        Drawable icon;
        if (FileIconProvider.hasIcon(extension))
            icon = FileIconProvider.getIcon(extension);
        else
            icon = FileIconProvider.getDefaultIcon();

        if (extension.equals("apk")) {
            requestApkIcon(file, icon, activity).into(view);
            return;
        }

        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        String contentType = mimeType != null ? mimeType.substring(0, mimeType.indexOf('/')) : "";
        if (mimeType != null && (contentType.equals("image") || contentType.equals("video"))) {
            requestMediaIcon(file, icon, activity).into(view);
            return;
        }

        view.setImageDrawable(icon);
    }
    private static RequestBuilder requestApkIcon(File file, Drawable placeHolderResourceId, Activity activity) {
        PackageManager pm = activity.getPackageManager();
        return Glide.with(activity)
                .load(new ApkImageData(file, pm))
                .placeholder(placeHolderResourceId)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
    }

    private static RequestBuilder requestMediaIcon(File file, Drawable placeHolderResourceId, Activity activity) {
        return Glide.with(activity)
                .load(file)
                .placeholder(placeHolderResourceId)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .centerCrop();
    }

    private static String getExtension(String name) {
        if (name == null || name.length() == 0)
            return "";

        int dotIndex = name.lastIndexOf('.');
        if (dotIndex == name.length() - 1 || dotIndex < 0)
            return "";

        return name.substring(dotIndex + 1).toLowerCase();
    }

    public static class ApkImageData{
        public File file;
        public PackageManager manager;

        public ApkImageData(File file, PackageManager manager) {
            this.file = file;
            this.manager = manager;
        }
    }
}
