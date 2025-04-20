package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import java.io.File;

public class DatabaseConnection
{
    private static final String DB_NAME = "juegos.sqlite";
    private static final String DB_URL = "jdbc:sqlite:" + DB_NAME;

    public static Connection getConnection () throws SQLException
    {
        return DriverManager.getConnection (DB_URL);
    }

    public static void initializeDatabase ()
    {
        try
        {
            // Se crea la Base de Datos si no existe
            File dbFile = new File (DB_NAME);
            if (!dbFile.exists ())
            {
                System.out.println ("Creando nueva base de SQLite: " + DB_NAME);
            }

            // Se establece conexión a la base de datos
            try (Connection conn = getConnection ();
                 Statement stmt = conn.createStatement ())
            {

                // Tabla Para el Problema del Caballo
                stmt.execute ("CREATE TABLE IF NOT EXISTS movimientos_caballo (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "game_id TEXT, " +
                        "move_number INTEGER, " +
                        "x_position INTEGER, " +
                        "y_position INTEGER)");

                // Tabla para el problema de las Reinas
                stmt.execute ("CREATE TABLE IF NOT EXISTS posiciones_reinas (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "game_id TEXT, " +
                        "queen_number INTEGER, " +
                        "row_position INTEGER, " +
                        "col_position INTEGER)");

                // Tabla para las torres de Hanoi
                stmt.execute ("CREATE TABLE IF NOT EXISTS movimientos_hanoi (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "game_id TEXT, " +
                        "move_number INTEGER, " +
                        "disk_number INTEGER, " +
                        "from_rod INTEGER, " +
                        "to_rod INTEGER)");

                System.out.println ("Tablas creadas correctamente.");
            }
        }
        catch (SQLException e)
        {
            System.err.println ("Error de inicialización: " + e.getMessage ());
            e.printStackTrace ();

            // Mostrar un mensaje de error si es necesario
            try
            {
                javax.swing.JOptionPane.showMessageDialog (null,
                        "Error inicializando la base: " + e.getMessage (),
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