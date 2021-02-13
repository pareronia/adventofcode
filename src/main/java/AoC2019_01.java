import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;

import java.util.List;

import com.github.pareronia.aocd.Aocd;

public class AoC2019_01 extends AoCBase {
    
    private final List<Integer> modules;

    private AoC2019_01(List<String> input, boolean debug) {
        super(debug);
        this.modules = input.stream().map(Integer::valueOf).collect(toList());
    }

    public static AoC2019_01 create(List<String> input) {
        return new AoC2019_01(input, false);
    }

    public static AoC2019_01 createDebug(List<String> input) {
        return new AoC2019_01(input, true);
    }
    
    private Integer fuelForMass(Integer m) {
        return m / 3 - 2;
    }

    @Override
    public Integer solvePart1() {
        return this.modules.stream()
                .map(this::fuelForMass)
                .collect(summingInt(Integer::intValue));
    }
    
    private Integer rocketEquation(Integer mass) {
        int totalFuel = 0;
        int fuel = fuelForMass(mass);
        while (fuel > 0) {
            totalFuel += fuel;
            fuel = fuelForMass(fuel);
        }
        return totalFuel;
    }

    @Override
    public Integer solvePart2() {
        return this.modules.stream()
                .map(this::rocketEquation)
                .collect(summingInt(Integer::intValue));
    }

    public static void main(String[] args) throws Exception {
        assert AoC2019_01.createDebug(splitLines("12")).solvePart1() == 2;
        assert AoC2019_01.createDebug(splitLines("14")).solvePart1() == 2;
        assert AoC2019_01.createDebug(splitLines("1969")).solvePart1() == 654;
        assert AoC2019_01.createDebug(splitLines("100756")).solvePart1() == 33583;
        assert AoC2019_01.createDebug(splitLines("12")).solvePart2() == 2;
        assert AoC2019_01.createDebug(splitLines("1969")).solvePart2() == 966;
        assert AoC2019_01.createDebug(splitLines("100756")).solvePart2() == 50346;

        final List<String> input = Aocd.getData(2019, 1);
        lap("Part 1", () -> AoC2019_01.create(input).solvePart1());
        lap("Part 2", () -> AoC2019_01.create(input).solvePart2());
    }

}