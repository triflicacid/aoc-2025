/*
 * Copyright (c) 2026 Ruben Saunders. All rights reserved.
 */

package aoc.y2025.day6;

import aoc.Challenge;
import aoc.Part;
import aoc.SourceFile;
import aoc.Utils;
import aoc.shared.ListUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * PART 1
 * - Mock data: 4277556
 * - Question: 5877594983578
 * PART 2:
 * - Mock data: 3263827
 * - Question: 11159825706149
 */
public class Main extends Challenge
{
    public Main()
    {
        super(2025, 6, Part.TWO, SourceFile.MOCK_DATA);
        debug = true;
    }

    @Override
    public void run()
    {
        List<String> lines = Utils.readLines(file());

        // operations for each list of number
        Iterator<Character> operations;
        // will contain numbers; apply op to inner list
        List<List<Long>> numbers;

        if (part1())
        {
            List<List<String>> items = lines.stream()
                .map(line -> Arrays.asList(line.split("\\s+")))
                .toList();

            // extract the operations (final row)
            operations = items.getLast().stream()
                .map(c -> c.charAt(0))
                .iterator();

            // extract raw numbers in rows
            numbers = items.subList(0, items.size() - 1).stream()
                .map(nums -> nums.stream()
                    .map(Long::parseUnsignedLong)
                    .toList())
                .toList();
            // transpose so each inner list contains a column
            numbers = ListUtils.transpose(numbers);
        }
        else
        {
            operations = Arrays.stream(lines.getLast().split("\\s+"))
                .map(c -> c.charAt(0))
                .iterator();

            List<Integer> numberLengths = ListUtils.transpose(lines
                .subList(0, lines.size() - 1)
                .stream()
                .map(line -> Arrays.stream(line.split("\\D+"))
                    .filter(Predicate.not(String::isEmpty))
                    .toList())
                .toList())
                .stream()
                .map(col -> col.stream()
                    .mapToInt(String::length)
                    .max()
                    .orElse(0))
                .toList();
            int maxLineLength = lines.stream().mapToInt(String::length).max().orElse(0);

            numbers = ListUtils.transpose(lines.subList(0, lines.size() - 1)
                .stream()
                .map(line -> padRight(line, maxLineLength, ' '))
                .map(line -> {
                    List<List<Character>> group = new ArrayList<>();
                    for (int i = 0, j = 0; i < numberLengths.size(); i++)
                    {
                        group.add(line.substring(j + i, j + i + numberLengths.get(i))
                            //.replace(' ', '0')
                            .chars()
                            .mapToObj(c -> (char) c)
                            .toList());
                        j += numberLengths.get(i);
                    }
                    return group;
                })
                .toList())
                .stream()
                .map(ListUtils::transpose)
                .map(groups -> groups.stream()
                    .map(digits -> digits.stream()
                        .map(String::valueOf)
                        .reduce(String::concat)
                        .get())
                    .map(String::trim)
                    .map(Long::parseLong)
                    .toList())
                .toList();
        }

        System.out.println(numbers);

        long sum = numbers.stream()
            .mapToLong(nums -> {
                boolean isMult = operations.next() == '*';
                return nums.stream()
                    .reduce(isMult ? 1L : 0L, (a, b) -> isMult ? a * b : a + b);
            })
            .sum();
        System.out.println(sum);
    }



    private String padRight(String s, int length, char c)
    {
        StringBuilder sBuilder = new StringBuilder(s);
        while (sBuilder.length() < length) sBuilder.append(c);
        return sBuilder.toString();
    }

    static void main(String[] args)
    {
        Challenge c = new Main();
        c.hello();
        c.run();
    }
}
