import static java.util.stream.Collectors.toList;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

public class AoC2022_19 extends AoCBase {
    
    private final List<Blueprint> blueprints;
    
    private AoC2022_19(final List<String> input, final boolean debug) {
        super(debug);
        this.blueprints = input.stream()
            .map(Utils::naturalNumbers)
            .map(nums -> new Blueprint(nums[0], nums[1], nums[2], nums[3],
                                        nums[4], nums[5], nums[6]))
            .collect(toList());
    }
    
    public static final AoC2022_19 create(final List<String> input) {
        return new AoC2022_19(input, false);
    }

    public static final AoC2022_19 createDebug(final List<String> input) {
        return new AoC2022_19(input, true);
    }
    
    private int solve(final Blueprint blueprint, final int maxTime) {
        final Deque<State> q = new ArrayDeque<>();
        q.add(new State(0, 0, 1, 0, 0, 0, 0, 0, 0));
        final Set<State> seen = new HashSet<>();
        int best = Integer.MIN_VALUE;
        while (!q.isEmpty()) {
            final State state = q.poll();
            if (state.time > maxTime || best >= state.maxPossibleGeode(maxTime)) {
                continue;
            }
            if (state.geodeStore > best) {
                best = state.geodeStore;
            }
            final List<State> next = new ArrayList<>();
            if (state.canBuildGeodeRobot(blueprint)) {
                next.add(state.buildGeodeRobot(blueprint));
            } else {
                if (state.needObsidianRobot(blueprint)
                        && state.canBuildObsidianRobot(blueprint)) {
                    next.add(state.buildObsidianRobot(blueprint));
                }
                if (state.needClayRobot(blueprint)
                        && state.canBuildClayRobot(blueprint)) {
                    next.add(state.buildClayRobot(blueprint));
                }
                if (state.needOreRobot(blueprint)
                        && state.canBuildOreRobot(blueprint)) {
                    next.add(state.buildOreRobot(blueprint));
                }
                next.add(state.noBuild(blueprint));
            }
            for (final State n : next) {
                if (!seen.contains(n)) {
                    seen.add(n);
                    q.add(n);
                }
            }
        }
        return best;
    }

    @Override
    public Integer solvePart1() {
        return this.blueprints.parallelStream()
                .mapToInt(bp -> bp.id * solve(bp, 24))
                .sum();
    }

