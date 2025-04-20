package org.example;

import java.util.ArrayList;
import java.util.List;

public abstract class Ficha
{
    public abstract List <int []> mover (int x, int y, int N, int [][] tableroSolucion);

    public static class FichaCaballo extends Ficha
    {
        private final int [] movimientosX = {2, 1, -1, -2, -2, -1, 1, 2};
        private final int [] movimientosY = {1, 2, 2, 1, -1, -2, -2, -1};

        @Override
        public List <int []> mover(int x, int y, int N, int [][] tableroSolucion)
        {
            List <int []> movimientosValidos = new ArrayList <> ( );

            for (int i = 0; i < 8; i++)
            {
                int siguienteX = x + movimientosX [i];
                int siguienteY = y + movimientosY [i];

                if (siguienteX >= 0 && siguienteX < N && siguienteY >= 0 && siguienteY < N && tableroSolucion [siguienteX][siguienteY] == -1)
                {
                    movimientosValidos.add (new int [] {siguienteX, siguienteY});
                }
            }

            return movimientosValidos;
        }
    }

    public static class FichaReinas extends Ficha
    {
        @Override
        public List <int []> mover (int x, int y, int N, int [][] tableroSolucion)
        {
            List <int []> movimientosValidos = new ArrayList <> ();

            for (int i = 0; i < N; i++)
            {
                if (i != x) movimientosValidos.add (new int [] {i, y});
                if (i != y) movimientosValidos.add (new int [] {x, i});
            }

            for (int i = 1; i < N; i++)
            {
                if (x + i < N && y + i < N) movimientosValidos.add (new int [] {x + i, y + i});
                if (x - i >= 0 && y - i >= 0) movimientosValidos.add (new int [] {x - i, y - i});
                if (x + i < N && y - i >= 0) movimientosValidos.add (new int [] {x + i, y - i});
                if (x - i >= 0 && y + i < N) movimientosValidos.add (new int [] {x - i, y + i});
            }

            return movimientosValidos;
        }
    }

    public static class FichaHanoi extends Ficha
    {
        @Override
        public List <int []> mover (int x, int y, int N, int [][] tableroSolucion)
        {
            List <int []> movimientosValidos = new ArrayList <> ();

            movimientosValidos.add (new int [] {0, 1});
            movimientosValidos.add (new int [] {0, 2});
            movimientosValidos.add (new int [] {1, 0});
            movimientosValidos.add (new int [] {1, 2});
            movimientosValidos.add (new int [] {2, 0});
            movimientosValidos.add (new int [] {2, 1});

            return movimientosValidos;
        }
    }
}