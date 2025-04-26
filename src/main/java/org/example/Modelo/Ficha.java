package org.example.Modelo;

import java.util.ArrayList;
import java.util.List;

public abstract class Ficha<T, S> {
    protected int size; // Common size parameter (boardSize, N, numDisks)
    protected S solutionState; // Generic state representation
    protected List<T> steps; // Generic list for steps/moves/positions

    // Constructor
    public Ficha(int size) {
        this.size = size;
        this.steps = new ArrayList<>();
    }

    // Common getters
    public int getSize() {
        return size;
    }

    public S getSolutionState() {
        return solutionState;
    }

    public List<T> getSteps() {
        return steps;
    }

    // Abstract method for solving the puzzle
    protected abstract boolean solve();

    // Placeholder for common functionality
    protected List<int[]> calcularMovimientosValidos(int x, int y) {
        return new ArrayList<>(); // Default implementation
    }
}