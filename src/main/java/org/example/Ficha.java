package org.example;

import java.util.ArrayList;
import java.util.List;

public abstract class Ficha {
    // Metodo abstracto (debe ser implementado por las subclases)
    public abstract List<int[]> mover(int x, int y, int N, int[][] tableroSolucion);

    // Subclase estática CaballoFicha
    public static class CaballoFicha extends Ficha {
        private final int[] movimientosX = {2, 1, -1, -2, -2, -1, 1, 2};
        private final int[] movimientosY = {1, 2, 2, 1, -1, -2, -2, -1};

        @Override
        public List<int[]> mover(int x, int y, int N, int[][] tableroSolucion) {
            List<int[]> movimientosValidos = new ArrayList<>();



            return movimientosValidos;
        }
    }
}