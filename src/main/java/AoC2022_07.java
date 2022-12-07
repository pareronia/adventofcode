import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.summingInt;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2022_07 extends AoCBase {
    
    private static final String ROOT = "/";
    
    private final Map<String, Integer> sizes;
    
    private AoC2022_07(final List<String> input, final boolean debug) {
        super(debug);
        final Deque<String> path = new ArrayDeque<>(List.of(ROOT));
        this.sizes = new HashMap<>(Map.of(ROOT, 0));
        input.stream().skip(1).forEach(line -> {
            if (line.startsWith("$ cd ")) {
                final String name = line.substring("$ cd ".length());
                if (name.equals("..")) {
                    path.removeLast();
                } else {
                    path.addLast(name);
                }
            } else if (!line.startsWith("$")) {
                final String[] splits = line.split(" ");
                if (!splits[0].equals("dir")) {
                    final int size = Integer.parseInt(splits[0]);
                    final List<String> parts = new ArrayList<>();
                    path.stream().takeWhile(p -> !p.equals(splits[1])).forEach(p -> {
                        parts.add(p);
                        final String pp = parts.stream().collect(joining("/"));
                        sizes.merge(pp, size, Integer::sum);
                    });
                }
            }
        });
    }
    
    public static final AoC2022_07 create(final List<String> input) {
        return new AoC2022_07(input, false);
    }

    public static final AoC2022_07 createDebug(final List<String> input) {
        return new AoC2022_07(input, true);
    }
    
    @Override
    public Integer solvePart1() {
        return this.sizes.values().stream()
                .filter(s -> s <= 100_000)
                .collect(summingInt(Integer::valueOf));
    }

    @Override
    public Integer solvePart2() {
        final int wanted = 30_000_000 - (70_000_000 - this.sizes.get(ROOT));
        return this.sizes.values().stream()
                .filter(v -> v >= wanted)
                .sorted()
                .findFirst().orElseThrow();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_07.createDebug(TEST).solvePart1() == 95_437;
        assert AoC2022_07.createDebug(TEST).solvePart2() == 24_933_642;

        final Puzzle puzzle = Aocd.puzzle(2022, 7);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_07.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_07.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines(
        "$ cd /\r\n" +
        "$ ls\r\n" +
        "dir a\r\n" +
        "14848514 b.txt\r\n" +
        "8504156 c.dat\r\n" +
        "dir d\r\n" +
        "$ cd a\r\n" +
        "$ ls\r\n" +
        "dir e\r\n" +
        "29116 f\r\n" +
        "2557 g\r\n" +
        "62596 h.lst\r\n" +
        "$ cd e\r\n" +
        "$ ls\r\n" +
        "584 i\r\n" +
        "$ cd ..\r\n" +
        "$ cd ..\r\n" +
        "$ cd d\r\n" +
        "$ ls\r\n" +
        "4060174 j\r\n" +
        "8033020 d.log\r\n" +
        "5626152 d.ext\r\n" +
        "7214296 k"
    );
}
