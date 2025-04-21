package org.example.Juegos;

import org.example.DatabaseConnection;
import org.example.Ficha;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Reinas extends JPanel
{
    private int N;
    private JPanel [][] casillas;
    private int [][] tableroSolucion;
    private Ficha.FichaReinas fichaReinas;

    public Reinas ()
    {
        // Se pide el tamaño deseado del tablero al usuario
        String input = JOptionPane.showInputDialog (this, "Ingrese el tamaño del tablero (>=4):", "Tamaño del Tablero", JOptionPane.QUESTION_MESSAGE);
        try
        {
            N = Integer.parseInt (input);
            if (N < 4)
            {
                throw new NumberFormatException ("El tamaño debe ser mayor o igual a 4.");
            }
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog (this, "Entrada inválida. Se usará el tamaño predeterminado de 8.", "Error", JOptionPane.ERROR_MESSAGE);
            N = 8;
        }

        casillas = new JPanel [N][N];
        tableroSolucion = new int [N][N];
        fichaReinas = new Ficha.FichaReinas ();

        setLayout (new BorderLayout ());

        // Creación del tablero
        JPanel tablero = new JPanel (new GridLayout (N, N))
        {
            @Override
            public Dimension getPreferredSize ()
            {
                // Ajusta el tamaño del tablero para que sea cuadrado
                int size = Math.min (getParent ().getWidth (), getParent ().getHeight ());
                return new Dimension (size, size);
            }

            @Override
            public void setBounds (int x, int y, int width, int height)
            {
                int size = Math.min (width, height);
                super.setBounds (x, y, size, size);
            }
        };

        addComponentListener (new java.awt.event.ComponentAdapter()
        {
            @Override
            public void componentResized (java.awt.event.ComponentEvent e)
            {
                tablero.revalidate ();
                tablero.repaint ();
            }
        });

        for (int fila = 0; fila < N; fila++)
        {
            for (int columna = 0; columna < N; columna++)
            {
                JPanel cuadrado = new JPanel ();
                cuadrado.setBorder (new LineBorder(Color.BLACK));
                cuadrado.setBackground ((fila + columna) % 2 == 0 ? Color.WHITE : Color.GRAY);
                casillas [fila][columna] = cuadrado;
                tablero.add (cuadrado);
            }
        }

        // Panel para mostrar las posiciones
        DefaultListModel <String> listModel = new DefaultListModel <> ();
        JList <String> positionList = new JList <> (listModel);
        JScrollPane listScrollPane = new JScrollPane (positionList);
        listScrollPane.setPreferredSize (new Dimension (200, 0));

        if (resolverReinas (0))
        {
            actualizarTablero ();
            actualizarLista (listModel);
        }
        else
        {
            JOptionPane.showMessageDialog (this, "No se encontró solución para el tamaño del tablero especificado.", "Sin Solución", JOptionPane.ERROR_MESSAGE);
        }

        // Botón para regresar a la pantalla inicial
        JButton botonRegresar = new JButton ("Regresar a la Pantalla Inicial");
        botonRegresar.addActionListener (new ActionListener ()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                SwingUtilities.getWindowAncestor (Reinas.this).dispose ();
                new Juegos();
            }
        });

        // Botón para guardar en la base de datos
        JButton saveButton = new JButton ("Guardar en Base de Datos");
        saveButton.addActionListener (e -> savePositionsToDatabase ());

        // Panel para los botones
        JPanel buttonPanel = new JPanel (new FlowLayout ());
        buttonPanel.add (botonRegresar);
        buttonPanel.add (saveButton);

        // Contenedor para el tablero y los botones
        JPanel contenedorPrincipal = new JPanel (new BorderLayout ());
        contenedorPrincipal.setBorder (new EmptyBorder(30, 30, 30, 30));
        contenedorPrincipal.add (tablero, BorderLayout.CENTER);
        contenedorPrincipal.add (buttonPanel, BorderLayout.SOUTH);

        // Agregar el tablero y la tabla
        add (contenedorPrincipal, BorderLayout.CENTER);
        add (listScrollPane, BorderLayout.EAST);
    }

    // Metodo para guardar las posiciones en la base de datos
    private void savePositionsToDatabase ()
    {
        String gameId = UUID.randomUUID ().toString ();
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement stmt = conn.prepareStatement (
                     "INSERT INTO posiciones_reinas (game_id, queen_number, row_position, col_position) VALUES (?, ?, ?, ?)")) {

            int queenNumber = 1;
            for (int fila = 0; fila < N; fila++)
            {
                for (int columna = 0; columna < N; columna++)
                {
                    if (tableroSolucion [fila][columna] == 1)
                    {
                        stmt.setString (1, gameId);
                        stmt.setInt (2, queenNumber++);
                        stmt.setInt (3, fila);
                        stmt.setInt (4, columna);
                        stmt.executeUpdate ();
                    }
                }
            }
            JOptionPane.showMessageDialog (this, "Posiciones guardadas en la base de datos",
                    "Guardado exitoso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
            JOptionPane.showMessageDialog (this, "Error al guardar en la base de datos: " + e.getMessage (),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Fórmula para resolver el problema
    private boolean resolverReinas (int fila)
    {
        if (fila == N)
        {
            return true;
        }

        for (int columna = 0; columna < N; columna++)
        {
            if (esSeguro (fila, columna))
            {
                tableroSolucion [fila][columna] = 1;

                List <int []> movimientosValidos = fichaReinas.mover (fila, columna, N, tableroSolucion);

                if (resolverReinas (fila + 1))
                {
                    return true;
                }

                tableroSolucion [fila][columna] = 0;
            }
        }

        return false;
    }

    // Ve si la posición es segura para colocar una reina
    private boolean esSeguro (int fila, int columna)
    {
        for (int i = 0; i < fila; i++)
        {
            if (tableroSolucion [i][columna] == 1)
            {
                return false;
            }
        }

        for (int i = fila, j = columna; i >= 0 && j >= 0; i--, j--)
        {
            if (tableroSolucion [i][j] == 1)
            {
                return false;
            }
        }

        for (int i = fila, j = columna; i >= 0 && j < N; i--, j++)
        {
            if (tableroSolucion [i][j] == 1)
            {
                return false;
            }
        }

        return true;
    }

    // Muestra la solución en el tablero
    private void actualizarTablero ()
    {
        for (int fila = 0; fila < N; fila++)
        {
            for (int columna = 0; columna < N; columna++)
            {
                casillas [fila][columna].removeAll ();
                casillas [fila][columna].setLayout (new BorderLayout());
                if (tableroSolucion [fila][columna] == 1)
                {
                    JLabel label = new JLabel ("Q");
                    label.setHorizontalAlignment (SwingConstants.CENTER);
                    label.setVerticalAlignment (SwingConstants.CENTER);
                    label.setFont (new Font ("Arial", Font.BOLD, 18));
                    casillas [fila][columna].add (label, BorderLayout.CENTER);
                }
                casillas [fila][columna].revalidate ();
                casillas [fila][columna].repaint ();
            }
        }
    }

    private void actualizarLista (DefaultListModel <String> listModel)
    {
        listModel.clear ();
        for (int fila = 0; fila < N; fila++)
        {
            for (int columna = 0; columna < N; columna++)
            {
                if (tableroSolucion [fila][columna] == 1)
                {
                    listModel.addElement ("[" + (fila + 1) + ", " + (columna + 1) + "]");
                }
            }
        }
    }
}