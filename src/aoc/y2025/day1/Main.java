package aoc.y2025.day1;

import aoc.Challenge;
import aoc.Part;
import aoc.SourceFile;
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
public class Main extends Challenge
{
    public Main()
    {
        super(2025, 1, Part.ONE, SourceFile.DATA);
        debug = true;
    }

    @Override
    public void run()
    {
        List<Instruction> instructions = Utils.readLines(file(), Instruction::parse);

        Wheel wheel = new Wheel(100).set(50);
        wheel.COUNT_PASS_ZERO = part2();

        if (debug)
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

    static void main(String[] args)
    {
        Challenge c = new Main();
        c.hello();
        c.run();
    }
}
