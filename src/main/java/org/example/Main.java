package org.example;

public class Main {
    public static void main(String[] args) {
        // Initialize database
        DatabaseConnection.initializeDatabase();

        // Start application
        new Juegos();
    }
}