import static com.github.pareronia.aoc.AssertUtils.assertFalse;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2021_23 extends AoCBase {
    
    private static final char WALL = '#';
    
    private static final Map<Amphipod, Integer> ENERGY = Map.of(
        Amphipod.A, 1,
        Amphipod.B, 10,
        Amphipod.C, 100,
        Amphipod.D, 1000
    );
    
    private final List<String> inputs;
    
    private AoC2021_23(final List<String> input, final boolean debug) {
        super(debug);
        this.inputs = input;
    }
    
    private final Diagram parse(final List<String> input) {
        final Amphipod[] hallway = new Amphipod[11];
        final List<Amphipod> roomA = new ArrayList<>();
        final List<Amphipod> roomB = new ArrayList<>();
        final List<Amphipod> roomC = new ArrayList<>();
        final List<Amphipod> roomD = new ArrayList<>();
        Arrays.fill(hallway, Amphipod.EMPTY);
        for (int i = 2; i < input.size() - 1; i++) {
            final List<Character> chars = Utils.asCharacterStream(input.get(i).strip())
                .filter(c -> c != WALL)
                .collect(toList());
            assert chars.size() == 4;
            roomA.add(0, Enum.valueOf(Amphipod.class, chars.get(0).toString()));
            roomB.add(0, Enum.valueOf(Amphipod.class, chars.get(1).toString()));
            roomC.add(0, Enum.valueOf(Amphipod.class, chars.get(2).toString()));
            roomD.add(0, Enum.valueOf(Amphipod.class, chars.get(3).toString()));
        }
        return new Diagram(
            hallway,
            roomA.toArray(new Amphipod[roomA.size()]),
            roomB.toArray(new Amphipod[roomB.size()]),
            roomC.toArray(new Amphipod[roomC.size()]),
            roomD.toArray(new Amphipod[roomD.size()]));
    }
    
    public static final AoC2021_23 create(final List<String> input) {
        return new AoC2021_23(input, false);
    }

    public static final AoC2021_23 createDebug(final List<String> input) {
        return new AoC2021_23(input, true);
    }
    
    @SuppressWarnings("unused")
    private int solveBis(final Diagram start) {
        final PriorityQueue<State> q = new PriorityQueue<>();
        q.add(new State(start, 0));
        final Set<Diagram> seen = new HashSet<>();
        while (!q.isEmpty()) {
            final State state = q.poll();
            if (state.diagram.complete()) {
                log(state.cost);
                return state.cost;
            }
            if (seen.contains(state.diagram)) {
                continue;
            }
            seen.add(state.diagram);
            for (final Move move : state.diagram.moves()) {
                final Diagram newDiagram = state.diagram.doMove(move);
                final int newCost = state.cost + move.energy;
                q.add(new State(newDiagram, newCost));
            }
        }
        throw new IllegalStateException("Unsolvable");
    }
    
    private int solve(final Diagram start) {
        final PriorityQueue<State> q = new PriorityQueue<>();
        q.add(new State(start, 0));
        final Map<Diagram, Integer> best = new HashMap<>();
        best.put(start, 0);
        int max = 0;
        while (!q.isEmpty()) {
            max = Math.max(q.size(), max);
            final State state = q.poll();
            if (state.diagram.complete()) {
                log("max: " + max);
                log(state.cost);
                return state.cost;
            }
            if (state.cost > best.getOrDefault(state.diagram, Integer.MAX_VALUE)) {
                continue;
            }
            for (final Move move : state.diagram.moves()) {
                final Diagram newDiagram = state.diagram.doMove(move);
                final int bestCost = best.getOrDefault(newDiagram, Integer.MAX_VALUE);
                final int newCost = state.cost + move.energy;
                if (newCost < bestCost) {
                    q.add(new State(newDiagram, newCost));
                    best.put(newDiagram, newCost);
                }
            }
        }
        log(max);
        throw new IllegalStateException("Unsolvable");
    }
    
    @Override
    public Integer solvePart1() {
        final AoC2021_23.Diagram initialDiagram = parse(this.inputs);
        return solve(initialDiagram);
    }
    
    @Override
    public Integer solvePart2() {
        final List<String> inputs2 = List.of(
            this.inputs.get(0),
            this.inputs.get(1),
            this.inputs.get(2),
            "  #D#C#B#A#",
            "  #D#B#A#C#",
            this.inputs.get(3),
            this.inputs.get(4)
        );
        final AoC2021_23.Diagram initialDiagram = parse(inputs2);
        return solve(initialDiagram);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_23.create(TEST).solvePart1() == 12_521;
        assert AoC2021_23.create(TEST).solvePart2() == 44_169;

        final Puzzle puzzle = Aocd.puzzle(2021, 23);
        puzzle.check(
            () -> lap("Part 1", AoC2021_23.create(puzzle.getInputData())::solvePart1),
            () -> lap("Part 2", AoC2021_23.create(puzzle.getInputData())::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines(
        "#############\r\n" +
        "#...........#\r\n" +
        "###B#C#B#D###\r\n" +
        "  #A#D#C#A#\r\n" +
        "  #########"
    );

    enum Amphipod { EMPTY, A, B, C, D }
    
    @RequiredArgsConstructor
    private static final class State implements Comparable<State> {
        private final Diagram diagram;
        private final int cost;
        
        @Override
        public int compareTo(final State other) {
            return Integer.compare(this.cost, other.cost);
        }
    }
    
    @RequiredArgsConstructor
    @Getter
    @Builder
    @EqualsAndHashCode
    @ToString
    static final class Room {
        private final Amphipod destinationFor;
        @ToString.Exclude private final int capacity;
        private final Amphipod[] amphipods;
        
        public int vacancyFor(final Amphipod amphipod) {
            assert amphipod != Amphipod.EMPTY;
//            System.out.println("Room " + destinationFor + ": " + amphipods[0] + ","+ amphipods[1]);
            if (amphipod != this.destinationFor) {
                return -1;
            }
            final int emptyCount = countEmpty();
            if (emptyCount == 0) {
                return -1;
            }
            if (emptyCount == this.capacity) {
//                System.out.println(" vacancy for " + amphipod + ": " + 0);
                return 0;
            }
            for (int i = 0; i < this.capacity; i++) {
                if (this.amphipods[i] == Amphipod.EMPTY) {
                    return i;
                } else if (this.amphipods[i] != amphipod) {
                    return -1;
                }
            }
            throw new IllegalStateException();
        }
        
        int availableToMove() {
            if (this.completeness() + this.countEmpty() == this.capacity) {
                return -1;
            }
            for (int i = this.capacity - 1; i >= 0; i--) {
//                System.out.println("Room " + destinationFor + ": " + amphipods[0] + ","+ amphipods[1]);
                if (this.amphipods[i] != Amphipod.EMPTY) {
//                    System.out.println(" available: " + i);
                    return i;
                }
            }
            return -1;
        }
        
        private int completeness() {
            return (int) Arrays.stream(this.amphipods)
                    .filter(a -> a == this.destinationFor)
                    .count();
        }
        
        private boolean complete() {
            return this.completeness() == this.capacity;
        }
        
        private int countEmpty() {
            return (int) Arrays.stream(this.amphipods)
                    .filter(a -> a == Amphipod.EMPTY)
                    .count();
        }
    }
    
    @EqualsAndHashCode
    @Getter
    @ToString
    static final class Diagram {
        private final Room hallway;
        private final Room roomA;
        private final Room roomB;
        private final Room roomC;
        private final Room roomD;
        
        public Diagram(
                final Amphipod[] hallway,
                final Amphipod[] amphipodsA,
                final Amphipod[] amphipodsB,
                final Amphipod[] amphipodsC,
                final Amphipod[] amphipodsD) {
            this.hallway = Room.builder()
                    .destinationFor(Amphipod.EMPTY)
                    .capacity(hallway.length)
                    .amphipods(hallway)
                    .build();
            this.roomA = Room.builder()
                    .destinationFor(Amphipod.A)
                    .capacity(amphipodsA.length)
                    .amphipods(amphipodsA)
                    .build();
            this.roomB = Room.builder()
                    .destinationFor(Amphipod.B)
                    .capacity(amphipodsB.length)
                    .amphipods(amphipodsB)
                    .build();
            this.roomC = Room.builder()
                    .destinationFor(Amphipod.C)
                    .capacity(amphipodsC.length)
                    .amphipods(amphipodsC)
                    .build();
            this.roomD = Room.builder()
                    .destinationFor(Amphipod.D)
                    .capacity(amphipodsD.length)
                    .amphipods(amphipodsD)
                    .build();
        }
        
        public boolean complete() {
            return Arrays.stream(this.hallway.amphipods).allMatch(a-> a == Amphipod.EMPTY)
                    && this.roomA.complete()
                    && this.roomB.complete()
                    && this.roomC.complete()
                    && this.roomD.complete();
        }
        
        public int completeness() {
            return this.roomA.completeness() + this.roomB.completeness()
                + this.roomC.completeness() + this.roomD.completeness();
        }
        
        public int countNonEmpty() {
            return (int) Arrays.stream(this.hallway.amphipods).filter(e -> e != Amphipod.EMPTY).count()
             + (int) Arrays.stream(this.roomA.amphipods).filter(e -> e != Amphipod.EMPTY).count()
             + (int) Arrays.stream(this.roomB.amphipods).filter(e -> e != Amphipod.EMPTY).count()
             + (int) Arrays.stream(this.roomC.amphipods).filter(e -> e != Amphipod.EMPTY).count()
             + (int) Arrays.stream(this.roomD.amphipods).filter(e -> e != Amphipod.EMPTY).count();
        }
        
        public Diagram copy() {
            return new Diagram(
                    Arrays.copyOf(this.hallway.amphipods, this.hallway.amphipods.length),
                    Arrays.copyOf(this.roomA.amphipods, this.roomA.amphipods.length),
                    Arrays.copyOf(this.roomB.amphipods, this.roomB.amphipods.length),
                    Arrays.copyOf(this.roomC.amphipods, this.roomC.amphipods.length),
                    Arrays.copyOf(this.roomD.amphipods, this.roomD.amphipods.length));
        }
        
        private void assertValid() {
            assert this.hallway.amphipods[2] == Amphipod.EMPTY
                    && this.hallway.amphipods[4] == Amphipod.EMPTY
                    && this.hallway.amphipods[6] == Amphipod.EMPTY
                    && this.hallway.amphipods[8] == Amphipod.EMPTY;
            assert this.countNonEmpty() == this.roomA.capacity
                    + this.roomB.capacity + this.roomC.capacity
                    + this.roomD.capacity;
            for (final Room room : Set.of(this.roomA, this.roomB, this.roomC, this.roomD)) {
                for (int i = 0; i < room.amphipods.length; i++) {
                    final Amphipod amphipod = room.amphipods[i];
                    if (amphipod == Amphipod.EMPTY && i < room.capacity - 1) {
                        assert room.amphipods[i + 1] == Amphipod.EMPTY;
                    }
                }
            }
        }
        
        private Room roomFor(final Amphipod amphipod) {
            assertFalse(amphipod == Amphipod.EMPTY, () -> "Can't assign room for EMPTY");
            if (amphipod == Amphipod.A) {
                return this.roomA;
            } else if (amphipod == Amphipod.B) {
                return this.roomB;
            } else if (amphipod == Amphipod.C) {
                return this.roomC;
            } else {
                return this.roomD;
            }
        }
        
        public Diagram doMove(final Move move) {
            final Diagram copy = this.copy();
            if (move instanceof MoveFromHallway) {
                assert copy.hallway.amphipods[move.posFrom] == move.room;
                final Amphipod temp = copy.roomFor(move.room).amphipods[move.posTo];
                assert temp == Amphipod.EMPTY;
                copy.roomFor(move.room).amphipods[move.posTo] = copy.hallway.amphipods[move.posFrom];
                copy.hallway.amphipods[move.posFrom] = temp;
            } else {
                final Amphipod temp = copy.hallway.amphipods[move.posTo];
                assert temp == Amphipod.EMPTY;
                copy.hallway.amphipods[move.posTo] = copy.roomFor(move.room).amphipods[move.posFrom];
                copy.roomFor(move.room).amphipods[move.posFrom] = temp;
            }
            this.assertValid();
            copy.assertValid();
            return copy;
        }
        
        public List<Move> moves() {
            return Stream.concat(
                    movesFromHallway().stream(),
                    movesToHallway().stream())
                .collect(toList());
        }

        private List<MoveToHallway> movesToHallway() {
            final List<MoveToHallway> moves = new ArrayList<>();
            final int aMove = this.roomA.availableToMove();
            if (aMove > -1) {
                for (final int f: this.freeLeftFromA()) {
                    final int steps = this.roomA.capacity - aMove + 2 - f;
                    moves.add(new MoveToHallway(Amphipod.A, aMove, f, steps * ENERGY.get(this.roomA.amphipods[aMove])));
                }
                for (final int f: this.freeRightFromA()) {
                    final int steps = this.roomA.capacity - aMove + f - 2;
                    moves.add(new MoveToHallway(Amphipod.A, aMove, f, steps * ENERGY.get(this.roomA.amphipods[aMove])));
                }
            }
            final int bMove = this.roomB.availableToMove();
            if (bMove > -1) {
                for (final int f: this.freeLeftFromB()) {
                    final int steps = this.roomB.capacity - bMove + 4 - f;
                    moves.add(new MoveToHallway(Amphipod.B, bMove, f, steps * ENERGY.get(this.roomB.amphipods[bMove])));
                }
                for (final int f: this.freeRightFromB()) {
                    final int steps = this.roomB.capacity - bMove + f - 4;
                    moves.add(new MoveToHallway(Amphipod.B, bMove, f, steps * ENERGY.get(this.roomB.amphipods[bMove])));
                }
            }
            final int cMove = this.roomC.availableToMove();
            if (cMove > -1) {
                for (final int f: this.freeLeftFromC()) {
                    final int steps = this.roomC.capacity - cMove + 6 - f;
                    moves.add(new MoveToHallway(Amphipod.C, cMove, f, steps * ENERGY.get(this.roomC.amphipods[cMove])));
                }
                for (final int f: this.freeRightFromC()) {
                    final int steps = this.roomC.capacity - cMove + f - 6;
                    moves.add(new MoveToHallway(Amphipod.C, cMove, f, steps * ENERGY.get(this.roomC.amphipods[cMove])));
                }
            }
            final int dMove = this.roomD.availableToMove();
            if (dMove > -1) {
                for (final int f: this.freeLeftFromD()) {
                    final int steps = this.roomD.capacity - dMove + 8 - f;
                    moves.add(new MoveToHallway(Amphipod.D, dMove, f, steps * ENERGY.get(this.roomD.amphipods[dMove])));
                }
                for (final int f: this.freeRightFromD()) {
                    final int steps = this.roomD.capacity - dMove + f - 8;
                    moves.add(new MoveToHallway(Amphipod.D, dMove, f, steps * ENERGY.get(this.roomD.amphipods[dMove])));
                }
            }
            return moves;
        }

        private List<MoveFromHallway> movesFromHallway() {
            final List<MoveFromHallway> moves = new ArrayList<>();
            for (int i = 0; i < this.hallway.capacity; i++) {
                if (this.hallway.amphipods[i] == Amphipod.A && this.freeToA(i)) {
                    final int posTo = this.roomA.vacancyFor(Amphipod.A);
                    if (posTo > -1) {
                        assert this.roomA.amphipods[posTo] == Amphipod.EMPTY;
                        final int steps = Math.abs(i - 2) + this.roomA.capacity - posTo;
                        moves.add(new MoveFromHallway(Amphipod.A, i, posTo, steps * ENERGY.get(this.hallway.amphipods[i])));
                    }
                }
                if (this.hallway.amphipods[i] == Amphipod.B && this.freeToB(i)) {
                    final int posTo = this.roomB.vacancyFor(Amphipod.B);
                    if (posTo > -1) {
                        assert this.roomB.amphipods[posTo] == Amphipod.EMPTY;
                        final int steps = Math.abs(i - 4) + this.roomB.capacity - posTo;
                        moves.add(new MoveFromHallway(Amphipod.B, i, posTo, steps * ENERGY.get(this.hallway.amphipods[i])));
                    }
                }
                if (this.hallway.amphipods[i] == Amphipod.C && this.freeToC(i)) {
                    final int posTo = this.roomC.vacancyFor(Amphipod.C);
                    if (posTo > -1) {
                        assert this.roomC.amphipods[posTo] == Amphipod.EMPTY;
                        final int steps = Math.abs(i - 6) + this.roomC.capacity - posTo;
                        moves.add(new MoveFromHallway(Amphipod.C, i, posTo, steps * ENERGY.get(this.hallway.amphipods[i])));
                    }
                }
                if (this.hallway.amphipods[i] == Amphipod.D && this.freeToD(i)) {
                    final int posTo = this.roomD.vacancyFor(Amphipod.D);
                    if (posTo > -1) {
                        assert this.roomD.amphipods[posTo] == Amphipod.EMPTY;
                        final int steps = Math.abs(i - 8) + this.roomD.capacity - posTo;
                        moves.add(new MoveFromHallway(Amphipod.D, i, posTo, steps * ENERGY.get(this.hallway.amphipods[i])));
                    }
                }
            }
            return moves;
        }
        
        private boolean hallwayEmpty(final int i) {
            return this.hallway.amphipods[i] == Amphipod.EMPTY;
        }
        
        private boolean freeToA(final int pos) {
            return freeTo(pos, 2);
        }
        
        private boolean freeToB(final int pos) {
            return freeTo(pos, 4);
        }
        
        private boolean freeToC(final int pos) {
            return freeTo(pos, 6);
        }

        private boolean freeToD(final int pos) {
            return freeTo(pos, 8);
        }
        
        private boolean freeTo(final int pos, final int target) {
            assertFalse(pos == target, () -> "");
            if (pos < target) {
                for (int i = pos + 1; i < target; i++) {
                    if (!this.hallwayEmpty(i)) {
                        return false;
                    }
                }
            } else {
                for (int i = this.hallway.capacity - 1; i > pos; i--) {
                    if (!this.hallwayEmpty(i)) {
                        return false;
                    }
                }
            }
            return true;
        }
        
        List<Integer> freeLeftFromA() {
            return IntStream.iterate(1, i -> i >= 0, i -> i - 1)
                .filter(i -> !Set.of(2, 4, 6, 8).contains(i))
                .takeWhile(this::hallwayEmpty)
                .mapToObj(Integer::valueOf)
                .collect(toList());
        }

        List<Integer> freeRightFromA() {
            return IntStream.iterate(3, i -> i < this.hallway.capacity, i -> i + 1)
                .filter(i -> !Set.of(2, 4, 6, 8).contains(i))
                .takeWhile(this::hallwayEmpty)
                .mapToObj(Integer::valueOf)
                .collect(toList());
        }
        
        List<Integer> freeLeftFromB() {
            return IntStream.iterate(3, i -> i >= 0, i -> i - 1)
                .filter(i -> !Set.of(2, 4, 6, 8).contains(i))
                .takeWhile(this::hallwayEmpty)
                .mapToObj(Integer::valueOf)
                .collect(toList());
        }
        
        List<Integer> freeRightFromB() {
            return IntStream.iterate(5, i -> i < this.hallway.capacity, i -> i + 1)
                .filter(i -> !Set.of(2, 4, 6, 8).contains(i))
                .takeWhile(this::hallwayEmpty)
                .mapToObj(Integer::valueOf)
                .collect(toList());
        }
        
        List<Integer> freeLeftFromC() {
            return IntStream.iterate(5, i -> i >= 0, i -> i - 1)
                .filter(i -> !Set.of(2, 4, 6, 8).contains(i))
                .takeWhile(this::hallwayEmpty)
                .mapToObj(Integer::valueOf)
                .collect(toList());
        }
        
        List<Integer> freeRightFromC() {
            return IntStream.iterate(7, i -> i < this.hallway.capacity, i -> i + 1)
                .filter(i -> !Set.of(2, 4, 6, 8).contains(i))
                .takeWhile(this::hallwayEmpty)
                .mapToObj(Integer::valueOf)
                .collect(toList());
        }
        
        List<Integer> freeLeftFromD() {
            return IntStream.iterate(7, i -> i >= 0, i -> i - 1)
                .filter(i -> !Set.of(2, 4, 6, 8).contains(i))
                .takeWhile(this::hallwayEmpty)
                .mapToObj(Integer::valueOf)
                .collect(toList());
        }
        
        List<Integer> freeRightFromD() {
            return IntStream.iterate(9, i -> i < this.hallway.capacity, i -> i + 1)
                .filter(i -> !Set.of(2, 4, 6, 8).contains(i))
                .takeWhile(this::hallwayEmpty)
                .mapToObj(Integer::valueOf)
                .collect(toList());
        }
    }
    
    @RequiredArgsConstructor
    @ToString
    static abstract class Move {
        private final Amphipod room;
        private final int posFrom;
        private final int posTo;
        private final int energy;
    }
    
    static final class MoveFromHallway extends Move {

        public MoveFromHallway(final Amphipod roomTo, final int posFrom, final int posTo, final int energy) {
            super(roomTo, posFrom, posTo, energy);
        }
    }

    static final class MoveToHallway extends Move {
        
        public MoveToHallway(final Amphipod roomFrom, final int posFrom, final int posTo, final int energy) {
            super(roomFrom, posFrom, posTo, energy);
        }
    }
}