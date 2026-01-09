package aoc.shared;

import java.util.function.BiFunction;

public record Location(
        int x,
        int y
)
{
    /**
     * @return A copy of this location
     */
    public Location copy()
    {
        return new Location(x, y);
    }

    /**
     * Move location in the x direction
     * @param dx Amount to move
     * @return New location
     */
    public Location moveX(int dx)
    {
        return new Location(x + dx, y);
    }

    /**
     * Move location in the y direction
     * @param dy Amount to move
     * @return New location
     */
    public Location moveY(int dy)
    {
        return new Location(x, y + dy);
    }

    /**
     * Move location in both the x and y directions
     * @param dx Amount to move in the x direction
     * @param dy Amount to move in the y direction
     * @return New location
     */
    public Location move(int dx, int dy)
    {
        return new Location(x + dx, y + dy);
    }

    /**
     * Move location in by another location
     * @param l Amount to move by
     * @return New location
     */
    public Location move(Location l)
    {
        return new Location(x + l.x, y + l.y);
    }

    /**
     * Apply this location to a function expecting co-ordinates
     */
    public <T> T apply(BiFunction<Integer, Integer, T> f)
    {
        return f.apply(x, y);
    }

    @Override
    public String toString()
    {
        return "(" + x + "," + y + ")";
    }

    /**
     * @return The origin location, (0, 0)
     */
    public static Location origin()
    {
        return new Location(0, 0);
    }

    /**
     * Create a new location
     */
    public static Location of(int x, int y)
    {
        return new Location(x, y);
    }

    /**
     * Create a new location
     */
    public static Location of(Location l)
    {
        return new Location(l.x, l.y);
    }
}
