package com.atypon.connector;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;

public class App
{
    public static void main( String[] args ) throws IOException {
        Connection connection = DriverManager.getConnection(
                "localhost/8084","aktham","123"
        );
        System.out.println(   connection.find(
                "store","products","products.json",
                "productName","ali"
        ));
     ;




    }
}
