package aoc;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Utils
{
    public static String currentWorkingDirectory()
    {
        return Paths.get(".").toAbsolutePath().normalize().toString();
    }

    public static String getDirectory(int year, int day)
    {
        return currentWorkingDirectory() + File.separator +
            "src" + File.separator +
            "aoc" + File.separator +
            "y" + year + File.separator +
            "day" + day + File.separator;
    }

    public static <T> List<T> readLines(String filename, Function<String, T> mapper)
    {
        try (FileReader reader = new FileReader(filename))
        {
            return reader.readAllLines().stream()
                .map(String::trim)
                .filter(Predicate.not(String::isEmpty))
                .map(mapper)
                .toList();
        }
        catch (IOException e)
        {
            System.err.printf("Failed to read file <%s> - %s%n", filename, e);
            return List.of();
        }
    }
}
