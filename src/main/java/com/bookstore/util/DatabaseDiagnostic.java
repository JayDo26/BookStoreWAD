package com.bookstore.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseDiagnostic {
    public static void runDiagnostic() {
        try (Connection connection = DBUtil.openConnection()) {
            System.out.println("=== DATABASE DIAGNOSTIC ===");
            System.out.println("Connected to database: " + connection.getCatalog());
            System.out.println("Current schema: " + connection.getSchema());
            
            // List all schemas
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT schema_name FROM information_schema.schemata");
                 ResultSet rs = ps.executeQuery()) {
                System.out.println("\nAvailable schemas:");
                while (rs.next()) {
                    System.out.println(" - " + rs.getString("schema_name"));
                }
            }
            
            // List all tables in all schemas
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT table_schema, table_name FROM information_schema.tables " +
                    "WHERE table_schema NOT IN ('pg_catalog', 'information_schema')");
                 ResultSet rs = ps.executeQuery()) {
                System.out.println("\nAll tables in database:");
                while (rs.next()) {
                    System.out.println(" - " + rs.getString("table_schema") + "." + 
                            rs.getString("table_name"));
                }
            }
            
            System.out.println("=== END DIAGNOSTIC ===");
        } catch (SQLException e) {
            System.err.println("Diagnostic error: " + e.getMessage());
        }
    }
}
