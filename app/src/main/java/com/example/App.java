package com.example;

import java.sql.*;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:sample.db";

        try (Connection conn = DriverManager.getConnection(url);
             Scanner scanner = new Scanner(System.in)) {

            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)");

            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1. Add user");
                System.out.println("2. List users");
                System.out.println("3. Update user");
                System.out.println("4. Delete user");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                String input = scanner.nextLine();

                switch (input) {
                    case "1":
                        System.out.print("Enter name: ");
                        String name = scanner.nextLine();
                        String insertSql = "INSERT INTO users (name) VALUES (?)";
                        try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                            pstmt.setString(1, name);
                            pstmt.executeUpdate();
                            System.out.println("User added!");
                        }
                        break;

                    case "2":
                        ResultSet rs = stmt.executeQuery("SELECT * FROM users");
                        System.out.println("Users:");
                        while (rs.next()) {
                            System.out.println(rs.getInt("id") + ": " + rs.getString("name"));
                        }
                        break;

                    case "3":
                        System.out.print("Enter user ID to update: ");
                        int updateId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter new name: ");
                        String newName = scanner.nextLine();
                        String updateSql = "UPDATE users SET name = ? WHERE id = ?";
                        try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                            pstmt.setString(1, newName);
                            pstmt.setInt(2, updateId);
                            int affected = pstmt.executeUpdate();
                            if (affected > 0) {
                                System.out.println("User updated!");
                            } else {
                                System.out.println("User not found.");
                            }
                        }
                        break;

                    case "4":
                        System.out.print("Enter user ID to delete: ");
                        int deleteId = Integer.parseInt(scanner.nextLine());
                        String deleteSql = "DELETE FROM users WHERE id = ?";
                        try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                            pstmt.setInt(1, deleteId);
                            int affected = pstmt.executeUpdate();
                            if (affected > 0) {
                                System.out.println("User deleted!");
                            } else {
                                System.out.println("User not found.");
                            }
                        }
                        break;

                    case "5":
                        System.out.println("Goodbye!");
                        return;

                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
