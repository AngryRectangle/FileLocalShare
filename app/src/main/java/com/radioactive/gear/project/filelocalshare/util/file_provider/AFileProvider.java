package com.radioactive.gear.project.filelocalshare.util.file_provider;

import java.io.File;

public abstract class AFileProvider {
    public abstract int getCount();
    public abstract File getFile(int index);
    public Runnable OnFileUpdate;
}
