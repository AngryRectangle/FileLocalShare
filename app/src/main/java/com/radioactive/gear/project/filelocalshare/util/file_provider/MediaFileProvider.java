package com.radioactive.gear.project.filelocalshare.util.file_provider;

import android.database.Cursor;
import android.net.Uri;

import com.radioactive.gear.project.filelocalshare.FileLocalShare;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MediaFileProvider extends AFileProvider {
    private static final int BUFFER_LENGTH = 10;
    private Uri _uri;
    private String[] _projection;
    private String _selection;
    private String[] _selectionArgs;
    private String _sortOrder;
    private Cursor _cursor;
    private List<File> _files;

    public MediaFileProvider(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        _uri = uri;
        _projection = projection;
        _selection = selection;
        _selectionArgs = selectionArgs;
        _sortOrder = sortOrder;
        crateCursor();
    }

    public MediaFileProvider(Uri uri, String[] projection, String sortOrder) {
        _uri = uri;
        _projection = projection;
        _sortOrder = sortOrder;
        crateCursor();
    }

    @Override
    public int getCount() {
        return _cursor.getCount();
    }

    @Override
    public File getFile(int index) {
        if (_files == null || _files.size() <= index)
            readFilesUntil(index);

        return _files.get(index);
    }

    private void crateCursor() {
        _cursor = FileLocalShare.getActivityContext().getContentResolver().query(_uri, _projection, _selection, _selectionArgs, _sortOrder);
    }

    private void readFilesUntil(int index) {
        if (_files == null)
            _files = new ArrayList<>();

        int startIndex = _files.size();
        if (startIndex > index)
            return;

        _cursor.moveToPosition(startIndex);
        for (int i = startIndex; i <= index + BUFFER_LENGTH; i++) {
            String path = _cursor.getString(0);
            File file = new File(path);
            _files.add(file);
            if (!_cursor.moveToNext())
                break;
        }
    }
}
