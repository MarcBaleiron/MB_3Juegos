package org.example.Modelo;

import java.util.ArrayList;
import java.util.List;

public class HanoiModelo extends Ficha<HanoiModelo.Move, List<Integer>[]>
{

    public HanoiModelo(int numDisks)
    {
        super(numDisks);
        this.solutionState = new ArrayList[3];
        for (int i = 0; i < 3; i++)
        {
            solutionState[i] = new ArrayList<>();
        }

        // Colocar todos los discos en la primera torre
        for (int i = numDisks; i > 0; i--)
        {
            solutionState[0].add(i);
        }

        // Calcular la solución
        solve();
    }

    @Override
    protected boolean solve()
    {
        resolverHanoi(size, 0, 2, 1);
        return true;
    }

    // Fórmula recursiva para resolver el problema
    private void resolverHanoi(int n, int from, int to, int aux)
    {
        if (n == 1)
        {
            steps.add(new Move(from, to));
            return;
        }
        resolverHanoi(n - 1, from, aux, to);
        steps.add(new Move(from, to));
        resolverHanoi(n - 1, aux, to, from);
    }

    // Obtener el número del disco que se movió en un paso específico
    public int getDiskNumber(Move move, int moveIndex)
    {
        List<Integer>[] simulatedRods = new ArrayList[3];
        for (int i = 0; i < 3; i++)
        {
            simulatedRods[i] = new ArrayList<>();
        }

        for (int i = size; i > 0; i--)
        {
            simulatedRods[0].add(i);
        }

        for (int i = 0; i < moveIndex; i++)
        {
            Move m = steps.get(i);
            simulatedRods[m.to].add(simulatedRods[m.from].remove(simulatedRods[m.from].size() - 1));
        }

        return simulatedRods[move.from].get(simulatedRods[move.from].size() - 1);
    }

    // Realizar un movimiento
    public void realizarMovimiento(int moveIndex)
    {
        if (moveIndex < steps.size())
        {
            Move move = steps.get(moveIndex);
            solutionState[move.to].add(solutionState[move.from].remove(solutionState[move.from].size() - 1));
        }
    }

    public int getNumDisks() { return size; }
    public List<Integer>[] getRods() { return solutionState; }
    public List<Move> getMoves() { return steps; }

    // Clase interna para representar un movimiento
    public static class Move
    {
        public int from, to;

        public Move(int from, int to)
        {
            this.from = from;
            this.to = to;
        }
    }
}