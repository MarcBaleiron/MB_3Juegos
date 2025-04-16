package org.example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Reinas extends JPanel
{
    public Reinas ()
    {
        // Botón para regresar
        JButton botonRegresar = new JButton("Regresar a la Pantalla Inicial");
        botonRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.getWindowAncestor(Reinas.this).dispose();
                new Juegos();
            }
        });

        // Contenedor para el tablero y el botón
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(30, 30, 30, 30)); // Espacio en blanco alrededor
        add(botonRegresar, BorderLayout.SOUTH);
    }
}