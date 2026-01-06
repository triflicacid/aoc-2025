package aoc.y2025.day4;

import aoc.Challenge;
import aoc.Utils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * PART 1
 * - Mock data: 13
 * - Question: 1467
 * PART 2:
 * - Mock data: 43
 * - Question: 8484
 */
public class Main
{
    public static final Challenge CHALLENGE = new Challenge(2025, 4);

    private static final boolean DEBUG = false;
    private static final String INPUT_FILE = "data.txt";
    private static final int PART = 2;

    static void main(String[] args)
    {
        Utils.hello(CHALLENGE);
        run();
    }

    private static void run()
    {
        String filename = CHALLENGE.path() + INPUT_FILE;
        List<String> lines = Utils.readLines(filename);

        Grid grid = Grid.parse(lines);

        int count = removeAccessibleRolls(grid);

        if (PART == 2)
        {
            int justRemoved = count;
            while (justRemoved > 0)
            {
                justRemoved = removeAccessibleRolls(grid);
                count += justRemoved;
            }
        }

        System.out.println(count);
    }

    private static int removeAccessibleRolls(Grid grid)
    {
        if (DEBUG)
        {
            // if DEBUG, then we placed blocks... remove them
            grid.iterate(cell -> cell.state() == Grid.State.BLOCKED ? Grid.State.EMPTY : null);

            System.out.println(grid);
        }

        AtomicInteger count = new AtomicInteger();
        grid.iterate(cell -> {
            if (canAccessToiletRollAt(grid, cell.x(), cell.y()))
            {
                count.getAndIncrement();
                if (DEBUG) return Grid.State.BLOCKED;
                if (PART == 2) return Grid.State.EMPTY;
            }
            return null;
        });

        if (DEBUG) System.out.println(grid);
        return count.get();
    }

    /**
     * We can access  toilet roll at (x, y) if:
     * - (x, y) is a toilet roll;
     * - (x, y) has at least 5 empty adjacent neighbours;
     */
    private static boolean canAccessToiletRollAt(Grid grid, int x, int y)
    {
        if (!grid.validPosition(x, y) || grid.get(x, y) != Grid.State.TOILET_ROLL) return false;
        List<Grid.State> adjacent = grid.getAdjacent(x, y);
        return adjacent.stream()
            .filter(s -> s == Grid.State.EMPTY)
            .count() > 4;
    }
}
