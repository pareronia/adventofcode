import java.util.List;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public final class AoC2017_17 extends AoCBase {
    
    private final int stepSize;
    
    private AoC2017_17(final List<String> inputs, final boolean debug) {
        super(debug);
        assert inputs.size() == 1;
        this.stepSize = Integer.parseInt(inputs.get(0));
    }

    public static AoC2017_17 create(final List<String> input) {
        return new AoC2017_17(input, false);
    }

    public static AoC2017_17 createDebug(final List<String> input) {
        return new AoC2017_17(input, true);
    }
    
    @Override
    public Integer solvePart1() {
        Node curr = new Node();
        curr.next = curr;
        for (int i = 1; i <= 2017; i++) {
            for (int j = 0; j < this.stepSize; j++) {
                curr = curr.next;
            }
            final Node newNode = new Node();
            newNode.value = i;
            newNode.next = curr.next;
            curr.next = newNode;
            curr = newNode;
        }
        return curr.next.value;
    }
    
    @Override
    public Integer solvePart2() {
        int pos = 1;
        int ans = -1;
        for (int i = 1; i <= 50_000_000; i++) {
            pos = (pos + this.stepSize) % i + 1;
            if (pos == 1) {
                ans = i;
            }
        }
        return ans;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_17.createDebug(TEST).solvePart1().equals(638);

        final Puzzle puzzle = Aocd.puzzle(2017, 17);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2017_17.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2017_17.create(puzzle.getInputData()).solvePart2())
        );
    }
    
    private static final List<String> TEST = splitLines(
            "3"
    );
    
    private static final class Node {
        private int value;
        private Node next;
    }
}
