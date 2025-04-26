package org.example.Controlador;

import org.example.Modelo.DatabaseConnection;
import org.example.Juegos;
import org.example.Modelo.HanoiModelo;
import org.example.Vista.HanoiVista;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.UUID;

public class HanoiControlador
{
    private HanoiModelo modelo;
    private HanoiVista vista;
    private Timer timer;
    private int moveIndex;

    public HanoiControlador(HanoiModelo modelo, HanoiVista vista)
    {
        this.modelo = modelo;
        this.vista = vista;
        this.moveIndex = 0;

        vista.actualizarTorres(modelo.getRods());

        setupEventHandlers();

        setupTimer();
    }

    private void setupEventHandlers()
    {
        // Botón de regreso a la pantalla inicial
        vista.getBotonRegresar().addActionListener(e ->
        {
            timer.stop();
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(vista);
            frame.dispose();
            new Juegos();
        });

        // Botón para guardar en la base de datos
        vista.getSaveButton().addActionListener(e -> saveMovesToDatabase());
    }

    private void setupTimer()
    {
        timer = new Timer(500, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (moveIndex < modelo.getMoves().size())
                {
                    HanoiModelo.Move move = modelo.getMoves().get(moveIndex);
                    int diskNumber = modelo.getDiskNumber(move, moveIndex);
                    modelo.realizarMovimiento(moveIndex);
                    vista.actualizarTorres(modelo.getRods());
                    vista.addMoveToList("Move " + (moveIndex + 1) +
                            ": Disk " + diskNumber +
                            " from Rod " + move.from +
                            " to Rod " + move.to);
                    moveIndex++;
                }
                else
                {
                    timer.stop();
                }
            }
        });
        // Iniciar la animación
        SwingUtilities.invokeLater(() -> timer.start());
    }

    private void saveMovesToDatabase()
    {
        String gameId = UUID.randomUUID().toString();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO movimientos_hanoi (game_id, move_number, disk_number, from_rod, to_rod) VALUES (?, ?, ?, ?, ?)")) {

            for (int i = 0; i < modelo.getMoves().size(); i++)
            {
                HanoiModelo.Move move = modelo.getMoves().get(i);
                int diskNumber = modelo.getDiskNumber(move, i);

                stmt.setString(1, gameId);
                stmt.setInt(2, i + 1);
                stmt.setInt(3, diskNumber);
                stmt.setInt(4, move.from);
                stmt.setInt(5, move.to);
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

    // Metodo estático para crear el panel completo de Hanoi (usado en Juegos.java)
    public static JPanel createHanoiPanel()
    {
        // Se pide el tamaño deseado del tablero al usuario
        String input = JOptionPane.showInputDialog(null,
                "Ingrese el número de discos (>=3):",
                "Torres de Hanoi",
                JOptionPane.QUESTION_MESSAGE);

        int numDisks;
        try
        {
            numDisks = Integer.parseInt(input);
            if (numDisks < 3)
            {
                throw new NumberFormatException("El número de discos debe ser mayor o igual a 3.");
            }
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(null,
                    "Entrada inválida. Se usará el número predeterminado de 3 discos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            numDisks = 3;
        }

        // Crear modelo, vista y controlador
        HanoiModelo modelo = new HanoiModelo(numDisks);
        HanoiVista vista = new HanoiVista();
        new HanoiControlador(modelo, vista);

        return vista;
    }
}