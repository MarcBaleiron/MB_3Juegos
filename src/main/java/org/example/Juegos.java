package org.example;

import org.example.Controlador.CaballoControlador;
import org.example.Controlador.HanoiControlador;
import org.example.Controlador.ReinasControlador;

import java.awt.CardLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Juegos extends JFrame
{
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public Juegos ()
    {
        // Configuración de la ventana
        setTitle ("Elección de Juegos");
        setSize (750, 650);
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo (null);

        // Crear un CardLayout para cambiar entre vistas
        cardLayout = new CardLayout ();
        mainPanel = new JPanel (cardLayout);
        mainPanel.setBorder (BorderFactory.createEmptyBorder (30, 30, 30, 30));

        // Panel inicial con botones
        JPanel buttonPanel = new JPanel ();
        buttonPanel.setLayout (new GridLayout (3, 1, 20, 20));

        // Botón para abrir la ventana de Caballo
        JButton botonAbrirCaballo = new JButton ("Problema del Caballo de Ajedrez");
        botonAbrirCaballo.addActionListener (e -> {
            JPanel panelCaballo = CaballoControlador.createCaballoPanel();
            mainPanel.add (panelCaballo, "Caballo");
            cardLayout.show (mainPanel, "Caballo");
        });

        // Botón para abrir la ventana de Problema de las Reinas
        JButton botonAbrirReinas = new JButton ("Problema de las Reinas");
        botonAbrirReinas.addActionListener (e -> {
            JPanel panelReinas = ReinasControlador.createReinasPanel();
            mainPanel.add (panelReinas, "Reinas");
            cardLayout.show (mainPanel, "Reinas");
        });

        // Botón para abrir la ventana de Torres de Hanoi
        JButton botonAbrirHanoi = new JButton ("Torres de Hanoi");
        botonAbrirHanoi.addActionListener (e -> {
            JPanel panelHanoi = HanoiControlador.createHanoiPanel();
            mainPanel.add (panelHanoi, "Hanoi");
            cardLayout.show (mainPanel, "Hanoi");
        });

        // Agregar los botones al panel de botones
        buttonPanel.add (botonAbrirCaballo);
        buttonPanel.add (botonAbrirHanoi);
        buttonPanel.add (botonAbrirReinas);

        // Agregar el panel de botones al CardLayout
        mainPanel.add (buttonPanel, "Menu");

        // Crear paneles para cada juego
        JPanel caballoPanel = new JPanel ();
        caballoPanel.add (new JLabel ("Panel del Problema del Caballo de Ajedrez"));

        JPanel hanoiPanel = new JPanel ();
        hanoiPanel.add (new JLabel ("Panel de las Torres de Hanoi"));

        JPanel reinasPanel = new JPanel ();
        reinasPanel.add (new JLabel ("Panel del Problema de las Reinas"));

        // Agregar los paneles de cada juego al CardLayout
        mainPanel.add (caballoPanel, "Caballo");
        mainPanel.add (hanoiPanel, "Hanoi");
        mainPanel.add (reinasPanel, "Reinas");

        // Agregar el panel principal a la ventana
        add (mainPanel);

        // Hacer visible la ventana
        setVisible (true);
    }

    public static void main (String [] args) {
        new Juegos ();
    }
}