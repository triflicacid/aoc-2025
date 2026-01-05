package aoc.y2025.day1;

/**
 * Describe a movement in a given direction.
 */
public record Instruction(
    Direction direction,
    int steps
)
{
    @Override
    public String toString()
    {
        return direction.toString() + steps;
    }

    /**
     * Parse a string (trimmed) as an instruction.
     * Format: `[LR]<steps>` e.g., `L50`.
     * Returns null if string is null or blank
     */
    public static Instruction parse(String string)
    {
        if (string == null || string.isBlank())
        {
            return null;
        }

        string = string.trim();
        Direction direction = Direction.fromString(string.substring(0, 1));
        int steps = Integer.parseInt(string.substring(1));

        return new Instruction(direction, steps);
    }

    public enum Direction {
        LEFT,
        RIGHT;

        public String toString()
        {
            return this == LEFT ? "L" : "R";
        }

        public static Direction fromString(String s)
        {
            if (s.equals("L")) return LEFT;
            if (s.equals("R")) return RIGHT;
            return null;
        }
    }
}
