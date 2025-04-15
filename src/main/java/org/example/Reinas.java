package org.example;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Reinas extends JFrame
{

    public Reinas ()
    {
        // Configuración de la ventana
        setTitle ("Problema de las Reinas");
        setSize (550, 550); // Tamaño de la ventana
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE); // Cerrar la aplicación al cerrar la ventana
        setLocationRelativeTo (null); // Centrar la ventana en la pantalla

        // Crear un botón para regresar a la ventana inicial
        JButton botonRegresar = new JButton("Regresar a la Pantalla Inicial");
        botonRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cerrar la ventana actual y abrir la ventana Juegos
                dispose(); // Cierra la ventana actual
                new Juegos(); // Abre la ventana inicial
            }
        });

        // Crear un contenedor para el botón
        JPanel contenedorPrincipal = new JPanel(new BorderLayout());
        contenedorPrincipal.setBorder(new EmptyBorder(30, 30, 30, 30)); // Espacio en blanco alrededor
        contenedorPrincipal.add(botonRegresar, BorderLayout.SOUTH); // Botón en la parte inferior

        // Agregar el contenedor principal a la ventana
        add(contenedorPrincipal); // Esto asegura que el contenedor se muestre en la ventana

        // Hacer visible la ventana
        setVisible (true);
    }

    public static void main (String [] args)
    {
        // Crear y mostrar la ventana
        new Reinas ();
    }
}