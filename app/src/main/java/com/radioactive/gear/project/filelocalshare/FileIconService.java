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

    public void setIcon(File file, ImageView view) {
        if (file.isDirectory()) {
            view.setImageResource(FileIconProvider.getFolderIcon());
            return;
        }

        String extension = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
        Activity activity = (Activity) FileLocalShare.getActivityContext();
        if (extension.equals("apk")) {
            view.setImageDrawable(getApkIcon(file, activity));
            return;
        }

        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        String contentType = mimeType!=null ? mimeType.substring(0, mimeType.indexOf('/')) : "";
        int icon;

        if (FileIconProvider.hasIcon(extension)) {
            icon = FileIconProvider.getIcon(extension);
        } else
            icon = FileIconProvider.getDefaultIcon();

        if (mimeType != null && (contentType.equals("image") || contentType.equals("video"))) {
            requestMediaIcon(file, icon, activity).into(view);
            return;
        }

        view.setImageResource(icon);
    }

    private static Drawable getApkIcon(File file, Activity activity) {
        PackageManager pm = activity.getPackageManager();
        PackageInfo pi = pm.getPackageArchiveInfo(file.getAbsolutePath(), 0);
        if (pi != null) {
            pi.applicationInfo.sourceDir = file.getAbsolutePath();
            pi.applicationInfo.publicSourceDir = file.getAbsolutePath();
            return pi.applicationInfo.loadIcon(pm);
        }
        return null;
    }

    private static RequestBuilder requestMediaIcon(File file, int placeHolderResourceId, Activity activity) {
        return Glide.with(activity)
                .load(file)
                .placeholder(placeHolderResourceId)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .centerCrop();
    }
}
