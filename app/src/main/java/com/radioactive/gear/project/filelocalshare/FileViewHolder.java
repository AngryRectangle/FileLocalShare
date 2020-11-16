package com.radioactive.gear.project.filelocalshare;

import android.widget.ImageView;
import android.widget.TextView;

import com.radioactive.gear.project.filelocalshare.util.Closable;

public final class FileViewHolder {
    public TextView fileName;
    public ImageView fileIcon;
    public Closable iconRequest;

    public FileViewHolder(TextView fileName, ImageView fileIcon) {
        this.fileName = fileName;
        this.fileIcon = fileIcon;
    }
}
