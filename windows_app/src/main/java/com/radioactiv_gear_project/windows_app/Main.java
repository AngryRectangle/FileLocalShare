package com.radioactiv_gear_project.windows_app;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import com.radioactiv_gear_project.core.NetworkInteraction;
import com.radioactiv_gear_project.core.SocketWrapper;

import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) {
        try{
            String pcName = InetAddress.getLocalHost().getHostName();
            DatagramPacket packet = NetworkInteraction.receivePacket(NetworkInteraction.DEFAULT_PC_GROUP);
            NetworkInteraction.multicast(pcName.getBytes(), NetworkInteraction.DEFAULT_ANDROID_GROUP);
            sleep(3);
            NetworkInteraction.multicast(("DickPC").getBytes(), NetworkInteraction.DEFAULT_ANDROID_GROUP);
            sleep(27);
            NetworkInteraction.multicast(("CockPC").getBytes(), NetworkInteraction.DEFAULT_ANDROID_GROUP);
            sleep(58);
            NetworkInteraction.multicast(("PooPC").getBytes(), NetworkInteraction.DEFAULT_ANDROID_GROUP);
            sleep(58);
            NetworkInteraction.multicast(("ShitPC").getBytes(), NetworkInteraction.DEFAULT_ANDROID_GROUP);
        }catch (Exception e){
            System.out.println(e.toString());
        }
        try {
            Socket socket = NetworkInteraction.host();
            System.out.println("Connected");
            SocketWrapper wrapper = new SocketWrapper(socket);
            while (true) {
                SocketWrapper.InteractionType type = wrapper.receiveCode();
                if(type==SocketWrapper.InteractionType.FILE_SENDING||type==SocketWrapper.InteractionType.DIRECTORY_SENDING){
                    System.out.println("Receiving");
                    wrapper.receiveData(type, "A:/FileLocalShare/");
                }
            }
        }catch (Exception e){
            System.out.println(e.toString());
        }
        /*try {
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

        }*/

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
