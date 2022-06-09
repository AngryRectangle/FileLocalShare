package com.radioactive.gear.project.filelocalshare.custom_view;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.radioactive.gear.project.filelocalshare.R;
import com.radioactive.gear.project.filelocalshare.adapter.FileViewAdapter;
import com.radioactive.gear.project.filelocalshare.util.file_provider.MediaFileProvider;

public class MediaExplorerFragment extends Fragment {


    private final Uri uri;
    private final String[] projection;
    private final String sortOrder;

    public MediaExplorerFragment(Uri uri, String[] projection, String sortOrder) {
        super();
        this.uri = uri;
        this.projection = projection;
        this.sortOrder = sortOrder;
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
        MediaFileProvider provider = new MediaFileProvider(uri, projection, sortOrder);
        FileViewAdapter adapter = new FileViewAdapter(getLayoutInflater(), provider);
        fileViewParent.setAdapter(adapter);
        return layout;
    }
}