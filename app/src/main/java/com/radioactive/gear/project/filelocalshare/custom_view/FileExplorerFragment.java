package com.radioactive.gear.project.filelocalshare.custom_view;

import android.os.Bundle;
import android.os.FileUtils;
import android.provider.ContactsContract;
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

import java.io.File;
import java.nio.file.DirectoryStream;

public class FileExplorerFragment extends Fragment {

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

        File file = new File("/storage/emulated/0/");
        File[] files = file.listFiles();
        files = FileSorter.sort(files, FileSorter.SortType.BY_NAME);

        FileViewAdapter adapter = new FileViewAdapter(getLayoutInflater(), files);
        fileViewParent.setAdapter(adapter);
        return layout;
    }
}