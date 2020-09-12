package com.radioactive.gear.project.core;

import java.net.DatagramPacket;

public abstract class Waiter implements Runnable {
    private final int WAIT_TIME = 100;

    protected Thread thread;
    private boolean isStopped = false;

    public void start() {
        if(isStopped){
            isStopped = false;
            return;
        }
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        isStopped = true;
    }

    @Override
    public void run() {
        try {
            while (!isStopped) {
                execute();
                Thread.sleep(WAIT_TIME);
            }
        }catch (InterruptedException e){
            Debug.warning(e.toString());
        }
    }

    public abstract void execute();
}
