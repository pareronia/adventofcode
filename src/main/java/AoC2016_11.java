import static com.github.pareronia.aoc.AssertUtils.unreachable;
import static com.github.pareronia.aoc.IterTools.combinations;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2016_11
                extends SolutionBase<AoC2016_11.State, Integer, Integer> {
    
    private AoC2016_11(final boolean debug) {
        super(debug);
    }

    public static AoC2016_11 create() {
        return new AoC2016_11(false);
    }

    public static AoC2016_11 createDebug() {
        return new AoC2016_11(true);
    }
    
    @Override
    protected State parseInput(final List<String> inputs) {
        return State.fromInput(inputs);
    }

    private int solve(final State initialState) {
        final Deque<Step> steps = new ArrayDeque<>();
        steps.add(Step.of(0, initialState));
        final Set<Integer> seen = new HashSet<>();
        seen.add(initialState.equivalentState());
        while (!steps.isEmpty()) {
            final Step step = steps.poll();
            final State state = step.state;
            if (state.isDestination()) {
                log("#steps: " + step.numberOfSteps);
                return step.numberOfSteps;
            }
            state.moves().stream()
                    .filter(m -> !seen.contains(m.equivalentState()))
                    .forEach(m -> {
                        seen.add(m.equivalentState());
                        steps.add(Step.of(step.numberOfSteps + 1, m));
                    });
        }
        throw unreachable();
    }

    @Override
    public Integer solvePart1(final State initialState) {
        return solve(initialState);
    }
    
    @Override
    public Integer solvePart2(final State initialState) {
        final Map<String, Integer> chips = new HashMap<>(initialState.getChips());
        chips.put("elerium", 1);
        chips.put("dilithium", 1);
        final Map<String, Integer> gennys = new HashMap<>(initialState.getGennys());
        gennys.put("elerium", 1);
        gennys.put("dilithium", 1);
        return solve(initialState.withChips(chips).withGennys(gennys));
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "11")
    })
    public static void main(final String[] args) throws Exception {
        AoC2016_11.create().run();
    }
    
    private static final String TEST = """
            The first floor contains a hydrogen-compatible microchip, and a lithium-compatible microchip.
            The second floor contains a hydrogen generator.
            The third floor contains a lithium generator.
            The fourth floor contains nothing relevant.
            """;
    
    static final class State {
        private static final List<Integer> FLOORS = List.of(1, 2, 3, 4);
        private static final int TOP = FLOORS.stream().mapToInt(Integer::intValue).max().getAsInt();
        private static final int BOTTOM = FLOORS.stream().mapToInt(Integer::intValue).min().getAsInt();
        private static final int MAX_ITEMS_PER_MOVE = 2;
        
        private final Integer elevator;
        private final Map<String, Integer> chips;
        private final Map<String, Integer> gennys;
        private final Map<Integer, List<String>> chipsPerFloor;
        private final Map<Integer, List<String>> gennysPerFloor;

        private State(
                final Integer elevator,
                final Map<String, Integer> chips,
                final Map<String, Integer> gennys,
                final Map<Integer, List<String>> chipsPerFloor,
                final Map<Integer, List<String>> gennysPerFloor
                ) {
            this.elevator = elevator;
            this.chips = chips;
            this.gennys = gennys;
            this.chipsPerFloor = chips.keySet().stream().collect(groupingBy(chips::get));
            this.gennysPerFloor = gennys.keySet().stream().collect(groupingBy(gennys::get));
        }
        
        State(
                final Integer elevator,
                final Map<String, Integer> chips,
                final Map<String, Integer> gennys
                ) {
            this(   elevator,
                    Collections.unmodifiableMap(chips),
                    Collections.unmodifiableMap(gennys),
                    null, null);
        }
        
        public static State fromInput(final List<String> inputs) {
            final Map<String, Integer> chips = new HashMap<>();
            final Map<String, Integer> generators = new HashMap<>();
            for (int i = 0; i < inputs.size(); i++) {
                String floor = inputs.get(i);
                floor = floor.replaceAll(",? and", ",");
                floor = floor.replace(".", "");
                final String contains = floor.split(" contains ")[1];
                final String[] contained = contains.split(", ");
                for (final String containee : contained) {
                    final String[] s = containee.split(" ");
                    if ("nothing".equals(s[0])) {
                       continue;
                    } else if ("generator".equals(s[2])) {
                        generators.put(s[1], i + 1);
                    } else {
                        chips.put(StringUtils.substringBefore(s[1], "-"), i + 1);
                    }
                 }
            }
            return new State(1, chips, generators);
        }
        
        public Map<String, Integer> getChips() {
            return chips;
        }

        public Map<String, Integer> getGennys() {
            return gennys;
        }

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
            return this.elevator == TOP
                    && this.chips.values().stream().allMatch(c -> c == TOP)
                    && this.gennys.values().stream().allMatch(g -> g == TOP);
        }
        
        public Integer equivalentState() {
            assert this.isSafe();
            final StringBuilder eq = new StringBuilder();
            eq.append(this.elevator);
            for (final int f : FLOORS) {
                eq.append(this.chipsPerFloor.getOrDefault(f, emptyList()).size());
                eq.append(this.gennysPerFloor.getOrDefault(f, emptyList()).size());
            }
            return Integer.valueOf(eq.toString());
        }
        
        public List<State> moves() {
            final List<State> states = new ArrayList<>();
            final Integer floor = this.elevator;
            final List<String> chipsOnFloor
                    = this.chipsPerFloor.getOrDefault(floor, emptyList());
            final List<String> gennysOnFloor
                    = this.gennysPerFloor.getOrDefault(floor, emptyList());
            states.addAll(moveMultipleChips(floor, chipsOnFloor));
            states.addAll(moveSingleChips(floor, chipsOnFloor));
            states.addAll(moveMultipleGennys(floor, gennysOnFloor));
            states.addAll(moveSingleGennys(floor, gennysOnFloor));
            states.addAll(moveChipAndGennyPairs(floor, chipsOnFloor, gennysOnFloor));
            return states.stream()
                    .filter(s -> FLOORS.contains(s.elevator))
                    .filter(State::isSafe)
                    .collect(toList());
        }

        private List<State> moveMultipleChips(
                final Integer floor,
                final List<String> chipsOnFloor) {
            final List<State> states = new ArrayList<>();
            if (chipsOnFloor.size() >= MAX_ITEMS_PER_MOVE) {
                combinations(chipsOnFloor.size(), MAX_ITEMS_PER_MOVE).stream()
                    .forEach(c -> {
                        final List<String> chipsToMove = Arrays.stream(c)
                                .mapToObj(chipsOnFloor::get).collect(toList());
                        states.add(moveUpWithChips(chipsToMove));
                        if (!floorsBelowEmpty(floor)) {
                            states.add(moveDownWithChips(chipsToMove));
                        }
                    });
            }
            return states;
        }

        private List<State> moveSingleChips(
                final Integer floor,
                final List<String> chipsOnFloor) {
            final List<State> states = new ArrayList<>();
            for (final String chip : chipsOnFloor) {
                states.add(moveUpWithChips(List.of(chip)));
                if (!floorsBelowEmpty(floor)) {
                    states.add(moveDownWithChips(List.of(chip)));
                }
            }
            return states;
        }

        private List<State> moveMultipleGennys(
                final Integer floor,
                final List<String> gennysOnFloor) {
            final List<State> states = new ArrayList<>();
            if (gennysOnFloor.size() >= MAX_ITEMS_PER_MOVE) {
                combinations(gennysOnFloor.size(), MAX_ITEMS_PER_MOVE).stream()
                .forEach(c -> {
                        final List<String> gennysToMove = Arrays.stream(c)
                                .mapToObj(gennysOnFloor::get).collect(toList());
                        states.add(moveUpWitGennys(gennysToMove));
                        if (!floorsBelowEmpty(floor)) {
                            states.add(moveDownWithGennys(gennysToMove));
                        }
                    });
            }
            return states;
        }

        private List<State> moveSingleGennys(
                final Integer floor,
                final List<String> gennysOnFloor) {
            final List<State> states = new ArrayList<>();
            for (final String genny : gennysOnFloor) {
                states.add(moveUpWitGennys(List.of(genny)));
                if (!floorsBelowEmpty(floor)) {
                    states.add(moveDownWithGennys(List.of(genny)));
                }
            }
            return states;
        }

        private List<State> moveChipAndGennyPairs(
                final Integer floor,
                final List<String> chipsOnFloor,
                final List<String> gennysOnFloor) {
            final List<State> states = new ArrayList<>();
            final List<String> intersection = new ArrayList<>(chipsOnFloor);
            intersection.retainAll(gennysOnFloor);
            for (final String match : intersection) {
                states.add(withChipsTo(List.of(match), floor + 1)
                        .withGennysTo(List.of(match), floor + 1)
                        .withElevator(floor + 1));
                if (!floorsBelowEmpty(floor)) {
                    states.add(withChipsTo(List.of(match), floor - 1)
                            .withGennysTo(List.of(match), floor - 1)
                            .withElevator(floor - 1));
                }
            }
            return states;
        }
        
        private State withElevator(final int elevator) {
            return new State(elevator, this.chips, this.gennys);
        }
        
        private State withChips(final Map<String, Integer> chips) {
            return new State(this.elevator, chips, this.gennys);
        }
        
        private State withGennys(final Map<String, Integer> gennys) {
            return new State(this.elevator, this.chips, gennys);
        }
        
        private State moveUpWithChips(final List<String> chips) {
            return withChipsTo(chips, this.elevator + 1)
                    .withElevator(this.elevator + 1);
        }
        
        private State moveUpWitGennys(final List<String> gennys) {
            return withGennysTo(gennys, this.elevator + 1)
                    .withElevator(this.elevator + 1);
        }
        
        private State moveDownWithChips(final List<String> chips) {
            return withChipsTo(chips, this.elevator - 1)
                    .withElevator(this.elevator - 1);
        }
        
        private State moveDownWithGennys(final List<String> gennys) {
            return withGennysTo(gennys, this.elevator - 1)
                    .withElevator(this.elevator - 1);
        }
        
        private State withChipsTo(final List<String> chips, final Integer floor) {
            final Map<String, Integer> newChips = new HashMap<>(this.chips);
            chips.forEach(c -> newChips.put(c, floor));
            return this.withChips(newChips);
        }
        
        private State withGennysTo(final List<String> generators, final Integer floor) {
            final Map<String, Integer> newGenerators = new HashMap<>(this.gennys);
            generators.forEach(g -> newGenerators.put(g, floor));
            return this.withGennys(newGenerators);
        }
        
        private boolean floorsBelowEmpty(final Integer floor) {
            for (int f = floor; f > BOTTOM; f--) {
                if (this.chipsPerFloor.get(f - 1) != null) {
                    return false;
                }
                if (this.gennysPerFloor.get(f - 1) != null) {
                    return false;
                }
            }
            return true;
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
            return Objects.equals(elevator, other.elevator)
                    && Objects.equals(chips, other.chips)
                    && Objects.equals(gennys, other.gennys);
        }

        @Override
        public int hashCode() {
            return Objects.hash(chips, elevator, gennys);
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("State [elevator=").append(elevator)
                    .append(", chips=").append(chips)
                    .append(", gennys=").append(gennys)
                    .append(", isSafe=").append(isSafe()).append("]");
            return builder.toString();
        }
    }
    
    record Step(int numberOfSteps, State state) {;
        
        public static Step of(final int numberOfSteps, final State state) {
            return new Step(numberOfSteps, state);
        }
    }
}
