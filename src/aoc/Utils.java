package aoc;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class Utils
{
    /**
     * Print "Hello challenge ..." message
     */
    public static void hello(int year, int day)
    {
        System.out.printf("Hello, Advent of Code %d Day %d!\n", year, day);
    }

    public static String currentWorkingDirectory()
    {
        return Paths.get(".").toAbsolutePath().normalize().toString();
    }

    /**
     * Get the directory that the challenge files for challenge day/year are situated in
     */
    public static String getDirectory(int year, int day)
    {
        return currentWorkingDirectory() + File.separator +
            "src" + File.separator +
            "aoc" + File.separator +
            "y" + year + File.separator +
            "day" + day + File.separator;
    }

    /**
     * Read lines from a file, return as a list (non-empty + trimmed lines only)
     * @param filename File to read
     */
    public static List<String> readLines(String filename)
    {
        try (FileReader reader = new FileReader(filename))
        {
            return reader.readAllLines().stream()
                .map(String::trim)
                .filter(Predicate.not(String::isEmpty))
                .toList();
        }
        catch (IOException e)
        {
            throw new RuntimeException(String.format("Failed to read file <%s> - %s%n", filename, e));
        }
    }

    /**
     * Read lines from a file, return list of them mapped using some function (excluding empty strings and null return values)
     * @param filename File to read
     * @param mapper Function to process each line
     */
    public static <T> List<T> readLines(String filename, Function<String, T> mapper)
    {
        try (FileReader reader = new FileReader(filename))
        {
            return reader.readAllLines().stream()
                .map(String::trim)
                .filter(Predicate.not(String::isEmpty))
                .map(mapper)
                .filter(Objects::nonNull)
                .toList();
        }
        catch (IOException e)
        {
            throw new RuntimeException(String.format("Failed to read file <%s> - %s%n", filename, e));
        }
    }

    /**
     * Return the text contents (trimmed) of a file
     */
    public static String readFile(String filename)
    {
        try (FileReader reader = new FileReader(filename))
        {
            return reader.readAllAsString().trim();
        }
        catch (IOException e)
        {
            throw new RuntimeException(String.format("Failed to read file <%s> - %s%n", filename, e));
        }
    }
}
