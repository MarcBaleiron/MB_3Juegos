package org.example.Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;

public class ReinasVista extends JPanel
{
    private int N;
    private JPanel[][] casillas;
    private DefaultListModel<String> listModel;
    private JList<String> positionList;
    private JButton botonRegresar;
    private JButton saveButton;

    public ReinasVista(int N)
    {
        this.N = N;
        this.casillas = new JPanel[N][N];
        this.listModel = new DefaultListModel<>();

        setLayout(new BorderLayout());

        // Creación del tablero
        JPanel tablero = createBoardPanel();

        // Panel para mostrar las posiciones
        positionList = new JList<>(listModel);
        JScrollPane listScrollPane = new JScrollPane(positionList);
        listScrollPane.setPreferredSize(new Dimension(200, 0));

        // Botones
        botonRegresar = new JButton("Regresar a la Pantalla Inicial");
        saveButton = new JButton("Guardar en Base de Datos");

        // Panel para los botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(botonRegresar);
        buttonPanel.add(saveButton);

        // Contenedor para el tablero y los botones
        JPanel contenedorPrincipal = new JPanel(new BorderLayout());
        contenedorPrincipal.setBorder(new EmptyBorder(30, 30, 30, 30));
        contenedorPrincipal.add(tablero, BorderLayout.CENTER);
        contenedorPrincipal.add(buttonPanel, BorderLayout.SOUTH);

        // Agregar el tablero y la tabla
        add(contenedorPrincipal, BorderLayout.CENTER);
        add(listScrollPane, BorderLayout.EAST);
    }

    private JPanel createBoardPanel()
    {
        JPanel tablero = new JPanel(new GridLayout(N, N))
        {
            @Override
            public Dimension getPreferredSize()
            {
                int size = Math.min(getParent().getWidth(), getParent().getHeight());
                return new Dimension(size, size);
            }

            @Override
            public void setBounds(int x, int y, int width, int height)
            {
                int size = Math.min(width, height);
                super.setBounds(x, y, size, size);
            }
        };

        addComponentListener(new java.awt.event.ComponentAdapter()
        {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                tablero.revalidate();
                tablero.repaint();
            }
        });

        for (int fila = 0; fila < N; fila++)
        {
            for (int columna = 0; columna < N; columna++)
            {
                JPanel cuadrado = new JPanel();
                cuadrado.setBorder(new LineBorder(Color.BLACK));
                cuadrado.setBackground((fila + columna) % 2 == 0 ? Color.WHITE : Color.GRAY);
                casillas[fila][columna] = cuadrado;
                tablero.add(cuadrado);
            }
        }

        return tablero;
    }

    public void actualizarTablero(int[][] tableroSolucion)
    {
        for (int fila = 0; fila < N; fila++)
        {
            for (int columna = 0; columna < N; columna++)
            {
                casillas[fila][columna].removeAll();
                casillas[fila][columna].setLayout(new BorderLayout());
                if (tableroSolucion[fila][columna] == 1)
                {
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

    public void actualizarLista(int[][] tableroSolucion)
    {
        listModel.clear();
        for (int fila = 0; fila < N; fila++)
        {
            for (int columna = 0; columna < N; columna++)
            {
                if (tableroSolucion[fila][columna] == 1)
                {
                    listModel.addElement("[" + (fila + 1) + ", " + (columna + 1) + "]");
                }
            }
        }
    }

    public JButton getBotonRegresar()
    {
        return botonRegresar;
    }

    public JButton getSaveButton()
    {
        return saveButton;
    }
}