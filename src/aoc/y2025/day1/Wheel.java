/*
 * Copyright (c) 2026 Ruben Saunders. All rights reserved.
 */

package aoc.y2025.day1;

/**
 * Describe a wheel with numbers [0, upper_bound).
 * The wheel has a position, which can be moved by an instruction, with a counter to record the number of times it has pointed at zero *after any one instruction*.
 * COUNT_PASS_ZERO => also include eny time we pass zero.
 */
public class Wheel
{
    private final int upper_bound;
    private int position = 0;
    private int zero_count = 0;
    public boolean COUNT_PASS_ZERO = false;

    public Wheel(int upper_bound)
    {
        this.upper_bound = upper_bound;
    }

    public int upper_bound()
    {
        return upper_bound;
    }

    public int position()
    {
        return position;
    }

    public int zero_count()
    {
        return zero_count;
    }

    /**
     * Set the wheel's position, skipping any counting logic
     *
     * @param position The wheel's new position, must be in the range [0, upper_bound)
     */
    public Wheel set(int position)
    {
        if (position < 0 || position >= upper_bound)
        {
            throw new IllegalArgumentException("Position must be within bounds [0, " +
                upper_bound +
                ")");
        }
        this.position = position;
        return this;
    }

    /**
     * Reset the wheel's position and counter to zero
     */
    public void reset()
    {
        position = 0;
        zero_count = 0;
    }

    /**
     * Turn the wheel as per the given instruction, updating the zero counter as necessary
     *
     * @param instruction Turn instruction
     */
    public Wheel turn(Instruction instruction)
    {
        if (instruction.steps() == 0) {return this;}

        // advance position in the correct direction
        int m = instruction.direction() == Instruction.Direction.LEFT ? -1 : 1;
        int newPosition = position + m * instruction.steps();

        if (COUNT_PASS_ZERO)
        {
            // count number of times we crossed the upper bound (i.e., zero)
            zero_count += Math.abs(newPosition) / upper_bound;
            // if we changed signs we also must've crossed zero
            if (position != 0 && Math.signum(position) != Math.signum(newPosition)) zero_count++;
        }

        // calculate our new position given we wrap around (mod)
        position = Math.floorMod(newPosition, upper_bound);

        // are we pointing at a zero? (!COUNT_PASS_ZERO to avoid double counting)
        if (!COUNT_PASS_ZERO && position == 0)
        {
            zero_count++;
        }

        return this;
    }

    @Override
    public String toString()
    {
        return "Wheel{" +
            "upper_bound=" + upper_bound +
            ", position=" + position +
            ", zero_count=" + zero_count +
            '}';
    }
}
