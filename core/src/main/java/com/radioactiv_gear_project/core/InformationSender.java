package com.radioactiv_gear_project.core;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
//max size 2gb
public class InformationSender {
    protected static void sendFile(File file, DataOutputStream stream) throws IOException {
        stream.write((byte) SocketWrapper.InteractionType.FILE_SENDING.ordinal());
        byte[] nameBytes = file.getName().getBytes();
        stream.writeInt(nameBytes.length);
        System.out.println("File sending with length of name "+file.getName().length());
        System.out.println("File name is "+file.getName());
        stream.write(nameBytes);
        stream.writeLong(file.length());
        stream.flush();
        DataInputStream fileStream = new DataInputStream(new FileInputStream(file));
        byte[] buffer = new byte[NetworkInteraction.IO_BUFFER_SIZE];
        int readed;
        while (fileStream.available() > 0) {
            readed = fileStream.read(buffer);
            stream.write(buffer, 0, readed);
            stream.flush();
        }
    }
    protected static void sendDirectory(File file, DataOutputStream stream)throws IOException, NullPointerException{
        stream.write((byte) SocketWrapper.InteractionType.DIRECTORY_SENDING.ordinal());
        stream.write(file.getName().length());
        stream.write(file.getName().getBytes());
        File[] files = file.listFiles();
        if(files!=null)
            stream.write(files.length);
        else{
            System.out.println("File isn't a directory");
            stream.write(0);
        }
        stream.flush();
    }
}
