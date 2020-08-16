package com.radioactiv_gear_project.windows_app;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class FileReceiver {
    //max size is 2GB;
    DataInputStream inputStream;
    byte[] output;
    public FileReceiver(DataInputStream inputStream){
        this.inputStream = inputStream;
    }
    public Thread ReceiveMessage(){
        return new FileReceiverThread("FileReceiver");
    }
    public  byte[] GetBytes(){
        return  output;
    }
    class FileReceiverThread extends Thread {

        FileReceiverThread(String name){
            super(name);

        }

        public void run(){

            try {
                System.out.println("Started "+ inputStream.available());
                while (inputStream.available()<4){
                    System.out.println("Sleep, "+inputStream.available());
                    sleep(5);
                }
                int length = inputStream.readInt();
                output = new byte[length];
                int blockCount = (int)Math.ceil((float)(length)/65536);
                byte[] block = new byte[65536];
                for(int i =0; i<blockCount; i++) {
                    while (inputStream.available() < Math.min(output.length-i*65536, 65536)) {
                        System.out.println("Sleep, " + inputStream.available());
                        sleep(5);
                    }
                    inputStream.read(block);
                    for(int j = 0; i*65536+j<length&&j<block.length; j++){
                        output[i*65536+j] = block[j];
                    }
                }
            }catch (Exception e){
                System.out.print(e.toString());
            }
        }
    }
}
