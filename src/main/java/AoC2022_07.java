import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2022_07 extends AoCBase {
    
    private final Map<List<String>, Node> nodes;
    
    private AoC2022_07(final List<String> input, final boolean debug) {
        super(debug);
        this.nodes = new HashMap<>();
        final Deque<String> path = new ArrayDeque<>();
        path.addLast("/");
        Node curr = new Node(List.of("/"), null, 0);
        nodes.put(curr.name, curr);
        for (final String line : input.subList(1, input.size())) {
            if (line.startsWith("$ cd ")) {
                final String name = line.substring("$ cd ".length());
                if (name.equals("..")) {
                    curr = curr.parent;
                    path.removeLast();
                } else {
                    path.addLast(name);
                    curr = nodes.get(path.stream().collect(toList()));
                }
            } else if (line.equals("$ ls")) {
                continue;
            } else {
                final String[] splits = line.split(" ");
                if (splits[0].equals("dir")) {
                    final String name = splits[1];
                    path.addLast(name);
                    final Node node = new Node(path.stream().collect(toList()), curr, 0);
                    curr.addChild(node);
                    assert !nodes.containsKey(node.name);
                    nodes.put(node.name, node);
                    path.removeLast();
                } else {
                    final String name = splits[1];
                    path.addLast(name);
                    final long size = Long.parseLong(splits[0]);
                    final Node node = new Node(path.stream().collect(toList()), curr, size);
                    curr.addChild(node);
                    assert !nodes.containsKey(node.name);
                    nodes.put(node.name, node);
                    path.removeLast();
                }
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
    
    private long getSize(final Node node) {
        if (node.children.isEmpty()) {
            return node.size;
        }
        long size = 0;
        for (final Node c : node.children) {
            size += getSize(c);
        }
        return size;
    }
    
    @Override
    public Long solvePart1() {
        return this.nodes.keySet().stream()
            .map(this.nodes::get)
            .filter(n -> !n.children.isEmpty())
            .mapToLong(this::getSize)
            .filter(s -> s <= 100_000)
            .sum();
    }

    @Override
    public Long solvePart2() {
        final Map<List<String>, Long> map = this.nodes.keySet().stream()
            .map(this.nodes::get)
            .filter(n -> !n.children.isEmpty())
            .collect(toMap(n -> n.name, this::getSize));
        final long total = map.get(List.of("/"));
        final long wanted = 30_000_000 - (70_000_000 - total);
        return map.values().stream()
                .sorted()
                .filter(v -> v >= wanted)
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
        "$ cd /\r\n"
        + "$ ls\r\n"
        + "dir a\r\n"
        + "14848514 b.txt\r\n"
        + "8504156 c.dat\r\n"
        + "dir d\r\n"
        + "$ cd a\r\n"
        + "$ ls\r\n"
        + "dir e\r\n"
        + "29116 f\r\n"
        + "2557 g\r\n"
        + "62596 h.lst\r\n"
        + "$ cd e\r\n"
        + "$ ls\r\n"
        + "584 i\r\n"
        + "$ cd ..\r\n"
        + "$ cd ..\r\n"
        + "$ cd d\r\n"
        + "$ ls\r\n"
        + "4060174 j\r\n"
        + "8033020 d.log\r\n"
        + "5626152 d.ext\r\n"
        + "7214296 k"
    );
    
    @RequiredArgsConstructor
    @ToString(onlyExplicitlyIncluded = true)
    private static final class Node {
        @ToString.Include
        private final List<String> name;
        private final Node parent;
        @ToString.Include
        private final long size;
        private final List<Node> children = new ArrayList<>();
        
        public void addChild(final Node node) {
            this.children.add(node);
        }
    }
}
