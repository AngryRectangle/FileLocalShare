package com.company;
import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println(InetAddress.getLocalHost().getHostAddress());
            ServerSocket server = new ServerSocket(5000, 1);
            while (true) {
                // Блокируется до возникновения нового соединения:
                Socket socket = server.accept();
                System.out.println(socket.getInetAddress().getHostAddress());
            }
        }catch (IOException e){

        }

    }
}
