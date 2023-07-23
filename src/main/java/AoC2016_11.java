import static com.github.pareronia.aoc.IterTools.combinations;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;

import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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
            floor = floor.replaceAll(",? and", ",");
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
        return new State(1, chips, generators);
    }

    public static AoC2016_11 create(final List<String> input) {
        return new AoC2016_11(input, false);
    }

    public static AoC2016_11 createDebug(final List<String> input) {
        return new AoC2016_11(input, true);
    }
    
    private Integer solve(final State initialState) {
        Integer numberOfSteps = 0;
        final PriorityQueue<Step> steps
                = new PriorityQueue<>(comparing(Step::score));
        steps.add(Step.of(0, initialState));
        final Set<Integer> seen = new HashSet<>();
        seen.add(initialState.equivalentState());
        int cnt = 0;
        while (!steps.isEmpty()) {
            final Step step = steps.poll();
            cnt++;
            final State state = step.state;
            if (state.isDestination()) {
                numberOfSteps = step.numberOfSteps;
                break;
            }
            state.moves().stream()
                    .filter(m -> !seen.contains(m.equivalentState()))
                    .forEach(m -> {
                        seen.add(m.equivalentState());
                        steps.add(Step.of(step.numberOfSteps + 1, m));
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

        final Puzzle puzzle = Aocd.puzzle(2016, 11);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2016_11.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2016_11.create(inputData)::solvePart2)
        );
    }
    
    private static final List<String> TEST = splitLines(
            "The first floor contains a hydrogen-compatible microchip, and a lithium-compatible microchip.\n" +
            "The second floor contains a hydrogen generator.\n" +
            "The third floor contains a lithium generator.\n" +
            "The fourth floor contains nothing relevant."
    );
    
    @Value
    static final class State {
        private static final Set<Integer> FLOORS = Set.of(1, 2, 3, 4);
        private static final int TOP = FLOORS.stream().mapToInt(Integer::intValue).max().getAsInt();
        private static final int BOTTOM = FLOORS.stream().mapToInt(Integer::intValue).min().getAsInt();
        private static final int MAX_ITEMS_PER_MOVE = 2;
        
        @Getter(AccessLevel.PRIVATE)
        @With(AccessLevel.PRIVATE)
        private final Integer elevator;
        @Getter(AccessLevel.PRIVATE)
        @With(AccessLevel.PRIVATE)
        private final Map<String, Integer> chips;
        @Getter(AccessLevel.PRIVATE)
        @With(AccessLevel.PRIVATE)
        private final Map<String, Integer> gennys;
        @With(AccessLevel.PRIVATE)
        @EqualsAndHashCode.Exclude
        private final Integer diff;
        @Getter(AccessLevel.PRIVATE)
        @ToString.Exclude
        @EqualsAndHashCode.Exclude
        private final Map<Integer, List<String>> chipsPerFloor;
        @Getter(AccessLevel.PRIVATE)
        @ToString.Exclude
        @EqualsAndHashCode.Exclude
        private final Map<Integer, List<String>> gennysPerFloor;

        private State(
                final Integer elevator,
                final Map<String, Integer> chips,
                final Map<String, Integer> gennys,
                final Integer diff,
                final Map<Integer, List<String>> chipsPerFloor,
                final Map<Integer, List<String>> gennysPerFloor
                ) {
            this.elevator = elevator;
            this.chips = chips;
            this.gennys = gennys;
            this.diff = diff;
            this.chipsPerFloor = chips.keySet().stream().collect(groupingBy(chips::get));
            this.gennysPerFloor = gennys.keySet().stream().collect(groupingBy(gennys::get));
        }
        
        private State(
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
                combinations(chipsOnFloor.size(), MAX_ITEMS_PER_MOVE).forEach(
                    c -> {
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
                combinations(gennysOnFloor.size(), MAX_ITEMS_PER_MOVE).forEach(
                    c -> {
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
                        .withElevator(floor + 1)
                        .withDiff(2));
                if (!floorsBelowEmpty(floor)) {
                    states.add(withChipsTo(List.of(match), floor - 1)
                            .withGennysTo(List.of(match), floor - 1)
                            .withElevator(floor - 1)
                            .withDiff(-2));
                }
            }
            return states;
        }
        
        private State moveUpWithChips(final List<String> chips) {
            return withChipsTo(chips, this.elevator + 1)
                    .withElevator(this.elevator + 1)
                    .withDiff(chips.size());
        }
        
        private State moveUpWitGennys(final List<String> gennys) {
            return withGennysTo(gennys, this.elevator + 1)
                    .withElevator(this.elevator + 1)
                    .withDiff(gennys.size());
        }
        
        private State moveDownWithChips(final List<String> chips) {
            return withChipsTo(chips, this.elevator - 1)
                    .withElevator(this.elevator - 1)
                    .withDiff(-chips.size());
        }
        
        private State moveDownWithGennys(final List<String> gennys) {
            return withGennysTo(gennys, this.elevator - 1)
                    .withElevator(this.elevator - 1)
                    .withDiff(-gennys.size());
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
    }
    
    @RequiredArgsConstructor(staticName = "of")
    private static final class Step {
        private final int numberOfSteps;
        private final State state;
        
        public int score() {
            return -state.getDiff() * numberOfSteps;
        }
    }
}
