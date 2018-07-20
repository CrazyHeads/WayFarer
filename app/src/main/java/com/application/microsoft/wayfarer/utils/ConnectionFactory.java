package com.application.microsoft.wayfarer.utils;

import android.annotation.SuppressLint;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionFactory {

    @SuppressLint("AuthLeak")
    public static Connection getConnection(){
        Connection connection = null;
       // String connectionUrl = "jdbc:sqlserver://wayfarerapp.database.windows.net:1433;database=Wayfarer;user=wayfarer@wayfarerapp;password=Tiger@1998;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";

        try{
            //i thin
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
           // connection = DriverManager.getConnection("jdbc:sqlserver://wayfarerapp.database.windows.net:1433;database=Wayfarer","wayfarer@wayfarerapp","Tiger@1998");
            connection = DriverManager.getConnection("jdbc:sqlserver://wayfarerapp.database.windows.net:1433/Wayfarer;","wayfarer@wayfarerapp","Tiger@1998");
            System.out.println("Connected");
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