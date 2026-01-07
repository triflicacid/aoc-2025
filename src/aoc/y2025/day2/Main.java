/*
 * Copyright (c) 2026 Ruben Saunders. All rights reserved.
 */

package aoc.y2025.day2;

import aoc.Challenge;
import aoc.Part;
import aoc.SourceFile;
import aoc.Utils;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.LongStream;

/**
 * PART 1
 * - Mock data: 1227775554
 * - Question: 19219508902
 * PART 2:
 * - Mock data: 4174379265
 * - Question: 27180728081
 */
public class Main extends Challenge
{
    public Main()
    {
        super(2025, 2, Part.TWO, SourceFile.DATA);
        debug = true;
    }

    @Override
    public void run()
    {
        String data = Utils.readFile(file());

        var invalidIds = Arrays.stream(data.split(","))
            .map(String::trim)
            .filter(Predicate.not(String::isBlank))
            .map(s -> s.split("-"))
            .map(xs -> Arrays.stream(xs).map(Long::parseUnsignedLong).toList())
            .mapToLong(parts -> getInvalidIdsInRange(parts.getFirst(), parts.getLast()).sum())
            .sum();
        System.out.println(invalidIds);
    }

    private LongStream getInvalidIdsInRange(long min, long max)
    {
        return LongStream.rangeClosed(min, max)
            .mapToObj(String::valueOf)
            .filter(this::isInvalidId)
            .mapToLong(Long::parseUnsignedLong);
    }

    /**
     * Return if both halves of the given string are equal.
     */
    private boolean areHalvesIdentical(String string)
    {
        int length = string.length();
        if (length % 2 != 0) {return false;}

        String a = string.substring(0, length / 2);
        String b = string.substring(length / 2);
        return a.equals(b);
    }

    private boolean isInvalidId(String id)
    {
        // if part 1, only check if two halves
        if (part1())
        {
            return areHalvesIdentical(id);
        }

        // for every valid segment length, check if it consists of repeated segments of said length
        final int length = id.length();
        for (int i = 1; 2 * i <= length; i++)
        {
            if (length % i != 0) {continue;}

            boolean isValid = true;
            String previousSubstring = null;
            for (int j = 0; j < length && isValid; j += i)
            {
                String substring = id.substring(j, j + i);
                if (previousSubstring != null && !substring.equals(previousSubstring))
                {isValid = false;}
                previousSubstring = substring;
            }

            if (isValid) return true;
        }

        return false;
    }

    static void main(String[] args)
    {
        Challenge c = new Main();
        c.hello();
        c.run();
    }
}
