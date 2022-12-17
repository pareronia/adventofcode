import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.HashSet;
import java.util.List;
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
import lombok.RequiredArgsConstructor;
import lombok.ToString;

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
    
    private void draw(final Stack stack) {
        if (!this.debug) {
            return;
        }
        final int max = stack.top;
        for (int y = max; y >= 0; y--) {
            final int they = y;
            log(IntStream.rangeClosed(0, 7)
                .mapToObj(x -> stack.contains(Position.of(x, they)) ? '#' : ' ')
                .collect(Utils.toAString()));
        }
    }
    
    private void fall(
            final Rock start, final Stack stack, final JetSupplier jetSupplier) {
        log(start);
        Rock rock = start;
        while (true) {
            final Heading jet = jetSupplier.get();
            log("move " + jet.toString());
            Rock moved = rock.move(jet);
            if (!moved.insideX(0, 6)) {
                log("hit side: undo");
            } else if (stack.overlappedBy(moved)) {
                log("hit stack: undo");
            } else {
                rock = moved;
            }
            log("move down");
            moved = rock.move(Headings.SOUTH.get());
            if (stack.overlappedBy(moved)) {
                log("hit stack: undo");
                break;
            }
            rock = moved;
        }
        log("add to stack");
        stack.add(rock);
        draw(stack);
    }
    
    private int solve1(final int count) {
        final Stack stack = new Stack(FLOOR);
        final JetSupplier jetSupplier = new JetSupplier(this.jets);
        Stream.generate(new ShapeSupplier()).limit(count)
            .map(shape -> new Rock(shape).move(Vector.of(2, stack.top + 3)))
            .forEach(rock -> fall(rock, stack, jetSupplier));
        return stack.top;
    }
    
    @Override
    public Integer solvePart1() {
        return solve1(2022);
    }

    @Override
    public Integer solvePart2() {
        return 0;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_17.create(TEST).solvePart1() == 3068;
        assert AoC2022_17.createDebug(TEST).solvePart2() == 0;

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
        private int top;
        
        public Stack(final Set<Position> positions) {
            this.positions = new HashSet<>(positions);
        }

        public void add(final Rock rock) {
            rock.blocks().forEach(p -> {
                this.positions.add(p);
                this.top = Math.max(top, p.getY() + 1);
            });
        }
        
        public boolean overlappedBy(final Rock rock) {
            return rock.blocks().anyMatch(this.positions::contains);
        }
        
        public boolean contains(final Position p) {
            return this.positions.contains(p);
        }
    }
    
    @AllArgsConstructor
    @ToString
    private static final class Rock {
        private final Set<Position> shape;
        
        public Rock move(final Vector vector) {
            final Set<Position> newShape = blocks()
                    .map(p -> p.translate(vector))
                    .collect(toSet());
            return new Rock(newShape);
        }
        
        public boolean insideX(final int startInclusive, final int endInclusive) {
            return blocks().mapToInt(Position::getX)
                    .allMatch(x -> startInclusive <= x && x <= endInclusive);
        }
        
        private Stream<Position> blocks() {
            return this.shape.stream();
        }
    }
    
    private static final class ShapeSupplier implements Supplier<Set<Position>> {
        private int idx = 0;
        
        @Override
        public Set<Position> get() {
            return SHAPES.get(idx++ % 5);
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
}
