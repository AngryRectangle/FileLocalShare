package com.radioactive.gear.project.filelocalshare.util.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.module.AppGlideModule;
import com.radioactive.gear.project.filelocalshare.FileIconService;

@com.bumptech.glide.annotation.GlideModule
public class GlideModule extends AppGlideModule {

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        registry.prepend(FileIconService.ApkImageData.class, Drawable.class, new ApkDrawableModelLoaderFactory( context ) );
    }
}
