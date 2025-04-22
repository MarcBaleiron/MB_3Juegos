package org.example.Juegos;

import org.example.DatabaseConnection;
import org.example.Ficha;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Hanoi extends JPanel
{
    private int numDisks;
    private List <Integer> [] rods;
    private JPanel [] rodPanels;
    private javax.swing.Timer timer;
    private List <Move> moves;
    private int moveIndex;
    private Ficha.FichaHanoi fichaHanoi;
    private DefaultListModel <String> moveListModel;

    public Hanoi ()
    {
        // Se pide el tamaño deseado del tablero al usuario
        String input = JOptionPane.showInputDialog (this, "Ingrese el número de discos (>=3):", "Torres de Hanoi", JOptionPane.QUESTION_MESSAGE);
        try
        {
            numDisks = Integer.parseInt (input);
            if (numDisks < 3)
            {
                throw new NumberFormatException ("El número de discos debe ser mayor o igual a 3.");
            }
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog (this, "Entrada inválida. Se usará el número predeterminado de 3 discos.", "Error", JOptionPane.ERROR_MESSAGE);
            numDisks = 3;
        }

        rods = new ArrayList [3];
        for (int i = 0; i < 3; i++)
        {
            rods [i] = new ArrayList <> ();
        }
        for (int i = numDisks; i > 0; i--)
        {
            rods [0].add (i);
        }
        moves = new ArrayList <> ();
        moveIndex = 0;
        fichaHanoi = new Ficha.FichaHanoi ();

        setLayout (new BorderLayout ());

        // Creación de los paneles
        JPanel rodsPanel = new JPanel (new GridLayout(1, 3, 20, 0));
        rodPanels = new JPanel [3];
        for (int i = 0; i < 3; i++)
        {
            rodPanels [i] = new JPanel ();
            rodPanels [i].setLayout (new BoxLayout(rodPanels[i], BoxLayout.Y_AXIS));
            rodPanels [i].setBorder (BorderFactory.createLineBorder (Color.BLACK));
            rodsPanel.add (rodPanels [i]);
        }

        resolverHanoi (numDisks, 0, 2, 1);

        actualizarTorres ();

        moveListModel = new DefaultListModel <> ();
        JList <String> moveList = new JList <> (moveListModel);
        JScrollPane moveScrollPane = new JScrollPane (moveList);
        moveScrollPane.setPreferredSize (new Dimension (240, 0));

        // Botón para regresar a la pantalla inicial
        JButton botonRegresar = new JButton ("Regresar a la Pantalla Inicial");
        botonRegresar.addActionListener (new ActionListener ()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                SwingUtilities.getWindowAncestor (Hanoi.this).dispose ();
                new Juegos();
            }
        });

        // Botón para guardar en la base de datos
        JButton saveButton = new JButton ("Guardar en Base de Datos");
        saveButton.addActionListener (e -> saveMovesToDatabase ());

        // Panel para los botones
        JPanel buttonPanel = new JPanel (new FlowLayout ());
        buttonPanel.add (botonRegresar);
        buttonPanel.add (saveButton);

        // Contenedor para el tablero y los botones
        JPanel contenedorPrincipal = new JPanel (new BorderLayout ());
        contenedorPrincipal.setBorder (new EmptyBorder (30, 30, 30, 30));
        contenedorPrincipal.add (rodsPanel, BorderLayout.CENTER);
        contenedorPrincipal.add (buttonPanel, BorderLayout.SOUTH);

        // Agregar el tablero y la lista de movimientos al panel principal
        add (contenedorPrincipal, BorderLayout.CENTER);
        add (moveScrollPane, BorderLayout.EAST);

        revalidate ();
        repaint ();

        // Tiempo que pasa entre cada movimiento
        timer = new javax.swing.Timer (500, new ActionListener ()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                if (moveIndex < moves.size ()) {
                    Move move = moves.get (moveIndex);
                    int disk = rods [move.from].get (rods [move.from].size () - 1);
                    rods [move.to].add (rods [move.from].remove (rods [move.from].size () - 1));
                    actualizarTorres ();
                    moveListModel.addElement ("Move " + (moveIndex + 1) + ": Disk " + disk + " from Rod " + move.from + " to Rod " + move.to); // Add move to the list
                    moveIndex++;
                }
                else
                {
                    timer.stop ();
                }
            }
        });
        SwingUtilities.invokeLater (() -> timer.start ());
    }

    // Metodo para guardar los movimientos en la base de datos
    private void saveMovesToDatabase ()
    {
        String gameId = UUID.randomUUID ().toString ();
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement stmt = conn.prepareStatement (
                     "INSERT INTO movimientos_hanoi (game_id, move_number, disk_number, from_rod, to_rod) VALUES (?, ?, ?, ?, ?)")) {

            for (int i = 0; i < moves.size (); i++)
            {
                Move move = moves.get (i);
                int diskNumber = getDiskNumber (move, i);

                stmt.setString (1, gameId);
                stmt.setInt (2, i + 1);
                stmt.setInt (3, diskNumber);
                stmt.setInt (4, move.from);
                stmt.setInt (5, move.to);
                stmt.executeUpdate ();
            }
            JOptionPane.showMessageDialog (this, "Movimientos guardados en la base de datos",
                    "Guardado exitoso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
            JOptionPane.showMessageDialog (this, "Error al guardar en la base de datos: " + e.getMessage (),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getDiskNumber (Move move, int moveIndex)
    {
        List <Integer> [] simulatedRods = new ArrayList [3];
        for (int i = 0; i < 3; i++)
        {
            simulatedRods[i] = new ArrayList <> ();
        }

        for (int i = numDisks; i > 0; i--)
        {
            simulatedRods [0].add (i);
        }

        for (int i = 0; i < moveIndex; i++)
        {
            Move m = moves.get (i);
            simulatedRods [m.to].add (simulatedRods[m.from].remove (simulatedRods [m.from].size () - 1));
        }

        return simulatedRods [move.from].get (simulatedRods [move.from].size () - 1);
    }

    // Fórmula para resolver el problema
    private void resolverHanoi (int n, int from, int to, int aux)
    {
        if (n == 1)
        {
            moves.add (new Move(from, to));
            return;
        }
        resolverHanoi (n - 1, from, aux, to);
        moves.add (new Move(from, to));
        resolverHanoi (n - 1, aux, to, from);
    }

    // Muestra la solución en las columnas
    private void actualizarTorres ()
    {
        for (int i = 0; i < 3; i++)
        {
            rodPanels [i].removeAll ();
            rodPanels [i].setLayout (new GridBagLayout ());
            GridBagConstraints gbc = new GridBagConstraints ();
            gbc.gridx = 0;
            gbc.gridy = GridBagConstraints.RELATIVE;
            gbc.anchor = GridBagConstraints.SOUTH;
            gbc.weighty = 1.0;

            for (int j = rods [i].size() - 1; j >= 0; j--)
            {
                int disk = rods [i].get (j);
                JLabel diskLabel = new JLabel ();
                diskLabel.setOpaque (true);
                diskLabel.setBackground (Color.BLUE);
                diskLabel.setForeground (Color.WHITE);
                diskLabel.setBorder (BorderFactory.createLineBorder (Color.BLACK));
                diskLabel.setHorizontalAlignment (SwingConstants.CENTER);
                diskLabel.setText (String.valueOf (disk));

                int diskWidth = 30 + disk * 20;
                diskLabel.setPreferredSize (new Dimension(diskWidth, 20));

                rodPanels [i].add (diskLabel, gbc);
            }

            rodPanels [i].revalidate ();
            rodPanels [i].repaint ();
        }
    }

    private static class Move
    {
        int from, to;

        Move (int from, int to)
        {
            this.from = from;
            this.to = to;
        }
    }
}