/*
 * Copyright (c) 2026 Ruben Saunders. All rights reserved.
 */

package aoc.y2025.day4;

import aoc.Challenge;
import aoc.Part;
import aoc.SourceFile;
import aoc.Utils;
import aoc.shared.Grid;

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
public class Main extends Challenge
{
    public Main()
    {
        super(2025, 4, Part.TWO, SourceFile.DATA);
    }

    @Override
    public void run()
    {
        List<String> lines = Utils.readLines(file());

        Grid<State> grid = Grid.parse(lines, State::parse, () -> State.EMPTY);

        int count = removeAccessibleRolls(grid);

        if (part2())
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

    private int removeAccessibleRolls(Grid<State> grid)
    {
        if (debug)
        {
            // if DEBUG, then we placed blocks... remove them
            grid.iterate(cell -> cell.state() == State.BLOCKED ? State.EMPTY : null);

            System.out.println(grid);
        }

        AtomicInteger count = new AtomicInteger();
        grid.iterate(cell -> {
            if (canAccessToiletRollAt(grid, cell.x(), cell.y()))
            {
                count.getAndIncrement();
                if (debug) return State.BLOCKED;
                if (part2()) return State.EMPTY;
            }
            return null;
        });

        if (debug) System.out.println(grid);
        return count.get();
    }

    /**
     * We can access  toilet roll at (x, y) if:
     * - (x, y) is a toilet roll;
     * - (x, y) has at least 5 empty adjacent neighbours;
     */
    private boolean canAccessToiletRollAt(Grid<State> grid, int x, int y)
    {
        if (!grid.validPosition(x, y) || grid.get(x, y) != State.TOILET_ROLL) {return false;}
        List<State> adjacent = grid.getAdjacent(x, y);
        return adjacent.stream()
            .filter(s -> s == State.EMPTY)
            .count() > 4;
    }

    static void main(String[] args)
    {
        Challenge c = new Main();
        c.hello();
        c.run();
    }
}
