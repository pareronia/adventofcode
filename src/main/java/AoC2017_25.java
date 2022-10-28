import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

public final class AoC2017_25 extends AoCBase {
    
    private final String start;
    private final int steps;
    private final Map<String, State> states = new HashMap<>();
    
    private AoC2017_25(final List<String> inputs, final boolean debug) {
        super(debug);
        final List<List<String>> blocks = toBlocks(inputs);
        final List<String> block0 = blocks.get(0);
        this.start = getLastWord(block0.get(0));
        log(this.start);
        this.steps = Integer.parseInt(block0.get(1).split(" ")[5]);
        log(this.steps);
        for (int i = 1; i < blocks.size(); i++) {
            final List<String> block = blocks.get(i);
            final String name = getLastWord(block.get(0));
            final Step step0 = parseStep(block.subList(2, 5));
            final Step step1 = parseStep(block.subList(6, 9));
            states.put(name, new State(new Step[] { step0, step1 }));
        }
        log(this.states);
    }
    
    private Step parseStep(final List<String> lines) {
        final int write0 = Integer.parseInt(getLastWord(lines.get(0)));
        final int move0 = "left".equals(getLastWord(lines.get(1))) ? -1 : 1;
        final String goTo0 = getLastWord(lines.get(2));
        return new Step(write0, move0, goTo0);
    }
    
    private String getLastWord(final String input) {
        final String[] splits = input.split(" ");
        final String last = splits[splits.length - 1];
        return last.substring(0, last.length() - 1);
    }

    public static AoC2017_25 create(final List<String> input) {
        return new AoC2017_25(input, false);
    }

    public static AoC2017_25 createDebug(final List<String> input) {
        return new AoC2017_25(input, true);
    }
        
    @Override
    public Long solvePart1() {
        final Map<Integer, Integer> tape = new HashMap<>();
        int pos = 0;
        State state = this.states.get(this.start);
        for (int i = 0; i < this.steps; i++) {
            log(pos);
            log(tape);
            final int value = tape.getOrDefault(pos, 0);
            final Step step = state.steps[value];
            tape.put(pos, step.write);
            pos += step.move;
            state = this.states.get(step.goTo);
            log("---");
        }
        return tape.values().stream()
                .filter(v -> v == 1)
                .count();
    }

    @Override
    public Integer solvePart2() {
        return 0;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_25.createDebug(TEST).solvePart1() == 3;
        
        final Puzzle puzzle = Aocd.puzzle(2017, 25);
        final List<String> input = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", create(input)::solvePart1),
            () -> lap("Part 2", create(input)::solvePart2)
        );
    }
    
    private static final List<String> TEST = splitLines(
          "Begin in state A.\r\n"
        + "Perform a diagnostic checksum after 6 steps.\r\n"
        + "\r\n"
        + "In state A:\r\n"
        + "  If the current value is 0:\r\n"
        + "    - Write the value 1.\r\n"
        + "    - Move one slot to the right.\r\n"
        + "    - Continue with state B.\r\n"
        + "  If the current value is 1:\r\n"
        + "    - Write the value 0.\r\n"
        + "    - Move one slot to the left.\r\n"
        + "    - Continue with state B.\r\n"
        + "\r\n"
        + "In state B:\r\n"
        + "  If the current value is 0:\r\n"
        + "    - Write the value 1.\r\n"
        + "    - Move one slot to the left.\r\n"
        + "    - Continue with state A.\r\n"
        + "  If the current value is 1:\r\n"
        + "    - Write the value 1.\r\n"
        + "    - Move one slot to the right.\r\n"
        + "    - Continue with state A."
    );
    
    @RequiredArgsConstructor
    @ToString
    private static final class Step {
        private final int write;
        private final int move;
        private final String goTo;
    }
    
    @RequiredArgsConstructor
    @ToString
    private static final class State {
        private final Step[] steps;
    }
}