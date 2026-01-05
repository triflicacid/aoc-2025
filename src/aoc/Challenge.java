package aoc;

import static aoc.Utils.getDirectory;

public record Challenge(int year, int day)
{
    public String path()
    {
        return getDirectory(year, day);
    }
}
