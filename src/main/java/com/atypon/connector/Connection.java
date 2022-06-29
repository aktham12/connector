package com.atypon.connector;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Connection {
    private final Socket socket;
    private final String url;
    private final String userName;
    private final String password;

    private final int port;

    BufferedReader bufferReader;


    protected Connection(String url, String userName, String password) throws IOException {
        String[] tmp = urlPortGetter(url);
        this.url = tmp[0];
        this.port = Integer.parseInt(tmp[1]);
        this.userName = userName;
        this.password = password;
        socket = new Socket(this.url, port);
        bufferReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        send(userName + " " + password);
        System.out.println(bufferReader.readLine());
        if(password.equals("admin") || userName.equals("admin")) {
            Scanner scanner = new Scanner(System.in);
            System.out.println(bufferReader.readLine());
            System.out.println("please enter a new password and username");
            userName = scanner.nextLine();
            password = scanner.nextLine();
            send(userName + " " + password);
            System.out.println(bufferReader.readLine());
        }


    }

    public void createDatabase(String databaseName) {
        try {
            send("createDatabase");
            send(databaseName);
            bufferReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void deleteDatabase(String databaseName) {
        try {
            send("deleteDatabase");
            send(databaseName);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createCollection(String databaseName, String collectionName) {
        try {
            send("createCollection");
            send(databaseName + " " + collectionName);
            bufferReader.readLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void deleteCollection(String databaseName, String collectionName) {
        try {
            send("deleteCollection");
            send(databaseName + " " + collectionName);
            bufferReader.readLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createDocument(String databaseName, String collectionName, String documentName) {
        try {
            send("createDocument");
            send(databaseName + " " + collectionName + " " + documentName);
            bufferReader.readLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteDocument(String databaseName, String collectionName, String documentName) {
        try {
            send("deleteDocument");
            send(databaseName + " " + collectionName + " " + documentName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void useDatabase(String databaseName) {
        try {
            send("useDatabase");
            send(databaseName);
            bufferReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void useCollection(String collectionName) {
        try {
            send("useCollection");
            send(collectionName);
            bufferReader.readLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void useDocument(String documentName) {
        try {
            send("useDocument");
            send(documentName);
            bufferReader.readLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(String json) {
        try {
            send("insert");

            send(json);
            bufferReader.readLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateOne(String property, String exactValue, String json) {
        try {
            send("updateOne");
            send(property + " " + exactValue + " " + json);
            bufferReader.readLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateMany(String property, String exactValue, String json) {
        try {
            send("updateMany");
            send(property + " " + exactValue + " " + json);
            bufferReader.readLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteOne(String property, String exactValue) {
        try {
            send("deleteOne");
            send(property + " " + exactValue + " ");
            bufferReader.readLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteMany(String property, String exactValue) {
        try {
            send("deleteMany");
            send(property + " " + exactValue + " ");
            bufferReader.readLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void exportDatabases(String pathToSave) {
        try {
            send("exportDatabases");
            SocketFileManager.create(this.socket).receiveFile("databases",pathToSave);
            bufferReader.readLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void exportDatabase(String databaseName, String pathToSave) {
        try {
            send("exportDatabase");
            send(databaseName);
            SocketFileManager.create(this.socket).receiveFile(databaseName,pathToSave);
            bufferReader.readLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void importDatabase(String path, String databaseName) {
        try {
            send(databaseName);
            send("importDatabase");
            SocketFileManager.create(this.socket).receiveFile(databaseName,path);

            bufferReader.readLine();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String find(String databaseName, String collectionName, String documentName, String property, String propertyValue) {
        try {
            send("find");
            int port = Integer.parseInt(bufferReader.readLine());
            bufferReader.readLine();
            Socket socket1 = new Socket("localhost", port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
            send(databaseName + " " + collectionName + " " + documentName, socket1);
            send("find" + " " + property + " " + propertyValue, socket1);
            return reader.readLine();

        } catch (IOException e) {
            throw new IllegalArgumentException("cannot connect to this socket");
        }
    }


    private void send(String message) throws IOException {
        PrintWriter pr = new PrintWriter(this.socket.getOutputStream());
        pr.println(message);
        pr.flush();
    }

    private void send(String message, Socket socket) throws IOException {
        PrintWriter pr = new PrintWriter(socket.getOutputStream());
        pr.println(message);
        pr.flush();
    }

    private String[] urlPortGetter(String url) {
        String[] temp = url.split("/");
        return temp;
    }

    public void scale(int n) throws IOException {
        send("scale");
        send(String.valueOf(n));
        bufferReader.readLine();
    }

    public void makeIndexOn(String property) throws IOException {
        send("makeIndexOn");
        send(property);
        bufferReader.readLine();
    }


    public void close() throws IOException {
        send("exit");
        socket.close();
    }

}