    @Override
    public Integer solvePart2() {
        return this.blueprints.parallelStream()
                .limit(3)
                .mapToInt(bp -> solve(bp, 32))
                .reduce(1, (a, b) -> a * b);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_19.createDebug(TEST).solvePart1() == 33;
        assert AoC2022_19.createDebug(TEST).solvePart2() == 56 * 62;

        final Puzzle puzzle = Aocd.puzzle(2022, 19);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_19.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_19.create(inputData)::solvePart2)
        );
    }
    
    private static final List<String> TEST = splitLines(
        "Blueprint 1:"
        + " Each ore robot costs 4 ore."
        + " Each clay robot costs 2 ore."
        + " Each obsidian robot costs 3 ore and 14 clay."
        + " Each geode robot costs 2 ore and 7 obsidian.\r\n"
        + "Blueprint 2:"
        + " Each ore robot costs 2 ore."
        + " Each clay robot costs 3 ore."
        + " Each obsidian robot costs 3 ore and 8 clay."
        + " Each geode robot costs 3 ore and 12 obsidian."
    );

    private static final class Blueprint {
        private final int id;
        private final int oreCost;
        private final int clayCost;
        private final int obisidanOreCost;
        private final int obisidanClayCost;
        private final int geodeOreCost;
        private final int geodeObisidanCost;
        private final int maxOre;
        
        public Blueprint(
                final int id, final int oreCost, final int clayCost,
                final int obisidanOreCost, final int obisidanClayCost,
                final int geodeOreCost, final int geodeObisidanCost
        ) {
            this.id = id;
            this.oreCost = oreCost;
            this.clayCost = clayCost;
            this.obisidanOreCost = obisidanOreCost;
            this.obisidanClayCost = obisidanClayCost;
            this.geodeOreCost = geodeOreCost;
            this.geodeObisidanCost = geodeObisidanCost;
            this.maxOre = IntStream.of(
                    oreCost, clayCost, geodeOreCost, obisidanOreCost)
                .max().orElseThrow();
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode
    private static final class State {
        private static final double CUSHION = 1.5d;

        private final byte time;
        private final byte oreStore;
        private final byte oreRobot;
        private final byte clayStore;
        private final byte clayRobot;
        private final byte obisidianStore;
        private final byte obsidianRobot;
        private final byte geodeStore;
        private final byte geodeRobot;

        public State(final int time, final int oreStore, final int oreRobot,
                final int clayStore, final int clayRobot,
                final int obisidianStore, final int obsidianRobot,
                final int geodStore, final int geodRobot
        ) {
            this(   (byte) time,
                    (byte) oreStore,
                    (byte) oreRobot,
                    (byte) clayStore,
                    (byte) clayRobot,
                    (byte) obisidianStore,
                    (byte) obsidianRobot,
                    (byte) geodStore,
                    (byte) geodRobot);
        }
        
        public int maxPossibleGeode(final int maxTime) {
            return this.geodeStore + (maxTime - this.time) * (this.geodeRobot + 1);
        }

        public boolean canBuildGeodeRobot(final Blueprint blueprint) {
            return this.oreStore >= blueprint.geodeOreCost
                    && this.obisidianStore >= blueprint.geodeObisidanCost;
        }

        public  boolean canBuildObsidianRobot(final Blueprint blueprint) {
            return this.oreStore >= blueprint.obisidanOreCost
                    && this.clayStore >= blueprint.obisidanClayCost;
        }

        public boolean needObsidianRobot(final Blueprint blueprint) {
            return this.obsidianRobot < blueprint.geodeObisidanCost;
        }
        
        public boolean needClayRobot(final Blueprint blueprint) {
            return this.clayRobot < blueprint.obisidanClayCost;
        }
        
        public boolean needOreRobot(final Blueprint blueprint) {
            return this.oreRobot < blueprint.maxOre;
        }

        public boolean canBuildOreRobot(final Blueprint bp) {
            return this.oreStore >= bp.oreCost;
        }

        public boolean canBuildClayRobot(final Blueprint bp) {
            return this.oreStore >= bp.clayCost;
        }

        public State buildGeodeRobot(final Blueprint blueprint) {
            return new State(
                this.time + 1,
                this.oreStore + this.oreRobot - blueprint.geodeOreCost,
                this.oreRobot,
                this.clayStore + this.clayRobot,
                this.clayRobot,
                this.obisidianStore + this.obsidianRobot - blueprint.geodeObisidanCost,
                this.obsidianRobot,
                this.geodeStore + this.geodeRobot,
                this.geodeRobot + 1);
        }
        
        public State buildOreRobot(final Blueprint blueprint) {
            return buildNext(
                blueprint,
                blueprint.maxOre,
                this.oreStore + this.oreRobot - blueprint.oreCost,
                this.oreRobot + 1,
                this.clayStore + this.clayRobot,
                this.clayRobot,
                this.obisidianStore + this.obsidianRobot,
                this.obsidianRobot,
                this.geodeStore + this.geodeRobot,
                this.geodeRobot);
        }
        
        public State buildClayRobot(final Blueprint blueprint) {
            return buildNext(
                blueprint,
                blueprint.maxOre,
                this.oreStore + this.oreRobot - blueprint.clayCost,
                this.oreRobot,
                this.clayStore + this.clayRobot,
                this.clayRobot + 1,
                this.obisidianStore + this.obsidianRobot,
                this.obsidianRobot,
                this.geodeStore + this.geodeRobot,
                this.geodeRobot);
        }
        
        public State buildObsidianRobot(final Blueprint blueprint) {
            return buildNext(
                blueprint,
                blueprint.maxOre,
                this.oreStore + this.oreRobot - blueprint.obisidanOreCost,
                this.oreRobot,
                this.clayStore + this.clayRobot - blueprint.obisidanClayCost,
                this.clayRobot,
                this.obisidianStore + this.obsidianRobot,
                this.obsidianRobot + 1,
                this.geodeStore + this.geodeRobot,
                this.geodeRobot);
        }
        
        public State noBuild(final Blueprint blueprint) {
            return buildNext(
                blueprint,
                blueprint.maxOre,
                this.oreStore + this.oreRobot,
                this.oreRobot,
                this.clayStore + this.clayRobot,
                this.clayRobot,
                this.obisidianStore + this.obsidianRobot,
                this.obsidianRobot,
                this.geodeStore + this.geodeRobot,
                this.geodeRobot);
        }

        private State buildNext(
                final Blueprint blueprint, final int maxOre,
                final int oreStore, final int oreRobot, final int clayStore,
                final int clayRobot, final int obisidianStore,
                final int obsidianRobot, final int geodeStore, final int geodeRobot) {
            return new State(
                    this.time + 1,
                    Math.min(oreStore, cushion(maxOre)),
                    oreRobot,
                    Math.min(clayStore, cushion(blueprint.obisidanClayCost)),
                    clayRobot,
                    Math.min(obisidianStore, cushion(blueprint.geodeObisidanCost)),
                    obsidianRobot,
                    geodeStore,
                    geodeRobot);
        }
    
        private int cushion(final int val) {
            return (int) Math.ceil(val * CUSHION);
        }
    }
}
