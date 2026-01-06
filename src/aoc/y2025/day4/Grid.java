package aoc.y2025.day4;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Grid
{
    private State[][] grid;

    public Grid(State[][] grid)
    {
        this.grid = grid;
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

    public State get(int x, int y)
    {
        return validPosition(x, y) ? grid[y][x] : State.EMPTY;
    }

    public void set(int x, int y, State state)
    {
        assert validPosition(x, y);
        grid[y][x] = state;
    }

    /**
     * Get the eight adjacent positions to a position, returned top-left to bottom-right
     */
    public List<State> getAdjacent(int x, int y)
    {
        List<State> adjacent = new ArrayList<>();
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
    public void iterate(Function<Cell, State> action)
    {
        for (int y = 0; y < height(); y++)
        {
            for (int x = 0; x < width(); x++)
            {
                State newState = action.apply(new Cell(x, y, grid[y][x]));
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
     */
    public static Grid parse(List<String> lines)
    {
        int width = lines.getFirst().length();
        int height = lines.size();
        State[][] grid = new State[width][height];

        for (int y = 0; y < height; y++)
        {
            String line = lines.get(y);
            for (int x = 0; x < width; x++)
            {
                grid[y][x] = State.fromString(line.charAt(x)) ;
            }
        }
        return new Grid(grid);
    }

    public enum State
    {
        EMPTY, TOILET_ROLL, BLOCKED;

        public String toString()
        {
           return switch (this)
           {
               case EMPTY -> ".";
               case TOILET_ROLL -> "@";
               case BLOCKED -> "x";
           };
        }

        public static State fromString(char c)
        {
            if (c == '.') return EMPTY;
            if (c == '@') return TOILET_ROLL;
            if (c == 'x') return BLOCKED;
            return null;
        }
    }

    public record Cell(int x, int y, State state)
    {}
}
