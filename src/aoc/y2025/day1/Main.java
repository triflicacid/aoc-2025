package aoc.y2025.day1;

import aoc.Challenge;
import aoc.Utils;

import java.util.List;

/**
 * PART 1
 * - Mock data: 3
 * - Question: 989
 * PART 2:
 * - Mock data: 6
 * - Question: 5941
 */
public class Main
{
    public static final Challenge CHALLENGE = new Challenge(2025, 1);

    private static final boolean DEBUG = false;
    private static final String INPUT_FILE = "data.txt";
    private static final int PART = 2;

    static void main(String[] args)
    {
        Utils.hello(CHALLENGE);
        run();
    }

    private static void run()
    {
        String filename = CHALLENGE.path() + INPUT_FILE;
        List<Instruction> instructions = Utils.readLines(filename, Instruction::parse);

        Wheel wheel = new Wheel(100).set(50);
        wheel.COUNT_PASS_ZERO = PART == 2;

        if (DEBUG)
        {
            instructions.forEach(i -> {
                wheel.turn(i);
                System.out.printf("[%s%3d] => %2d (%2d)\n", i.direction(), i.steps(), wheel.position(), wheel.zero_count());
            });

            System.out.println(wheel);
        }
        else
        {
            instructions.forEach(wheel::turn);
            System.out.println(wheel.zero_count());
        }
    }
}
