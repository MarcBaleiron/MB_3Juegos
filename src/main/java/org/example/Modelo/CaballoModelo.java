package org.example.Modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CaballoModelo {
    private int boardSize;
    private int[][] solutionBoard;
    private List<Paso> steps;

    private final int[] movimientosX = {2, 1, -1, -2, -2, -1, 1, 2};
    private final int[] movimientosY = {1, 2, 2, 1, -1, -2, -2, -1};

    public CaballoModelo(int boardSize) {
        this.boardSize = boardSize;
        this.solutionBoard = new int[boardSize][boardSize];
        this.steps = new ArrayList<>();

        // Initialize empty board
        for (int i = 0; i < boardSize; i++) {
            Arrays.fill(solutionBoard[i], -1);
        }

        // Set initial position
        solutionBoard[0][0] = 0;
        steps.add(new Paso(0, 0, 0));
        solve();

        // Reset board except starting position
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (!(i == 0 && j == 0)) {
                    solutionBoard[i][j] = -1;
                }
            }
        }
    }

    // Formula para calcular los movimientos validos
    private List<int[]> calcularMovimientosValidos(int x, int y) {
        List<int[]> movimientosValidos = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            int siguienteX = x + movimientosX[i];
            int siguienteY = y + movimientosY[i];

            if (siguienteX >= 0 && siguienteX < boardSize &&
                    siguienteY >= 0 && siguienteY < boardSize &&
                    solutionBoard[siguienteX][siguienteY] == -1) {
                movimientosValidos.add(new int[]{siguienteX, siguienteY});
            }
        }

        return movimientosValidos;
    }

    private boolean solve() {
        return resolverCaballo(0, 0, 1);
    }

    private boolean resolverCaballo(int x, int y, int moveNum) {
        if (moveNum == boardSize * boardSize) {
            return true;
        }

        List<int[]> validMoves = calcularMovimientosValidos(x, y);

        for (int[] move : validMoves) {
            int nextX = move[0];
            int nextY = move[1];

            solutionBoard[nextX][nextY] = moveNum;
            steps.add(new Paso(nextX, nextY, moveNum));

            if (resolverCaballo(nextX, nextY, moveNum + 1)) {
                return true;
            }

            solutionBoard[nextX][nextY] = -1;
            steps.remove(steps.size() - 1);
        }
        return false;
    }

    // Getters and setters
    public int getBoardSize() { return boardSize; }
    public int[][] getTableroSolucion() { return solutionBoard; }
    public List<Paso> getSteps() { return steps; }
    public void updateBoard(int x, int y, int value) { solutionBoard[x][y] = value; }

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