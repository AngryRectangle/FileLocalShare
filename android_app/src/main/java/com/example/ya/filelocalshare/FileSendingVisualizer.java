package com.example.ya.filelocalshare;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.radioactiv_gear_project.core.SocketWrapper;

import java.io.File;
import java.util.LinkedList;

public class FileSendingVisualizer {
    LinkedList<FileSendingViewData> viewData = new LinkedList<>();
    LinearLayout ticketHolder;
    MainActivity activity;
    static String[] designations = new String[]{
            "B",
            "KB",
            "MB",
            "GB",
            "TB"
    };
    private class FileSendingViewData{
        File file;
        long size;
        long progress;
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
        FileSendingViewData data = new FileSendingViewData(file, file.length(), 0);
        viewData.add(data);
        viewFileSendingData(data);
    }
    private void viewFileSendingData(FileSendingViewData data){
        View view = activity.inflater.inflate(R.layout.sending_progress_bar, null, false);
        FileViewer.setFileIcon(data.file, (ImageView) view.findViewById(R.id.fileIcon), activity);
        ((TextView)view.findViewById(R.id.fileName)).setText(data.file.getName());
        ((TextView)view.findViewById(R.id.totalSizeText)).setText(designateBytesToString(data.size));
        ((TextView)view.findViewById(R.id.sendingSpeedText)).setText(designateBytesToString(0)+"/s");
        data.view = view;
        ticketHolder.addView(view);
    }
    public void removeFirstTicket(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FileSendingViewData data = viewData.getFirst();
                if(data.view!=null)
                    ticketHolder.removeView(data.view);
                viewData.removeFirst();
            }
        });
    }
    public void removeAllTickets(){
        int size = viewData.size();
        for(int i =0; i<size; i++)
            removeFirstTicket();
    }
    public void setProgress(final long sended){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FileSendingViewData data = viewData.getFirst();
                long delta = sended-data.progress;
                data.progress = sended;
                delta *= (1000f/ SocketWrapper.PROGRESS_SENDING_DELAY);
                ((ProgressBar)data.view.findViewById(R.id.sendingProgress))
                        .setProgress((int)Math.round((double)sended/data.size*100));
                ((TextView)data.view.findViewById(R.id.sendingSpeedText)).setText(designateBytesToString(delta)+"/s");
            }
        });
    }
    private String designateBytesToString(long bytes){
        int power = Converter.getPower(bytes);
        return Converter.bytesToPower(bytes, power)+Converter.powerToDesignation(designations, power);
    }
}
