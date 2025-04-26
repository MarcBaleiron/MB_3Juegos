package org.example.Controlador;

import org.example.Modelo.DatabaseConnection;
import org.example.Juegos;
import org.example.Modelo.CaballoModelo;
import org.example.Vista.CaballoVista;

import javax.swing.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.UUID;

public class CaballoControlador
{
    private CaballoModelo modelo;
    private CaballoVista vista;
    private Timer timer;
    private int currentStep = 0;

    public CaballoControlador(CaballoModelo modelo, CaballoVista vista)
    {
        this.modelo = modelo;
        this.vista = vista;

        vista.actualizarTablero(modelo.getTableroSolucion());

        setupEventHandlers();

        setupAnimationTimer();
    }

    private void setupEventHandlers()
    {
        // Botón de regreso
        vista.getBotonRegresar().addActionListener(e ->
        {
            timer.stop();
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(vista);
            frame.dispose();
            new Juegos();
        });

        // Botón de guardar
        vista.getSaveButton().addActionListener(e -> saveMovesToDatabase());
    }

    private void setupAnimationTimer()
    {
        timer = new Timer(500, e ->
        {
            if (currentStep < modelo.getSteps().size())
            {
                CaballoModelo.Paso step = modelo.getSteps().get(currentStep);
                modelo.updateBoard(step.getX(), step.getY(), step.getNumero());
                vista.actualizarTablero(modelo.getTableroSolucion());
                vista.addMoveToList("Move " + step.getNumero() + ": (" + step.getX() + ", " + step.getY() + ")");
                currentStep++;
            }
            else
            {
                timer.stop();
            }
        });
        timer.start();
    }

    private void saveMovesToDatabase()
    {
        String gameId = UUID.randomUUID().toString();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO movimientos_caballo (game_id, move_number, x_position, y_position) VALUES (?, ?, ?, ?)")) {

            for (CaballoModelo.Paso paso : modelo.getSteps())
            {
                stmt.setString(1, gameId);
                stmt.setInt(2, paso.getNumero());
                stmt.setInt(3, paso.getX());
                stmt.setInt(4, paso.getY());
                stmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(vista, "Movimientos guardados en la base de datos",
                    "Guardado exitoso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error al guardar en la base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo principal de construcción para usar en Juegos.java
    public static JPanel createCaballoPanel()
    {
        String input = JOptionPane.showInputDialog(null,
                "Ingrese el tamaño del tablero (=>5):",
                "Tamaño del Tablero",
                JOptionPane.QUESTION_MESSAGE);

        int boardSize;
        try
        {
            boardSize = Integer.parseInt(input);
            if (boardSize < 5)
            {
                throw new NumberFormatException();
            }
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(null,
                    "Entrada inválida. Se usará el tamaño predeterminado de 8.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            boardSize = 8;
        }

        CaballoModelo modelo = new CaballoModelo(boardSize);
        CaballoVista vista = new CaballoVista(boardSize);
        new CaballoControlador(modelo, vista);

        return vista;
    }
}