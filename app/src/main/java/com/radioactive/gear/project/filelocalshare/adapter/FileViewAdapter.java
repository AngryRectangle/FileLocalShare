package com.radioactive.gear.project.filelocalshare.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.radioactive.gear.project.filelocalshare.FileIconService;
import com.radioactive.gear.project.filelocalshare.FileViewHolder;
import com.radioactive.gear.project.filelocalshare.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileViewAdapter extends BaseAdapter {
    private List<File> _files;
    private LayoutInflater _inflater;

    public FileViewAdapter(LayoutInflater inflater, File[] files) {
        super();
        _inflater = inflater;
        _files = Arrays.asList(files);
    }

    public FileViewAdapter(LayoutInflater inflater, List<File> files) {
        super();
        _inflater = inflater;
        _files = files;
    }

    public FileViewAdapter(LayoutInflater inflater) {
        super();
        _inflater = inflater;
        _files = new ArrayList<>();
    }

    public void addFiles(File[] files) {
        _files.addAll(Arrays.asList(files));
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return _files.size();
    }

    @Override
    public File getItem(int position) {
        return _files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getAbsolutePath().hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FileViewHolder holder;

        if (convertView == null) {
            convertView = _inflater.inflate(R.layout.file_view, parent, false);
            holder = new FileViewHolder(
                    (TextView) convertView.findViewById(R.id.fileName),
                    (ImageView) convertView.findViewById(R.id.fileIcon)
            );
            convertView.setTag(holder);
        }else
            holder = (FileViewHolder) convertView.getTag();
        File item = getItem(position);
        holder.fileName.setText(item.getName());
        FileIconService.get().setIcon(item, holder.fileIcon);
        return convertView;
    }
}