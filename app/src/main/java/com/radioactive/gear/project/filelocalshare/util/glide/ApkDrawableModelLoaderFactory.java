package com.radioactive.gear.project.filelocalshare.util.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.radioactive.gear.project.filelocalshare.FileIconService;

public class ApkDrawableModelLoaderFactory implements ModelLoaderFactory<FileIconService.ApkImageData, Drawable> {

    private final Context mContext;

    ApkDrawableModelLoaderFactory(Context context ) {
        mContext = context;
    }

    @NonNull
    @Override
    public ModelLoader<FileIconService.ApkImageData, Drawable> build(@NonNull MultiModelLoaderFactory multiFactory) {
        return new ApkDrawableModelLoader( mContext );
    }

    @Override
    public void teardown() {
        // Empty Implementation.
    }
}
