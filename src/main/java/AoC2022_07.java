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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2022_07 extends AoCBase {
    
    private static final String ROOT = "/";
    
    private final Map<String, Node> nodes;
    
    private AoC2022_07(final List<String> input, final boolean debug) {
        super(debug);
        this.nodes = new HashMap<>();
        final Deque<String> path = new ArrayDeque<>();
        path.addLast(ROOT);
        Node curr = new Node(ROOT, null, 0);
        nodes.put(ROOT, curr);
        for (final String line : input.subList(1, input.size())) {
            if (line.equals("$ ls")) {
                continue;
            } else if (line.startsWith("$ cd ")) {
                final String name = line.substring("$ cd ".length());
                if (name.equals("..")) {
                    curr = curr.parent;
                    path.removeLast();
                } else {
                    path.addLast(name);
                    curr = nodes.get(path.stream().collect(joining("/")));
                }
            } else {
                final String[] splits = line.split(" ");
                final int size;
                if (splits[0].equals("dir")) {
                    size = 0;
                } else {
                    size = Integer.parseInt(splits[0]);
                }
                path.addLast(splits[1]);
                final String name = path.stream().collect(joining("/"));
                final Node node = new Node(name, curr, size);
                curr.addChild(node);
                assert !nodes.containsKey(name);
                nodes.put(name, node);
                path.removeLast();
            }
        }
        log("nodes: " + nodes);
    }
    
    public static final AoC2022_07 create(final List<String> input) {
        return new AoC2022_07(input, false);
    }

    public static final AoC2022_07 createDebug(final List<String> input) {
        return new AoC2022_07(input, true);
    }
    
    private int getSize(final Map<String, Integer> map, final Node node) {
        if (node.children.isEmpty()) {
            return node.size;
        } else if (map.containsKey(node.name)) {
            return map.get(node.name);
        }
        final int size = node.children.stream()
                .mapToInt(n -> getSize(map, n))
                .sum();
        map.put(node.name, size);
        return size;
    }

    private Map<String, Integer> directoriesWithSizes() {
        final Map<String, Integer> map = new HashMap<>();
        getSize(map, nodes.get(ROOT));
        return map;
    }
    
    @Override
    public Integer solvePart1() {
        return directoriesWithSizes().values().stream()
                .filter(s -> s <= 100_000)
                .collect(summingInt(Integer::valueOf));
    }

    @Override
    public Integer solvePart2() {
        final Map<String, Integer> map = directoriesWithSizes();
        final int total = map.get(ROOT);
        final int wanted = 30_000_000 - (70_000_000 - total);
        return map.values().stream()
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
    
    @RequiredArgsConstructor
    @ToString(onlyExplicitlyIncluded = true)
    private static final class Node {
        @ToString.Include
        @Getter
        private final String name;
        private final Node parent;
        @ToString.Include
        private final int size;
        private final List<Node> children = new ArrayList<>();
        
        public void addChild(final Node node) {
            this.children.add(node);
        }
    }
}
