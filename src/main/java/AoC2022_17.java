import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aoc.AssertUtils;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.geometry.Vector;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

// TODO: needs more cleanup
public class AoC2022_17 extends AoCBase {
    
    private static final int OFFSET_X = 2;
    private static final int OFFSET_Y = 3;
    private static final int WIDTH = 7;
    private static final int KEEP_ROWS = 55;
    private static final int LOOP_TRESHOLD = 3_000;
    private static final Set<Position> FLOOR = IntStream.range(0, WIDTH)
            .mapToObj(x -> Position.of(x, -1)).collect(toSet());
    private static final List<Set<Position>> SHAPES = List.of(
        // ####
        Set.of(Position.of(0, 0), Position.of(1, 0), Position.of(2, 0), Position.of(3, 0)),
        //  #
        // ###
        //  #
        Set.of(Position.of(1, 2), Position.of(0, 1), Position.of(1, 1), Position.of(2, 1), Position.of(1, 0)),
        //   #
        //   #
        // ###
        Set.of(Position.of(2, 2), Position.of(2, 1), Position.of(0, 0), Position.of(1, 0), Position.of(2, 0)),
        // #
        // #
        // #
        // #
        Set.of(Position.of(0, 0), Position.of(0, 1), Position.of(0, 2), Position.of(0, 3)),
        // ##
        // ##
        Set.of(Position.of(0, 0), Position.of(0, 1), Position.of(1, 0), Position.of(1, 1))
    );

    private final List<Direction> jets;
    private final Map<State, List<Cycle>> states;
    
    private AoC2022_17(final List<String> input, final boolean debug) {
        super(debug);
        this.jets = Utils.asCharacterStream(input.get(0))
            .map(Direction::fromChar)
            .collect(toList());
        this.states = new HashMap<>();
    }
    
    public static final AoC2022_17 create(final List<String> input) {
        return new AoC2022_17(input, false);
    }

    public static final AoC2022_17 createDebug(final List<String> input) {
        return new AoC2022_17(input, true);
    }
    
    public static final AoC2022_17 createTrace(final List<String> input) {
        final AoC2022_17 puzzle = new AoC2022_17(input, true);
        puzzle.setTrace(true);
        return puzzle;
    }
    
    private void draw(final Stack stack) {
        if (!this.trace) {
            return;
        }
        final int max = stack.top;
        for (int y = max; y >= max - KEEP_ROWS; y--) {
            final int they = y;
            trace(IntStream.rangeClosed(0, 7)
                .mapToObj(x -> stack.contains(Position.of(x, they)) ? '#' : ' ')
                .collect(Utils.toAString()));
        }
    }

    private State drop(
            final int dropIndex,
            final Stack stack,
            final ShapeSupplier shapeSupplier,
            final JetSupplier jetSupplier
    ) {
        final var shape = shapeSupplier.get();
        final var start = new Rock(shape.idx, shape.shape)
                .move(Vector.of(OFFSET_X, stack.getTop() + OFFSET_Y));
        trace(() -> start);
        Rock rock = start;
        State state;
        int cnt = 0;
        while (true) {
            AssertUtils.assertTrue(cnt < 10000, () -> "infinite loop");
            final var jet = jetSupplier.get();
            state = new State(rock.idx, stack.getTopsNormalised(), jet);
            if (cnt++ == 1) {
                this.states.computeIfAbsent(state, k -> new ArrayList<>())
                    .add(new Cycle(dropIndex, stack.getTop()));
            }
            trace(() -> "move " + jet.toString());
            Rock moved = rock.move(jet.getVector());
            if (!moved.insideX(0, WIDTH)) {
                trace(() -> "hit side: undo");
            } else if (stack.overlappedBy(moved)) {
                trace(() -> "hit stack: undo");
            } else {
                rock = moved;
            }
            trace(() -> "move down");
            moved = rock.move(Direction.DOWN.getVector());
            if (stack.overlappedBy(moved)) {
                trace(() -> "hit stack: undo");
                break;
            }
            rock = moved;
        }
        trace(() -> "add to stack");
        stack.add(rock);
        draw(stack);
        return state;
    }

