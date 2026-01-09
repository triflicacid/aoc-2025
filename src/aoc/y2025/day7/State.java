package aoc.y2025.day7;

public enum State
{
    EMPTY, SOURCE, BEAM, SPLITTER;

    public String toString()
    {
        return switch (this)
        {
            case EMPTY -> ".";
            case SOURCE -> "S";
            case BEAM -> "|";
            case SPLITTER -> "^";
        };
    }

    public static State parse(char c)
    {
        return switch (c)
        {
            case '.' -> EMPTY;
            case 'S' -> SOURCE;
            case '|' -> BEAM;
            case '^' -> SPLITTER;
            default -> null;
        };
    }
}
