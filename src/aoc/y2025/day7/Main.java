/*
 * Copyright (c) 2026 Ruben Saunders. All rights reserved.
 */

package aoc.y2025.day7;

import aoc.Challenge;
import aoc.Part;
import aoc.SourceFile;
import aoc.Utils;
import aoc.shared.Grid;

import java.util.Arrays;
import java.util.List;

/**
 * PART 1
 * - Mock data: 21
 * - Question: 1570
 * PART 2:
 * - Mock data: 40
 * - Question: 15118009521693
 */
public class Main extends Challenge
{
    public Main()
    {
        super(2025, 7, Part.TWO, SourceFile.DATA);
        debug = true;
    }

    @Override
    public void run()
    {
        TachyonManifold manifold = new TachyonManifold(
                Grid.parse(
                        Utils.readLines(file()),
                        State::parse,
                        () -> State.EMPTY
                ));
        manifold.COLISSION_BEHAVIOUR = part1()
                ? ColissionBehaviour.MERGE
                : ColissionBehaviour.ADDITIVE;

        manifold.reset();
        if (debug)
        {
            while (!manifold.isFinished())
            {
                manifold.simulateStep();
                System.out.println(manifold);
                manifold.printBeams();
            }
        }
        else
        {
            manifold.simulate();
        }

        System.out.println(part1() ? manifold.countSplits() : manifold.countBeams());
    }

    /**
     * A shorter solution.
     */
    public void run2()
    {
        List<String> grid = Utils.readLines(file());
        int[][] beams = new int[grid.size()][grid.getFirst().length()];

        for (int y = 0; y < beams.length; y++)
        {
            for (int x = 0; x < beams[y].length; x++)
            {
                if (grid.get(y).charAt(x) == 'S')
                {
                    beams[y][x] = 1;
                }

                if (y < 1)
                    continue;

                if (x > 0 && grid.get(y - 1).charAt(x - 1) == '^')
                {
                    beams[y][x] += beams[y - 1][x - 1];
                }
                if (x < beams[y].length - 1 && grid.get(y - 1).charAt(x + 1) == '^')
                {
                    beams[y][x] += beams[y - 1][x + 1];
                }
                if (grid.get(y - 1).charAt(x) != '^')
                {
                    beams[y][x] += beams[y - 1][x];
                }
            }
        }

        int totalBeams = Arrays.stream(beams[beams.length - 1]).sum();
        System.out.println(totalBeams);
    }

    static void main(String[] args)
    {
        Main c = new Main();
        c.hello();
        c.run();
        //c.run2();
    }
}
