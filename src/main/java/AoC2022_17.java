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

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

// TODO: needs a lot of cleanup, part 2 loop size visually derived
public class AoC2022_17 extends AoCBase {
    
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
    
//    private void draw(final Stack stack) {
//        if (!this.debug) {
//            return;
//        }
//        final int max = stack.top;
//        for (int y = max; y >= 0; y--) {
//            final int they = y;
//            log(IntStream.rangeClosed(0, 7)
//                .mapToObj(x -> stack.contains(Position.of(x, they)) ? '#' : ' ')
//                .collect(Utils.toAString()));
//        }
//    }
//
    int cnt;
    Map<State, List<Cycle>> states;
    
    private State fall(
            final Rock start, final Stack stack, final JetSupplier jetSupplier) {
        log(start);
        Rock rock = start;
        State state;
        int cnt = 0;
        while (true) {
            assert cnt++ < 10000;
            final Heading jet = jetSupplier.get();
            state = new State(rock.idx, stack.getTopRows(20));
            if (cnt == 1) {
                this.states.computeIfAbsent(state, k -> new ArrayList<>())
                    .add(new Cycle(this.cnt++, stack.getTop()));
            }
            log("move " + jet.toString());
            Rock moved = rock.move(jet);
            if (!moved.insideX(0, 6)) {
                log("hit side: undo");
            } else if (stack.overlappedBy(moved)) {
//                assert stack.overlappedBy2(moved);
                log("hit stack: undo");
            } else {
                rock = moved;
            }
            log("move down");
            moved = rock.move(Headings.SOUTH.get());
            if (stack.overlappedBy(moved)) {
//                assert stack.overlappedBy2(moved);
                log("hit stack: undo");
                break;
            }
            rock = moved;
        }
        log("add to stack");
        stack.add(rock);
//        assert stack.top == stack.getTops().values().stream().mapToInt(Integer::valueOf).max().orElse(0) + 1;
//        draw(stack);
        return state;
    }
    
    private int solve1(final int count) {
        this.cnt = 0;
        this.states = new HashMap<>();
        final Stack stack = new Stack(FLOOR);
        final JetSupplier jetSupplier = new JetSupplier(this.jets);
        Stream.generate(new ShapeSupplier()).limit(count)
            .map(shape -> new Rock(shape.idx, shape.shape).move(Vector.of(2, stack.getTop() + 3)))
            .forEach(rock -> fall(rock, stack, jetSupplier));
        final int ans = stack.getTop();
        System.out.println(ans);
        System.out.println(this.cnt);
        System.out.println(this.states.size());
        this.states.entrySet().stream().filter(e -> e.getValue().size() > 1)
                .forEach(System.out::println);
        return ans;
    }
    
    @Override
    public Integer solvePart1() {
        return solve1(2022);
    }

    @Override
    public Long solvePart2() {
        this.cnt = 0;
        this.states = new HashMap<>();
        final Stack stack = new Stack(FLOOR);
        final JetSupplier jetSupplier = new JetSupplier(this.jets);
        final ShapeSupplier shapeSupplier = new ShapeSupplier();
        int drops = 0;
        State state;
        while (true) {
            final Shape shape = shapeSupplier.get();
            final Rock rock = new Rock(shape.idx, shape.shape)
                    .move(Vector.of(2, stack.getTop() + 3));
            state = fall(rock, stack, jetSupplier);
            drops++;
            if (this.states.getOrDefault(state, List.of()).size() > 1) {
                break;
            }
        }
        final List<Cycle> cycles = this.states.get(state);
        final int loopsize = 1725;//cycles.get(1).cycle - cycles.get(0).cycle;
        System.out.println("loopsize: " + loopsize);
        final int diff = 2728;//cycles.get(1).top - cycles.get(0).top;
        System.out.println("diff: " + diff);
        final long loops = Math.floorDiv(1_000_000_000_000L - drops, loopsize);
        System.out.println("loops: " + loops);
        final long left = 1_000_000_000_000L - (drops + loops * loopsize);
        System.out.println("left: " + left);
        for (int i = 0; i < left; i++) {
            final Shape shape = shapeSupplier.get();
            final Rock rock = new Rock(shape.idx, shape.shape)
                    .move(Vector.of(2, stack.getTop() + 3));
            fall(rock, stack, jetSupplier);
        }
        final int topTail = stack.getTop();
        final long ans = topTail + loops * diff;
        System.out.println(ans);
        return ans;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_17.create(TEST).solvePart1() == 3068;
//        assert AoC2022_17.create(TEST).solvePart2() == 1_514_285_714_288L;

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
    
    @AllArgsConstructor
    @ToString
    private static final class Rock {
        private final int idx;
        private final Set<Position> shape;
        
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
    
    @RequiredArgsConstructor
    private static final class Shape {
        private final int idx;
        private final Set<Position> shape;
    }
    
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
    
    @RequiredArgsConstructor
    @EqualsAndHashCode
    @ToString
    private static final class State {
        private final int shape;
        private final Set<Position> top;
    }
    
    @RequiredArgsConstructor
    @ToString
    private static final class Cycle {
        private final int cycle;
        private final int top;
    }
}
