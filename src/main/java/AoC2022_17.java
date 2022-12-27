import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.geometry.Vector;
import com.github.pareronia.aoc.navigation.Heading;
import com.github.pareronia.aoc.navigation.Headings;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.RequiredArgsConstructor;

// TODO: needs more cleanup
public class AoC2022_17 extends AoCBase {
    
    private static final int OFFSET_X = 2;
    private static final int OFFSET_Y = 3;
    private static final int LOOP_TRESHOLD = 3_000;
    private static final Set<Position> FLOOR = IntStream.rangeClosed(0, 6)
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

    private final List<Heading> jets;
    
    private AoC2022_17(final List<String> input, final boolean debug) {
        super(debug);
        this.jets = Utils.asCharacterStream(input.get(0))
            .map(c -> c== '>' ? Headings.EAST.get() : Headings.WEST.get())
            .collect(toList());
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
    
//    private void draw(final Stack stack) {
//        if (!this.trace) {
//            return;
//        }
//        final int max = stack.top;
//        for (int y = max; y >= 0; y--) {
//            final int they = y;
//            trace(IntStream.rangeClosed(0, 7)
//                .mapToObj(x -> stack.contains(Position.of(x, they)) ? '#' : ' ')
//                .collect(Utils.toAString()));
//        }
//    }
//
    Map<State, List<Cycle>> states;
    
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
            state = new State(rock.idx, stack.getTopRows(20));
            if (cnt++ == 1) {
                this.states.computeIfAbsent(state, k -> new ArrayList<>())
                    .add(new Cycle(dropIndex, stack.getTop()));
            }
            final var jet = jetSupplier.get();
            trace(() -> "move " + jet.toString());
            Rock moved = rock.move(jet);
            if (!moved.insideX(0, 6)) {
                trace(() -> "hit side: undo");
            } else if (stack.overlappedBy(moved)) {
//                assert stack.overlappedBy2(moved);
                trace(() -> "hit stack: undo");
            } else {
                rock = moved;
            }
            trace(() -> "move down");
            moved = rock.move(Headings.SOUTH.get());
            if (stack.overlappedBy(moved)) {
//                assert stack.overlappedBy2(moved);
                trace(() -> "hit stack: undo");
                break;
            }
            rock = moved;
        }
        trace(() -> "add to stack");
        stack.add(rock);
//        assert stack.top == stack.getTops().values().stream().mapToInt(Integer::valueOf).max().orElse(0) + 1;
//        draw(stack);
        return state;
    }

    private Long solve(final long requestedDrops) {
        this.states = new HashMap<>();
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
        private final Set<Position> positions;
        private final Map<Integer, Integer> tops;
        private int top;
        
        public Stack(final Set<Position> positions) {
            this.positions = new HashSet<>(positions);
            this.tops = getTops(positions);
        }
        
        public int getTop() {
            return top;
//            return this.tops.values().stream()
//                    .mapToInt(Integer::valueOf)
//                    .max().orElse(0) + 1;
        }
        
        public Set<Position> getTopRows(final int n) {
            return this.positions.stream()
                    .filter(p -> p.getY() > top - n)
                    .map(p -> Position.of(p.getX(), p.getY() - (top - n)))
                    .collect(toSet());
        }

        public void add(final Rock rock) {
            rock.blocks().forEach(p -> {
                this.tops.merge(p.getX(), p.getY(), Math::max);
                this.positions.add(p);
                this.top = Math.max(top, p.getY() + 1);
            });
        }
        
        public boolean overlappedBy(final Rock rock) {
            return rock.blocks().anyMatch(this.positions::contains);
//            return rock.blocks()
//                    .anyMatch(b -> b.getY() <= this.tops.get(b.getX()));
        }
        
        public boolean overlappedBy2(final Rock rock) {
            return !rock.blocks()
                    .allMatch(b -> b.getY() > this.tops.get(b.getX()));
        }
        
        public Map<Integer, Integer> getTops() {
            return tops;
        }

        public Map<Integer, Integer> getTops(final Set<Position> positions) {
            return positions.stream()
                .collect(groupingBy(
                    Position::getX,
                    mapping(p -> p.getY(), reducing(Integer.MIN_VALUE, Math::max))));
        }
//
//        public boolean contains(final Position p) {
//            return this.positions.contains(p);
//        }
    }
    
    private static final record Rock(int idx, Set<Position> shape) {
        
        public Rock move(final Vector vector) {
            final Set<Position> newShape = blocks()
                    .map(p -> p.translate(vector))
                    .collect(toSet());
            return new Rock(this.idx, newShape);
        }
        
        public boolean insideX(final int startInclusive, final int endInclusive) {
            return blocks().mapToInt(Position::getX)
                    .allMatch(x -> startInclusive <= x && x <= endInclusive);
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
            final int index = idx++ % 5;
            return new Shape(index, SHAPES.get(index));
        }
    }
    
    @RequiredArgsConstructor
    public static final class JetSupplier implements Supplier<Heading> {
        private final List<Heading> jets;
        private int idx = 0;

        @Override
        public Heading get() {
            return this.jets.get(idx++ % jets.size());
        }
    }
    
    private static final record State(int shape, Set<Position> top) { }
    
    private static final record Cycle(int cycle, int top) { }
}
