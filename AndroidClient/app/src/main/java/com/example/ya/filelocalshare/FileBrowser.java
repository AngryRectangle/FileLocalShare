package com.example.ya.filelocalshare;

import java.io.File;

public interface FileBrowser {
    public void displayFiles(File[] files, FileExplorer explorer);
    public void displayFile(File file, FileExplorer explorer);
    public void displayPath(String path, FileExplorer explorer);
    public void clearFileView();
    public void startSearching(File dir, String target);
}
