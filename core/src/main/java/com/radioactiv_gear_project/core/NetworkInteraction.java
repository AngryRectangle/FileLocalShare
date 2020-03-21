package com.radioactiv_gear_project.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class NetworkInteraction {
    public static void broadcast() throws IOException {
        DatagramSocket socket = new DatagramSocket();
        String dString = "q";
        byte[] buf;
        buf = dString.getBytes();
        InetAddress group = InetAddress.getByName("230.22.10.99");
        DatagramPacket packet;
        packet = new DatagramPacket(buf, buf.length, group, 22102);
        socket.send(packet);
        socket.close();
    }
}
