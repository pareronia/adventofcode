import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.Comparator.comparing;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aocd.Aocd;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public final class AoC2016_17 extends AoCBase {

    private static final Position START = Position.of(0, 0);
    private static final Position DESTINATION = Position.of(3, 3);
    private static final List<Character> OPEN_CHARS = List.of('b', 'c', 'd', 'e', 'f');
    private static final char UP = 'U';
    private static final char DOWN = 'D';
    private static final char LEFT = 'L';
    private static final char RIGHT = 'R';
    private static final char[] DOORS = { UP, DOWN, LEFT, RIGHT };
    
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
    
    private void findPaths(final List<Path> paths, final Deque<Character> steps) {
        final String _path = steps.stream().collect(toAString());
        final Path path = new Path(_path);
        if (path.isAtDestination()) {
            paths.add(path);
            return;
        }
        for (final Character door : path.findNextOpenDoors(this.input)) {
            final Path newPath = new Path(path.getPath() + door);
            if (newPath.isInBounds()) {
                steps.add(door);
                findPaths(paths, steps);
                steps.removeLast();
            }
        }
    }
    
    @Override
    public String solvePart1() {
        final PriorityQueue<Path> paths
                = new PriorityQueue<>(comparing(Path::length));
        Path path = new Path("");
        paths.add(path);
        while (!path.isAtDestination()) {
            path = paths.poll();
            for (final Character door : path.findNextOpenDoors(this.input)) {
                final Path newPath = new Path(path.getPath() + door);
                if (newPath.isInBounds()) {
                    paths.add(newPath);
                }
            }
        }
        log(() -> Path.cacheStats());
        return path.getPath();
    }
    
    @Override
    public Integer solvePart2() {
        final List<Path> paths = new ArrayList<>();
        findPaths(paths, new ArrayDeque<Character>());
        log(() -> Path.cacheStats());
        return paths.stream()
                .max(comparing(Path::length))
                .map(Path::length).orElseThrow();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2016_17.createDebug(splitLines("ihgpwlah")).solvePart2() == 370;
        assert AoC2016_17.createDebug(splitLines("kglvqrro")).solvePart2() == 492;
        assert AoC2016_17.createDebug(splitLines("ulqzkmiv")).solvePart2() == 830;

        final List<String> input = Aocd.getData(2016, 17);
        lap("Part 1", () -> AoC2016_17.create(input).solvePart1());
        lap("Part 2", () -> AoC2016_17.create(input).solvePart2());
    }
    
    @RequiredArgsConstructor
    @EqualsAndHashCode
    private static final class Path {
        private static final Map<String, Position> POSITIONCACHE = new HashMap<>();
        private static int hits;
        private static int misses;
        
        @Getter
        private final String path;
        
        private static Position findPosition(final String steps) {
            if (!POSITIONCACHE.containsKey(steps)) {
                misses++;
                final Position position;
                if (StringUtils.isEmpty(steps)) {
                    position = START;
                } else {
                    final CharSequence prevStep = steps.subSequence(0, steps.length() - 1);
                    final Position prevPos = findPosition(prevStep.toString());
                    final char thisStep
                            = steps.subSequence(steps.length() - 1,
                                                steps.length()).charAt(0);
                    if (thisStep == UP) {
                        position = Position.of(prevPos.getX(), prevPos.getY() - 1);
                    } else if (thisStep == DOWN) {
                        position = Position.of(prevPos.getX(), prevPos.getY() + 1);
                    } else if (thisStep == LEFT) {
                        position = Position.of(prevPos.getX() - 1, prevPos.getY());
                    } else if (thisStep == RIGHT) {
                        position = Position.of(prevPos.getX() + 1, prevPos.getY());
                    } else {
                        throw new IllegalStateException("Invalid: " + thisStep);
                    }
                }
                POSITIONCACHE.put(steps, position);
            } else {
                hits++;
            }
            return POSITIONCACHE.get(steps);
        }
        
        public static String cacheStats() {
            return String.format("Size: %d, hits: %d, misses: %d",
                                 POSITIONCACHE.size(), hits, misses);
        }
        
        public int length() {
            return this.path.length();
        }
        
        public Position getPosition() {
            return findPosition(this.path);
        }

        public boolean isAtDestination() {
            return getPosition().equals(DESTINATION);
        }
        
        public boolean isInBounds() {
            final Position position = getPosition();
            return position.getX() >= START.getX()
                    && position.getX() <= DESTINATION.getY()
                    && position.getY() >= START.getY()
                    && position.getY() <= DESTINATION.getY();
        }
        
        public List<Character> findNextOpenDoors(final String input) {
            final String data
                    = new StringBuilder().append(input).append(path).toString();
            final String md5Hex = DigestUtils.md5Hex(data);
            final List<Character> doors = new ArrayList<>();
            for (int i = 0; i < DOORS.length; i++) {
                if (OPEN_CHARS.contains(md5Hex.charAt(i))) {
                    doors.add(DOORS[i]);
                }
            }
            return doors;
        }
    }
}
