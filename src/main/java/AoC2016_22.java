import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.github.pareronia.aoc.geometry.Point;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aocd.Aocd;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2016_22 extends AoCBase {

    private static final Pattern REGEX = Pattern.compile(
            "^\\/dev\\/grid\\/node-x([0-9]+)-y([0-9]+)\\s+[0-9]+T" +
            "\\s+([0-9]+)T\\s+([0-9]+)T\\s+[0-9]+%$");

    private final List<Node> nodes;

    private AoC2016_22(final List<String> input, final boolean debug) {
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

    public static AoC2016_22 create(final List<String> input) {
        return new AoC2016_22(input, false);
    }

    public static AoC2016_22 createDebug(final List<String> input) {
        return new AoC2016_22(input, true);
    }
    
    private Set<Node> getUnusableNodes() {
        final Integer maxAvailable = this.nodes.stream()
                .max(comparing(Node::getAvailable))
                .map(Node::getAvailable).orElseThrow();
        return this.nodes.stream()
                .filter(n -> n.getUsed() > maxAvailable)
                .collect(toSet());
    }
    
    private Node getEmptyNode() {
        final List<Node> emptyNodes = nodes.stream()
                .filter(n -> n.getUsed() == 0)
                .collect(toList());
        if (emptyNodes.size() != 1) {
            throw new IllegalArgumentException("Expected 1 empty node");
        }
        return emptyNodes.get(0);
    }
    
    private Integer getMaxX() {
        return this.nodes.stream()
                .max(comparing(Node::getX))
                .map(Node::getX).orElseThrow();
    }
    
    private Integer getMaxY() {
        return this.nodes.stream()
                .max(comparing(Node::getY))
                .map(Node::getY).orElseThrow();
    }
    
    private Node getGoalNode() {
        return nodes.stream()
                .filter(n -> n.getX() == getMaxX() && n.getY() == 0)
                .findFirst().orElseThrow();
    }
    
    private Node getAccessibleNode() {
        return nodes.stream()
                .filter(n -> n.getX() == 0 && n.getY() == 0)
                .findFirst().orElseThrow();
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
    
    private void visualize() {
        final Integer maxX = getMaxX();
        final Integer maxY = getMaxY();
        final List<Node> sorted = this.nodes.stream()
                .sorted(comparing(n -> n.getX() * maxY + n.getY()))
                .collect(toList());
        final List<List<Node>> grid = Stream.iterate(0, i -> i <= maxX, i -> i + 1)
                .map(i -> sorted.stream()
                                .skip(i * (maxY + 1))
                                .takeWhile(n -> n.getX() == i)
                                .collect(toList()))
                .collect(toList());
        final Set<Node> unusableNodes = getUnusableNodes();
        final Node emptyNode = getEmptyNode();
        final Node goalNode = getGoalNode();
        final Node accessibleNode = getAccessibleNode();
        for (final List<Node> row : grid) {
            final String line = row.stream()
                    .map(n -> {
                        if (unusableNodes.contains(n)) {
                            return " # ";
                        } else if (emptyNode.equals(n)) {
                            return " _ ";
                        } else if (goalNode.equals(n)) {
                            return " G ";
                        } else if (accessibleNode.equals(n)) {
                            return "(.)";
                        } else {
                            return " . ";
                        }
                    })
                    .collect(joining(""));
            log(line);
        }
    }
    
    private Position toPosition(final Node node) {
        return Position.of(node.getX(), node.getY());
    }
    
    private Function<Path, Boolean> stopAt(
            final Position position, final List<Path> paths) {
        return path -> {
            if (path.isAt(position)) {
                paths.add(path);
                return true;
            }
            return false;
        };
    }

    private Integer solve2() {
        visualize();
        final Set<Position> unusableNodes = getUnusableNodes().stream()
                .map(this::toPosition)
                .collect(toSet());
        final Position emptyNode = toPosition(getEmptyNode());
        final Position accessibleNode = toPosition(getAccessibleNode());
        final Integer maxX = getMaxX();
        final Integer maxY = getMaxY();
        final Position goalNode = toPosition(getGoalNode());
        log("Unusable: "+ unusableNodes);
        log("Empty: " + emptyNode);
        log("Accessible: " + accessibleNode);
        log("Goal: " + goalNode);
        final List<Path> paths = new ArrayList<>();
        final Position destination1
                = Position.of(goalNode.getX() - 1, goalNode.getY());
        assert !unusableNodes.contains(destination1);
        final Position max = Position.of(maxX, maxY);
        new PathFinder(emptyNode, destination1, max, unusableNodes)
                .findPaths(stopAt(destination1, paths));
        final Position destination2
                = Position.of(accessibleNode.getX() + 1, accessibleNode.getY());
        assert !unusableNodes.contains(destination2);
        new PathFinder(goalNode, destination2, max, unusableNodes)
                .findPaths(stopAt(destination2, paths));
        unusableNodes.add(destination2);
        new PathFinder(goalNode, accessibleNode, max, unusableNodes)
                .findPaths(stopAt(accessibleNode, paths));
        final Integer length = paths.stream()
                .map(Path::getLength)
                .collect(summingInt(Integer::valueOf));
        log(length);
        return length + 1;
    }
    
    private Integer solve2Cheat() {
        visualize();
        final Set<Node> unusableNodes = getUnusableNodes();
        log(unusableNodes);
        final Set<Integer> holeYs = unusableNodes.stream()
                .map(Node::getY)
                .collect(toSet());
        if (holeYs.size() != 1) {
            throw new IllegalArgumentException("Expected all unusable nodes in 1 row");
        }
        final Integer holeY = holeYs.iterator().next();
        if (holeY <= 2) {
            throw new IllegalStateException("Unsolvable");
        }
        if (unusableNodes.stream()
                .max(comparing(Node::getX))
                .map(Node::getX)
                .orElseThrow() != getMaxX()) {
            throw new IllegalArgumentException("Expected unusable row to touch side");
        }
        final Integer holeX = unusableNodes.stream()
                .min(comparing(Node::getX))
                .map(Node::getX)
                .orElseThrow();
        final Position hole = Position.of(holeX - 1, holeY);
        final Position emptyNode = toPosition(getEmptyNode());
        final int part1 = emptyNode.manhattanDistance(hole);
        final Position goalNode = toPosition(getGoalNode());
        final int part2 = hole.manhattanDistance(
                    Position.of(goalNode.getX() - 1, goalNode.getY()));
        final int part3 = 5 * (goalNode.getX() - 1);
        return part1 + part2 + part3 + 1;
    }

    @Override
    public Integer solvePart2() {
         return solve2Cheat();
    }
    
    public static void main(final String[] args) throws Exception {
        assert AoC2016_22.createDebug(TEST).solvePart1() == 7;
        assert AoC2016_22.createDebug(TEST).solve2() == 7;

        final List<String> input = Aocd.getData(2016, 22);
        lap("Part 1", () -> AoC2016_22.create(input).solvePart1());
        lap("Part 2", () -> AoC2016_22.createDebug(input).solvePart2());
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
    
    @RequiredArgsConstructor
    private static final class PathFinder {
        private static final int UP = 0;
        private static final int DOWN = 1;
        private static final int LEFT = 2;
        private static final int RIGHT = 3;
        private static final int[] DIRECTIONS = { UP, DOWN, LEFT, RIGHT };
        private static final int[] DX = { 0, 0, -1, 1 };
        private static final int[] DY = { -1, 1, 0, 0 };

        private final Position start;
        private final Position destination;
        private final Position max;
        private final Set<Position> unusable;
    
        public void findPaths(final Function<Path, Boolean> stop) {
            final Deque<Path> paths = new ArrayDeque<>();
            Path path = new Path(0, this.start);
            paths.add(path);
            final Set<Position> seen = new HashSet<>();
            while (!stop.apply(path) && !paths.isEmpty()) {
                path = paths.removeFirst();
                if (path.isAt(this.destination)) {
                    continue;
                }
                for (final int d : DIRECTIONS) {
                    final Path newPath = buidNewPath(path, d);
                    if (isInBounds(newPath.getPosition())
                            && isUsable(newPath.getPosition())
                            && !seen.contains(newPath.getPosition())) {
                        paths.add(newPath);
                        seen.add(newPath.getPosition());
                    }
                }
            }
        }
        
        private Path buidNewPath(final Path path, final int direction) {
            return new Path(path.getLength() + 1,
                            Position.of(path.getPosition().getX() + DX[direction],
                                        path.getPosition().getY() + DY[direction]));
        }
        
        private boolean isInBounds(final Position position) {
            return position.getX() >= 0
                    && position.getY() >= 0
                    && position.getX() <= this.max.getX()
                    && position.getY() <= this.max.getY();
        }
    
        private boolean isUsable(final Point position) {
            return !this.unusable.contains(position);
        }
    }
    
    @RequiredArgsConstructor
    @EqualsAndHashCode
    @ToString
    private static final class Path {
        @Getter
        private final Integer length;
        @Getter
        private final Position position;
        
        public boolean isAt(final Position position) {
            return this.position.equals(position);
        }
    }

    @AllArgsConstructor
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @ToString(onlyExplicitlyIncluded = true)
    private static final class Node {
        @Getter
        @EqualsAndHashCode.Include
        @ToString.Include
        private final Integer x;
        @Getter
        @EqualsAndHashCode.Include
        @ToString.Include
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