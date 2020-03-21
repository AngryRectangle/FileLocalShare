package com.radioactiv_gear_project.core;

import java.io.*;

import static java.lang.Thread.sleep;

public class FileReceiver {
    public static void receiveFile(DataInputStream stream, String filePath) throws IOException, InterruptedException {
        String path = filePath != null ? filePath : "";
        File file = new File(path + SocketWrapper.getString(stream));
        writeFile(file, stream);
    }

    private static void writeFile(File file, DataInputStream stream) throws IOException, InterruptedException {
        FileOutputStream fileStream = new FileOutputStream(file);
        long remaining = stream.readLong();
        byte[] buffer = new byte[NetworkInteraction.IO_BUFFER_SIZE];
        int readed;
        int needToRead;
        while (remaining > 0) {
            if (remaining < NetworkInteraction.IO_BUFFER_SIZE)
                needToRead = Math.min((int) remaining, buffer.length);
            else
                needToRead = NetworkInteraction.IO_BUFFER_SIZE;
            readed = stream.read(buffer, 0, needToRead);
            fileStream.write(buffer, 0, readed);
            fileStream.flush();
            remaining -= readed;
            sleep(NetworkInteraction.INFORMATION_RECEIVING_SLEEP_TIME);
        }
    }
}