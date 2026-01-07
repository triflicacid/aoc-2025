package aoc;

/**
 * Describes a basic challenge with the following fields:
 * - date of the challenge (day/year)
 * - challenge's part
 * - challenge's source file
 */
public abstract class Challenge
{
    private final int year;
    private final int day;
    private final Part part;
    private final String file;

    public boolean debug = false;

    public Challenge(int year, int day, Part part, String file)
    {
        this.year = year;
        this.day = day;
        this.part = part;
        this.file = file;
    }

    public Challenge(int year, int day, Part part, SourceFile file)
    {
        this(year, day, part, file.toString());
    }

    public final int year()
    {
        return year;
    }

    public final int day()
    {
        return day;
    }

    /**
     * Are we part 1 of the challenge?
     */
    public final boolean part1()
    {
        return part == Part.ONE;
    }

    /**
     * Are we part 2 of the challenge?
     */
    public final boolean part2()
    {
        return part == Part.TWO;
    }

    /**
     * Say hello from the challenge
     */
    public final void hello()
    {
        Utils.hello(year, day);
    }

    /**
     * Return an absolute path to the challenge's input file
     */
    public final String file()
    {
        return Utils.getDirectory(year, day) + file;
    }

    /**
     * Override: the body of the challenge
     */
    public abstract void run();
}
