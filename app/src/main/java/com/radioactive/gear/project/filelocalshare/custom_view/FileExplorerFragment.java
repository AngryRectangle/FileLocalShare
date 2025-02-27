package com.radioactive.gear.project.filelocalshare.custom_view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.radioactive.gear.project.filelocalshare.FileLocalShare;
import com.radioactive.gear.project.filelocalshare.R;
import com.radioactive.gear.project.filelocalshare.adapter.FileViewAdapter;
import com.radioactive.gear.project.filelocalshare.sort.FileSorter;
import com.radioactive.gear.project.filelocalshare.util.file_provider.PathFileProvider;

public class FileExplorerFragment extends Fragment {

    private String path;
    private FileSorter.SortType sortType;
    public FileExplorerFragment(String path, FileSorter.SortType sortType) {
        super();
        this.path = path;
        this.sortType = sortType;
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
        PopupMenu popupMenu = new PopupMenu(FileLocalShare.getActivityContext(), layout.findViewById(R.id.menu_button));
        popupMenu.inflate(R.menu.popup_menu);

        PathFileProvider provider = new PathFileProvider(path);
        provider.setSort(sortType);
        provider.loadFiles();
        FileViewAdapter adapter = new FileViewAdapter(getLayoutInflater(), provider);
        fileViewParent.setAdapter(adapter);
        return layout;
    }
}