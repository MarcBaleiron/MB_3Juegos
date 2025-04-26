package org.example.Modelo;

import java.util.ArrayList;
import java.util.List;

public class ReinasModelo extends Ficha<ReinasModelo.Posicion, int[][]> {

    public ReinasModelo(int N) {
        super(N);
        this.solutionState = new int[N][N];

        // Intentar resolver el problema
        solve();
    }

    @Override
    protected List<int[]> calcularMovimientosValidos(int x, int y) {
        List<int[]> movimientosValidos = new ArrayList<>();

        // Horizontal and vertical moves
        for (int i = 0; i < size; i++) {
            if (i != x) movimientosValidos.add(new int[]{i, y});
            if (i != y) movimientosValidos.add(new int[]{x, i});
        }

        // Diagonal moves
        for (int i = 1; i < size; i++) {
            if (x + i < size && y + i < size) movimientosValidos.add(new int[]{x + i, y + i});
            if (x - i >= 0 && y - i >= 0) movimientosValidos.add(new int[]{x - i, y - i});
            if (x + i < size && y - i >= 0) movimientosValidos.add(new int[]{x + i, y - i});
            if (x - i >= 0 && y + i < size) movimientosValidos.add(new int[]{x - i, y + i});
        }

        return movimientosValidos;
    }

    @Override
    protected boolean solve() {
        return resolverReinas(0);
    }

    public boolean resolverReinas(int fila) {
        if (fila == size) {
            return true;
        }

        for (int columna = 0; columna < size; columna++) {
            if (esSeguro(fila, columna)) {
                solutionState[fila][columna] = 1;

                if (resolverReinas(fila + 1)) {
                    steps.add(new Posicion(fila, columna));
                    return true;
                }

                solutionState[fila][columna] = 0;
            }
        }

        return false;
    }

    private boolean esSeguro(int fila, int columna) {
        for (int i = 0; i < fila; i++) {
            if (solutionState[i][columna] == 1) {
                return false;
            }
        }

        for (int i = fila, j = columna; i >= 0 && j >= 0; i--, j--) {
            if (solutionState[i][j] == 1) {
                return false;
            }
        }

        for (int i = fila, j = columna; i >= 0 && j < size; i--, j++) {
            if (solutionState[i][j] == 1) {
                return false;
            }
        }

        return true;
    }

    // Specific methods
    public int getN() { return size; }
    public int[][] getTableroSolucion() { return solutionState; }
    public List<Posicion> getPosicionesReinas() { return steps; }

    public static class Posicion {
        private int fila;
        private int columna;

        public Posicion(int fila, int columna) {
            this.fila = fila;
            this.columna = columna;
        }

        public int getFila() { return fila; }
        public int getColumna() { return columna; }
    }
}