package com.radioactive.gear.project.core;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;

public class SocketWrapper {
    public enum InteractionType {
        CANCEL_CONNECTION,
        DATA_SENDING,
        FILE_SENDING,
        DIRECTORY_SENDING,
        START_FILE_RECEIVING,
        PROGRESS_SENDING,
        SUCCESSFUL_SENDING
    }
    public static int PROGRESS_SENDING_DELAY = 500;
    DataInputStream reader;
    DataOutputStream writer;
    FileReceiver fileReceiver = new FileReceiver();
    DirectoryReceiver directoryReceiver = new DirectoryReceiver();
    InformationRouter informationRouter = new InformationRouter();
    Timer progressSendingTimer;
    long totalReceived = 0;
    private ArrayList<CodeReceiveHandler> listeners = new ArrayList<>();

    public SocketWrapper(Socket socket) throws IOException {
        reader = new DataInputStream(socket.getInputStream());
        writer = new DataOutputStream(socket.getOutputStream());
    }

    public void startProgressSending(int delayInMs) {
        if (progressSendingTimer != null)
            progressSendingTimer.cancel();
        System.out.println("Timer is starting");
        progressSendingTimer = new Timer();
        progressSendingTimer.scheduleAtFixedRate(new DataSendingClass(), delayInMs,delayInMs);
    }

    private class DataSendingClass extends TimerTask {
        @Override
        public void run() {
            if (totalReceived > 0)
                try {
                    informationRouter.sendProgress(totalReceived);
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
        }
    }

    public static String getString(DataInputStream stream) throws IOException {
        int length = stream.readInt();
        System.out.println("String length is " + length);
        return new String(getByteArray(stream, length));
    }

    //for small arrays
    private static byte[] getByteArray(DataInputStream stream, int size) throws IOException {
        byte[] output = new byte[size];
        int shift = 0;
        while (shift < size) {
            shift += stream.read(output);
        }
        return output;
    }

    public InteractionType receiveCode() throws IOException {
        InteractionType type = InteractionType.values()[getByteArray(reader, 1)[0]];
        return type;
    }

    public void receiveData(String path) throws IOException, InterruptedException {
        totalReceived = 0;
        InteractionType type = receiveCode();
        System.out.println("receiving type is " + type);
        writer.writeByte((byte) InteractionType.START_FILE_RECEIVING.ordinal());
        writer.flush();
        if (type == InteractionType.FILE_SENDING)
            fileReceiver.receiveFile(path);
        else if (type == InteractionType.DIRECTORY_SENDING)
            directoryReceiver.receiveDirectory(path);
        totalReceived = 0;
        writer.writeByte((byte) InteractionType.SUCCESSFUL_SENDING.ordinal());
        writer.flush();
    }

    private void receiveInDirectoryData(String path) throws IOException, InterruptedException {
        InteractionType type = receiveCode();
        System.out.println("receiving type is " + type);
        if (type == InteractionType.FILE_SENDING)
            fileReceiver.receiveFile(path);
        else if (type == InteractionType.DIRECTORY_SENDING)
            directoryReceiver.receiveDirectory(path);
    }

    public void sendData(File file) throws IOException {
        System.out.println("Start to sending code...");
        writer.write((byte) InteractionType.DATA_SENDING.ordinal());
        writer.flush();
        System.out.println("Sending code...");
        if (file.isDirectory())
            informationRouter.sendDirectory(file);
        else
            informationRouter.sendFile(file);
    }

    private void sendInDirectoryData(File file) throws IOException {
        if (file.isDirectory())
            informationRouter.sendDirectory(file);
        else
            informationRouter.sendFile(file);
    }

    public long receiveProgress() throws IOException {
        return reader.readLong();
    }

