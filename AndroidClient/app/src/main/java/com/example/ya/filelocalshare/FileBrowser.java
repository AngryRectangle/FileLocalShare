package com.example.ya.filelocalshare;

import java.io.File;

public interface FileBrowser {
    public void displayFiles(File[] files, FileExplorer explorer);
    public void displayPath(String path, FileExplorer explorer);
}
