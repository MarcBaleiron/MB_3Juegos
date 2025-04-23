package org.example.Modelo;

import org.example.Ficha;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CaballoModelo {
    private int boardSize;
    private int[][] solutionBoard;
    private List<Paso> steps;
    private Ficha.FichaCaballo knight;

    public CaballoModelo(int boardSize) {
        this.boardSize = boardSize;
        this.solutionBoard = new int[boardSize][boardSize];
        this.steps = new ArrayList<>();
        this.knight = new Ficha.FichaCaballo();

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

    private boolean solve() {
        return solveKnightTour(0, 0, 1);
    }

    private boolean solveKnightTour(int x, int y, int moveNum) {
        if (moveNum == boardSize * boardSize) {
            return true;
        }

        List<int[]> validMoves = knight.mover(x, y, boardSize, solutionBoard);

        for (int[] move : validMoves) {
            int nextX = move[0];
            int nextY = move[1];

            solutionBoard[nextX][nextY] = moveNum;
            steps.add(new Paso(nextX, nextY, moveNum));

            if (solveKnightTour(nextX, nextY, moveNum + 1)) {
                return true;
            }

            solutionBoard[nextX][nextY] = -1;
            steps.remove(steps.size() - 1);
        }
        return false;
    }

    // Getters and setters
    public int getBoardSize() { return boardSize; }
    public int[][] getSolutionBoard() { return solutionBoard; }
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