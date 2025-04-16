package org.example;

import java.sql.*;

public class Database {
    private static final String URL = "jdbc:sqlite:movements.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS Movimientos_Caballo (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                move_number INTEGER NOT NULL,
                x_position INTEGER NOT NULL,
                y_position INTEGER NOT NULL,
                board_size INTEGER NOT NULL
            );
        """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Table 'Movimientos_Caballo' ensured to exist.");
        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
            e.printStackTrace();
        }
    }
}