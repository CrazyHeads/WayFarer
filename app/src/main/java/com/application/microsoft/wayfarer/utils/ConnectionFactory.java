package com.application.microsoft.wayfarer.utils;

import android.annotation.SuppressLint;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionFactory {

    @SuppressLint("AuthLeak")
    public static Connection getConnection(){
        Connection connection = null;
        try{
            System.out.println("Connected");

            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:jtds:sqlserver://wayfarer.database.windows.net:1433/wayfarer","wayfarer@wayfarer", "Tiger@1998");


        } catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
        }

        return connection;
    }
    public static void close(Object object) {
        try {
            if (object != null) {
                if (object instanceof Connection){
                    ((Connection)object).close();
                }
                else if (object instanceof Statement) {
                    ((Statement)object).close();
                }
                else if(object instanceof PreparedStatement) {
                    ((PreparedStatement)object).close();
                }
                else if (object instanceof CallableStatement) {
                    ((CallableStatement)object).close();
                }
                else if (object instanceof ResultSet) {
                    ((ResultSet)object).close();
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            // TODO: handle exception
        }
    }

}