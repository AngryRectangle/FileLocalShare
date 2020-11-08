package com.radioactive.gear.project.filelocalshare;

import android.widget.ImageView;
import android.widget.TextView;

public final class FileViewHolder {
    public TextView fileName;
    public ImageView fileIcon;

    public FileViewHolder(TextView fileName, ImageView fileIcon) {
        this.fileName = fileName;
        this.fileIcon = fileIcon;
    }
}