    public class FileReceiver {
        public void receiveFile(String filePath) throws IOException, InterruptedException {
            String path = filePath != null ? filePath : "";
            System.out.println("Creating file on path " + path);
            File file = new File(path + SocketWrapper.getString(reader));
            if (file.createNewFile())
                System.out.println("Succesful created file " + file.getAbsolutePath());
            else
                System.out.println("Failed to create file " + file.getAbsolutePath());
            System.out.println("Receiving of " + file.getName() + " in path " + file.getPath());
            writeFile(file);
            System.out.println("Successful");
        }

        private void writeFile(File file) throws IOException, InterruptedException {
            FileOutputStream fileStream = new FileOutputStream(file);
            long remaining = reader.readLong();
            byte[] buffer = new byte[NetworkInteraction.IO_BUFFER_SIZE];
            int readed;
            int needToRead;
            while (remaining > 0) {
                if (remaining < NetworkInteraction.IO_BUFFER_SIZE)
                    needToRead = Math.min((int) remaining, buffer.length);
                else
                    needToRead = NetworkInteraction.IO_BUFFER_SIZE;
                readed = reader.read(buffer, 0, needToRead);
                fileStream.write(buffer, 0, readed);
                fileStream.flush();
                remaining -= readed;
                totalReceived += readed;
                sleep(NetworkInteraction.INFORMATION_RECEIVING_SLEEP_TIME);
            }
            fileStream.close();
        }
    }

    public class DirectoryReceiver {
        public void receiveDirectory(String filePath) throws IOException, InterruptedException {
            String path = filePath != null ? filePath : "";
            File file = new File(path + SocketWrapper.getString(reader));
            System.out.println("Directory creating in " + path + " with name " + file.getName());
            if (file.mkdir()) {
                writeDirectory(file);
            } else
                System.out.println("Fuck u");
        }

        private void writeDirectory(File file) throws IOException, InterruptedException {
            int fileCount = reader.readInt();
            System.out.println("Writing directory " + file.getName() + " with " + fileCount + " files");
            System.out.println("Files will be in " + file.getCanonicalPath());
            for (int i = 0; i < fileCount; i++) {
                receiveInDirectoryData(file.getAbsolutePath() + "/");
            }
        }
    }

    public class InformationRouter {
        protected void sendFile(File file) throws IOException {
            writer.write((byte) SocketWrapper.InteractionType.FILE_SENDING.ordinal());
            byte[] nameBytes = file.getName().getBytes();
            writer.writeInt(nameBytes.length);
            System.out.println("File sending with length of name " + file.getName().length());
            System.out.println("File name is " + file.getName());
            writer.write(nameBytes);
            writer.writeLong(file.length());
            writer.flush();
            DataInputStream fileStream = new DataInputStream(new FileInputStream(file));
            byte[] buffer = new byte[NetworkInteraction.IO_BUFFER_SIZE];
            int readed;
            while (fileStream.available() > 0) {
                readed = fileStream.read(buffer);
                writer.write(buffer, 0, readed);
                writer.flush();
            }
        }

        protected void sendDirectory(File file) throws IOException, NullPointerException {
            writer.write((byte) SocketWrapper.InteractionType.DIRECTORY_SENDING.ordinal());
            byte[] nameBytes = file.getName().getBytes();
            writer.writeInt(nameBytes.length);
            writer.write(nameBytes);
            File[] files = file.listFiles();
            if (files != null) {
                writer.writeInt(files.length);
                for (int i = 0; i < files.length; i++)
                    sendInDirectoryData(files[i]);
            } else {
                System.out.println("File isn't a directory");
                writer.write(0);
            }
            writer.flush();
        }

        //0-100
        protected void sendProgress(
                long totalSended
        ) throws IOException {
            writer.writeByte((byte) InteractionType.PROGRESS_SENDING.ordinal());
            writer.writeLong(totalSended);
        }
    }

    public interface CodeReceiveHandler {
        void execute(InteractionType type);
    }

    public void addListener(CodeReceiveHandler handler) {
        listeners.add(handler);
    }

    public void executeListeners()throws IOException {
        InteractionType type = receiveCode();
        for(int i =0; i<listeners.size(); i++)
            listeners.get(i).execute(type);
    }
}