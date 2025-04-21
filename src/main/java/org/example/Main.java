package org.example;

import org.example.Juegos.Juegos;

public class Main
{
    public static void main (String [] args)
    {
        DatabaseConnection.initializeDatabase ();

        new Juegos();
    }
}