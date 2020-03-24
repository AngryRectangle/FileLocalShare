package com.radioactiv_gear_project.core;

import java.io.*;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class SocketWrapper {
    public enum InteractionType{
        DATA_SENDING,
        FILE_SENDING,
        DIRECTORY_SENDING,
        START_FILE_RECEIVING,
        PROGRESS_SENDING,
        SUCCESSFUL_SENDING,
        CANCEL_CONNECTION
    }
    DataInputStream reader;
    DataOutputStream writer;
    FileReceiver fileReceiver = new FileReceiver();
    DirectoryReceiver directoryReceiver = new DirectoryReceiver();
    InformationRouter informationRouter = new InformationRouter();

    public SocketWrapper(Socket socket) throws IOException {
        reader = new DataInputStream(socket.getInputStream());
        writer = new DataOutputStream(socket.getOutputStream());
    }

    public static String getString(DataInputStream stream)throws IOException{
        int length = stream.readInt();
        System.out.println("String length is "+length);
        return new String(getByteArray(stream, length));
    }
    //for small arrays
    private static byte[] getByteArray(DataInputStream stream, int size)throws IOException{
        byte[] output = new byte[size];
        int shift = 0;
        System.out.println("Getting byte array with length "+size);
        while (shift<size){
            shift+=stream.read(output);
        }
        return output;
    }

    public InteractionType receiveCode()throws IOException {
        return InteractionType.values()[getByteArray(reader, 1)[0]];
    }
    public void receiveData(String path) throws IOException, InterruptedException{
        InteractionType type = receiveCode();
        System.out.println("receiving type is "+type);
        /*writer.writeByte((byte)InteractionType.START_FILE_RECEIVING.ordinal());
        writer.flush();*/
        if(type==InteractionType.FILE_SENDING)
            fileReceiver.receiveFile(path);
        else if(type==InteractionType.DIRECTORY_SENDING)
            directoryReceiver.receiveDirectory(path);
    }
    public void sendData(File file) throws IOException{
        writer.write((byte) SocketWrapper.InteractionType.DATA_SENDING.ordinal());
        if(file.isDirectory())
            informationRouter.sendDirectory(file);
        else
            informationRouter.sendFile(file);
    }
    public long receiveProgress() throws IOException{
        return reader.readLong();
    }

    public class FileReceiver {
        public void receiveFile(String filePath) throws IOException, InterruptedException {
            String path = filePath != null ? filePath : "";
            System.out.println("Creating file on path "+path);
            File file = new File(path + SocketWrapper.getString(reader));
            if(file.createNewFile())
                System.out.println("Succesful created file "+file.getAbsolutePath());
            else
                System.out.println("Failed to create file "+file.getAbsolutePath());
            System.out.println("Receiving of "+file.getName()+" in path "+file.getPath());
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
                sleep(NetworkInteraction.INFORMATION_RECEIVING_SLEEP_TIME);
            }
            fileStream.close();
        }
    }
    public class DirectoryReceiver {
        public void receiveDirectory(String filePath)throws IOException, InterruptedException{
            String path = filePath != null ? filePath : "";
            File file = new File(path + SocketWrapper.getString(reader));
            System.out.println("Directory creating in "+path+" with name "+file.getName());
            if(file.mkdir()) {
                writeDirectory(file);
            }else
                System.out.println("Fuck u");
        }
        private void writeDirectory(File file) throws IOException, InterruptedException{
            int fileCount = reader.readInt();
            System.out.println("Writing directory "+file.getName()+" with "+fileCount+" files");
            System.out.println("Files will be in "+file.getCanonicalPath());
            for(int i =0; i<fileCount; i++){
                reader.skipBytes(1);
                receiveData(file.getAbsolutePath()+"/");
            }
        }
    }
    public class InformationRouter {
        protected void sendFile(File file) throws IOException {
            writer.write((byte) SocketWrapper.InteractionType.FILE_SENDING.ordinal());
            byte[] nameBytes = file.getName().getBytes();
            writer.writeInt(nameBytes.length);
            System.out.println("File sending with length of name "+file.getName().length());
            System.out.println("File name is "+file.getName());
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
        protected  void sendDirectory(File file)throws IOException, NullPointerException{
            writer.write((byte) SocketWrapper.InteractionType.DIRECTORY_SENDING.ordinal());
            byte[] nameBytes = file.getName().getBytes();
            writer.writeInt(nameBytes.length);
            writer.write(nameBytes);
            File[] files = file.listFiles();
            if(files!=null) {
                writer.writeInt(files.length);
                for(int i = 0;i<files.length; i++)
                    sendData(files[i]);
            }
            else{
                System.out.println("File isn't a directory");
                writer.write(0);
            }
            writer.flush();
        }
        //0-100
        protected void sendProgress(
                SocketWrapper.InteractionType type,
                long totalSended
        )throws IOException{
            writer.writeByte((byte)type.ordinal());
            writer.writeLong(totalSended);
        }
    }
}