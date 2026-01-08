/*
 * Copyright (c) 2026 Ruben Saunders. All rights reserved.
 */

package aoc.y2025.day5;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Inclusive ID range
 */
public class IdRange
{
    private final long min;
    private final long max;

    public IdRange(long min, long max)
    {
        this.min = min;
        this.max = max;
    }

    public long min()
    {
        return min;
    }

    public long max()
    {
        return max;
    }

    /**
     * Is this range empty (contains no elements/invalid)
     */
    public boolean isEmpty()
    {
        return min > max;
    }

    /**
     * Return the number of integers in this range;
     * equivalent to `max() - min() + 1``
     */
    public long size()
    {
        if (isEmpty()) return 0;
        return max - min + 1;
    }

    /**
     * Does the given value lie withing this *inclusive* range?
     */
    public boolean contains(long n)
    {
        return n >= min && n <= max;
    }

    /**
     * Count the number of overlapping integers between this and another range
     */
    public long countOverlapping(IdRange other)
    {
        long overlapMin = Math.max(min, other.min);
        long overlapMax = Math.min(max, other.max);
        return Math.max(0, (overlapMax - overlapMin) + 1);
    }

    /**
     * Return the overlap/intersection between this and another range
     */
    public IdRange overlap(IdRange other)
    {
        long overlapMin = Math.max(min, other.min);
        long overlapMax = Math.min(max, other.max);
        return new IdRange(overlapMin, overlapMax);
    }

    /**
     * Convert this range into a stream of its elements
     */
    public Stream<Long> stream()
    {
        return isEmpty()
            ? Stream.empty()
            : LongStream.rangeClosed(min, max).boxed();
    }

    @Override
    public String toString()
    {
        return isEmpty()
            ? "∅"
            : "[" + min + ", " + max + "]";
    }

    /**
     * Convert range in the string form "<lower>-<upper>" into a range object
     */
    public static IdRange parse(String string)
    {
        if (string == null || string.isBlank()) {return empty();}
        String[] parts = string.split("-");
        return new IdRange(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
    }

    /**
     * Return an empty range
     */
    public static IdRange empty()
    {
        return new IdRange(1, -1);
    }

    /**
     * Return largest possible range
     */
    public static IdRange full()
    {
        return new IdRange(Long.MIN_VALUE, Long.MAX_VALUE);
    }

    /**
     * Calculate the intersection between a list of ranges
     */
    public static IdRange intersection(List<IdRange> ranges)
    {
        IdRange intersection = full();
        for (IdRange range : ranges)
        {
            intersection = intersection.overlap(range);
            if (intersection.isEmpty()) {return empty();}
        }

        return intersection;
    }

    /**
     * Calculate the union between a list of ranges
     */
    public static List<IdRange> union(List<IdRange> ranges)
    {
        // sorting helps the union process
        List<IdRange> processing = new ArrayList<>(ranges.stream()
            .sorted(Comparator.comparingLong(IdRange::min))
            .toList());
        List<IdRange> union = new ArrayList<>();

        while (true)
        {
            // if only one element remaining, just add it
            if (processing.size() == 1)
            {
                union.addAll(processing);
                break;
            }

            // grab top two smallest ranges
            IdRange a = processing.removeFirst();
            if (a.isEmpty()) {continue;}

            IdRange b = processing.removeFirst();

            // if `a` lies fully below `b`...
            if (b.min() > a.max())
            {
                // `a` is included in the union; continue processing with `b`
                union.add(a);
                processing.addFirst(b);
            }
            else
            {
                // we know `a` is smaller, but maximise upper bound for next processing step
                processing.addFirst(new IdRange(a.min(), Math.max(a.max(), b.max())));
            }
        }

        return union;
    }
}
