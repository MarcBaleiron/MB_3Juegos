package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;

public class DatabaseConnection {
    private static final String DB_NAME = "juegos.sqlite";
    private static final String DB_URL = "jdbc:sqlite:" + DB_NAME;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        try {
            // Ensure the database file exists
            File dbFile = new File(DB_NAME);
            if (!dbFile.exists()) {
                System.out.println("Creating new SQLite database file: " + DB_NAME);
            }

            // Create connection and tables
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement()) {

                // Create table for Knight's Tour
                stmt.execute("CREATE TABLE IF NOT EXISTS movimientos_caballo (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "game_id TEXT, " +
                        "move_number INTEGER, " +
                        "x_position INTEGER, " +
                        "y_position INTEGER)");

                // Create table for N-Queens
                stmt.execute("CREATE TABLE IF NOT EXISTS posiciones_reinas (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "game_id TEXT, " +
                        "queen_number INTEGER, " +
                        "row_position INTEGER, " +
                        "col_position INTEGER)");

                // Create table for Tower of Hanoi
                stmt.execute("CREATE TABLE IF NOT EXISTS movimientos_hanoi (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "game_id TEXT, " +
                        "move_number INTEGER, " +
                        "disk_number INTEGER, " +
                        "from_rod INTEGER, " +
                        "to_rod INTEGER)");

                System.out.println("Tables created successfully in SQLite database.");
            }
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
            e.printStackTrace();

            // Show error in a dialog if in a GUI context
            try {
                javax.swing.JOptionPane.showMessageDialog(null,
                        "Error initializing database: " + e.getMessage(),
                        "Database Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                // Ignore if not in GUI context
            }
        }
    }
}