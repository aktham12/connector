package com.atypon.connector;

import java.io.IOException;

public class DriverManager {

    private DriverManager() {}

    public static Connection getConnection(String url,String username,String password) throws IOException {
        return new Connection(url,username,password);
    }
}
