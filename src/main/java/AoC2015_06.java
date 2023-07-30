import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Consumer;

import com.github.pareronia.aoc.IntGrid;
import com.github.pareronia.aoc.IntGrid.Cell;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

// TODO: better -> https://www.reddit.com/r/adventofcode/comments/3vmltn/day_6_solutions/cxozgap/
public final class AoC2015_06 extends AoCBase {
    
    private final transient List<Instruction> input;

    private AoC2015_06(final List<String> inputs, final boolean debug) {
        super(debug);
        this.input = inputs.stream()
                .map(s -> s.replace("turn ", "turn_"))
                .map(s -> {
                    final String[] splits = s.split(" through ");
                    final String[] actionAndStartSplits = splits[0].split(" ");
                    final String[] startSplits = actionAndStartSplits[1].split(",");
                    final Cell start = Cell.at(Integer.valueOf(startSplits[0]), Integer.valueOf(startSplits[1]));
                    final String[] endSplits = splits[1].split(",");
                    final Cell end = Cell.at(Integer.valueOf(endSplits[0]), Integer.valueOf(endSplits[1]));
                    if ("turn_on".equals(actionAndStartSplits[0])) {
                        return Instruction.turnOn(start, end);
                    } else if ("turn_off".equals(actionAndStartSplits[0])) {
                        return Instruction.turnOff(start, end);
                    } else {
                        return Instruction.toggle(start, end);
                    }
                })
                .collect(toList());
        log(this.input);
    }

    public static AoC2015_06 create(final List<String> input) {
        return new AoC2015_06(input, false);
    }

    public static AoC2015_06 createDebug(final List<String> input) {
        return new AoC2015_06(input, true);
    }
    
    private void forEachCell(final Cell start, final Cell end, final Consumer<Cell> action) {
        for (int rr = start.getRow(); rr <= end.getRow(); rr++) {
            for (int cc = start.getCol(); cc <= end.getCol(); cc++) {
                action.accept(IntGrid.Cell.at(rr, cc));
            }
        }
    }
    
    @Override
    public Integer solvePart1() {
        final IntGrid lights = new IntGrid(new int[1_000][1_000]);
        this.input.stream().forEach(ins -> {
            if (ins.isTurnOn()) {
                forEachCell(ins.getStart(), ins.getEnd(),
                    c -> lights.setValue(c, 1));
            } else if (ins.isTurnOff()) {
                forEachCell(ins.getStart(), ins.getEnd(),
                    c -> lights.setValue(c, 0));
            } else if (ins.isToggle()) {
                forEachCell(ins.getStart(), ins.getEnd(),
                    c -> lights.setValue(c, lights.getValue(c) == 1 ? 0 : 1));
            }
        });
        return (int) lights.countAllEqualTo(1);
    }

    @Override
    public Integer solvePart2() {
        final IntGrid lights = new IntGrid(new int[1_000][1_000]);
        this.input.stream().forEach(ins -> {
            if (ins.isTurnOn()) {
                forEachCell(ins.getStart(), ins.getEnd(),
                    c -> lights.setValue(c, lights.getValue(c) + 1));
            } else if (ins.isTurnOff()) {
                forEachCell(ins.getStart(), ins.getEnd(),
                    c -> lights.setValue(c, Math.max(lights.getValue(c) - 1, 0)));
            } else if (ins.isToggle()) {
                forEachCell(ins.getStart(), ins.getEnd(),
                    c -> lights.setValue(c, lights.getValue(c) + 2));
            }
        });
        return lights.getCells()
                .mapToInt(c -> lights.getValue(c))
                .sum();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2015_06.createDebug(TEST1).solvePart1() == 1_000_000;
        assert AoC2015_06.createDebug(TEST2).solvePart1() == 1_000;
        assert AoC2015_06.createDebug(TEST3).solvePart1() == 0;
        assert AoC2015_06.createDebug(TEST4).solvePart2() == 1;
        assert AoC2015_06.createDebug(TEST5).solvePart2() == 2_000_000;

        final Puzzle puzzle = Aocd.puzzle(2015, 6);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2015_06.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2015_06.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST1 = splitLines("turn on 0,0 through 999,999");
    private static final List<String> TEST2 = splitLines("toggle 0,0 through 999,0");
    private static final List<String> TEST3 = splitLines("turn off 499,499 through 500,500");
    private static final List<String> TEST4 = splitLines("turn on 0,0 through 0,0");
    private static final List<String> TEST5 = splitLines("toggle 0,0 through 999,999");

    @RequiredArgsConstructor
    @ToString
    private static final class Instruction {
        private enum Action { TURN_ON, TURN_OFF, TOGGLE }
        
        private final Action action;
        @Getter
        private final Cell start;
        @Getter
        private final Cell end;
        
        public static Instruction turnOn(final Cell start, final Cell end) {
            return new Instruction(Action.TURN_ON, start, end);
        }
        
        public static Instruction turnOff(final Cell start, final Cell end) {
            return new Instruction(Action.TURN_OFF, start, end);
        }
        
        public static Instruction toggle(final Cell start, final Cell end) {
            return new Instruction(Action.TOGGLE, start, end);
        }
        
        public boolean isTurnOn() {
            return this.action == Action.TURN_ON;
        }
        
        public boolean isTurnOff() {
            return this.action == Action.TURN_OFF;
        }
        
        public boolean isToggle() {
            return this.action == Action.TOGGLE;
        }
    }
}