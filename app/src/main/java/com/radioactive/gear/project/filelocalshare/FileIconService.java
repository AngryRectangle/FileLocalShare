package com.radioactive.gear.project.filelocalshare;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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

        String extension = getExtension(file.getName());
        Activity activity = (Activity) FileLocalShare.getActivityContext();
        if (extension.equals("apk")) {
            ApkImageRequestTask task = new ApkImageRequestTask();
            task.execute(new ApkImageData(file, view, activity));
            return;
        }

        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        String contentType = mimeType != null ? mimeType.substring(0, mimeType.indexOf('/')) : "";
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

    private static String getExtension(String name) {
        if (name == null || name.length() == 0)
            return "";

        int dotIndex = name.lastIndexOf('.');
        if (dotIndex == name.length() - 1 || dotIndex < 0)
            return "";

        return name.substring(dotIndex + 1).toLowerCase();
    }

    private class ApkImageRequestTask extends AsyncTask<ApkImageData, Void, Void>{
        private ApkImageData[] _datas;
        private Drawable[] _drawableResult;

        @Override
        protected Void doInBackground(ApkImageData... datas) {
            _datas = datas;
            _drawableResult = new Drawable[datas.length];
            for(int i = 0; i< datas.length; i++){
                ApkImageData data = datas[0];
                _drawableResult[i] = getApkIcon(data.file, data.activity);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for(int i = 0; i< _datas.length; i++)
                _datas[i].view.setImageDrawable(_drawableResult[i]);
        }
    }

    private class ApkImageData{
        public File file;
        public ImageView view;
        public Activity activity;

        public ApkImageData(File file, ImageView view, Activity activity) {
            this.file = file;
            this.view = view;
            this.activity = activity;
        }
    }
}
