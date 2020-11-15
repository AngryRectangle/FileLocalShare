package com.radioactive.gear.project.filelocalshare.util.file_provider;

import android.os.AsyncTask;

import androidx.core.util.Consumer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class SearchFileProvider extends AFileProvider {
    private AsyncSearch _searchingTask;
    private ArrayList<File> _fileSearchingStack = new ArrayList<>();

    @Override
    public int getCount() {
        return _fileSearchingStack.size();
    }

    @Override
    public File getFile(int index) {
        return _fileSearchingStack.get(index);
    }

    public void startSearching(String directory, String targetName) {
        startSearching(new File(directory), targetName);
    }

    public void startSearching(File directory, String targetName) {
        if (_searchingTask != null) {
            _searchingTask.cancel(true);
            _fileSearchingStack.clear();
            callFileUpdate();
        }

        _searchingTask = new AsyncSearch(new OnFilesSearched(), directory);
        _searchingTask.execute(targetName);
    }

    public void stopSearching() {
        if (_searchingTask != null)
            _searchingTask.cancel(true);
    }

    private class OnFilesSearched implements Consumer<File[]> {

        @Override
        public void accept(File[] files) {
            if (files.length == 1)
                _fileSearchingStack.add(files[0]);
            else
                _fileSearchingStack.addAll(Arrays.asList(files));
            callFileUpdate();
        }
    }

    private class AsyncSearch extends AsyncTask<String, File, Void> {
        public Consumer<File[]> OnFileFound;
        public File StartingDirectory;

        public AsyncSearch(Consumer<File[]> onFileFound, File startingDirectory) {
            OnFileFound = onFileFound;
            StartingDirectory = startingDirectory;
        }

        @Override
        protected Void doInBackground(String... targetString) {
            ArrayList<File> dirs = new ArrayList<>();
            dirs.add(StartingDirectory);
            File[] files;
            for (int i = 0; i < dirs.size() && !isCancelled(); i++) {
                files = dirs.get(i).listFiles();
                if (files == null)
                    continue;

                for (int j = 0; j < files.length; j++) {
                    if (files[j].getName().contains(targetString[0]))
                        publishProgress(files[j]);
                    if (files[j].isDirectory())
                        dirs.add(files[j]);
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(File... file) {
            if (!isCancelled()) OnFileFound.accept(file);
        }
    }
}
