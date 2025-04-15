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
        // Crear un botón para regresar a la ventana inicial
        JButton botonRegresar = new JButton("Regresar a la Pantalla Inicial");
        botonRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cerrar la ventana actual y abrir la ventana Juegos
                SwingUtilities.getWindowAncestor(Hanoi.this).dispose(); // Cierra la ventana que contiene este panel
                new Juegos(); // Abre la ventana inicial
            }
        });

        // Crear un contenedor para el tablero y el botón
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(30, 30, 30, 30)); // Espacio en blanco alrededor
        add(botonRegresar, BorderLayout.SOUTH); // Botón en la parte inferior
    }

    public static void main (String [] args)
    {
        // Crear un JFrame para contener el panel Hanoi
        JFrame frame = new JFrame("Torres de Hanoi");
        frame.setSize(550, 550); // Tamaño de la ventana
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cerrar la aplicación al cerrar la ventana
        frame.setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        frame.add(new Hanoi()); // Agregar el panel Hanoi al JFrame
        frame.setVisible(true); // Hacer visible la ventana
    }
}