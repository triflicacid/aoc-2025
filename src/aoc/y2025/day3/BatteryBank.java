package aoc.y2025.day3;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

public class BatteryBank
{
    private final List<Long> batteries;

    public BatteryBank(List<Long> batteries)
    {
        this.batteries = batteries;
    }

    /**
     * Joltage = largest in-order list of batteries
     */
    public long joltage(int batteryCount)
    {
        if (batteries.isEmpty()) return 0;
        if (batteries.size() == 1) return batteries.getFirst();
        if (batteryCount == 1) return batteries.stream().max(Long::compareTo).get();

        if (batteryCount == 2)
        {
            // get largest battery (first digit)
            long largest = batteries.stream().max(Long::compareTo).get();
            int index = batteries.indexOf(largest);

            // if last battery, we can do better (repeat, but exclude last digit)
            if (index == batteries.size() - 1)
            {
                largest = batteries.subList(
                    0, batteries.size() -
                        1).stream().max(Long::compareTo).get();
                index = batteries.indexOf(largest);
            }

            // remaining batteries (largest is best pick for tens digit)
            List<Long> remaining = batteries.subList(index + 1, batteries.size());
            // concat largest digit with next largest digit in remaining batteries and convert to an integer
            return Integer.parseInt(String.valueOf(largest) +
                remaining.stream().max(Long::compareTo).get());
        }

        List<Long> remaining = new ArrayList<>(batteries);
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < batteryCount; i++)
        {
            // best digit == max digit with most number of remaining cells after it (for the amount we have left)
            long battery = remaining.subList(0, Math.min(remaining.size(), remaining.size() - batteryCount + i + 1)).stream()
                .max(Long::compareTo)
                .get();
            builder.append(battery);
            remaining = remaining.subList(remaining.indexOf(battery) + 1, remaining.size());
        }
        return Long.parseLong(builder.toString());
    }

    /**
     * Return the best digit to start calculating joltage from.
     * As subsequent digits of the joltage may only be after the first digit in the battery bank, we need the largest leading digit that has 'batteryCount - 1' digits after it.
     */
    public long getBestStartingBattery(int batteryCount)
    {
        return batteries.subList(0, batteries.size() - batteryCount).stream()
            .max(Long::compareTo)
            .get();
    }

    @Override
    public String toString()
    {
        return batteries.stream()
            .map(Object::toString)
            .reduce((a, b) -> a + ", " + b)
            .orElse("<empty>");
    }

    /**
     * Return a battery bank from a sequence of digits, e.g., '1234'
     */
    public static BatteryBank fromSequence(String sequence)
    {
        return new BatteryBank(CharBuffer.wrap(sequence.toCharArray())
            .chars()
            .mapToLong(c -> c - 48) // adjust ASCII code to number (48 == 0)
            .boxed()
            .toList());
    }
}
