package com.radioactive.gear.project.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class Receiver extends Waiter {
    private MulticastSocket socket;
    private ResultHandler handler;

    public Receiver(MulticastSocket socket, ResultHandler handler) {
        this.socket = socket;
        this.handler = handler;
    }

    @Override
    public void execute() {
        byte[] buf = new byte[64];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        try {
            socket.receive(packet);
            handler.execute(packet);
        }catch (IOException e){
            Debug.error(e.toString());
        }
    }

    public static abstract class ResultHandler{
        public abstract void execute(DatagramPacket packet);
    }
}
