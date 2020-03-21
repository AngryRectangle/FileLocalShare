package com.radioactiv_gear_project.core;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;

public class DirectoryReceiver {
    public static void receiveDirectory(DataInputStream stream, String filePath)throws IOException, InterruptedException{
        String path = filePath != null ? filePath : "";
        File file = new File(path + SocketWrapper.getString(stream));
        if(file.mkdir()) {
            writeDirectory(file, stream);
        }else
            System.out.println("Fuck u");
    }
    private static void writeDirectory(File file, DataInputStream stream) throws IOException, InterruptedException{
        int fileCount = stream.readInt();
        for(int i =0; i<fileCount; i++){
            SocketWrapper.receiveData(file.getAbsolutePath(), stream);
        }
    }
}
