import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.ListUtils.intersection;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.tuple.Tuples;

import com.github.pareronia.aocd.Aocd;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;
import lombok.With;

public final class AoC2016_11 extends AoCBase {
    
    private final transient State initialState;

    private AoC2016_11(final List<String> inputs, final boolean debug) {
        super(debug);
        this.initialState = parse(inputs);
    }

    private State parse(final List<String> inputs) {
        final Map<String, Integer> chips = new HashMap<>();
        final Map<String, Integer> generators = new HashMap<>();
        for (int i = 0; i < inputs.size(); i++) {
            String floor = inputs.get(i);
            floor = floor.replaceAll(", and", ",");
            floor = floor.replaceAll("\\.", "");
            final String contains = floor.split(" contains ")[1];
            final String[] contained = contains.split(", ");
            for (final String containee : contained) {
                final String[] s = containee.split(" ");
                if ("nothing".equals(s[0])) {
                   continue;
                } else if ("generator".equals(s[2])) {
                    generators.put(s[1], i + 1);
                } else if ("microchip".equals(s[2])) {
                    chips.put(StringUtils.substringBefore(s[1], "-"), i + 1);
                } else {
                    throw new IllegalArgumentException("Invalid input");
                }
             }
        }
        final Integer elevator = 1;
        return new State(elevator, chips, generators);
    }

    public static AoC2016_11 create(final List<String> input) {
        return new AoC2016_11(input, false);
    }

    public static AoC2016_11 createDebug(final List<String> input) {
        return new AoC2016_11(input, true);
    }
    
    @Override
    public Integer solvePart1() {
        log(() -> "Initial state: " + this.initialState);
        this.initialState.moves().forEach(this::log);
        Integer numberOfSteps = 0;
        State complete = null;
        final Deque<Pair<Integer, State>> steps = new ArrayDeque<>();
        steps.add(Tuples.pair(0, this.initialState));
        final Set<State> seen = new HashSet<>();
        while (!steps.isEmpty() && steps.size() < 5_000_000) {
            final Pair<Integer, State> step = steps.poll();
            final State state = step.getTwo();
            if (state.getElevator() == 4
                    && state.getChips().values().stream().allMatch(c -> c == 4)
                    && state.getGennys().values().stream().allMatch(g -> g == 4)) {
                numberOfSteps = step.getOne();
                complete = state;
                break;
            }
            if (seen.contains(state)) {
                continue;
            }
            seen.add(state);
            final List<State> moves = state.moves();
            final List<State> valid = moves.stream()
                    .filter(m -> Set.of(1, 2, 3, 4).contains(m.getElevator()))
                    .filter(State::isSafe)
                    .collect(toList());
            valid.forEach(m -> steps.add(Tuples.pair(step.getOne() + 1, m)));
        }
        if (complete == null) {
            throw new IllegalStateException("Unsolvable");
        }
        log("#steps: " + numberOfSteps);
        log("Path:");
        log(complete);
        State cameFrom = complete.getCameFrom();
        while (cameFrom != null) {
            log(cameFrom);
            cameFrom = cameFrom.getCameFrom();
        }
        log("==================");
        return numberOfSteps;
    }
    
    @Override
    public Integer solvePart2() {
        return 0;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2016_11.createDebug(TEST).solvePart1() == 11;

        final List<String> input = Aocd.getData(2016, 11);
        lap("Part 1", () -> AoC2016_11.create(input).solvePart1());
        lap("Part 2", () -> AoC2016_11.create(input).solvePart2());
    }
    
    private static final List<String> TEST = splitLines(
            "The first floor contains a hydrogen-compatible microchip, and a lithium-compatible microchip.\n" +
            "The second floor contains a hydrogen generator.\n" +
            "The third floor contains a lithium generator.\n" +
            "The fourth floor contains nothing relevant."
    );
    
    @Value
    @RequiredArgsConstructor
    private static final class State {
        @With
        private final Integer elevator;
        @With
        private final Map<String, Integer> chips;
        @With
        private final Map<String, Integer> gennys;
        @EqualsAndHashCode.Exclude
        @ToString.Exclude
        @With
        private final State cameFrom;

