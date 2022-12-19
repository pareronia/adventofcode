import static java.util.stream.Collectors.toList;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Very slow on part1, OOM on part2 after about 40 min...
 */
public class AoC2022_19 extends AoCBase {
    
    private final List<Blueprint> blueprints;
    private final Map<Material, Integer> maxSpend;
    
    private AoC2022_19(final List<String> input, final boolean debug) {
        super(debug);
        this.blueprints = input.stream()
            .map(line -> {
                final int[] nums = Utils.integerNumbers(line);
                final int id = nums[0];
                final Robot robot1
                    = new Robot(Material.ORE, Map.of(Material.ORE, nums[1]));
                final Robot robot2
                    = new Robot(Material.CLAY, Map.of(Material.ORE, nums[2]));
                final Robot robot3
                    = new Robot(Material.OBSIDIAN,
                        Map.of(Material.ORE, nums[3], Material.CLAY, nums[4]));
                final Robot robot4
                    = new Robot(Material.GEODE,
                        Map.of(Material.ORE, nums[5], Material.OBSIDIAN, nums[6]));
                return new Blueprint(id, Set.of(robot1, robot2, robot3, robot4));
            })
            .collect(toList());
//        log(this.blueprints);
        this.maxSpend = new HashMap<>();
        for (final Blueprint blueprint : this.blueprints) {
            for (final Material material : Material.values()) {
                if (material == Material.GEODE) {
                    continue;
                }
                final int max = blueprint.robots.stream()
                        .mapToInt(r -> r.getCosts().getOrDefault(material, 0))
                        .max().orElseThrow();
                this.maxSpend.merge(material, max, Math::max);
            }
        }
        log(this.maxSpend);
    }
    
    public static final AoC2022_19 create(final List<String> input) {
        return new AoC2022_19(input, false);
    }

    public static final AoC2022_19 createDebug(final List<String> input) {
        return new AoC2022_19(input, true);
    }
    
    public static final AoC2022_19 createTrace(final List<String> input) {
        final AoC2022_19 puzzle = new AoC2022_19(input, true);
        puzzle.setTrace(true);
        return puzzle;
    }
    
    @RequiredArgsConstructor
    @EqualsAndHashCode
    private static final class State {
        private final int timeLeft;
        private final Map<Material, Integer> factory;
        private final Map<Material, Integer> inventory;
    }
    
    private int dfs(final Blueprint blueprint, final Map<State, Integer> seen, final State state) {
        trace(() -> "timeLeft: " + state.timeLeft);
        trace(() -> "inventory: " + state.inventory);
        trace(() -> "factory: " + state.factory);
        final int geodes = state.inventory.getOrDefault(Material.GEODE, 0);
        if (geodes >= 7) {
//            log(() -> String.format("timeLeft / geodes: %d / %d", state.timeLeft, geodes));
        }
        if (state.timeLeft <= 0) {
//            log("Hey");
            return geodes;
        }
        if (seen.containsKey(state)) {
//            log("seen");
            return seen.get(state);
        }

        int max = geodes + state.factory.getOrDefault(Material.GEODE, 0) * state.timeLeft;
        
        for (final Material material : Material.values()) {
            trace(() -> "Material:" + material);
            if (material != Material.GEODE && state.factory.getOrDefault(material, 0) >= this.maxSpend.get(material)) {
                trace("Enough bots already");
                continue;
            }
            
            boolean botAvailable = true;
            int timeToBuild = 0;
            final Map<Material, Integer> spent = new HashMap<>();
            for (final Material botm : blueprint.getCosts(material).keySet()) {
                if (!state.factory.containsKey(botm)) {
                    botAvailable = false;
                    break;
                }
                final int cost = blueprint.getCosts(material).get(botm);
                spent.put(botm, cost);
                final int required = Math.max(0, cost - state.inventory.getOrDefault(botm, 0));
                final int bots = state.factory.get(botm);
                final int bottime = (required + bots - 1) / bots;
                timeToBuild = Math.max(timeToBuild, bottime);
                assert timeToBuild >= 0;
            }
            if (botAvailable) {
                final int newTimeLeft = state.timeLeft - timeToBuild - 1;
                if (newTimeLeft <= 0) {
                    continue;
                }
                final HashMap<Material, Integer> newInventory = new HashMap<>(state.inventory);
                final HashMap<Material, Integer> newFactory = new HashMap<>(state.factory);
                for (final Material fm : state.factory.keySet()) {
                    newInventory.merge(fm, (timeToBuild + 1) * newFactory.get(fm), Integer::sum);
                }
                for (final Material sm : spent.keySet()) {
                    assert newInventory.containsKey(sm);
                    newInventory.merge(sm, -spent.get(sm), Integer::sum);
                }
                newFactory.merge(material, 1, Integer::sum);
                for (final Material mm : EnumSet.of(Material.ORE, Material.CLAY, Material.OBSIDIAN)) {
                    final int maxNeeded = this.maxSpend.get(mm) * newTimeLeft;
                    newInventory.computeIfPresent(mm, (k, v) -> Math.min(v, maxNeeded));
                }
                assert newTimeLeft < state.timeLeft;
                final State newState = new State(newTimeLeft, newFactory, newInventory);
                max = Math.max(max, dfs(blueprint, seen, newState));
            }
        }
        seen.put(state, max);
        return max;
    }
    
    @Override
    public Integer solvePart1() {
        int ans = 0;
        for (final Blueprint blueprint : this.blueprints) {
            log("Blueprint: " + blueprint.id);
            final State start = new State(24, new HashMap<>(Map.of(Material.ORE, 1)), new HashMap<>());
            final int result = dfs(blueprint, new HashMap<>(), start);
            log(result);
            ans += blueprint.id * result;
        }
        return ans;
    }

    @Override
    public Integer solvePart2() {
        int ans = 1;
        for (final Blueprint blueprint : this.blueprints.subList(0, Math.min(this.blueprints.size(), 3))) {
            log("Blueprint: " + blueprint.id);
            final State start = new State(32, new HashMap<>(Map.of(Material.ORE, 1)), new HashMap<>());
            final int result = dfs(blueprint, new HashMap<>(), start);
            log(result);
            ans *= result;
        }
        return ans;
    }

    public static void main(final String[] args) throws Exception {
//        assert AoC2022_19.createDebug(TEST).solvePart1() == 33;
//        assert AoC2022_19.createDebug(TEST).solvePart2() == 62;

        final Puzzle puzzle = Aocd.puzzle(2022, 19);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_19.createDebug(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_19.createDebug(inputData)::solvePart2)
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
    
    private enum Material { ORE, CLAY, OBSIDIAN, GEODE }
    
    @RequiredArgsConstructor
    @ToString
    private static final class Robot {
        private final Material type;
        @Getter
        private final Map<Material, Integer> costs;
    }
    
    @RequiredArgsConstructor
    @ToString
    private static final class Blueprint {
        private final int id;
        private final Set<Robot> robots;
        
        public Map<Material, Integer> getCosts(final Material material) {
            return this.robots.stream()
                .filter(robot -> robot.type == material)
                .map(Robot::getCosts)
                .findFirst().orElseThrow();
        }
    }
}
