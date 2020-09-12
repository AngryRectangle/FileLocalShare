package com.radioactive.gear.project.core;

import java.net.DatagramPacket;
import java.net.MulticastSocket;

import static com.radioactive.gear.project.core.NetworkInteraction.DEFAULT_PORT;

public class Pinger extends Waiter {
    private static final int WAIT_TIME = 250;

    public MulticastSocket socket;
    private DatagramPacket packet;

    public Pinger(MulticastSocket socket, byte[] buffer) {
        this.socket = socket;
        packet = new DatagramPacket(buffer, buffer.length, DEFAULT_PORT);
    }

    @Override
    public void execute() {
        try {
            socket.send(packet);
            thread.wait(WAIT_TIME);
        }catch (Exception e) {
            Debug.error(e.toString());
        }
    }
}
