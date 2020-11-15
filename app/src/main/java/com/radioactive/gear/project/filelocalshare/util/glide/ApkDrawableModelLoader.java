package com.radioactive.gear.project.filelocalshare.util.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;
import com.radioactive.gear.project.filelocalshare.FileIconService;

class ApkDrawableModelLoader implements ModelLoader<FileIconService.ApkImageData, Drawable> {
    private final Context mContext;

    ApkDrawableModelLoader(Context context) {
        mContext = context;
    }

    @Nullable
    @Override
    public ModelLoader.LoadData<Drawable> buildLoadData(@NonNull FileIconService.ApkImageData data, int width, int height, @NonNull Options options) {

        return new LoadData<>(new ObjectKey(data),
                new ApkDrawableDataFetcher(mContext, data));
    }

    @Override
    public boolean handles(@NonNull FileIconService.ApkImageData data) {
        return true;
    }
}
