package org.example;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Juegos extends JFrame {

    public Juegos() {
        // Configuración de la ventana
        setTitle("Elección de Juegos");
        setSize(550, 550); // Tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cerrar la aplicación al cerrar la ventana
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        // Crear un panel para organizar los botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(3, 1, 20, 20)); // 3 filas, 1 columna, con espacio entre botones
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Espacio alrededor del panel

        // Botón para abrir la ventana de Caballo
        JButton botonAbrirCaballo = new JButton("Abrir Ventana Caballo");
        botonAbrirCaballo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Caballo(); // Abre la ventana Caballo
            }
        });

        // Botón para abrir la ventana de Hanoi
        JButton botonAbrirHanoi = new JButton("Abrir Ventana Hanoi");
        botonAbrirHanoi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Hanoi(); // Abre la ventana Hanoi (debes crear esta clase)
            }
        });

        // Botón para abrir la ventana de Reinas
        JButton botonAbrirReinas = new JButton("Abrir Ventana Reinas");
        botonAbrirReinas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Reinas(); // Abre la ventana Reinas (debes crear esta clase)
            }
        });

        // Agregar los botones al panel
        panelBotones.add(botonAbrirCaballo);
        panelBotones.add(botonAbrirHanoi);
        panelBotones.add(botonAbrirReinas);

        // Agregar el panel de botones a la ventana
        add(panelBotones);

        // Hacer visible la ventana
        setVisible(true);
    }

    public static void main(String[] args) {
        // Crear y mostrar la ventana
        new Juegos();
    }
}