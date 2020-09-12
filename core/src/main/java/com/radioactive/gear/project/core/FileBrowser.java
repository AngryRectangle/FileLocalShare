package com.radioactive.gear.project.core;

import java.io.File;

public interface FileBrowser {
    void displayFiles(File[] files);
    void displayFile(File file);
    void displayPath(String path);
    void clearFileView();
    void startSearching(File dir, String target);
    void cancelSearching();
}
