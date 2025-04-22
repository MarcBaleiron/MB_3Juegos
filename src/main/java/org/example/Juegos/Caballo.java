package org.example.Juegos;

import org.example.DatabaseConnection;
import org.example.Ficha;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.UUID;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Caballo extends JPanel
{
    private int N;
    private JPanel [][] casillas;
    private int [][] tableroSolucion;
    private javax.swing.Timer timer;
    private java.util.List <Paso> pasos = new ArrayList <> ();
    private DefaultListModel <String> moveListModel;
    private Ficha.FichaCaballo fichaCaballo;

    public Caballo ()
    {
        // Se pide el tamaño deseado del tablero al usuario
        String input = JOptionPane.showInputDialog (this, "Ingrese el tamaño del tablero (=>5):", "Tamaño del Tablero", JOptionPane.QUESTION_MESSAGE);
        try
        {
            N = Integer.parseInt (input);
            if (N < 5) {
                throw new NumberFormatException ("El tamaño debe ser mayor o igual a 5.");
            }
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog (this, "Entrada inválida. Se usará el tamaño predeterminado de 8.", "Error", JOptionPane.ERROR_MESSAGE);
            N = 8;
        }

        casillas = new JPanel [N][N];
        tableroSolucion = new int [N][N];
        fichaCaballo = new Ficha.FichaCaballo ();

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

        for (int fila = 0; fila < N; fila++)
        {
            for (int columna = 0; columna < N; columna++)
            {
                JPanel cuadrado = new JPanel ();
                cuadrado.setBorder (new LineBorder (Color.BLACK));
                cuadrado.setBackground ((fila + columna) % 2 == 0 ? Color.WHITE : Color.GRAY);
                casillas [fila][columna] = cuadrado;
                tablero.add (cuadrado);
            }
        }

        // Se inicializa el tablero vacío
        for (int i = 0; i < N; i++)
        {
            Arrays.fill (tableroSolucion [i], -1);
        }

        // Posición inicial del caballo
        tableroSolucion [0][0] = 0;
        pasos.add (new Paso (0, 0, 0));
        resolverCaballoUtil (0, 0, 1);

        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if (!(i == 0 && j == 0))
                {
                    tableroSolucion [i][j] = -1;
                }
            }
        }

        actualizarTablero ();

        // Panel para mostrar los movimientos
        moveListModel = new DefaultListModel <> ();
        JList <String> moveList = new JList <> (moveListModel);
        JScrollPane moveScrollPane = new JScrollPane (moveList);
        moveScrollPane.setBorder (new EmptyBorder (10, 10, 10, 10));
        moveScrollPane.setPreferredSize (new Dimension (200, 0));

        // Tiempo que pasa entre cada número
        timer = new javax.swing.Timer (500, new ActionListener ()
        {
            int pasoIndex = 0;

            @Override
            public void actionPerformed (ActionEvent e)
            {
                if (pasoIndex < pasos.size ())
                {
                    Paso paso = pasos.get (pasoIndex);
                    tableroSolucion [paso.x][paso.y] = paso.numero;
                    actualizarTablero ();
                    moveListModel.addElement ("Move " + paso.numero + ": (" + paso.x + ", " + paso.y + ")");
                    pasoIndex++;
                }
                else
                {
                    timer.stop ();
                }
            }
        });
        timer.start ();

        // Botón para regresar a la pantalla inicial
        JButton botonRegresar = new JButton ("Regresar a la Pantalla Inicial");
        botonRegresar.addActionListener (new ActionListener ()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                SwingUtilities.getWindowAncestor (Caballo.this).dispose ();
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
        contenedorPrincipal.add (tablero, BorderLayout.CENTER);
        contenedorPrincipal.add (buttonPanel, BorderLayout.SOUTH);

        // Agregar el tablero y la lista de movimientos al panel principal
        add (contenedorPrincipal, BorderLayout.CENTER);
        add (moveScrollPane, BorderLayout.EAST);
    }

    // Metodo para guardar los movimientos en la base de datos
    private void saveMovesToDatabase ()
    {
        String gameId = UUID.randomUUID ().toString ();
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement stmt = conn.prepareStatement (
                     "INSERT INTO movimientos_caballo (game_id, move_number, x_position, y_position) VALUES (?, ?, ?, ?)")) {

            for (Paso paso : pasos)
            {
                stmt.setString (1, gameId);
                stmt.setInt (2, paso.numero);
                stmt.setInt (3, paso.x);
                stmt.setInt (4, paso.y);
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

    // Fórmula para resolver el problema
    private boolean resolverCaballoUtil (int x, int y, int movimiento)
    {
        if (movimiento == N * N)
        {
            return true;
        }

        List <int []> movimientosValidos = fichaCaballo.mover (x, y, N, tableroSolucion);

        for (int [] movimientoValido : movimientosValidos)
        {
            int siguienteX = movimientoValido [0];
            int siguienteY = movimientoValido [1];

            tableroSolucion [siguienteX][siguienteY] = movimiento;
            pasos.add (new Paso (siguienteX, siguienteY, movimiento));

            if (resolverCaballoUtil (siguienteX, siguienteY, movimiento + 1))
            {
                return true;
            }

            tableroSolucion [siguienteX][siguienteY] = -1;
            pasos.remove (pasos.size () - 1);
        }

        return false;
    }

    // Muestra la solución en el tablero
    private void actualizarTablero ()
    {
        for (int fila = 0; fila < N; fila++) {
            for (int columna = 0; columna < N; columna++)
            {
                casillas [fila][columna].removeAll ();
                casillas [fila][columna].setLayout (new BorderLayout ());
                if (tableroSolucion [fila][columna] != -1)
                {
                    JLabel label = new JLabel (String.valueOf (tableroSolucion [fila][columna]));
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

    private static class Paso
    {
        int x, y, numero;

        Paso (int x, int y, int numero)
        {
            this.x = x;
            this.y = y;
            this.numero = numero;
        }
    }
}