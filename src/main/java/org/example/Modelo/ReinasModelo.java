package org.example.Modelo;

import java.util.ArrayList;
import java.util.List;

public class ReinasModelo {
    private int N;
    private int[][] tableroSolucion;
    private List<Posicion> posicionesReinas;

    public ReinasModelo(int N) {
        this.N = N;
        this.tableroSolucion = new int[N][N];
        this.posicionesReinas = new ArrayList<>();

        // Intentar resolver el problema
        resolverReinas(0);
    }

    // Fórmula para calcular los movimientos válidos
    private List<int[]> calcularMovimientosValidos(int x, int y) {
        List<int[]> movimientosValidos = new ArrayList<>();

        // Horizontal and vertical moves
        for (int i = 0; i < N; i++) {
            if (i != x) movimientosValidos.add(new int[]{i, y});
            if (i != y) movimientosValidos.add(new int[]{x, i});
        }

        // Diagonal moves
        for (int i = 1; i < N; i++) {
            if (x + i < N && y + i < N) movimientosValidos.add(new int[]{x + i, y + i});
            if (x - i >= 0 && y - i >= 0) movimientosValidos.add(new int[]{x - i, y - i});
            if (x + i < N && y - i >= 0) movimientosValidos.add(new int[]{x + i, y - i});
            if (x - i >= 0 && y + i < N) movimientosValidos.add(new int[]{x - i, y + i});
        }

        return movimientosValidos;
    }

    public boolean resolverReinas(int fila) {
        if (fila == N) {
            return true;
        }

        for (int columna = 0; columna < N; columna++) {
            if (esSeguro(fila, columna)) {
                tableroSolucion[fila][columna] = 1;

                List<int[]> movimientosValidos = calcularMovimientosValidos(fila, columna);

                if (resolverReinas(fila + 1)) {
                    posicionesReinas.add(new Posicion(fila, columna));
                    return true;
                }

                tableroSolucion[fila][columna] = 0;
            }
        }

        return false;
    }

    private boolean esSeguro(int fila, int columna) {
        for (int i = 0; i < fila; i++) {
            if (tableroSolucion[i][columna] == 1) {
                return false;
            }
        }

        for (int i = fila, j = columna; i >= 0 && j >= 0; i--, j--) {
            if (tableroSolucion[i][j] == 1) {
                return false;
            }
        }

        for (int i = fila, j = columna; i >= 0 && j < N; i--, j++) {
            if (tableroSolucion[i][j] == 1) {
                return false;
            }
        }

        return true;
    }

    // Getters y setters
    public int getN() {
        return N;
    }

    public int[][] getTableroSolucion() {
        return tableroSolucion;
    }

    public List<Posicion> getPosicionesReinas() {
        return posicionesReinas;
    }

    public static class Posicion {
        private int fila;
        private int columna;

        public Posicion(int fila, int columna) {
            this.fila = fila;
            this.columna = columna;
        }

        public int getFila() {
            return fila;
        }

        public int getColumna() {
            return columna;
        }
    }
}