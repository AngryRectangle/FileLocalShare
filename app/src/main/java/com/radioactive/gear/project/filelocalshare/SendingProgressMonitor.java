package com.radioactive.gear.project.filelocalshare;

import android.util.Log;

import com.radioactive.gear.project.core.SocketWrapper;

public class SendingProgressMonitor {
    SocketWrapper wrapper;
    Thread receiveThread;
    public ProgressHandler progressListener;

    public SendingProgressMonitor(final SocketWrapper wrapper){
        this.wrapper = wrapper;
        receiveThread = new Thread(new Runnable()
        {
            public void run()
            {
                try {
                    while (true) {
                        if (progressListener != null) {
                            SocketWrapper.InteractionType type = wrapper.receiveCode();
                            if (type == SocketWrapper.InteractionType.PROGRESS_SENDING)
                                progressListener.execute(type, wrapper.receiveProgress());
                            if (type == SocketWrapper.InteractionType.SUCCESSFUL_SENDING)
                                progressListener.execute(type, 100);
                        }
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
    public interface ProgressHandler{
        void execute(SocketWrapper.InteractionType type, long progress);
    }
}
