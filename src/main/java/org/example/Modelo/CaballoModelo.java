package org.example.Modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CaballoModelo extends Ficha<CaballoModelo.Paso, int[][]> {
    private final int[] movimientosX = {2, 1, -1, -2, -2, -1, 1, 2};
    private final int[] movimientosY = {1, 2, 2, 1, -1, -2, -2, -1};

    public CaballoModelo(int boardSize) {
        super(boardSize);
        this.solutionState = new int[boardSize][boardSize];

        // Initialize empty board
        for (int i = 0; i < boardSize; i++) {
            Arrays.fill(solutionState[i], -1);
        }

        // Set initial position
        solutionState[0][0] = 0;
        steps.add(new Paso(0, 0, 0));
        solve();

        // Reset board except starting position
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (!(i == 0 && j == 0)) {
                    solutionState[i][j] = -1;
                }
            }
        }
    }

    @Override
    protected List<int[]> calcularMovimientosValidos(int x, int y) {
        List<int[]> movimientosValidos = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            int siguienteX = x + movimientosX[i];
            int siguienteY = y + movimientosY[i];

            if (siguienteX >= 0 && siguienteX < size &&
                    siguienteY >= 0 && siguienteY < size &&
                    solutionState[siguienteX][siguienteY] == -1) {
                movimientosValidos.add(new int[]{siguienteX, siguienteY});
            }
        }

        return movimientosValidos;
    }

    @Override
    protected boolean solve() {
        return resolverCaballo(0, 0, 1);
    }

    private boolean resolverCaballo(int x, int y, int moveNum) {
        if (moveNum == size * size) {
            return true;
        }

        List<int[]> validMoves = calcularMovimientosValidos(x, y);

        for (int[] move : validMoves) {
            int nextX = move[0];
            int nextY = move[1];

            solutionState[nextX][nextY] = moveNum;
            steps.add(new Paso(nextX, nextY, moveNum));

            if (resolverCaballo(nextX, nextY, moveNum + 1)) {
                return true;
            }

            solutionState[nextX][nextY] = -1;
            steps.remove(steps.size() - 1);
        }
        return false;
    }

    // Specific methods for CaballoModelo
    public int getBoardSize() { return size; }
    public int[][] getTableroSolucion() { return solutionState; }
    public void updateBoard(int x, int y, int value) { solutionState[x][y] = value; }

    public static class Paso {
        private int x, y, numero;

        public Paso(int x, int y, int numero) {
            this.x = x;
            this.y = y;
            this.numero = numero;
        }

        public int getX() { return x; }
        public int getY() { return y; }
        public int getNumero() { return numero; }
    }
}