package org.example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Reinas extends JPanel {
    private int N;
    private JPanel[][] casillas;
    private int[][] tableroSolucion;
    private Ficha.FichaReinas fichaReinas;

    public Reinas() {
        // Ask the user for the board size
        String input = JOptionPane.showInputDialog(this, "Ingrese el tamaño del tablero (>=4):", "Tamaño del Tablero", JOptionPane.QUESTION_MESSAGE);
        try {
            N = Integer.parseInt(input);
            if (N < 4) {
                throw new NumberFormatException("El tamaño debe ser mayor o igual a 4.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Entrada inválida. Se usará el tamaño predeterminado de 8.", "Error", JOptionPane.ERROR_MESSAGE);
            N = 8;
        }

        casillas = new JPanel[N][N];
        tableroSolucion = new int[N][N];
        fichaReinas = new Ficha.FichaReinas();

        setLayout(new BorderLayout());

        // Create the board
        JPanel tablero = new JPanel(new GridLayout(N, N)) {
            @Override
            public Dimension getPreferredSize() {
                int size = Math.min(getParent().getWidth(), getParent().getHeight());
                return new Dimension(size, size);
            }

            @Override
            public void setBounds(int x, int y, int width, int height) {
                int size = Math.min(width, height);
                super.setBounds(x, y, size, size);
            }
        };

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                tablero.revalidate();
                tablero.repaint();
            }
        });

        for (int fila = 0; fila < N; fila++) {
            for (int columna = 0; columna < N; columna++) {
                JPanel cuadrado = new JPanel();
                cuadrado.setBorder(new LineBorder(Color.BLACK));
                cuadrado.setBackground((fila + columna) % 2 == 0 ? Color.WHITE : Color.GRAY);
                casillas[fila][columna] = cuadrado;
                tablero.add(cuadrado);
            }
        }

        // Declare and initialize DefaultTableModel and tableModel
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> positionList = new JList<>(listModel);
        JScrollPane listScrollPane = new JScrollPane(positionList);
        listScrollPane.setPreferredSize(new Dimension(200, 0)); // Set width for the list

        // Solve the N-Queens problem
        if (resolverReinas(0)) {
            actualizarTablero();
            actualizarLista(listModel);
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró solución para el tamaño del tablero especificado.", "Sin Solución", JOptionPane.ERROR_MESSAGE);
        }

        // Button to return to the main menu
        JButton botonRegresar = new JButton("Regresar a la Pantalla Inicial");
        botonRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.getWindowAncestor(Reinas.this).dispose();
                new Juegos();
            }
        });

        // Contenedor para el tablero y el botón
        JPanel contenedorPrincipal = new JPanel(new BorderLayout());
        contenedorPrincipal.setBorder(new EmptyBorder(30, 30, 30, 30));
        contenedorPrincipal.add(tablero, BorderLayout.CENTER);
        contenedorPrincipal.add(botonRegresar, BorderLayout.SOUTH);

        // Agregar el tablero y la tabla
        add(contenedorPrincipal, BorderLayout.CENTER);
        add(listScrollPane, BorderLayout.EAST);
    }

    // Backtracking algorithm to solve the N-Queens problem
    private boolean resolverReinas(int fila) {
        if (fila == N) {
            return true; // All queens are placed
        }

        for (int columna = 0; columna < N; columna++) {
            if (esSeguro(fila, columna)) {
                tableroSolucion[fila][columna] = 1; // Place the queen

                // Get valid moves for the queen
                List<int[]> movimientosValidos = fichaReinas.mover(fila, columna, N, tableroSolucion);

                if (resolverReinas(fila + 1)) {
                    return true;
                }

                tableroSolucion[fila][columna] = 0; // Backtrack
            }
        }

        return false; // No valid position found
    }

    // Check if it's safe to place a queen at (fila, columna)
    private boolean esSeguro(int fila, int columna) {
        // Check the column
        for (int i = 0; i < fila; i++) {
            if (tableroSolucion[i][columna] == 1) {
                return false;
            }
        }

        // Check the upper-left diagonal
        for (int i = fila, j = columna; i >= 0 && j >= 0; i--, j--) {
            if (tableroSolucion[i][j] == 1) {
                return false;
            }
        }

        // Check the upper-right diagonal
        for (int i = fila, j = columna; i >= 0 && j < N; i--, j++) {
            if (tableroSolucion[i][j] == 1) {
                return false;
            }
        }

        return true;
    }

    // Update the UI to display the solution
    private void actualizarTablero() {
        for (int fila = 0; fila < N; fila++) {
            for (int columna = 0; columna < N; columna++) {
                casillas[fila][columna].removeAll();
                casillas[fila][columna].setLayout(new BorderLayout());
                if (tableroSolucion[fila][columna] == 1) {
                    JLabel label = new JLabel("Q");
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setVerticalAlignment(SwingConstants.CENTER);
                    label.setFont(new Font("Arial", Font.BOLD, 18));
                    casillas[fila][columna].add(label, BorderLayout.CENTER);
                }
                casillas[fila][columna].revalidate();
                casillas[fila][columna].repaint();
            }
        }
    }

    // Update the table with queen positions
    private void actualizarLista(DefaultListModel<String> listModel) {
        listModel.clear(); // Clear existing items
        for (int fila = 0; fila < N; fila++) {
            for (int columna = 0; columna < N; columna++) {
                if (tableroSolucion[fila][columna] == 1) {
                    listModel.addElement("[" + (fila + 1) + ", " + (columna + 1) + "]"); // Add 1 for 1-based indexing
                }
            }
        }
    }
}