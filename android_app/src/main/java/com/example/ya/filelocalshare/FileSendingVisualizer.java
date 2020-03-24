package com.example.ya.filelocalshare;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.LinkedList;

public class FileSendingVisualizer {
    LinkedList<FileSendingViewData> viewData = new LinkedList<>();
    LinearLayout ticketHolder;
    MainActivity activity;
    private class FileSendingViewData{
        File file;
        long size;
        int progress;
        public View view;

        private FileSendingViewData(File file, long size, int progress) {
            this.file = file;
            this.size = size;
            this.progress = progress;
        }
    }
    public FileSendingVisualizer(LinearLayout ticketHolder, MainActivity activity){
        this.ticketHolder = ticketHolder;
        this.activity = activity;
    }
    public void addProgressTicket(File file){
        viewData.add(new FileSendingViewData(file, file.length(), 0));
    }
    public void viewFileSendingData(FileSendingViewData data){
        View view = activity.inflater.inflate(R.layout.sending_progress_bar, null, false);
        FileViewer.setFileIcon(data.file, (ImageView) view.findViewById(R.id.fileIcon), activity);
        ((TextView)view.findViewById(R.id.fileName)).setText(data.file.getName());
        ((TextView)view.findViewById(R.id.totalSizeText)).setText(data.size+"");
        data.view = view;
        ticketHolder.addView(view);
    }
    public void removeFirstTicket(){
        FileSendingViewData data = viewData.getFirst();
        if(data.view!=null)
            ticketHolder.removeView(data.view);
        viewData.removeFirst();
    }
    public void removeAllTickets(){
        int size = viewData.size();
        for(int i =0; i<size; i++)
            removeFirstTicket();
    }
}
