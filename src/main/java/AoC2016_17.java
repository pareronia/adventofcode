import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.apache.commons.codec.digest.DigestUtils;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public final class AoC2016_17 extends AoCBase {

    private static final List<Character> OPEN_CHARS = List.of('b', 'c', 'd', 'e', 'f');
    private static final char UP = 'U';
    private static final char DOWN = 'D';
    private static final char LEFT = 'L';
    private static final char RIGHT = 'R';
    
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
        final PriorityQueue<Path> paths
                = new PriorityQueue<>(comparing(Path::length));
        Path path = new Path("");
        paths.add(path);
        while (!path.isDestination()) {
            path = paths.poll();
            for (final Character door : path.findNextOpenDoors(this.input)) {
                paths.add(new Path(path.getPath() + String.valueOf(door)));
            }
        }
        return path.getPath();
    }

    @Override
    public Integer solvePart2() {
        return 0;
    }

    public static void main(final String[] args) throws Exception {
        final List<String> input = Aocd.getData(2016, 17);
        lap("Part 1", () -> AoC2016_17.create(input).solvePart1());
        lap("Part 2", () -> AoC2016_17.create(input).solvePart2());
    }
    
    @RequiredArgsConstructor
    @EqualsAndHashCode
    private static final class Path {
        @Getter
        private final String path;
        
        public int length() {
           return this.path.length();
        }

        public boolean isDestination() {
            final Map<Character, Long> steps = Utils.asCharacterStream(this.path)
                    .collect(groupingBy(step -> step, HashMap::new, counting()));
            final long goneDown
                    = steps.getOrDefault(DOWN, 0L) - steps.getOrDefault(UP, 0L);
            final long goneRight
                    = steps.getOrDefault(RIGHT, 0L) - steps.getOrDefault(LEFT, 0L);
            return goneDown == 3L && goneRight == 3L;
        }
        
        public List<Character> findNextOpenDoors(final String input) {
            final String data
                    = new StringBuilder().append(input).append(path).toString();
            final String md5Hex = DigestUtils.md5Hex(data);
            final List<Character> doors = new ArrayList<>();
            if (OPEN_CHARS.contains(md5Hex.charAt(0))) {
                doors.add(UP);
            }
            if (OPEN_CHARS.contains(md5Hex.charAt(1))) {
                doors.add(DOWN);
            }
            if (OPEN_CHARS.contains(md5Hex.charAt(2))) {
                doors.add(LEFT);
            }
            if (OPEN_CHARS.contains(md5Hex.charAt(3))) {
                doors.add(RIGHT);
            }
            return doors;
        }
    }
}
