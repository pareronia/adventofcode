import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.List;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2017_17 extends SolutionBase<Integer, Integer, Integer> {

    private AoC2017_17(final boolean debug) {
        super(debug);
    }

    public static AoC2017_17 create() {
        return new AoC2017_17(false);
    }

    public static AoC2017_17 createDebug() {
        return new AoC2017_17(true);
    }

    @Override
    protected Integer parseInput(final List<String> inputs) {
        return Integer.valueOf(inputs.getFirst());
    }

    @Override
    @SuppressWarnings({"PMD.LawOfDemeter", "PMD.AvoidInstantiatingObjectsInLoops"})
    public Integer solvePart1(final Integer stepSize) {
        Node curr = new Node();
        curr.next = curr;
        for (int i = 1; i <= 2017; i++) {
            for (int j = 0; j < stepSize; j++) {
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
    @SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
    public Integer solvePart2(final Integer stepSize) {
        int pos = 1;
        int ans = -1;
        for (int i = 1; i <= 50_000_000; i++) {
            pos = (pos + stepSize) % i + 1;
            if (pos == 1) {
                ans = i;
            }
        }
        return ans;
    }

    @Samples({
        @Sample(method = "part1", input = "3", expected = "638"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final class Node {
        private int value;
        private Node next;
    }
}
