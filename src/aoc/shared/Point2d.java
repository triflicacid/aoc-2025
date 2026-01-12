/*
 * Copyright (c) 2026 Ruben Saunders. All rights reserved.
 */

package aoc.shared;

import java.util.function.BiFunction;
import java.util.function.Function;

public record Point2d(
        int x,
        int y
)
{
    public Point2d copy()
    {
        return new Point2d(x, y);
    }

    public Point2d add(Point2d other)
    {
        return new Point2d(x + other.x, y + other.y);
    }

    public Point2d addX(int dx)
    {
        return new Point2d(x + dx, y);
    }

    public Point2d addY(int dy)
    {
        return new Point2d(x, y + dy);
    }

    public Point2d add(int dx, int dy)
    {
        return new Point2d(x + dx, y + dy);
    }

    /**
     * Apply this location to a function expecting co-ordinates
     */
    public <T> T apply(BiFunction<Integer, Integer, T> f)
    {
        return f.apply(x, y);
    }

    /**
     * Apply the given function to all three components
     */
    public Point2d map(Function<Integer, Integer> f)
    {
        return new Point2d(
                f.apply(x),
                f.apply(y)
        );
    }

    /**
     * Calculate the length of this 3d point (distance from (0,0))
     */
    public double length()
    {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the distance between two 2d points
     */
    public double distanceTo(Point2d other)
    {
        return add(other).length();
    }

    @Override
    public String toString()
    {
        return "(" + x + "," + y + ")";
    }
}
