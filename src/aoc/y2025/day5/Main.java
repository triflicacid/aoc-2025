/*
 * Copyright (c) 2026 Ruben Saunders. All rights reserved.
 */

package aoc.y2025.day5;

import aoc.Challenge;
import aoc.Part;
import aoc.SourceFile;
import aoc.Utils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * PART 1
 * - Mock data: 3
 * - Question: 821
 * PART 2:
 * - Mock data: 14
 * - Question: 344771884978261
 */
public class Main extends Challenge
{
    public Main()
    {
        super(2025, 5, Part.TWO, SourceFile.DATA);
        debug = false;
    }

    @Override
    public void run()
    {
        List<String> lines = Utils.readLines(file());
        int blankLine = lines.indexOf("");

        // every item above the blank line is a numerical range
        List<IdRange> ranges = lines.subList(0, blankLine)
            .stream()
            .map(IdRange::parse)
            .toList();

        if (part1())
        {
            // every item below is a single integer id
            Set<Long> ids = lines.subList(blankLine + 1, lines.size())
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toSet());

            // set of fresh ids = ids which lie in at least one range
            Set<Long> freshIds = ids.stream()
                .filter(id -> ranges.stream().anyMatch(range -> range.contains(id)))
                .collect(Collectors.toSet());

            if (debug) System.out.println(freshIds);
            System.out.println(freshIds.size());
        }
        else
        {
            List<IdRange> merged = IdRange.union(ranges);
            if (debug) System.out.println(merged);

            long count = merged.stream()
                .mapToLong(IdRange::size)
                .sum();
            System.out.println(count);
        }
    }

    static void main(String[] args)
    {
        Challenge c = new Main();
        c.hello();
        c.run();
    }
}
