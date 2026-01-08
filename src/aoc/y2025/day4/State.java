package aoc.y2025.day4;

public enum State
{
    EMPTY, TOILET_ROLL, BLOCKED;

    public String toString()
    {
        return switch (this)
        {
            case EMPTY -> ".";
            case TOILET_ROLL -> "@";
            case BLOCKED -> "x";
        };
    }

    public static State parse(char c)
    {
        if (c == '.') return EMPTY;
        if (c == '@') return TOILET_ROLL;
        if (c == 'x') return BLOCKED;
        return null;
    }
}
