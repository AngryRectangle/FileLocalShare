package com.example.ya.filelocalshare;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import java.io.File;

public class FileExplorer {
    String currentPath;
    FileBrowser fileBrowser;
    public  FileExplorer(@NonNull FileBrowser fileBrowser, String currentPath){
        this.fileBrowser = fileBrowser;
        this.currentPath = currentPath;
    }
    public File[] getFiles(){
        File root = new File(currentPath);
        return root.listFiles();
    }
    public void openDirectory(String path){
        currentPath = path;
        fileBrowser.displayFiles(getFiles(), this);
        fileBrowser.displayPath(path, this);
    }
    public void openDirectory(File dir){
        openDirectory(dir.getAbsolutePath());
    }
    /*private class AsyncSearch extends AsyncTask<BitmapHolder[], BitmapHolder, Void> {
        @Override
        protected Void doInBackground(BitmapHolder[]... holders) {
            android.os.Process.setThreadPriority(  android.os.Process.THREAD_PRIORITY_BACKGROUND +   android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE);
            for(int i =0; i<holders[0].length; i++){

                holders[0][i].bitmap = getBitmap(holders[0][i].file);
                publishProgress(holders[0][i]);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(BitmapHolder... holder) {
            holder[0].view.setImageBitmap(holder[0].bitmap);
        }
    }*/
}
