package aoc.y2025.day3;

import aoc.Challenge;
import aoc.Part;
import aoc.SourceFile;
import aoc.Utils;

import java.util.List;

/**
 * PART 1
 * - Mock data: 357
 * - Question: 17694
 * PART 2:
 * - Mock data: 3121910778619
 * - Question: 175659236361660
 */
public class Main extends Challenge
{
    public Main()
    {
        super(2025, 3, Part.TWO, SourceFile.DATA);
    }

    @Override
    public void run()
    {
        List<BatteryBank> batteryBanks = Utils.readLines(file(), BatteryBank::fromSequence);

        if (debug)
        {
            long joltage = batteryBanks.stream()
                .mapToLong(this::calculateJoltage)
                .peek(System.out::println)
                .sum();
            System.out.println("\n" + joltage);
        }
        else
        {
            long joltage = batteryBanks.stream()
                .mapToLong(this::calculateJoltage)
                .sum();
            System.out.println(joltage);
        }
    }

    private long calculateJoltage(BatteryBank batteryBank)
    {
        return batteryBank.joltage(part1() ? 2 : 12);
    }

    static void main(String[] args)
    {
        Challenge c = new Main();
        c.hello();
        c.run();
    }
}
