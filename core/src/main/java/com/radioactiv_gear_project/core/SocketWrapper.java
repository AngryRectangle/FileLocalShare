package com.radioactiv_gear_project.core;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class SocketWrapper {
    public enum InteractionType{
        FILE_SENDING,
        DIRECTORY_SENDING,
        PROGRESS_SENDING,
        CANCEL_CONNECTION
    }
    DataInputStream reader;
    DataOutputStream writer;

    public SocketWrapper(Socket socket) throws IOException {
        reader = new DataInputStream(socket.getInputStream());
        writer = new DataOutputStream(socket.getOutputStream());
    }

    public static String getString(DataInputStream stream)throws IOException{
        int length = stream.readInt();
        return new String(getByteArray(stream, length));
    }
    //for small arrays
    private static byte[] getByteArray(DataInputStream stream, int size)throws IOException{
        byte[] output = new byte[size];
        int shift = 0;
        while (shift<output.length){
            shift+=stream.read(output);
        }
        return output;
    }

    public InteractionType receiveCode()throws IOException {
        return InteractionType.values()[getByteArray(reader, 1)[0]];
    }
    public void receiveData(InteractionType type, String path) throws IOException, InterruptedException{
        if(type==InteractionType.FILE_SENDING)
            FileReceiver.receiveFile(reader, path);
        else if(type==InteractionType.DIRECTORY_SENDING)
            DirectoryReceiver.receiveDirectory(reader, path);
    }
    protected static void receiveData(String path, DataInputStream reader) throws IOException, InterruptedException{
        InteractionType type = InteractionType.values()[getByteArray(reader, 1)[0]];
        if(type==InteractionType.FILE_SENDING)
            FileReceiver.receiveFile(reader, path);
        else if(type==InteractionType.DIRECTORY_SENDING)
            DirectoryReceiver.receiveDirectory(reader, path);
    }
    public void sendData(File file) throws IOException{
        if(file.isDirectory())
            InformationSender.sendDirectory(file, writer);
        else
            InformationSender.sendFile(file, writer);
    }
}
