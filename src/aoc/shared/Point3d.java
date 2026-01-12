/*
 * Copyright (c) 2026 Ruben Saunders. All rights reserved.
 */

package aoc.shared;

import java.util.function.Function;

public record Point3d(
    int x,
    int y,
    int z
)
{
    public Point3d copy()
    {
        return new Point3d(x, y, z);
    }

    public Point3d add(Point3d other)
    {
        return new Point3d(x + other.x, y + other.y, z + other.z);
    }

    public Point3d addX(int x)
    {
        return new Point3d(this.x + x, y, z);
    }

    public Point3d addY(int y)
    {
        return new Point3d(x, this.y + y, z);
    }

    public Point3d addZ(int z)
    {
        return new Point3d(x, y, this.z + z);
    }

    public Point3d add(int scalar)
    {
        return new Point3d(x + scalar, y + scalar, z + scalar);
    }

    /**
     * Apply the given function to all three components
     */
    public Point3d map(Function<Integer, Integer> f)
    {
        return new Point3d(
                f.apply(x),
                f.apply(y),
                f.apply(z)
        );
    }

    /**
     * Calculate the length of this 3d point (distance from (0,0,0))
     */
    public double length()
    {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Calculate the distance between two 3d points
     */
    public double distanceTo(Point3d other)
    {
        return add(other).length();
    }

    @Override
    public String toString()
    {
        return "(" + x + "," + y + "," + z + ")";
    }
}
