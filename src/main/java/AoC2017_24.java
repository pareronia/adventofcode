import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public final class AoC2017_24 extends AoCBase {
    
    private final Set<Component> components;
    
    private AoC2017_24(final List<String> inputs, final boolean debug) {
        super(debug);
        final Function<String, Component> parseComponent = input -> {
            final Integer[] ports = Arrays.stream(input.split("/"))
                    .map(Integer::parseInt)
                    .toArray(Integer[]::new);
            return new Component(Pair.of(ports[0], ports[1]));
        };
        this.components = inputs.stream().map(parseComponent).collect(toSet());
        log(this.components);
    }

    public static AoC2017_24 create(final List<String> input) {
        return new AoC2017_24(input, false);
    }

    public static AoC2017_24 createDebug(final List<String> input) {
        return new AoC2017_24(input, true);
    }
        
    private Set<Bridge> getBridges() {
        final Set<Bridge> bridges = new HashSet<>();
        final Deque<Bridge> q = new ArrayDeque<>();
        q.add(new Bridge(Set.of(), 0, 0));
        while (!q.isEmpty()) {
            final Bridge b = q.poll();
            this.components.stream()
                .filter(c -> c.hasPort(b.getLast()))
                .filter(c -> !b.contains(c))
                .map(c -> b.extend(c))
                .filter(nb -> !bridges.contains(nb))
                .forEach(nb -> {
                    bridges.add(nb);
                    q.add(nb);
                });
        }
        return bridges;
    }
    
    @Override
    public Integer solvePart1() {
        return getBridges().stream()
            .peek(this::log)
            .mapToInt(Bridge::getStrength)
            .max().getAsInt();
    }

    @Override
    public Integer solvePart2() {
        return getBridges().stream()
            .sorted(Bridge.byLength().reversed()
                        .thenComparing(Bridge.byStrength().reversed()))
            .findFirst()
            .map(Bridge::getStrength).orElseThrow();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_24.createDebug(TEST).solvePart1() == 31;
        assert AoC2017_24.createDebug(TEST).solvePart2() == 19;
        
        final Puzzle puzzle = Aocd.puzzle(2017, 24);
        final List<String> input = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", create(input)::solvePart1),
            () -> lap("Part 2", create(input)::solvePart2)
        );
    }
    
    private static final List<String> TEST = splitLines(
        "0/2\r\n" +
        "2/2\r\n" +
        "2/3\r\n" +
        "3/4\r\n" +
        "3/5\r\n" +
        "0/1\r\n" +
        "10/1\r\n" +
        "9/10"
    );
    
    @RequiredArgsConstructor
    @EqualsAndHashCode
    private static final class Component {
        private final Pair<Integer, Integer> ports;
        
        public boolean hasPort(final int port) {
            return ports.getLeft() == port || ports.getRight() == port;
        }

        @Override
        public String toString() {
            return String.format("%d/%d", ports.getLeft(), ports.getRight());
        }
    }
    
    @EqualsAndHashCode()
    private static final class Bridge {
        private final Set<Component> components;
        @Getter
        private final int strength;
        @Getter
        private final Integer last;
        
        private Bridge(
            final Set<Component> components,
            final int strength,
            final Integer last
         ) {
            this.components = components;
            this.strength = strength;
            this.last = last;
        }
        
        public Bridge extend(final Component component) {
            final Set<Component> newComponents = new HashSet<>(this.components);
            newComponents.add(component);
            final int left = component.ports.getLeft();
            final int right = component.ports.getRight();
            final int newStrength = this.strength + left + right;
            final int newLast = left == this.last ? right : left;
            return new Bridge(newComponents, newStrength, newLast);
        }
        
        public boolean contains(final Component component) {
            return this.components.contains(component);
        }
        
        public static Comparator<Bridge> byLength() {
            return (b1, b2) ->
                Integer.compare(b1.components.size(), b2.components.size());
        }

        public static Comparator<Bridge> byStrength() {
            return (b1, b2) -> Integer.compare(b1.strength, b2.strength);
        }
        
        @Override
        public String toString() {
            return this.components.stream()
                  .map(Component::toString)
                  .collect(joining("--"));
        }
    }
}