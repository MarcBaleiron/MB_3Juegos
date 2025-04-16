package org.example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Caballo extends JPanel {

    private int N;
    private JPanel[][] casillas;
    private int[][] tableroSolucion;
    private javax.swing.Timer timer;
    private java.util.List<Paso> pasos = new ArrayList<>();
    private DefaultListModel<String> moveListModel; // Model for the move list
    private Ficha.FichaCaballo fichaCaballo;

    public Caballo() {
        Database.initializeDatabase();

        // Se pide el tamaño deseado del tablero al usuario
        String input = JOptionPane.showInputDialog(this, "Ingrese el tamaño del tablero (=>5):", "Tamaño del Tablero", JOptionPane.QUESTION_MESSAGE);
        try {
            N = Integer.parseInt(input);
            if (N < 5) {
                throw new NumberFormatException("El tamaño debe ser mayor o igual a 5.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Entrada inválida. Se usará el tamaño predeterminado de 8.", "Error", JOptionPane.ERROR_MESSAGE);
            N = 8;
        }

        casillas = new JPanel[N][N];
        tableroSolucion = new int[N][N];
        fichaCaballo = new Ficha.FichaCaballo();

        setLayout(new BorderLayout());

        // Creación del tablero
        JPanel tablero = new JPanel(new GridLayout(N, N)) {
            @Override
            public Dimension getPreferredSize() {
                // Ensure the board is square by using the smaller dimension
                int size = Math.min(getParent().getWidth(), getParent().getHeight());
                return new Dimension(size, size);
            }

            @Override
            public void setBounds(int x, int y, int width, int height) {
                // Ensure the board remains square when resized
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

        for (int i = 0; i < N; i++) {
            Arrays.fill(tableroSolucion[i], -1);
        }

        // Posición inicial del caballo
        tableroSolucion[0][0] = 0;
        pasos.add(new Paso(0, 0, 0));
        resolverCaballoUtil(0, 0, 1);

        // Se inicializa el tablero vacío
        for (int i = 0; i < N; i++) {
            Arrays.fill(tableroSolucion[i], -1);
        }
        actualizarTablero();

        // Panel para mostrar los movimientos
        moveListModel = new DefaultListModel<>();
        JList<String> moveList = new JList<>(moveListModel);
        JScrollPane moveScrollPane = new JScrollPane(moveList);
        moveScrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        moveScrollPane.setPreferredSize(new Dimension(200, 0)); // Set width for the move list

        // Tiempo que pasa entre cada número
        timer = new javax.swing.Timer(500, new ActionListener() {
            int pasoIndex = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (pasoIndex < pasos.size()) {
                    Paso paso = pasos.get(pasoIndex);
                    tableroSolucion[paso.x][paso.y] = paso.numero;
                    actualizarTablero();
                    moveListModel.addElement("Move " + paso.numero + ": (" + paso.x + ", " + paso.y + ")");
                    pasoIndex++;
                } else {
                    timer.stop();
                }
            }
        });
        timer.start();

        // Botón para regresar
        JButton botonRegresar = new JButton("Regresar a la Pantalla Inicial");
        botonRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.getWindowAncestor(Caballo.this).dispose();
                new Juegos();
            }
        });

        // Contenedor para el tablero y el botón
        JPanel contenedorPrincipal = new JPanel(new BorderLayout());
        contenedorPrincipal.setBorder(new EmptyBorder(30, 30, 30, 30));
        contenedorPrincipal.add(tablero, BorderLayout.CENTER);
        contenedorPrincipal.add(botonRegresar, BorderLayout.SOUTH);

        // Agregar el tablero y la lista de movimientos al panel principal
        add(contenedorPrincipal, BorderLayout.CENTER);
        add(moveScrollPane, BorderLayout.EAST);
    }

    private void saveMovementsToDatabase() {
        try {
            Database.createTableIfNotExists(); // Ensures the table exists
            String insertSQL = "INSERT INTO movimientos_Caballo (move_number, x_position, y_position, board_size) VALUES (?, ?, ?, ?)";

            try (Connection conn = Database.connect();
                 PreparedStatement stmt = conn.prepareStatement(insertSQL)) {

                for (Paso paso : pasos) {
                    stmt.setInt(1, paso.numero);
                    stmt.setInt(2, paso.x);
                    stmt.setInt(3, paso.y);
                    stmt.setInt(4, N);
                    stmt.addBatch();
                }

                stmt.executeBatch();
                System.out.println("Movements saved successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error saving movements to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Fórmula para resolver el problema
    private boolean resolverCaballoUtil(int x, int y, int movimiento) {
        if (movimiento == N * N) {
            saveMovementsToDatabase();
            return true;
        }

        List<int[]> movimientosValidos = fichaCaballo.mover(x, y, N, tableroSolucion);

        for (int [] movimientoValido : movimientosValidos) {
            int siguienteX = movimientoValido[0];
            int siguienteY = movimientoValido[1];

            tableroSolucion[siguienteX][siguienteY] = movimiento;
            pasos.add(new Paso(siguienteX, siguienteY, movimiento));

            if (resolverCaballoUtil(siguienteX, siguienteY, movimiento + 1)) {
                return true;
            }

            tableroSolucion[siguienteX][siguienteY] = -1;
            pasos.remove(pasos.size() - 1);
        }

        return false;
    }

    // Muestra la solución el en tablero
    private void actualizarTablero() {
        for (int fila = 0; fila < N; fila++) {
            for (int columna = 0; columna < N; columna++) {
                casillas[fila][columna].removeAll();
                casillas[fila][columna].setLayout(new BorderLayout());
                if (tableroSolucion[fila][columna] != -1) {
                    JLabel label = new JLabel(String.valueOf(tableroSolucion[fila][columna]));
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

    private static class Paso {
        int x, y, numero;

        Paso(int x, int y, int numero) {
            this.x = x;
            this.y = y;
            this.numero = numero;
        }
    }
}