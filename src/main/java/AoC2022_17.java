import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Grid;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.navigation.Heading;
import com.github.pareronia.aoc.navigation.Headings;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2022_17 extends AoCBase {
    
    private static final Grid[] ROCKS = new Grid[] {
        Grid.from(List.of(
                "####")),
        Grid.from(List.of(
                ".#.",
                "###",
                ".#.")),
        Grid.from(List.of(
                "..#",
                "..#",
                "###")),
        Grid.from(List.of(
                "#",
                "#",
                "#",
                "#")),
        Grid.from(List.of(
                "##",
                "##")),
    };

    private final List<Heading> jets;
    
    private AoC2022_17(final List<String> input, final boolean debug) {
        super(debug);
        this.jets = Utils.asCharacterStream(input.get(0))
            .map(c -> {
                if (c == '>') {
                    return Headings.EAST.get();
                } else {
                    return Headings.WEST.get();
                }
            })
            .collect(toList());
    }
    
    public static final AoC2022_17 create(final List<String> input) {
        return new AoC2022_17(input, false);
    }

    public static final AoC2022_17 createDebug(final List<String> input) {
        return new AoC2022_17(input, true);
    }
    
    private int topMost(final Deque<Rock> stack) {
        return stack.stream().flatMap(Rock::get)
            .mapToInt(Position::getY)
            .max().orElseThrow();
    }
    
    private void draw(final Deque<Rock> stack) {
        if (!this.debug) {
            return;
        }
        final Set<Position> positions = stack.stream().flatMap(Rock::get).collect(toSet());
        final int max = positions.stream()
            .mapToInt(Position::getY)
            .max().orElseThrow();
        for (int y = max; y >= 0; y--) {
            final int they = y;
            log(IntStream.rangeClosed(0, 7)
                .mapToObj(x -> positions.contains(Position.of(x, they)) ? '#' : ' ')
                .collect(Utils.toAString()));
        }
    }
    
    private boolean hit(final Deque<Rock> stack, final Rock rock) {
        final Set<Position> positions = rock.get().collect(toSet());
        return stack.stream()
                .flatMap(r -> r.get())
                .anyMatch(positions::contains);
    }
    
    private void fall(final Rock rock, final Deque<Rock> stack, final JetSupplier jetSupplier) {
        int cnt = 0;
        while (true) {
            assert cnt++ < 10_000;
            final Heading jet = jetSupplier.get();
            log("move " + jet.toString());
            rock.move(jet);
            if (rock.leftMost() < 0 || rock.rightMost() > 6) {
                log("hit side: undo");
                rock.move(jet.rotate(180));
            } else if (hit(stack, rock)) {
                log("hit stack: undo");
                rock.move(jet.rotate(180));
            }
            log("move down");
            rock.move(Headings.SOUTH.get());
            if (rock.bottomMost() < 0) {
                log("hit bottom: undo");
                rock.move(Headings.NORTH.get());
                break;
            } else if (hit(stack, rock)) {
                log("hit stack: undo");
                rock.move(Headings.NORTH.get());
                break;
            }
        }
        stack.addFirst(rock);
        draw(stack);
    }
    
    @Override
    public Integer solvePart1() {
        final Deque<Rock> stack = new ArrayDeque<>();
        final JetSupplier jetSupplier = new JetSupplier(this.jets);
        Stream.generate(new RockSupplier()).limit(2022)
            .map(g -> {
                final Position p = stack.isEmpty()
                    ? Position.of(2, 3)
                    : Position.of(2, topMost(stack) + 4);
                return new Rock(g, p);
            })
            .forEach(rock -> fall(rock, stack, jetSupplier));
        final int ans = topMost(stack) + 1;
        System.out.println(ans);
        return ans;
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
    
    @AllArgsConstructor
    @ToString(onlyExplicitlyIncluded = true)
    private static final class Rock {
        private static final char EMPTY = '.';
        private final Grid shape;
        @ToString.Include
        private Position position;
        
        public Stream<Position> get() {
            return this.shape.findAllMatching(ch -> ch != EMPTY)
                .map(cell -> Position.of(
                        this.position.getX() + cell.getCol(),
                        this.position.getY() + this.shape.getMaxRowIndex() - cell.getRow()));
        }
        
        @ToString.Include
        public String grid() {
            return System.lineSeparator() + this.shape.toString();
        }
        
        public int bottomMost() {
            return this.position.getY();
        }
        
        public int leftMost() {
            return this.position.getX();
        }
        
        public int rightMost() {
            return this.position.getX() + this.shape.getMaxColIndex();
        }
        
        public void move(final Heading heading) {
            this.position = this.position.translate(heading);
        }
    }
    
    private static final class RockSupplier implements Supplier<Grid> {
        private int idx = 0;

        @Override
        public Grid get() {
            return ROCKS[idx++ % 5];
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
