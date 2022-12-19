import static java.util.stream.Collectors.toList;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2022_19 extends AoCBase {
    
    private final List<Blueprint> blueprints;
    
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
        log(this.blueprints);
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
    
    private final class DFS {
        private final int maxTime;
        private final Factory factory;
        private int best = 0;
        
        public DFS(final Blueprint blueprint, final int maxTime) {
            this.factory = new Factory(blueprint);
            this.maxTime = maxTime;
        }

        public void dfs(final int time) {
            trace("time: " + time);
            for (int i = Material.values().length - 1; i >= 0; i--) {
                final Material material = Material.values()[i];
                trace(() -> "Material:" + material);
                final Map<Material, Integer> costs = this.factory.blueprint.getCosts(material);
                if (!costs.keySet().stream().allMatch(m -> this.factory.hasCapacity(m))) {
                    trace("No capacity");
                    continue;
                }
                int newTime = time + this.factory.timeToNextRobot(material, time);
                if (newTime + 1 < maxTime) {
                    int steps = 0;
                    for (final Entry<Material, Integer> e : costs.entrySet()) {
                        trace(() -> String.format("Spending %d %s", e.getValue(), e.getKey()));
                        this.factory.spend(e.getKey(), e.getValue(), time);
                        steps++;
                    }
                    newTime = newTime + 1;
                    trace(() -> String.format("Adding %s robot", material));
                    this.factory.add(material, newTime);
                    steps++;
                    dfs(newTime);
                    for (int j = 0; j < steps; j++) {
                        this.factory.removeLast();
                    }
                } else {
                    trace("No time left");
                }
            }
            this.best = Math.max(
                    this.best,
                    this.factory.getTotal(Material.GEODE, maxTime));
        }
    }
    
    @Override
    public Integer solvePart1() {
        int ans = 0;
        for (final Blueprint blueprint : this.blueprints) {
            log("Blueprint: " + blueprint.id);
            final DFS dfs = new DFS(this.blueprints.get(blueprint.id - 1), 24);
            dfs.dfs(0);
            log("best: " + dfs.best);
            ans += blueprint.id * dfs.best;
        }
        return ans;
    }

    @Override
    public Integer solvePart2() {
        return 0;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_19.createDebug(TEST).solvePart1() == 33;
        assert AoC2022_19.createDebug(TEST).solvePart2() == 0;

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
        
        public Robot getRobot(final Material material) {
            return this.robots.stream().filter(r -> r.type == material)
                    .findFirst().orElseThrow();
        }
    }
    
    private static final class Factory {
        private final Blueprint blueprint;
        private final Deque<Step> steps = new ArrayDeque<>();
        
        protected Factory(final Blueprint blueprint) {
            this.blueprint = blueprint;
            this.steps.addLast(new Step(Material.ORE, 1, 0));
        }

        public void add(final Material material, final int time) {
            this.steps.addLast(new Step(material, 1, time));
        }
        
        public void spend(final Material material, final int amount, final int time) {
            this.steps.addLast(new Step(material, -amount, time));
        }
        
        public void removeLast() {
            this.steps.removeLast();
        }

        public int getTotal(final Material material, final int time) {
            return this.steps.stream()
                .filter(step -> step.material == material)
                .mapToInt(step -> {
                    if (step.amount < 0) {
                        return step.amount;
                    } else {
                        return (time - step.time) * step.amount;
                    }
                })
                .sum();
        }
        
        public boolean hasCapacity(final Material material) {
            return this.steps.stream().anyMatch(step -> step.amount > 0 && step.material == material);
        }
        
        public int timeToNextRobot(final Material material, final int now) {
            final Map<Material, Integer> costs = this.blueprint.getCosts(material);
            int time = now;
            while (true) {
                final int theTime = time;
                if (costs.keySet().stream()
                        .allMatch(m -> getTotal(m, theTime) >= costs.get(m))) {
                    break;
                }
                time++;
            }
            return time - now;
        }

        @RequiredArgsConstructor
        private static final class Step {
            private final Material material;
            private final int amount;
            private final int time;
        }
    }
}
