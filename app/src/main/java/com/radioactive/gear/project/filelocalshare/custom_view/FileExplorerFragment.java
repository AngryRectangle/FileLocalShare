package com.radioactive.gear.project.filelocalshare.custom_view;

import android.os.Bundle;
import android.os.FileUtils;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.radioactive.gear.project.filelocalshare.R;
import com.radioactive.gear.project.filelocalshare.adapter.FileViewAdapter;
import com.radioactive.gear.project.filelocalshare.sort.FileSorter;
import com.radioactive.gear.project.filelocalshare.util.file_provider.AFileProvider;
import com.radioactive.gear.project.filelocalshare.util.file_provider.MediaFileProvider;
import com.radioactive.gear.project.filelocalshare.util.file_provider.PathFileProvider;

import java.io.File;
import java.nio.file.DirectoryStream;

public class FileExplorerFragment extends Fragment {

    private String _path;
    public FileExplorerFragment(String path) {
        super();
        _path = path;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_file_explorer, container, false);
        GridView fileViewParent = layout.findViewById(R.id.fileViewHolder);

        /*PathFileProvider provider = new PathFileProvider("/storage/emulated/0/");
        provider.setSort(FileSorter.SortType.BY_NAME);
        provider.loadFiles();*/

        String[] projection = {MediaStore.Audio.AudioColumns.DATA};
        MediaFileProvider provider = new MediaFileProvider(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, MediaStore.Audio.Media.TITLE + " ASC");

        FileViewAdapter adapter = new FileViewAdapter(getLayoutInflater(), provider);
        fileViewParent.setAdapter(adapter);
        return layout;
    }
}