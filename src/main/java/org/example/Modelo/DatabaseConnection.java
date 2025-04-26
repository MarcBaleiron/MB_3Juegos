package org.example.Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection
{
    private static final String DB_NAME = "DBJuegos";
    private static final String DB_URL = "jdbc:h2:./data/" + DB_NAME;
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    public static Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void initializeDatabase()
    {
        try
        {
            // Se establece conexión a la base de datos H2
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement())
            {
                stmt.execute("CREATE TABLE IF NOT EXISTS movimientos_caballo (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "game_id VARCHAR(255), " +
                        "move_number INT, " +
                        "x_position INT, " +
                        "y_position INT)");

                stmt.execute("CREATE TABLE IF NOT EXISTS posiciones_reinas (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "game_id VARCHAR(255), " +
                        "queen_number INT, " +
                        "row_position INT, " +
                        "col_position INT)");

                stmt.execute("CREATE TABLE IF NOT EXISTS movimientos_hanoi (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "game_id VARCHAR(255), " +
                        "move_number INT, " +
                        "disk_number INT, " +
                        "from_rod INT, " +
                        "to_rod INT)");

                System.out.println("Tablas creadas correctamente en H2.");
            }
            org.h2.tools.Server server = org.h2.tools.Server.createTcpServer("-tcpPort", "9092").start();
        }
        catch (SQLException e)
        {
            System.err.println("Error de inicialización: " + e.getMessage());
            e.printStackTrace();

            // Mostrar un mensaje de error si es necesario
            try
            {
                javax.swing.JOptionPane.showMessageDialog(null,
                        "Error inicializando la base: " + e.getMessage(),
                        "Database Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
            catch (Exception ex)
            {
                // Manejo de excepciones si no se puede mostrar el mensaje
            }
        }
    }
}