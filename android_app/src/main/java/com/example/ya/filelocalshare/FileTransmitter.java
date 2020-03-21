package com.example.ya.filelocalshare;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;

public class FileTransmitter {
    //max size is 2GB;
    DataOutputStream outputStream;
    byte[] output;
    public FileTransmitter(DataOutputStream outputStream){
        this.outputStream = outputStream;
    }
    public Thread SendMessage(){
        return new FileReceiverThread("FileReceiver");
    }
    public void PrepareBytes(byte[] bytes){
        output = new byte[bytes.length+4];
        byte[] lengthBytes =  ByteBuffer.allocate(4).putInt(bytes.length).array();
        for(int i =0; i<4; i++)
            output[i] = lengthBytes[i];
        for(int i = 0; i<bytes.length; i++)
            output[i+4] = bytes[i];
        Log.d("DEBUG", output.length+"");
    }
    class FileReceiverThread extends Thread {

        FileReceiverThread(String name){
            super(name);
        }

        public void run(){
            try {
                Log.d("DEBUG", output.length+" bytes flushed");
                outputStream.write(output);
                Log.d("DEBUG", output.length+" bytes flushed");
                outputStream.flush();
                Log.d("DEBUG", output.length+" bytes flushed");

            }catch (Exception e){
                Log.e("ERROR", e.toString());
            }
        }
    }
}
