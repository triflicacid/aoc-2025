package aoc.y2025.day2;

import aoc.Challenge;
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
public class Main
{
    public static final Challenge CHALLENGE = new Challenge(2025, 2);

    private static final boolean DEBUG = true;
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
        String data = Utils.readFile(filename);

        var invalidIds = Arrays.stream(data.split(","))
            .map(String::trim)
            .filter(Predicate.not(String::isBlank))
            .map(s -> s.split("-"))
            .map(xs -> Arrays.stream(xs).map(Long::parseUnsignedLong).toList())
            .mapToLong(parts -> getInvalidIdsInRange(parts.getFirst(), parts.getLast()).sum())
            .sum();
        System.out.println(invalidIds);
    }

    private static LongStream getInvalidIdsInRange(long min, long max)
    {
        return LongStream.rangeClosed(min, max)
            .mapToObj(String::valueOf)
            .filter(Main::isInvalidId)
            .mapToLong(Long::parseUnsignedLong);
    }

    /**
     * Return if both halves of the given string are equal.
     */
    private static boolean areHalvesIdentical(String string)
    {
        int length = string.length();
        if (length % 2 != 0) return false;

        String a = string.substring(0, length / 2);
        String b = string.substring(length / 2);
        return a.equals(b);
    }

    private static boolean isInvalidId(String id)
    {
        // if part 1, only check if two halves
        if (PART == 1)
        {
            return areHalvesIdentical(id);
        }

        // for every valid segment length, check if it consists of repeated segments of said length
        final int length = id.length();
        for (int i = 1; 2 * i <= length; i++)
        {
            if (length % i != 0) continue;

            boolean isValid = true;
            String previousSubstring= null;
            for (int j = 0; j < length && isValid; j += i)
            {
                String substring = id.substring(j, j + i);
                if (previousSubstring != null && !substring.equals(previousSubstring)) isValid = false;
                previousSubstring = substring;
            }

            if (isValid) return true;
        }

        return false;
    }
}
