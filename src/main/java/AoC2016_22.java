import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.regex.Pattern;

import com.github.pareronia.aocd.Aocd;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

public class AoC2016_22 extends AoCBase {

    private static final Pattern REGEX = Pattern.compile(
            "^\\/dev\\/grid\\/node-x([0-9]+)-y([0-9]+)\\s+[0-9]+T" +
            "\\s+([0-9]+)T\\s+([0-9]+)T\\s+[0-9]+%$");

    private final List<Node> nodes;

    private AoC2016_22(List<String> input, boolean debug) {
        super(debug);
        this.nodes = input.stream()
                .skip(2)
                .flatMap(s -> REGEX.matcher(s).results())
                .map(m -> {
                    final Integer x = Integer.valueOf(m.group(1));
                    final Integer y = Integer.valueOf(m.group(2));
                    final Integer used = Integer.valueOf(m.group(3));
                    final Integer available = Integer.valueOf(m.group(4));
                    return new Node(x, y, used, available);
                })
                .collect(toList());
    }

    public static AoC2016_22 create(List<String> input) {
        return new AoC2016_22(input, false);
    }

    public static AoC2016_22 createDebug(List<String> input) {
        return new AoC2016_22(input, true);
    }

    @Override
    public Integer solvePart1() {
        return (int) this.nodes.stream()
                .filter(Node::isNotEmpty)
                .flatMap(a -> this.nodes.stream()
                                .filter(b -> !a.equals(b))
                                .filter(b -> a.getUsed() <= b.getAvailable()))
                .count();
    }

    @Override
    public Integer solvePart2() {
        return 0;
    }

    public static void main(String[] args) throws Exception {
        assert AoC2016_22.createDebug(TEST).solvePart1() == 7;
        assert AoC2016_22.createDebug(TEST).solvePart2() == 0;

        final List<String> input = Aocd.getData(2016, 22);
        lap("Part 1", () -> AoC2016_22.createDebug(input).solvePart1());
        lap("Part 2", () -> AoC2016_22.create(input).solvePart2());
    }

    private static final List<String> TEST = splitLines(
            "root@ebhq-gridcenter# df -h\r\n" +
            "Filesystem            Size  Used  Avail  Use%\r\n" +
            "/dev/grid/node-x0-y0   10T    8T     2T   80%\r\n" +
            "/dev/grid/node-x0-y1   11T    6T     5T   54%\r\n" +
            "/dev/grid/node-x0-y2   32T   28T     4T   87%\r\n" +
            "/dev/grid/node-x1-y0    9T    7T     2T   77%\r\n" +
            "/dev/grid/node-x1-y1    8T    0T     8T    0%\r\n" +
            "/dev/grid/node-x1-y2   11T    7T     4T   63%\r\n" +
            "/dev/grid/node-x2-y0   10T    6T     4T   60%\r\n" +
            "/dev/grid/node-x2-y1    9T    8T     1T   88%\r\n" +
            "/dev/grid/node-x2-y2    9T    6T     3T   66%"
    );

    @AllArgsConstructor
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static final class Node {
        @EqualsAndHashCode.Include
        private final Integer x;
        @EqualsAndHashCode.Include
        private final Integer y;
        @Getter
        private final Integer used;
        @Getter
        private final Integer available;
        
        public boolean isNotEmpty() {
            return this.used != 0;
        }
    }
}