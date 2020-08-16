package com.example.ya.filelocalshare;

import android.util.Log;

import com.radioactiv_gear_project.core.SocketWrapper;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;

public class SendingQueue {
    private final LinkedBlockingQueue<File> sendingQueue = new LinkedBlockingQueue<>();
    private Thread sendingThread;
    private SocketWrapper wrapper;

    public SendingQueue(final SocketWrapper wrapper){
        this.wrapper = wrapper;
    }
    private void createThread(){
        sendingThread = new Thread(new Runnable()
        {
            public void run()
            {
                try {
                    while (sendingQueue.size()>0){
                        wrapper.sendData(sendingQueue.remove());
                    }
                }catch (Exception e){
                    Log.e("ERROR", e.toString());
                }
            }
        });
        sendingThread.setPriority(10);
    }
    public void startDataTransmitting(){
        if(sendingThread==null||!sendingThread.isAlive()) {
            createThread();
        }
        if(!sendingThread.isAlive())
            sendingThread.start();
    }
    public void addFileToQueue(File file){
        sendingQueue.add(file);
        System.out.println("queue "+sendingQueue.size());
    }
}
