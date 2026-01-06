package aoc.y2025.day3;

import aoc.Challenge;
import aoc.Utils;
import aoc.y2025.day1.Instruction;
import aoc.y2025.day1.Wheel;

import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.List;

/**
 * PART 1
 * - Mock data: - 357
 * - Question: - 17694
 * PART 2:
 * - Mock data: - 3121910778619
 * - Question: - 175659236361660
 */
public class Main
{
    public static final Challenge CHALLENGE = new Challenge(2025, 3);

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
        List<BatteryBank> batteryBanks = Utils.readLines(filename, BatteryBank::fromSequence);

        if (DEBUG)
        {
            long joltage = batteryBanks.stream()
                .mapToLong(Main::calculateJoltage)
                .peek(System.out::println)
                .sum();
            System.out.println("\n" + joltage);
        }
        else
        {
            long joltage = batteryBanks.stream()
                .mapToLong(Main::calculateJoltage)
                .sum();
            System.out.println(joltage);
        }
    }

    private static long calculateJoltage(BatteryBank batteryBank)
    {
        return batteryBank.joltage(PART == 1 ? 2 : 12);
    }
}
