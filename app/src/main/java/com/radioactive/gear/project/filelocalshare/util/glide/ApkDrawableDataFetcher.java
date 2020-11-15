package com.radioactive.gear.project.filelocalshare.util.glide;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.radioactive.gear.project.filelocalshare.FileIconService;

public class ApkDrawableDataFetcher implements DataFetcher<Drawable> {
    private final FileIconService.ApkImageData mModel;
    private final Context mContext;


    ApkDrawableDataFetcher(Context context, FileIconService.ApkImageData model ) {
        mModel = model;
        mContext = context;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super Drawable> callback) {
        PackageManager pm = mModel.manager;
        PackageInfo pi = pm.getPackageArchiveInfo(mModel.file.getAbsolutePath(), 0);
        ApplicationInfo applicationInfo =  pi.applicationInfo;
        applicationInfo.sourceDir = mModel.file.getAbsolutePath();
        applicationInfo.publicSourceDir = mModel.file.getAbsolutePath();
        final Drawable icon = applicationInfo.loadIcon(pm);
        callback.onDataReady( icon );
    }

    @Override
    public void cleanup() {
        // Empty Implementation
    }

    @Override
    public void cancel() {
        // Empty Implementation
    }

    @NonNull
    @Override
    public Class<Drawable> getDataClass() {
        return Drawable.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.LOCAL;
    }
}
