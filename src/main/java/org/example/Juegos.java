package org.example;

import java.awt.CardLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Juegos extends JFrame {

    private CardLayout cardLayout; // Declare CardLayout as a class-level field
    private JPanel mainPanel; // Declare mainPanel as a class-level field

    public Juegos() {
        // Configuración de la ventana
        setTitle("Elección de Juegos");
        setSize(750, 650); // Tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cerrar la aplicación al cerrar la ventana
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        // Crear un CardLayout para cambiar entre vistas
        cardLayout = new CardLayout(); // Initialize the class-level CardLayout
        mainPanel = new JPanel(cardLayout); // Initialize the class-level mainPanel
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Espacio alrededor del panel

        // Panel inicial con botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 20, 20)); // 3 filas, 1 columna, con espacio entre botones

        // Botón para abrir la ventana de Caballo
        JButton botonAbrirCaballo = new JButton("Problema del Caballo de Ajedrez");
        botonAbrirCaballo.addActionListener(e -> {
            Caballo panelCaballo = new Caballo();
            mainPanel.add(panelCaballo, "Caballo");
            cardLayout.show(mainPanel, "Caballo");
        });

        // Botón para abrir la ventana de Torres de Hanoi
        JButton botonAbrirHanoi = new JButton("Torres de Hanoi");
        botonAbrirHanoi.addActionListener(e -> {
            Hanoi panelCaballo = new Hanoi();
            mainPanel.add(panelCaballo, "Hanoi");
            cardLayout.show(mainPanel, "Hanoi");
        });

        // Botón para abrir la ventana de Problema de las Reinas
        JButton botonAbrirReinas = new JButton("Problema de las Reinas");
        botonAbrirReinas.addActionListener(e -> {
            Reinas panelCaballo = new Reinas();
            mainPanel.add(panelCaballo, "Reinas");
            cardLayout.show(mainPanel, "Reinas");
        });

        // Agregar los botones al panel de botones
        buttonPanel.add(botonAbrirCaballo);
        buttonPanel.add(botonAbrirHanoi);
        buttonPanel.add(botonAbrirReinas);

        // Agregar el panel de botones al CardLayout
        mainPanel.add(buttonPanel, "Menu");

        // Crear paneles para cada juego (pueden ser reemplazados con contenido real)
        JPanel caballoPanel = new JPanel();
        caballoPanel.add(new JLabel("Panel del Problema del Caballo de Ajedrez"));

        JPanel hanoiPanel = new JPanel();
        hanoiPanel.add(new JLabel("Panel de las Torres de Hanoi"));

        JPanel reinasPanel = new JPanel();
        reinasPanel.add(new JLabel("Panel del Problema de las Reinas"));

        // Agregar los paneles de cada juego al CardLayout
        mainPanel.add(caballoPanel, "Caballo");
        mainPanel.add(hanoiPanel, "Hanoi");
        mainPanel.add(reinasPanel, "Reinas");

        // Agregar el panel principal a la ventana
        add(mainPanel);

        // Hacer visible la ventana
        setVisible(true);
    }

    public static void main(String[] args) {
        new Juegos();
    }
}