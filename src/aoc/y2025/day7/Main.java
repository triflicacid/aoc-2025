/*
 * Copyright (c) 2026 Ruben Saunders. All rights reserved.
 */

package aoc.y2025.day7;

import aoc.Challenge;
import aoc.Part;
import aoc.SourceFile;
import aoc.Utils;
import aoc.shared.Grid;

/**
 * PART 1
 * - Mock data: 21
 * - Question: 1570
 * PART 2:
 * - Mock data: -
 * - Question: -
 */
public class Main extends Challenge
{
    public Main()
    {
        super(2025, 7, Part.TWO, SourceFile.MOCK_DATA);
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

    static void main(String[] args)
    {
        Challenge c = new Main();
        c.hello();
        c.run();
    }
}