        public State(
                final Integer elevator,
                final Map<String, Integer> chips,
                final Map<String, Integer> gennys
                ) {
            this(elevator, chips, gennys, null);
        }
        
        @ToString.Include
        public boolean isSafe() {
            final Map<Integer, List<String>> gennysPerFloor = this.gennysPerFloor();
            for (final Entry<String, Integer> chip : this.chips.entrySet()) {
                final List<String> gennysOnSameFloor
                        = gennysPerFloor.get(chip.getValue());
                if (gennysOnSameFloor != null
                        && !gennysOnSameFloor.contains(chip.getKey())) {
                    return false;
                }
            }
            return true;
        }

        public List<State> moves() {
            final List<State> states = new ArrayList<>();
            final Map<Integer, List<String>> chipsPerFloor = this.chipsPerFloor();
            final Integer floor = this.elevator;
            final List<String> chipsOnFloor
                    = chipsPerFloor.getOrDefault(floor, emptyList());
            if (chipsOnFloor.size() >= 2) {
                CombinatoricsUtils.combinationsIterator(chipsOnFloor.size(), 2)
                    .forEachRemaining(c -> {
                        final List<String> chipsToMove
                            = List.of(chipsOnFloor.get(c[0]),
                                      chipsOnFloor.get(c[1]));
                        states.add(withChipsTo(chipsToMove, floor + 1)
                                    .withCameFrom(this));
                        states.add(withChipsTo(chipsToMove, floor - 1)
                                    .withCameFrom(this));
                    });
            }
            for (final String chip : chipsOnFloor) {
                states.add(withChipsTo(List.of(chip), floor + 1)
                            .withCameFrom(this));
                states.add(withChipsTo(List.of(chip), floor - 1)
                            .withCameFrom(this));
            }
            final Map<Integer, List<String>> gennysPerFloor = this.gennysPerFloor();
            final List<String> gennysOnFloor
                    = gennysPerFloor.getOrDefault(floor, emptyList());
            if (gennysOnFloor.size() >= 2) {
                CombinatoricsUtils.combinationsIterator(gennysOnFloor.size(), 2)
                    .forEachRemaining(c -> {
                        final List<String> gennysToMove
                            = List.of(gennysOnFloor.get(c[0]),
                                      gennysOnFloor.get(c[1]));
                        states.add(withGeneratorsTo(gennysToMove, floor + 1)
                                    .withCameFrom(this));
                        states.add(withGeneratorsTo(gennysToMove, floor - 1)
                                    .withCameFrom(this));
                    });
            }
            for (final String genny : gennysOnFloor) {
                states.add(withGeneratorsTo(List.of(genny), floor + 1)
                            .withCameFrom(this));
                states.add(withGeneratorsTo(List.of(genny), floor - 1)
                            .withCameFrom(this));
            }
            for (final String match : intersection(chipsOnFloor, gennysOnFloor)) {
                states.add(withChipsTo(List.of(match), floor + 1)
                        .withGeneratorsTo(List.of(match), floor + 1)
                        .withCameFrom(this));
                states.add(withChipsTo(List.of(match), floor - 1)
                        .withGeneratorsTo(List.of(match), floor - 1)
                        .withCameFrom(this));
            }
            return states;
        }
        
        private Map<Integer, List<String>> chipsPerFloor() {
            return this.chips.keySet().stream()
                    .collect(groupingBy(this.chips::get));
        }
        
        private Map<Integer, List<String>> gennysPerFloor() {
            return this.gennys.keySet().stream()
                    .collect(groupingBy(this.gennys::get));
        }
        
        private State withChipsTo(final List<String> chips, final Integer floor) {
            final Map<String, Integer> newChips = new HashMap<>(this.chips);
            chips.forEach(c -> newChips.put(c, floor));
            return this.withChips(newChips).withElevator(floor);
        }
        
        private State withGeneratorsTo(final List<String> generators, final Integer floor) {
            final Map<String, Integer> newGenerators = new HashMap<>(this.gennys);
            generators.forEach(g -> newGenerators.put(g, floor));
            return this.withGennys(newGenerators).withElevator(floor);
        }
    }
}
