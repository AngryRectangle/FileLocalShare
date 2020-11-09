package com.radioactive.gear.project.filelocalshare.util.file_provider;

import com.radioactive.gear.project.filelocalshare.sort.FileSorter;

import java.io.File;

public class PathFileProvider extends AFileProvider {
    private String _path;
    private File[] _files;
    private FileSorter.SortType _sortType;

    public PathFileProvider(String path) {
        _path = path;
    }

    @Override
    public int getCount() {
        return _files.length;
    }

    @Override
    public File getFile(int index) {
        return _files[index];
    }

    public void loadFiles() {
        File directory = new File(_path);
        _files = directory.listFiles();
        trySortFiles();
        callFileUpdate();
    }
    public void setPath(String newPath){
        _path = newPath;
        loadFiles();
    }

    public void setSort(FileSorter.SortType type) {
        if(_sortType == type)
            return;

        _sortType = type;
        trySortFiles();
        callFileUpdate();
    }

    private void trySortFiles() {
        if (_sortType == null || _files == null)
            return;

        _files = FileSorter.sort(_files, _sortType);
    }

    private void callFileUpdate() {
        if (OnFileUpdate != null)
            OnFileUpdate.run();
    }
}
