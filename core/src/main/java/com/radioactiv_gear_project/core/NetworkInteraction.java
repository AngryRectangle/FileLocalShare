package com.radioactiv_gear_project.core;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class NetworkInteraction {
    static final int DEFAULT_PORT = 22099;
    public static final String DEFAULT_PC_GROUP = "230.22.10.99";
    public static final String DEFAULT_ANDROID_GROUP = "230.22.10.100";
    public static final int IO_BUFFER_SIZE = 65535;
    public static final int INFORMATION_RECEIVING_SLEEP_TIME = 2;
    static final int API_VERSION = 0;
    static final int TIME_TO_TIME_EXCEED = 100;
    static final int DEFAULT_MULTICAST_REQUEST_LENGTH = 4;

    public static void multicast(byte[] buffer, String groupAdress) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress group = InetAddress.getByName(groupAdress);
        System.out.println(group);
        DatagramPacket packet;
        packet = new DatagramPacket(buffer, buffer.length, group, DEFAULT_PORT);
        socket.send(packet);
        System.out.println("send");
        socket.close();
    }

    public static DatagramPacket[] receivePackets(String groupAdress) throws IOException, InterruptedException {
        MulticastSocket socket = new MulticastSocket(DEFAULT_PORT);
        InetAddress group = InetAddress.getByName(groupAdress);
        socket.joinGroup(group);
        ArrayList<DatagramPacket> packets = new ArrayList<>();
        socket.setSoTimeout(TIME_TO_TIME_EXCEED);
        DatagramPacket packet;
        byte[] buf;
        for (int i = 0; i < 10; i++) {
            try {
                buf = new byte[65535];
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                packets.add(packet);
            } catch (SocketTimeoutException ste) {
                break;
            }
        }
        socket.leaveGroup(group);
        socket.close();
        return packets.toArray(new DatagramPacket[0]);
    }

    public static DatagramPacket receivePacket(String groupAdress) throws IOException, InterruptedException {
        MulticastSocket socket = new MulticastSocket(DEFAULT_PORT);
        InetAddress group = InetAddress.getByName(groupAdress);
        socket.joinGroup(group);
        DatagramPacket packet;
        byte[] buf = new byte[65535];
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        socket.leaveGroup(group);
        socket.close();
        return packet;
    }

    public enum ConnectionType {
        SUCCESSFUL,
        OBSOLETE_SENDER,
        OBSOLETE_CLIENT,
        TIME_LIMIT_EXCEEDED
    }

    public static ConnectionType getConnectionType(DatagramPacket packet) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(packet.getData());
        int senderVersion = byteBuffer.getInt();
        if (senderVersion == API_VERSION)
            return ConnectionType.SUCCESSFUL;
        if (senderVersion < API_VERSION)
            return ConnectionType.OBSOLETE_SENDER;
        return ConnectionType.OBSOLETE_CLIENT;
    }

    public static byte[] getVersionByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(API_VERSION);
        return buffer.array();
    }

    public static InetAddress getAdress(DatagramPacket packet) {
        return packet.getAddress();
    }

    public static Socket host() throws IOException {
        ServerSocket serverSocket = null;
        serverSocket = new ServerSocket(DEFAULT_PORT, 1);
        //serverSocket.setSoTimeout(TIME_TO_TIME_EXCEED);
        return serverSocket.accept();
    }
    public static Socket connect(InetAddress address) throws IOException{
        return new Socket(address, DEFAULT_PORT);
    }
}
