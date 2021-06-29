import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang3.mutable.MutableInt;

import com.github.pareronia.aoc.Grid;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aocd.Aocd;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public final class AoC2016_13 extends AoCBase {

    private static final char ONE = '1';
    private static final char FLOOR = '.';
    private static final char WALL = '#';
    private static final Position START = Position.of(1, 1);
    
    private final transient Integer input;
    private final transient Map<Position, Boolean> openSpaceCache;

    private AoC2016_13(final List<String> inputs, final boolean debug) {
        super(debug);
        assert inputs.size() == 1;
        this.input = Integer.valueOf(inputs.get(0));
        this.openSpaceCache = new HashMap<>();
    }

    public static AoC2016_13 create(final List<String> input) {
        return new AoC2016_13(input, false);
    }

    public static AoC2016_13 createDebug(final List<String> input) {
        return new AoC2016_13(input, true);
    }
    
    private boolean isOpenSpace(final Position position) {
        return this.openSpaceCache.computeIfAbsent(position, pos -> {
            final int x = position.getX();
            final int y = position.getY();
            final int t = this.input + x * x + 3 * x + 2 * x * y + y + y * y;
            final long ones = Utils.asCharacterStream(Integer.toBinaryString(t))
                    .filter(c -> c == ONE).count();
            return ones % 2 == 0;
        });
    }
    
    private Grid createGrid(final Integer rows, final Integer cols) {
        final char[][] grid = new char[rows + 1][cols + 1];
        for (int rr = 0; rr <= rows; rr++) {
            final char[] newrow = new char[cols + 1];
            for (int cc = 0; cc <= cols; cc++) {
                newrow[cc] = isOpenSpace(Position.of(cc, rr)) ? FLOOR : WALL;
            }
            grid[rr] = newrow;
        }
        return new Grid(grid);
    }
    
    private Iterator<Position> neighbours(final Position pos) {
        return Set.of(Position.of(pos.getX() + 1, pos.getY()),
                Position.of(pos.getX() - 1, pos.getY()),
                Position.of(pos.getX(), pos.getY() + 1),
                Position.of(pos.getX(), pos.getY() - 1)).stream()
            .filter(p -> p.getX() >= 0 && p.getY() >= 0)
            .filter(this::isOpenSpace)
            .iterator();
    }

    private ElementSet getDistance(final Position source, final Position destination) {
        final ElementSet set = new ElementSet();
        set.add(destination, 0);
        final MutableInt size = new MutableInt(1);
        for (int i = 0; i < size.getValue(); i++) {
            final Element next = set.get(i);
            if (next.getPosition().equals(source)) {
                break;
            }
            neighbours(next.getPosition()).forEachRemaining(position -> {
                if (!set.contains(position)) {
                    set.add(position, next.cost + 1);
                    size.setValue(set.size());
                }
            });
        }
        return set;
    }
    
    private Optional<Integer> findStepsFromStart(final Position position) {
        final ElementSet set = getDistance(START, position);
        return set.find(START).map(Element::getCost);
    }

    @Override
    public Integer solvePart1() {
        return findStepsFromStart(Position.of(31, 39))
                .orElseThrow(() -> new IllegalStateException("Unsolvable"));
    }

    @Override
    public Integer solvePart2() {
        log(() -> createGrid(51, 51));
        return (int) Stream.iterate(0, x -> x <= 51, x -> x + 1)
                .flatMap(x -> Stream.iterate(0, y -> y <= 51, y -> y + 1)
                        .map(y -> Position.of(x, y))
                        .filter(this::isOpenSpace)
                        .map(this::findStepsFromStart)
                        .filter(Optional::isPresent)
                        .map(Optional::get))
                .filter(c -> c <= 50)
                .count();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2016_13.createDebug(splitLines("10")).findStepsFromStart(Position.of(1, 1)).get() == 0;
        assert AoC2016_13.createDebug(splitLines("10")).findStepsFromStart(Position.of(7, 4)).get() == 11;
        
        final List<String> input = Aocd.getData(2016, 13);
        lap("Part 1", () -> AoC2016_13.create(input).solvePart1());
        lap("Part 2", () -> AoC2016_13.create(input).solvePart2());
    }
    
    @RequiredArgsConstructor
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @ToString(includeFieldNames = false)
    private static final class Element {
        @EqualsAndHashCode.Include
        private final Integer x;
        @EqualsAndHashCode.Include
        private final Integer y;
        @Getter
        private final Integer cost;
        
        public Position getPosition() {
            return Position.of(x, y);
        }
    }
    
    @ToString(includeFieldNames = false)
    private static final class ElementSet {
        private final List<Element> elements = new ArrayList<>();
        
        public void add(final Position position, final Integer cost) {
            this.elements.add(new Element(position.getX(), position.getY(), cost));
        }
        
        public boolean contains(final Position position) {
            return this.elements.stream()
                    .anyMatch(e -> e.x == position.getX() && e.y == position.getY());
        }
        
        public Optional<Element> find(final Position position) {
            return this.elements.stream()
                    .filter(e -> e.x == position.getX() && e.y == position.getY())
                    .findFirst();
        }
        
        public int size() {
            return this.elements.size();
        }
        
        public Element get(final int index) {
            return this.elements.get(index);
        }
    }
}