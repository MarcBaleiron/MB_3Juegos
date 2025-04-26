package org.example.Modelo;

import java.util.ArrayList;
import java.util.List;

public abstract class Ficha<T, S>
{
    protected int size;
    protected S solutionState;
    protected List<T> steps;

    // Constructor
    public Ficha(int size)
    {
        this.size = size;
        this.steps = new ArrayList<>();
    }

    // Getters comunes
    public int getSize()
    {
        return size;
    }

    public S getSolutionState()
    {
        return solutionState;
    }

    public List<T> getSteps()
    {
        return steps;
    }

    // Metodo abstracto para resolver el puzzle
    protected abstract boolean solve();

    protected List<int[]> calcularMovimientosValidos(int x, int y)
    {
        return new ArrayList<>();
    }
}