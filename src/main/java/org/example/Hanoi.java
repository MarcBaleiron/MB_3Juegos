package org.example;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Hanoi extends JPanel
{

    public Hanoi ()
    {
        // Botón para regresar
        JButton botonRegresar = new JButton("Regresar a la Pantalla Inicial");
        botonRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.getWindowAncestor(Hanoi.this).dispose();
                new Juegos();
            }
        });

        // Crear un contenedor para el tablero y el botón
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(30, 30, 30, 30)); // Espacio en blanco alrededor
        add(botonRegresar, BorderLayout.SOUTH);
    }
}