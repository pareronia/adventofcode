import static java.util.Comparator.comparing;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Function;

import com.github.pareronia.aoc.codec.MD5;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Point;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public final class AoC2016_17 extends AoCBase {

    private static final Position START = Position.of(0, 0);
    private static final Position DESTINATION = Position.of(3, 3);
    
    private final transient String input;

    private AoC2016_17(final List<String> inputs, final boolean debug) {
        super(debug);
        assert inputs.size() == 1;
        this.input = inputs.get(0);
    }

    public static AoC2016_17 create(final List<String> input) {
        return new AoC2016_17(input, false);
    }

    public static AoC2016_17 createDebug(final List<String> input) {
        return new AoC2016_17(input, true);
    }
    
    @Override
    public String solvePart1() {
        final List<Path> paths = new ArrayList<>();
        new PathFinder(START, DESTINATION, this.input).findPaths(path -> {
            if (path.isAt(DESTINATION)) {
                paths.add(path);
                return true;
            }
            return false;
        });
        return paths.stream()
                .findFirst()
                .map(Path::getPath)
                .orElseThrow();
    }
    
    @Override
    public Integer solvePart2() {
        final List<Path> paths = new ArrayList<>();
        new PathFinder(START, DESTINATION, this.input).findPaths(path -> {
            if (path.isAt(DESTINATION)) {
                paths.add(path);
            }
            return false;
        });
        return paths.stream()
                .map(Path::length)
                .max(comparing(Integer::intValue))
                .orElseThrow();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2016_17.createDebug(splitLines("ihgpwlah")).solvePart1().equals("DDRRRD");
        assert AoC2016_17.createDebug(splitLines("kglvqrro")).solvePart1().equals("DDUDRLRRUDRD");
        assert AoC2016_17.createDebug(splitLines("ulqzkmiv")).solvePart1().equals("DRURDRUDDLLDLUURRDULRLDUUDDDRR");
        assert AoC2016_17.createDebug(splitLines("ihgpwlah")).solvePart2() == 370;
        assert AoC2016_17.createDebug(splitLines("kglvqrro")).solvePart2() == 492;
        assert AoC2016_17.createDebug(splitLines("ulqzkmiv")).solvePart2() == 830;

        final Puzzle puzzle = Aocd.puzzle(2016, 17);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2016_17.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2016_17.create(inputData)::solvePart2)
        );
    }
    
    @RequiredArgsConstructor
    @EqualsAndHashCode
    @ToString
    private static final class Path {
        @Getter
        private final String path;
        @Getter
        private final Position position;
        
        public int length() {
            return this.path.length();
        }
        
        public boolean isAt(final Position position) {
            return this.position.equals(position);
        }
    }

    @RequiredArgsConstructor
    private static final class PathFinder {
        private static final List<Character> OPEN_CHARS = List.of('b', 'c', 'd', 'e', 'f');
        private static final List<Direction> DIRECTIONS = List.of(
                Direction.DOWN, Direction.UP, Direction.LEFT, Direction.RIGHT);
        private static final char[] DOORS = { 'U', 'D', 'L', 'R' };

        private final Position start;
        private final Position destination;
        private final String salt;
    
        public void findPaths(final Function<Path, Boolean> stop) {
            final Deque<Path> paths = new ArrayDeque<>();
            Path path = new Path("", this.start);
            paths.add(path);
            while (!stop.apply(path) && !paths.isEmpty()) {
                path = paths.removeFirst();
                if (path.isAt(this.destination)) {
                    continue;
                }
                final boolean[] doors = areDoorsOpen(path);
                for (final Direction direction : DIRECTIONS) {
                    final Path newPath = buildNewPath(path, direction);
                    if (doors[DIRECTIONS.indexOf(direction)]
                            && isInBounds(newPath.getPosition())) {
                        paths.add(newPath);
                    }
                }
            }
        }
        
        private boolean[] areDoorsOpen(final Path path) {
            final String data = new StringBuilder().append(this.salt)
                    .append(path.getPath()).toString();
            final String md5Hex = MD5.md5Hex(data);
            final boolean[] doors = new boolean[DOORS.length];
            for (int d = 0; d < DIRECTIONS.size(); d++) {
                doors[d] = OPEN_CHARS.contains(md5Hex.charAt(d));
            }
            return doors;
        }
    
        private Path buildNewPath(final Path path, final Direction direction) {
            return new Path(path.getPath() + DOORS[DIRECTIONS.indexOf(direction)],
                    Position.of(path.getPosition().getX() + direction.getX(),
                                path.getPosition().getY() + direction.getY()));
        }
    
        private boolean isInBounds(final Point position) {
            return position.getX() >= this.start.getX()
                    && position.getY() >= this.start.getY()
                    && position.getX() <= this.destination.getX()
                    && position.getY() <= this.destination.getY();
        }
    }
}
