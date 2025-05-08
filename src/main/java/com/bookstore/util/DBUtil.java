package com.bookstore.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {

    private static Connection connection = null;

    public static Connection openConnection() {
        try {
            if(connection == null || connection.isClosed()) {
                Class.forName("org.postgresql.Driver");
                
                // Supabase connection parameters
                String user = "postgres";
                String password = "Do@nhkiet262271";
                String host = "db.owhkwengmxzzgrnhwjva.supabase.co";
                String port = "5432";
                String dbName = "postgres";
                
                // Use Properties to handle special characters in password
                java.util.Properties props = new java.util.Properties();
                props.setProperty("user", user);
                props.setProperty("password", password);
                
                // Form the connection URL without embedding credentials
                String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
                
                System.out.println("Attempting to connect to Supabase: " + host);
                
                // Connect using properties object to handle special characters in password
                connection = DriverManager.getConnection(url, props);
                System.out.println("Successfully connected to Supabase database");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }
    
    public static void closeConnection(Connection con) {
        try {
            if(con != null && !con.isClosed())
                con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection(ResultSet rs) {
        try {
            if(rs != null && !rs.isClosed())
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection(PreparedStatement ps) {
        try {
            if(ps != null && !ps.isClosed())
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

