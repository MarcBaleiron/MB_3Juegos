package org.example.Controlador;

import org.example.Modelo.DatabaseConnection;
import org.example.Juegos;
import org.example.Modelo.ReinasModelo;
import org.example.Vista.ReinasVista;

import javax.swing.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.UUID;

public class ReinasControlador
{
    private ReinasModelo modelo;
    private ReinasVista vista;

    public ReinasControlador(ReinasModelo modelo, ReinasVista vista)
    {
        this.modelo = modelo;
        this.vista = vista;

        vista.actualizarTablero(modelo.getTableroSolucion());
        vista.actualizarLista(modelo.getTableroSolucion());

        setupEventHandlers();
    }

    private void setupEventHandlers()
    {
        // Botón de regreso a la pantalla inicial
        vista.getBotonRegresar().addActionListener(e ->
        {
            SwingUtilities.getWindowAncestor(vista).dispose();
            new Juegos();
        });

        // Botón para guardar en la base de datos
        vista.getSaveButton().addActionListener(e -> savePositionsToDatabase());
    }

    private void savePositionsToDatabase()
    {
        String gameId = UUID.randomUUID().toString();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO posiciones_reinas (game_id, queen_number, row_position, col_position) VALUES (?, ?, ?, ?)")) {

            int queenNumber = 1;
            for (int fila = 0; fila < modelo.getN(); fila++)
            {
                for (int columna = 0; columna < modelo.getN(); columna++)
                {
                    if (modelo.getTableroSolucion()[fila][columna] == 1)
                    {
                        stmt.setString(1, gameId);
                        stmt.setInt(2, queenNumber++);
                        stmt.setInt(3, fila);
                        stmt.setInt(4, columna);
                        stmt.executeUpdate();
                    }
                }
            }
            JOptionPane.showMessageDialog(vista, "Posiciones guardadas en la base de datos",
                    "Guardado exitoso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error al guardar en la base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo estático para crear el panel completo (usado en Juegos.java)
    public static JPanel createReinasPanel()
    {
        // Se pide el tamaño deseado del tablero al usuario
        String input = JOptionPane.showInputDialog(null,
                "Ingrese el tamaño del tablero (>=4):",
                "Tamaño del Tablero",
                JOptionPane.QUESTION_MESSAGE);

        int boardSize;
        try
        {
            boardSize = Integer.parseInt(input);
            if (boardSize < 4)
            {
                throw new NumberFormatException("El tamaño debe ser mayor o igual a 4.");
            }
        } catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(null,
                    "Entrada inválida. Se usará el tamaño predeterminado de 8.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            boardSize = 8;
        }

        // Crear modelo, vista y controlador
        ReinasModelo modelo = new ReinasModelo(boardSize);

        // Verificar si se encontró solución
        if (modelo.getTableroSolucion()[0][0] == 0 && modelo.getPosicionesReinas().isEmpty())
        {
            JOptionPane.showMessageDialog(null,
                    "No se encontró solución para el tamaño del tablero especificado.",
                    "Sin Solución",
                    JOptionPane.ERROR_MESSAGE);
            return new JPanel(); // Devolver un panel vacío si no hay solución
        }

        ReinasVista vista = new ReinasVista(boardSize);
        new ReinasControlador(modelo, vista);

        return vista;
    }
}