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
        try {
            String pcName = InetAddress.getLocalHost().getHostName();
            DatagramPacket packet = NetworkInteraction.receivePacket(NetworkInteraction.DEFAULT_PC_GROUP);
            NetworkInteraction.multicast(pcName.getBytes(), NetworkInteraction.DEFAULT_ANDROID_GROUP);
            sleep(3);
            NetworkInteraction.multicast(("SecondPC").getBytes(), NetworkInteraction.DEFAULT_ANDROID_GROUP);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        try {
            Socket socket = NetworkInteraction.host();
            System.out.println("Connected");
            SocketWrapper wrapper = new SocketWrapper(socket);
            wrapper.startProgressSending(500);
            wrapper.addListener(new FileReceiver(wrapper));
            while (true){
                wrapper.executeListeners();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    public static class FileReceiver implements SocketWrapper.CodeReceiveHandler{
        SocketWrapper wrapper;
        public FileReceiver(SocketWrapper w){
            wrapper = w;
        }
        public void execute(SocketWrapper.InteractionType type){
            try {
                if (type == SocketWrapper.InteractionType.DATA_SENDING) {
                    System.out.println("Start receiving");
                    wrapper.receiveData("A:/FileLocalShare/");
                }
            }catch (Exception e){
                System.out.println(e.toString());
            }
        }
    }
}
