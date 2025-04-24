package org.example.Modelo;

import org.example.Ficha;

import java.util.ArrayList;
import java.util.List;

public class HanoiModelo {
    private int numDisks;
    private List<Integer>[] rods;
    private List<Move> moves;
    private Ficha.FichaHanoi fichaHanoi;

    public HanoiModelo(int numDisks) {
        this.numDisks = numDisks;
        this.rods = new ArrayList[3];
        for (int i = 0; i < 3; i++) {
            rods[i] = new ArrayList<>();
        }

        // Colocar todos los discos en la primera torre
        for (int i = numDisks; i > 0; i--) {
            rods[0].add(i);
        }

        this.moves = new ArrayList<>();
        this.fichaHanoi = new Ficha.FichaHanoi();

        // Calcular la solución
        resolverHanoi(numDisks, 0, 2, 1);
    }

    // Fórmula recursiva para resolver el problema
    private void resolverHanoi(int n, int from, int to, int aux) {
        if (n == 1) {
            moves.add(new Move(from, to));
            return;
        }
        resolverHanoi(n - 1, from, aux, to);
        moves.add(new Move(from, to));
        resolverHanoi(n - 1, aux, to, from);
    }

    // Obtener el número del disco que se movió en un paso específico
    public int getDiskNumber(Move move, int moveIndex) {
        List<Integer>[] simulatedRods = new ArrayList[3];
        for (int i = 0; i < 3; i++) {
            simulatedRods[i] = new ArrayList<>();
        }

        for (int i = numDisks; i > 0; i--) {
            simulatedRods[0].add(i);
        }

        for (int i = 0; i < moveIndex; i++) {
            Move m = moves.get(i);
            simulatedRods[m.to].add(simulatedRods[m.from].remove(simulatedRods[m.from].size() - 1));
        }

        return simulatedRods[move.from].get(simulatedRods[move.from].size() - 1);
    }

    // Realizar un movimiento
    public void realizarMovimiento(int moveIndex) {
        if (moveIndex < moves.size()) {
            Move move = moves.get(moveIndex);
            rods[move.to].add(rods[move.from].remove(rods[move.from].size() - 1));
        }
    }

    // Getters
    public int getNumDisks() {
        return numDisks;
    }

    public List<Integer>[] getRods() {
        return rods;
    }

    public List<Move> getMoves() {
        return moves;
    }

    // Clase interna para representar un movimiento
    public static class Move {
        public int from, to;

        public Move(int from, int to) {
            this.from = from;
            this.to = to;
        }
    }
}