/*
 * Copyright (c) 2026 Ruben Saunders. All rights reserved.
 */

package aoc.y2025.day7;

import aoc.shared.Location;

import java.util.Objects;

public class Beam
{
    private final Location location;
    private final long instances;

    private Beam(Location location, long instances)
    {
        this.location = location;
        this.instances = instances;
    }

    public Beam(Location location)
    {
        this(location, 1);
    }

    public Location location()
    {
        return location;
    }

    /**
     * Return the number of instances of this beam
     */
    public long instances()
    {
        return instances;
    }

    /**
     * Create a new beam in the given location with the same instance count as the original beam
     */
    public Beam createNew(Location location)
    {
        return new Beam(location, instances);
    }

    /**
     * Create a new beam in the same location with the given instance + the origial instance count count
     */
    public Beam createNew(long instances)
    {
        return new Beam(location, this.instances + instances);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Beam beam = (Beam) o;
        return Objects.equals(location, beam.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location);
    }

    @Override
    public String toString() {
        return location.toString() + "x" + instances;
    }
}
