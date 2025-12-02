package com.login;

import java.sql.*;
import java.util.Scanner;

public class LoginWithSetup {

    // Change these according to your MySQL settings
    private static final String DB_URL_WITHOUT_DB = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "user_db";  // new database
    private static final String DB_USER = "root";     // MySQL username
    private static final String DB_PASS = "localhost"; // ✅ your MySQL password

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            // 1. Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded...");

            // 2. Create Database if not exists
            try (Connection conn = DriverManager.getConnection(DB_URL_WITHOUT_DB, DB_USER, DB_PASS);
                 Statement stmt = conn.createStatement()) {

                String createDbSql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
                stmt.executeUpdate(createDbSql);
                System.out.println("Database ready: " + DB_NAME);
            }

            // 3. Connect to that database
            String DB_URL = DB_URL_WITHOUT_DB + DB_NAME;
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                 Statement stmt = conn.createStatement()) {

                // 4. Create table if not exists
                String createTableSql =
                        "CREATE TABLE IF NOT EXISTS user_details (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "username VARCHAR(50) UNIQUE NOT NULL, " +
                        "password VARCHAR(50) NOT NULL" +
                        ")";
                stmt.executeUpdate(createTableSql);
                System.out.println("Table ready: user_details");

                // 5. Insert a new user (Registration)
                System.out.println("\n--- User Registration ---");
                System.out.print("Enter new username: ");
                String newUsername = sc.next();

                System.out.print("Enter new password: ");
                String newPassword = sc.next();

                String insertSql = "INSERT INTO user_details (username, password) VALUES (?, ?)";
                try (PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
                    psInsert.setString(1, newUsername);
                    psInsert.setString(2, newPassword);
                    psInsert.executeUpdate();
                    System.out.println("User registered successfully!");
                } catch (SQLException e) {
                    System.out.println("Could not insert user (maybe username already exists): " + e.getMessage());
                }

                // 6. Login Check
                System.out.println("\n--- Login ---");
                System.out.print("Enter username: ");
                String loginUsername = sc.next();

                System.out.print("Enter password: ");
                String loginPassword = sc.next();

                String selectSql = "SELECT * FROM user_details WHERE username = ? AND password = ?";
                try (PreparedStatement psSelect = conn.prepareStatement(selectSql)) {
                    psSelect.setString(1, loginUsername);
                    psSelect.setString(2, loginPassword);

                    try (ResultSet rs = psSelect.executeQuery()) {
                        if (rs.next()) {
                            System.out.println("Login Successful...");
                        } else {
                            System.out.println("Oops...Login Failed...");
                        }
                    }
                }
            }

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}
