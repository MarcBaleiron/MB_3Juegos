package org.example;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Caballo extends JFrame
{

    private int N;
    private JPanel [][] casillas;
    private int [][] tableroSolucion;
    private int [] movimientosX = {2, 1, -1, -2, -2, -1, 1, 2};
    private int [] movimientosY = {1, 2, 2, 1, -1, -2, -2, -1};
    private javax.swing.Timer timer;
    private java.util.List <Paso> pasos = new ArrayList <> ();

    public Caballo ()
    {
        // Se pide el tamaño deseado del tablero al usuario
        String input = JOptionPane.showInputDialog (this, "Ingrese el tamaño del tablero (=>5):", "Tamaño del Tablero", JOptionPane.QUESTION_MESSAGE);
        try
        {
            N = Integer.parseInt (input);
            if (N < 5)
            {
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

        setTitle ("Problema del Caballo");
        setSize (550, 550);
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo (null);

        // Creación del tablero
        JPanel tablero = new JPanel (new GridLayout (N, N));

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

        for (int i = 0; i < N; i++)
        {
            Arrays.fill (tableroSolucion[i], -1);
        }

        // Posición inicial del caballo
        tableroSolucion [0][0] = 0;
        pasos.add (new Paso (0, 0, 0));
        resolverCaballoUtil (0, 0, 1);

        // Se inicializa el tablero vacío
        for (int i = 0; i < N; i++)
        {
            Arrays.fill (tableroSolucion [i], -1);
        }
        actualizarTablero ();

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
                    pasoIndex++;
                }
                else
                {
                    timer.stop ();
                }
            }
        });
        timer.start ();

        // Botón para regresar
        JButton botonRegresar = new JButton ("Regresar a la Pantalla Inicial");
        botonRegresar.addActionListener (new ActionListener ()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                dispose ();
                new Juegos ();
            }
        });

        // Contenedor para el tablero y el botón
        JPanel contenedorPrincipal = new JPanel (new BorderLayout ());
        contenedorPrincipal.setBorder (new EmptyBorder (30, 30, 30, 30));
        contenedorPrincipal.add (tablero, BorderLayout.CENTER);
        contenedorPrincipal.add (botonRegresar, BorderLayout.SOUTH);

        add (contenedorPrincipal);
        setVisible (true);
    }

    // Fórmula para resolver el problema
    private boolean resolverCaballoUtil (int x, int y, int movimiento)
    {
        if (movimiento == N * N)
        {
            return true;
        }

        int [][] movimientosPosibles = new int [8][3];
        int contadorMovimientos = 0;

        for (int i = 0; i < 8; i++)
        {
            int siguienteX = x + movimientosX [i];
            int siguienteY = y + movimientosY [i];

            if (esMovimientoValido(siguienteX, siguienteY))
            {
                int grado = calcularGrado(siguienteX, siguienteY);
                movimientosPosibles [contadorMovimientos][0] = siguienteX;
                movimientosPosibles [contadorMovimientos][1] = siguienteY;
                movimientosPosibles [contadorMovimientos][2] = grado;
                contadorMovimientos++;
            }
        }

        Arrays.sort (movimientosPosibles, 0, contadorMovimientos, (a, b) -> Integer.compare (a [2], b [2]));

        for (int i = 0; i < contadorMovimientos; i++)
        {
            int siguienteX = movimientosPosibles [i][0];
            int siguienteY = movimientosPosibles [i][1];

            tableroSolucion [siguienteX][siguienteY] = movimiento;
            pasos.add (new Paso (siguienteX, siguienteY, movimiento));

            if (resolverCaballoUtil (siguienteX, siguienteY, movimiento + 1))
            {
                return true;
            }

            tableroSolucion [siguienteX][siguienteY] = -1;
            pasos.remove (pasos.size() - 1);
        }

        return false;
    }

    // Cálculo del grado de cada casilla
    private int calcularGrado (int x, int y)
    {
        int grado = 0;
        for (int i = 0; i < 8; i++)
        {
            int siguienteX = x + movimientosX [i];
            int siguienteY = y + movimientosY [i];
            if (esMovimientoValido (siguienteX, siguienteY))
            {
                grado++;
            }
        }
        return grado;
    }

    private boolean esMovimientoValido (int x, int y)
    {
        return x >= 0 && x < N && y >= 0 && y < N && tableroSolucion [x][y] == -1;
    }

    // Muestra la solución el en tablero
    private void actualizarTablero ()
    {
        for (int fila = 0; fila < N; fila++)
        {
            for (int columna = 0; columna < N; columna++)
            {
                casillas [fila][columna].removeAll ();
                casillas [fila][columna].setLayout (new BorderLayout ());
                if (tableroSolucion [fila][columna] != -1)
                {
                    JLabel label = new JLabel (String.valueOf (tableroSolucion [fila][columna]));
                    label.setHorizontalAlignment (SwingConstants.CENTER);
                    label.setVerticalAlignment (SwingConstants.CENTER);
                    label.setFont (new Font ("Arial", Font.BOLD, 20));
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

    public static void main (String [] args)
    {
        new Caballo ();
    }
}
