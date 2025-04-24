package org.example.Vista;

import org.example.Modelo.HanoiModelo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

import java.util.List;

public class HanoiVista extends JPanel {
    private JPanel[] rodPanels;
    private DefaultListModel<String> moveListModel;
    private JList<String> moveList;
    private JButton botonRegresar;
    private JButton saveButton;
    private JPanel contenedorPrincipal;

    public HanoiVista() {
        setLayout(new BorderLayout());

        // Creación del modelo de lista de movimientos
        moveListModel = new DefaultListModel<>();
        moveList = new JList<>(moveListModel);
        JScrollPane moveScrollPane = new JScrollPane(moveList);
        moveScrollPane.setPreferredSize(new Dimension(240, 0));

        // Panel para las torres
        JPanel rodsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        rodPanels = new JPanel[3];
        for (int i = 0; i < 3; i++) {
            rodPanels[i] = new JPanel();
            rodPanels[i].setLayout(new BoxLayout(rodPanels[i], BoxLayout.Y_AXIS));
            rodPanels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            rodsPanel.add(rodPanels[i]);
        }

        // Botón para regresar a la pantalla inicial
        botonRegresar = new JButton("Regresar a la Pantalla Inicial");

        // Botón para guardar en la base de datos
        saveButton = new JButton("Guardar en Base de Datos");

        // Panel para los botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(botonRegresar);
        buttonPanel.add(saveButton);

        // Contenedor para el tablero y los botones
        contenedorPrincipal = new JPanel(new BorderLayout());
        contenedorPrincipal.setBorder(new EmptyBorder(30, 30, 30, 30));
        contenedorPrincipal.add(rodsPanel, BorderLayout.CENTER);
        contenedorPrincipal.add(buttonPanel, BorderLayout.SOUTH);

        // Agregar el tablero y la lista de movimientos al panel principal
        add(contenedorPrincipal, BorderLayout.CENTER);
        add(moveScrollPane, BorderLayout.EAST);
    }

    // Actualizar visualización de las torres
    public void actualizarTorres(List<Integer>[] rods) {
        for (int i = 0; i < 3; i++) {
            rodPanels[i].removeAll();
            rodPanels[i].setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = GridBagConstraints.RELATIVE;
            gbc.anchor = GridBagConstraints.SOUTH;
            gbc.weighty = 1.0;

            for (int j = rods[i].size() - 1; j >= 0; j--) {
                int disk = rods[i].get(j);
                JLabel diskLabel = new JLabel();
                diskLabel.setOpaque(true);
                diskLabel.setBackground(Color.BLUE);
                diskLabel.setForeground(Color.WHITE);
                diskLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                diskLabel.setHorizontalAlignment(SwingConstants.CENTER);
                diskLabel.setText(String.valueOf(disk));

                int diskWidth = 30 + disk * 20;
                diskLabel.setPreferredSize(new Dimension(diskWidth, 20));

                rodPanels[i].add(diskLabel, gbc);
            }

            rodPanels[i].revalidate();
            rodPanels[i].repaint();
        }
    }

    // Añadir movimiento a la lista de movimientos
    public void addMoveToList(String move) {
        moveListModel.addElement(move);
    }

    // Getters para los componentes UI
    public JButton getBotonRegresar() {
        return botonRegresar;
    }

    public JButton getSaveButton() {
        return saveButton;
    }
}