/*
 * Copyright (c) 2026 Ruben Saunders. All rights reserved.
 */

package aoc.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents an WxH grid of state of type T
 */
public class Grid<T>
{
    private final T[][] grid;
    private final Supplier<T> emptySupplier;

    public Grid(T[][] grid, Supplier<T> emptySupplier)
    {
        this.grid = grid;
        this.emptySupplier = emptySupplier;
    }

    public Grid(T[][] grid)
    {
        this(grid, null);
    }

    public int width()
    {
        return grid[0].length;
    }

    public int height()
    {
        return grid.length;
    }

    public boolean validPosition(int x, int y)
    {
        return x >= 0 && x < width() && y >= 0 && y < height();
    }

    private T getEmpty()
    {
        return emptySupplier == null ? null : emptySupplier.get();
    }

    public T get(int x, int y)
    {
        return validPosition(x, y) ? grid[y][x] : getEmpty();
    }

    public void set(int x, int y, T el)
    {
        assert validPosition(x, y);
        grid[y][x] = el;
    }

    /**
     * Get the eight adjacent positions to a position, returned top-left to bottom-right
     */
    public List<T> getAdjacent(int x, int y)
    {
        List<T> adjacent = new ArrayList<>();
        for (int dy = -1; dy <= 1; dy++)
        {
            for (int dx = -1; dx <= 1; dx++)
            {
                if (dy == 0 && dx == 0) continue;
                adjacent.add(get(x + dx, y + dy));
            }
        }
        return adjacent;
    }

    /**
     * Iterate through the grid, executing the given function on each cell and setting the cell to the return value (if it is not null)
     */
    public void iterate(Function<Cell<T>, T> action)
    {
        for (int y = 0; y < height(); y++)
        {
            for (int x = 0; x < width(); x++)
            {
                T newState = action.apply(new Cell<>(x, y, grid[y][x]));
                if (newState != null) grid[y][x] = newState;
            }
        }
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height(); y++)
        {
            for (int x = 0; x < width(); x++)
            {
                sb.append(grid[y][x].toString());
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * Create grid from a list of lines like '@...@.@@' where '.' is empty and '@' is toilet roll
     * @param lines Lines to generate from
     * @param getState given char from input, return state
     * @param emptySupplier supplier for empty/OOB location queries, defaults to null
     */
    public static <T> Grid<T> parse(List<String> lines, Function<Character, T> getState, Supplier<T> emptySupplier)
    {
        int width = lines.getFirst().length();
        int height = lines.size();
        T[][] grid = (T[][]) new Object[width][height];

        for (int y = 0; y < height; y++)
        {
            String line = lines.get(y);
            for (int x = 0; x < width; x++)
            {
                T state = getState.apply(line.charAt(x));
                grid[y][x] = state == null ? emptySupplier == null ? null : emptySupplier.get() : state;
            }
        }

        return new Grid<>(grid, emptySupplier);
    }

    public static <T> Grid<T> parse(List<String> lines, Function<Character, T> getState)
    {
        return parse(lines, getState, null);
    }

    public record Cell<T>(int x, int y, T state)
    {}
}
