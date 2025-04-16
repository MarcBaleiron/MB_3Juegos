package org.example;

import java.util.ArrayList;
import java.util.List;

public abstract class Ficha {
    public abstract List<int[]> mover(int x, int y, int N, int[][] tableroSolucion);

    public static class FichaCaballo extends Ficha {
        private final int[] movimientosX = {2, 1, -1, -2, -2, -1, 1, 2};
        private final int[] movimientosY = {1, 2, 2, 1, -1, -2, -2, -1};

        @Override
        public List<int[]> mover(int x, int y, int N, int[][] tableroSolucion) {
            List<int[]> movimientosValidos = new ArrayList<>();

            for (int i = 0; i < 8; i++) {
                int siguienteX = x + movimientosX[i];
                int siguienteY = y + movimientosY[i];

                if (siguienteX >= 0 && siguienteX < N && siguienteY >= 0 && siguienteY < N && tableroSolucion[siguienteX][siguienteY] == -1) {
                    movimientosValidos.add(new int[]{siguienteX, siguienteY});
                }
            }

            return movimientosValidos;
        }
    }
}