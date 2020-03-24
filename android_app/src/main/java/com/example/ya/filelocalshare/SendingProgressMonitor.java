package com.example.ya.filelocalshare;

import android.util.Log;

import com.radioactiv_gear_project.core.SocketWrapper;

public class SendingProgressMonitor {
    SocketWrapper wrapper;
    Thread receiveThread;
    public SendingProgressMonitor(final SocketWrapper wrapper){
        this.wrapper = wrapper;
        receiveThread = new Thread(new Runnable()
        {
            public void run()
            {
                try {
                    while (true){
                        if (wrapper.receiveCode() == SocketWrapper.InteractionType.PROGRESS_SENDING)
                            Log.d("PROGRESS", wrapper.receiveProgress() + "");
                    /*if(wrapper.receiveCode() == SocketWrapper.InteractionType.SUCCESSFUL_SENDING)
                        break;*/
                    }
                }catch (Exception e){
                    Log.e("ERROR", e.toString());
                }
            }
        });
        receiveThread.setDaemon(true);
    }
    public void startMonitoring(){
        if(wrapper!=null)
            receiveThread.start();
    }
}