    private Long solve(final long requestedDrops) {
        final var stack = new Stack(FLOOR);
        final var jetSupplier = new JetSupplier(this.jets);
        final var shapeSupplier = new ShapeSupplier();
        int drops = 0;
        State state;
        while (true) {
            state = drop(drops++, stack, shapeSupplier, jetSupplier);
            if (drops == requestedDrops) {
                return (long) stack.getTop();
            }
            if (drops >= LOOP_TRESHOLD
                    && this.states.getOrDefault(state, List.of()).size() > 1) {
                break;
            }
        }
        log("drops: " + drops);
        final List<Cycle> cycles = this.states.get(state);
        final int loopsize = cycles.get(1).cycle - cycles.get(0).cycle;
        log("loopsize: " + loopsize);
        final int diff = cycles.get(1).top - cycles.get(0).top;
        log("diff: " + diff);
        final long loops = Math.floorDiv(requestedDrops - drops, loopsize);
        final long left = requestedDrops - (drops + loops * loopsize);
        for (int i = 0; i < left; i++) {
            drop(drops++, stack, shapeSupplier, jetSupplier);
        }
        return stack.getTop() + loops * diff;
    }
    
    @Override
    public Long solvePart1() {
        return solve(2022L);
    }
    
    @Override
    public Long solvePart2() {
        return solve(1_000_000_000_000L);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_17.createDebug(TEST).solvePart1() == 3068;
        assert AoC2022_17.createDebug(TEST).solvePart2() == 1_514_285_714_288L;

        final Puzzle puzzle = Aocd.puzzle(2022, 17);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_17.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_17.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines(
        ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"
    );
    
    private static final class Stack {
        private Set<Position> positions;
        private final Map<Integer, Integer> tops;
        private int top;
        
        public Stack(final Set<Position> positions) {
            this.positions = new HashSet<>(positions);
            this.tops = getTops(positions);
        }
        
        public int getTop() {
            return top;
        }
        
        public int[] getTopsNormalised() {
            return IntStream.range(0, WIDTH)
                .map(i -> this.top - this.tops.get(i))
                .toArray();
        }

        public Set<Position> getTopRows(final int n) {
            return this.positions.stream()
                    .filter(p -> p.getY() > top - n)
                    .collect(toSet());
        }

        public void add(final Rock rock) {
            rock.blocks().forEach(p -> {
                this.tops.merge(p.getX(), p.getY(), Math::max);
                this.positions.add(p);
                this.top = Math.max(top, p.getY() + 1);
            });
            this.positions = getTopRows(KEEP_ROWS);
        }
        
        public boolean overlappedBy(final Rock rock) {
            return rock.blocks().anyMatch(this.positions::contains);
        }
        
        public Map<Integer, Integer> getTops(final Set<Position> positions) {
            return positions.stream()
                .collect(groupingBy(
                    Position::getX,
                    mapping(Position::getY, reducing(Integer.MIN_VALUE, Math::max))));
        }

        public boolean contains(final Position p) {
            return this.positions.contains(p);
        }
    }
    
    private static final record Rock(int idx, Set<Position> shape) {
        
        public Rock move(final Vector vector) {
            final Set<Position> newShape = blocks()
                    .map(p -> p.translate(vector))
                    .collect(toSet());
            return new Rock(this.idx, newShape);
        }
        
        public boolean insideX(final int startInclusive, final int endExclusive) {
            return blocks().mapToInt(Position::getX)
                    .allMatch(x -> startInclusive <= x && x < endExclusive);
        }
        
        private Stream<Position> blocks() {
            return this.shape.stream();
        }
    }
    
    private static final record Shape(int idx, Set<Position> shape) { }
    
    private static final class ShapeSupplier implements Supplier<Shape> {
        private int idx = 0;
        
        @Override
        public Shape get() {
            final int index = idx++ % SHAPES.size();
            return new Shape(index, SHAPES.get(index));
        }
    }
    
    public static final class JetSupplier implements Supplier<Direction> {
        private final List<Direction> jets;
        private int idx = 0;

        public JetSupplier(final List<Direction> jets) {
            this.jets = jets;
        }

        @Override
        public Direction get() {
            return this.jets.get(idx++ % jets.size());
        }
    }
    
    record State(int shape, int[] tops, Direction jet) {
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + Arrays.hashCode(tops);
            return prime * result + Objects.hash(jet, shape);
        }
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final State other = (State) obj;
            return jet == other.jet && shape == other.shape && Arrays.equals(tops, other.tops);
        }
    }
    
    private static final record Cycle(int cycle, int top) { }
}
