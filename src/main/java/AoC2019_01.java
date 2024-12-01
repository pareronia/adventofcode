import java.util.Arrays;
import java.util.List;
import java.util.function.IntUnaryOperator;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2019_01 extends SolutionBase<int[], Integer, Integer> {
    
    private AoC2019_01(final boolean debug) {
        super(debug);
    }

    public static AoC2019_01 create() {
        return new AoC2019_01(false);
    }

    public static AoC2019_01 createDebug() {
        return new AoC2019_01(true);
    }
 
    @Override
    protected int[] parseInput(final List<String> inputs) {
        return inputs.stream().mapToInt(Integer::parseInt).toArray();
    }

    private int fuelForMass(final int m) {
        return m / 3 - 2;
    }
    
    private int rocketEquation(final int mass) {
        int totalFuel = 0;
        int fuel = fuelForMass(mass);
        while (fuel > 0) {
            totalFuel += fuel;
            fuel = fuelForMass(fuel);
        }
        return totalFuel;
    }
    
    private int sum(final int[] modules, final IntUnaryOperator strategy) {
        return Arrays.stream(modules).map(strategy).sum();
    }
    
    @Override
    public Integer solvePart1(final int[] modules) {
        return sum(modules, this::fuelForMass);
    }

    @Override
    public Integer solvePart2(final int[] modules) {
        return sum(modules, this::rocketEquation);
    }

    @Samples({
        @Sample(method = "part1", input = "12", expected = "2"),
        @Sample(method = "part1", input = "14", expected = "2"),
        @Sample(method = "part1", input = "1969", expected = "654"),
        @Sample(method = "part1", input = "100756", expected = "33583"),
        @Sample(method = "part2", input = "12", expected = "2"),
        @Sample(method = "part2", input = "1969", expected = "966"),
        @Sample(method = "part2", input = "100756", expected = "50346"),
    })
    public static void main(final String[] args) throws Exception {
        AoC2019_01.create().run();
    }
}
