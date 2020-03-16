package com.company;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println(InetAddress.getLocalHost().getHostAddress());
            ServerSocket server = new ServerSocket(5000, 1);
            while (true) {
                // Блокируется до возникновения нового соединения:
                Socket socket = server.accept();
                System.out.println(socket.getInetAddress().getHostAddress());
                byte[] fileV = ReadFile(socket);
                System.out.println("Transmitted file with length "+fileV.length);
                WriteFile(fileV, "A://", "image.jpg");
            }
        }catch (IOException e){

        }

    }
    final static int DEFAULT_BLOCK_SIZE = 1024;
    public static byte[] ReadFile(Socket socket) {
        try {
            FileReceiver receiver = new FileReceiver(new DataInputStream(socket.getInputStream()));
            Thread receiveThread = receiver.ReceiveMessage();
            receiveThread.start();
            receiveThread.join();
            return receiver.output;
        } catch (Exception e) {
            System.out.println(e.toString());
            return  null;
        }
    }
    public static void WriteFile(byte[] bytes, String path, String name)throws IOException{
        File f = new File(path+name);
        f.createNewFile();
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bytes);
        fos.close();
    }
}
