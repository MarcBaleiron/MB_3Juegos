package org.example;

public class Main
{
    public static void main (String [] args)
    {
        DatabaseConnection.initializeDatabase ();

        new Juegos ();
    }
}