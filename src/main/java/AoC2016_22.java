import static com.github.pareronia.aoc.AssertUtils.assertFalse;
import static com.github.pareronia.aoc.AssertUtils.assertTrue;
import static com.github.pareronia.aoc.StringOps.splitLines;
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
import java.util.stream.Stream;

import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Point;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2016_22
            extends SolutionBase<AoC2016_22.Cluster, Integer, Integer> {

    private AoC2016_22(final boolean debug) {
        super(debug);
    }

    public static AoC2016_22 create() {
        return new AoC2016_22(false);
    }

    public static AoC2016_22 createDebug() {
        return new AoC2016_22(true);
    }

    @Override
    protected Cluster parseInput(final List<String> inputs) {
        return new Cluster(inputs.stream()
                .skip(2)
                .map(Node::fromInput)
                .collect(toList()));
    }

    @Override
    public Integer solvePart1(final Cluster cluster) {
        return (int) cluster.nodes.stream()
                .filter(Node::isNotEmpty)
                .flatMap(a -> cluster.nodes.stream()
                                .filter(b -> !a.equals(b))
                                .filter(b -> a.used() <= b.available()))
                .count();
    }
    
    private void visualize(final Cluster cluster) {
        final Integer maxX = cluster.getMaxX();
        final Integer maxY = cluster.getMaxY();
        final List<Node> sorted = cluster.nodes.stream()
                .sorted(comparing(n -> n.x() * maxY + n.y()))
                .collect(toList());
        final List<List<Node>> grid = Stream.iterate(0, i -> i <= maxX, i -> i + 1)
                .map(i -> sorted.stream()
                                .skip(i * (maxY + 1))
                                .takeWhile(n -> n.x() == i)
                                .collect(toList()))
                .collect(toList());
        final Set<Node> unusableNodes = cluster.getUnusableNodes();
        final Node emptyNode = cluster.getEmptyNode();
        final Node goalNode = cluster.getGoalNode();
        final Node accessibleNode = cluster.getAccessibleNode();
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
        return Position.of(node.x(), node.y());
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

    private Integer solve2(final Cluster cluster) {
        visualize(cluster);
        final Set<Position> unusableNodes = cluster.getUnusableNodes().stream()
                .map(this::toPosition)
                .collect(toSet());
        final Position emptyNode = toPosition(cluster.getEmptyNode());
        final Position accessibleNode = toPosition(cluster.getAccessibleNode());
        final Integer maxX = cluster.getMaxX();
        final Integer maxY = cluster.getMaxY();
        final Position goalNode = toPosition(cluster.getGoalNode());
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
                .map(Path::length)
                .collect(summingInt(Integer::valueOf));
        log(length);
        return length + 1;
    }
    
    private Integer solve2Cheat(final Cluster cluster) {
        visualize(cluster);
        final Set<Node> unusableNodes = cluster.getUnusableNodes();
        log(unusableNodes);
        final Set<Integer> holeYs = unusableNodes.stream()
                .map(Node::y)
                .collect(toSet());
        assertTrue(holeYs.size() == 1, () -> "Expected all unusable nodes in 1 row");
        final Integer holeY = holeYs.iterator().next();
        if (holeY <= 1) {
            throw new IllegalStateException("Unsolvable");
        }
        assertFalse(unusableNodes.stream()
                    .max(comparing(Node::x))
                    .map(Node::x)
                    .orElseThrow() != cluster.getMaxX(),
                () -> "Expected unusable row to touch side");
        final Integer holeX = unusableNodes.stream()
                .min(comparing(Node::x))
                .map(Node::x)
                .orElseThrow();
        final Position hole = Position.of(holeX - 1, holeY);
        final Position emptyNode = toPosition(cluster.getEmptyNode());
        final int part1 = emptyNode.manhattanDistance(hole);
        final Position goalNode = toPosition(cluster.getGoalNode());
        final int part2 = hole.manhattanDistance(
                    Position.of(goalNode.getX() - 1, goalNode.getY()));
        final int part3 = 5 * (goalNode.getX() - 1);
        return part1 + part2 + part3 + 1;
    }

    @Override
    public Integer solvePart2(final Cluster cluster) {
        return solve2Cheat(cluster);
    }

    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "7")
    })
    public void samples() {
        final AoC2016_22 test = AoC2016_22.createDebug();
        assert test.solve2(test.parseInput(splitLines(TEST))) == 7;
    }

    public static void main(final String[] args) throws Exception {
        AoC2016_22.createDebug().run();
    }

    private static final String TEST = """
            root@ebhq-gridcenter# df -h\r
            Filesystem            Size  Used  Avail  Use%\r
            /dev/grid/node-x0-y0   10T    8T     2T   80%\r
            /dev/grid/node-x0-y1   11T    6T     5T   54%\r
            /dev/grid/node-x0-y2   32T   28T     4T   87%\r
            /dev/grid/node-x1-y0    9T    7T     2T   77%\r
            /dev/grid/node-x1-y1    8T    0T     8T    0%\r
            /dev/grid/node-x1-y2   11T    7T     4T   63%\r
            /dev/grid/node-x2-y0   10T    6T     4T   60%\r
            /dev/grid/node-x2-y1    9T    8T     1T   88%\r
            /dev/grid/node-x2-y2    9T    6T     3T   66%
            """;
    
    private static final class PathFinder {
        private final Position start;
        private final Position destination;
        private final Position max;
        private final Set<Position> unusable;
    
        public PathFinder(
                final Position start, final Position destination,
                final Position max, final Set<Position> unusable
        ) {
            this.start = start;
            this.destination = destination;
            this.max = max;
            this.unusable = unusable;
        }

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
                for (final Direction direction : Direction.CAPITAL) {
                    final Path newPath = buildNewPath(path, direction);
                    if (isInBounds(newPath.position())
                            && isUsable(newPath.position())
                            && !seen.contains(newPath.position())) {
                        paths.add(newPath);
                        seen.add(newPath.position());
                    }
                }
            }
        }
        
        private Path buildNewPath(final Path path, final Direction direction) {
            return new Path(path.length() + 1,
                    path.position().translate(direction));
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
    
    record Path(int length, Position position) {
        public boolean isAt(final Position position) {
            return this.position.equals(position);
        }
    }
    
    record Node(int x, int y, int used, int available) {
        public static Node fromInput(final String line) {
            final String[] splits = line.split("\\s+");
            final String[] xy = splits[0].split("-");
            return new Node(
                Integer.parseInt(xy[1].substring(1)),
                Integer.parseInt(xy[2].substring(1)),
                Integer.parseInt(splits[2].substring(0, splits[2].length() - 1)),
                Integer.parseInt(splits[3].substring(0, splits[3].length() - 1))
            );
        }
        
        public boolean isNotEmpty() {
            return this.used != 0;
        }
    }
    
    record Cluster(List<Node> nodes) {
        
        public Set<Node> getUnusableNodes() {
            final Integer maxAvailable = this.nodes.stream()
                    .max(comparing(Node::available))
                    .map(Node::available).orElseThrow();
            return this.nodes.stream()
                    .filter(n -> n.used() > maxAvailable)
                    .collect(toSet());
        }
        
        public Node getEmptyNode() {
            final List<Node> emptyNodes = this.nodes.stream()
                    .filter(n -> n.used() == 0)
                    .collect(toList());
            assertTrue(emptyNodes.size() == 1, () -> "Expected 1 empty node");
            return emptyNodes.get(0);
        }
        
        public Integer getMaxX() {
            return this.nodes.stream()
                    .max(comparing(Node::x))
                    .map(Node::x).orElseThrow();
        }
        
        public Integer getMaxY() {
            return this.nodes.stream()
                    .max(comparing(Node::y))
                    .map(Node::y).orElseThrow();
        }
        
        public Node getGoalNode() {
            return this.nodes.stream()
                    .filter(n -> n.x() == getMaxX() && n.y() == 0)
                    .findFirst().orElseThrow();
        }
        
        public Node getAccessibleNode() {
            return this.nodes.stream()
                    .filter(n -> n.x() == 0 && n.y() == 0)
                    .findFirst().orElseThrow();
        }
    }
}