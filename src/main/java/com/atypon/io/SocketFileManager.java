package com.atypon.io;

import com.atypon.io.ZipManager;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.Socket;

public class SocketFileManager {

    private final DataOutputStream dataOutputStream;
    private final DataInputStream dis;

    private SocketFileManager(Socket socket) throws IOException {
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dis = new DataInputStream(socket.getInputStream());
    }

    public static SocketFileManager create(Socket socket) {
        try {
            return new SocketFileManager(socket);
        } catch (IOException e) {
            throw new RuntimeException("can't create a new File manager with this socket");
        }
    }


    public  void sendFile(String filePath) throws IOException {
        try {
            double n = Math.random();

            ZipManager.getInstance().zip("databasesToBeSend"+n+".zip", filePath);

            int bytes = 0;
            File fileToBeSent = new File("databasesToBeSend"+n+".zip");

            FileInputStream fileInputStream = new FileInputStream(fileToBeSent);

            dataOutputStream.writeLong(fileToBeSent.length());

            byte[] buffer = new byte[4 * 1024];

            while ((bytes = fileInputStream.read(buffer)) != -1) {
                dataOutputStream.write(buffer, 0, bytes);
                dataOutputStream.flush();
            }
            fileInputStream.close();
            FileUtils.delete(fileToBeSent);
        }finally {
        }
    }

    public  void receiveFile(String fileName,String savePath) throws IOException {
            FileOutputStream fos = new FileOutputStream(fileName + ".zip");
            int bytesRead;
            long size = dis.readLong();
            byte[] buffer = new byte[4 * 1024];

            while (size > 0 && (bytesRead = dis.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                fos.write(buffer, 0, bytesRead);
                size -= bytesRead;
            }
            fos.close();
            ZipManager.getInstance().unZip(new File(fileName + ".zip").toPath(), new File(savePath).toPath());
            FileUtils.delete(new File(fileName + ".zip"));


    }

}
