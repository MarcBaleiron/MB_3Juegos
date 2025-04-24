package org.example.Modelo;

import org.example.Controlador.Ficha;

import java.util.ArrayList;
import java.util.List;

public class ReinasModelo {
    private int N;
    private int[][] tableroSolucion;
    private Ficha.FichaReinas fichaReinas;
    private List<Posicion> posicionesReinas;

    public ReinasModelo(int N) {
        this.N = N;
        this.tableroSolucion = new int[N][N];
        this.fichaReinas = new Ficha.FichaReinas();
        this.posicionesReinas = new ArrayList<>();

        // Intentar resolver el problema
        resolverReinas(0);
    }

    public boolean resolverReinas(int fila) {
        if (fila == N) {
            return true;
        }

        for (int columna = 0; columna < N; columna++) {
            if (esSeguro(fila, columna)) {
                tableroSolucion[fila][columna] = 1;

                List<int[]> movimientosValidos = fichaReinas.mover(fila, columna, N, tableroSolucion);

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