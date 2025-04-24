package org.example;

import org.example.Modelo.DatabaseConnection;

public class Main
{
    public static void main (String [] args)
    {
        DatabaseConnection.initializeDatabase ();

        new Juegos();
    }
}