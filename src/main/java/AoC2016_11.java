import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.ListUtils.intersection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.tuple.Tuples;

import com.github.pareronia.aocd.Aocd;

import lombok.AccessLevel;
import lombok.Getter;
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
    
    private Integer scoreStep(final Pair<Integer, State> p) {
        final Integer numberOfSteps = p.getOne();
        final State state = p.getTwo();
        return -state.getDiff() * numberOfSteps;
    }
    
    private Integer solve(final State initialState) {
        Integer numberOfSteps = 0;
        final PriorityQueue<Pair<Integer, State>> steps
                = new PriorityQueue<>(comparing(this::scoreStep));
        steps.add(Tuples.pair(0, initialState));
        final Set<Pair<Integer, Map<Integer, Map<String, Integer>>>> seen = new HashSet<>();
        seen.add(initialState.equivalentState());
        int cnt = 0;
        while (!steps.isEmpty()) {
            final Pair<Integer, State> step = steps.poll();
            cnt++;
            final State state = step.getTwo();
            if (state.isDestination()) {
                numberOfSteps = step.getOne();
                break;
            }
            state.moves().stream()
                    .filter(m -> !seen.contains(m.equivalentState()))
                    .forEach(m -> {
                        seen.add(m.equivalentState());
                        steps.add(Tuples.pair(step.getOne() + 1, m));
                    });
        }
        log(cnt);
        log("#steps: " + numberOfSteps);
        return numberOfSteps;
    }

    @Override
    public Integer solvePart1() {
        log("==================");
        log(() -> "Initial state: " + this.initialState);
        this.initialState.moves().forEach(this::log);
        return solve(this.initialState);
    }
    
    @Override
    public Integer solvePart2() {
        final Map<String, Integer> chips = new HashMap<>(this.initialState.getChips());
        chips.put("elerium", 1);
        chips.put("dilithium", 1);
        final Map<String, Integer> gennys = new HashMap<>(this.initialState.getGennys());
        gennys.put("elerium", 1);
        gennys.put("dilithium", 1);
        final State newState = this.initialState.withChips(chips).withGennys(gennys);
        log("==================");
        log(() -> "Initial state: " + newState);
        return solve(newState);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2016_11.createDebug(TEST).solvePart1() == 11;

        final List<String> input = Aocd.getData(2016, 11);
        lap("Part 1", () -> AoC2016_11.createDebug(input).solvePart1());
        lap("Part 2", () -> AoC2016_11.createDebug(input).solvePart2());
    }
    
    private static final List<String> TEST = splitLines(
            "The first floor contains a hydrogen-compatible microchip, and a lithium-compatible microchip.\n" +
            "The second floor contains a hydrogen generator.\n" +
            "The third floor contains a lithium generator.\n" +
            "The fourth floor contains nothing relevant."
    );
    
    @Value
    static final class State {
        @Getter(value = AccessLevel.PRIVATE)
        @With
        private final Integer elevator;
        @Getter(value = AccessLevel.PRIVATE)
        @With
        private final Map<String, Integer> chips;
        @Getter(value = AccessLevel.PRIVATE)
        @With
        private final Map<String, Integer> gennys;
        @With
        private final Integer diff;
        @Getter(value = AccessLevel.PRIVATE)
        @ToString.Exclude
        private final Map<Integer, List<String>> chipsPerFloor;
        @Getter(value = AccessLevel.PRIVATE)
        @ToString.Exclude
        private final Map<Integer, List<String>> gennysPerFloor;

        private State(
                final Integer elevator,
                final Map<String, Integer> chips,
                final Map<String, Integer> gennys,
                final Integer diff,
                final Map<Integer, List<String>> chipsPerFloor,
                final Map<Integer, List<String>> gennysPerFloor
                ) {
            super();
            this.elevator = elevator;
            this.chips = chips;
            this.gennys = gennys;
            this.diff = diff;
            this.chipsPerFloor = chips.keySet().stream().collect(groupingBy(chips::get));
            this.gennysPerFloor = gennys.keySet().stream().collect(groupingBy(gennys::get));
        }
        
        public State(
                final Integer elevator,
                final Map<String, Integer> chips,
                final Map<String, Integer> gennys,
                final Integer diff
                ) {
            this(   elevator,
                    Collections.unmodifiableMap(chips),
                    Collections.unmodifiableMap(gennys),
                    diff, null, null);
        }

        public State(
                final Integer elevator,
                final Map<String, Integer> chips,
                final Map<String, Integer> gennys
                ) {
            this(elevator, chips, gennys, 0);
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
            final boolean same = Objects.equals(elevator, other.elevator)
                                    && Objects.equals(chips, other.chips)
                                    && Objects.equals(gennys, other.gennys);
            return same;
        }

        @Override
        public int hashCode() {
            return Objects.hash(chips, elevator, gennys);
        }
        
        @ToString.Include
        boolean isSafe() {
            for (final Entry<String, Integer> chip : this.chips.entrySet()) {
                final List<String> gennysOnSameFloor
                        = this.gennysPerFloor.get(chip.getValue());
                if (gennysOnSameFloor != null
                        && !gennysOnSameFloor.contains(chip.getKey())) {
                    return false;
                }
            }
            return true;
        }
        
        public boolean isDestination() {
            return this.elevator == 4
                    && this.chips.values().stream().allMatch(c -> c == 4)
                    && this.gennys.values().stream().allMatch(g -> g == 4);
        }
        
        public List<State> moves() {
            final List<State> states = new ArrayList<>();
            final Integer floor = this.elevator;
            final List<String> chipsOnFloor
                    = this.chipsPerFloor.getOrDefault(floor, emptyList());
            if (chipsOnFloor.size() >= 2) {
                CombinatoricsUtils.combinationsIterator(chipsOnFloor.size(), 2)
                    .forEachRemaining(c -> {
                        final List<String> chipsToMove
                            = List.of(chipsOnFloor.get(c[0]),
                                      chipsOnFloor.get(c[1]));
                        states.add(withChipsTo(chipsToMove, floor + 1)
                                    .withDiff(2));
                        if (!floorsBelowEmpty(floor)) {
                            states.add(withChipsTo(chipsToMove, floor - 1)
                                    .withDiff(-2));
                        }
                    });
            }
            for (final String chip : chipsOnFloor) {
                states.add(withChipsTo(List.of(chip), floor + 1)
                            .withDiff(1));
                if (!floorsBelowEmpty(floor)) {
                    states.add(withChipsTo(List.of(chip), floor - 1)
                            .withDiff(-1));
                }
            }
            final List<String> gennysOnFloor
                    = this.gennysPerFloor.getOrDefault(floor, emptyList());
            if (gennysOnFloor.size() >= 2) {
                CombinatoricsUtils.combinationsIterator(gennysOnFloor.size(), 2)
                    .forEachRemaining(c -> {
                        final List<String> gennysToMove
                            = List.of(gennysOnFloor.get(c[0]),
                                      gennysOnFloor.get(c[1]));
                        states.add(withGeneratorsTo(gennysToMove, floor + 1)
                                    .withDiff(2));
                        if (!floorsBelowEmpty(floor)) {
                            states.add(withGeneratorsTo(gennysToMove, floor - 1)
                                    .withDiff(-2));
                        }
                    });
            }
            for (final String genny : gennysOnFloor) {
                states.add(withGeneratorsTo(List.of(genny), floor + 1)
                            .withDiff(1));
                if (!floorsBelowEmpty(floor)) {
                    states.add(withGeneratorsTo(List.of(genny), floor - 1)
                            .withDiff(-1));
                }
            }
            for (final String match : intersection(chipsOnFloor, gennysOnFloor)) {
                states.add(withChipsTo(List.of(match), floor + 1)
                        .withGeneratorsTo(List.of(match), floor + 1)
                        .withDiff(2));
                if (!floorsBelowEmpty(floor)) {
                    states.add(withChipsTo(List.of(match), floor - 1)
                            .withGeneratorsTo(List.of(match), floor - 1)
                            .withDiff(-2));
                }
            }
            return states.stream()
                    .filter(s -> Set.of(1, 2, 3, 4).contains(s.elevator))
                    .filter(State::isSafe)
                    .collect(toList());
        }
        
        public Pair<Integer, Map<Integer, Map<String, Integer>>> equivalentState() {
            assert this.isSafe();
            return Tuples.pair(this.elevator, countOfType());
        }
        
        private Map<Integer, Map<String, Integer>> countOfType() {
            final Map<Integer, Map<String, Integer>> counts = new HashMap<>();
            for (final String chip : this.chips.keySet()) {
                counts.computeIfAbsent(this.chips.get(chip), HashMap::new)
                        .merge("chip", 1, Integer::sum);
            }
            for (final String genny : this.gennys.keySet()) {
                counts.computeIfAbsent(this.gennys.get(genny), HashMap::new)
                        .merge("genny", 1, Integer::sum);
            }
            return counts;
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
        
        private boolean floorsBelowEmpty(final Integer floor) {
            for (int f = floor; f > 1; f--) {
                if (this.chipsPerFloor.get(f - 1) != null) {
                    return false;
                }
                if (this.gennysPerFloor.get(f - 1) != null) {
                    return false;
                }
            }
            return true;
        }
    }
}
