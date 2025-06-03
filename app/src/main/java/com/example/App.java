package com.example;

import java.sql.*;
import java.util.Scanner;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class App {
    private UserRepository userRepository;
    private Scanner scanner;

    public App(String dbUrl) {
        this.userRepository = new UserRepository(dbUrl);
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            displayMenu();
            String input = scanner.nextLine();

            try {
                switch (input) {
                    case "1":
                        addUser();
                        break;
                    case "2":
                        listUsers();
                        break;
                    case "3":
                        updateUser();
                        break;
                    case "4":
                        deleteUser();
                        break;
                    case "5":
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number for ID.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Add user");
        System.out.println("2. List users");
        System.out.println("3. Update user");
        System.out.println("4. Delete user");
        System.out.println("5. Exit");
        System.out.print("Choose an option: ");
    }

    private void addUser() throws SQLException {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        userRepository.addUser(new User(name));
    }

    private void listUsers() throws SQLException {
        List<User> users = userRepository.getAllUsers();
        System.out.println("Users:");
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            users.forEach(System.out::println);
        }
    }

    private void updateUser() throws SQLException {
        System.out.print("Enter user ID to update: ");
        int updateId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        User userToUpdate = new User(updateId, newName);
        if (userRepository.updateUser(userToUpdate)) {
            System.out.println("User updated!");
        } else {
            System.out.println("User not found.");
        }
    }

    private void deleteUser() throws SQLException {
        System.out.print("Enter user ID to delete: ");
        int deleteId = Integer.parseInt(scanner.nextLine());
        if (userRepository.deleteUser(deleteId)) {
            System.out.println("User deleted!");
        } else {
            System.out.println("User not found.");
        }
    }

    public static void main(String[] args) {
        String url = "jdbc:sqlite:sample.db";
        App app = new App(url);
        app.run();
    }
}