package com.radioactive.gear.project.filelocalshare.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.radioactive.gear.project.filelocalshare.FileIconService;
import com.radioactive.gear.project.filelocalshare.FileViewHolder;
import com.radioactive.gear.project.filelocalshare.R;
import com.radioactive.gear.project.filelocalshare.util.file_provider.AFileProvider;

import java.io.File;

public class FileViewAdapter extends BaseAdapter {
    private AFileProvider _fileProvider;
    private LayoutInflater _inflater;

    public FileViewAdapter(LayoutInflater inflater, AFileProvider fileProvider) {
        super();
        _inflater = inflater;
        _fileProvider = fileProvider;

        _fileProvider.OnFileUpdate = new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getCount() {
        return _fileProvider.getCount();
    }

    @Override
    public File getItem(int position) {
        return _fileProvider.getFile(position);
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