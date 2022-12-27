import java.util.List;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

/**
 * Thx to https://github.com/joshuaruegge/adventofcode/blob/645d9b0f8cdc2891ae23cec44920c2522d09a731/advent/aoc2022/Day19.java
 */
public class AoC2022_19 extends AoCBase {
    
    private final int[][] bp;
    
    private AoC2022_19(final List<String> input, final boolean debug) {
        super(debug);
        this.bp = new int[input.size()][6];
        for (int i = 0; i < input.size(); i++) {
            final int[] nums = Utils.naturalNumbers(input.get(i));
            for (int j = 1; j <= 6; j++) {
                bp[i][j - 1] = nums[j];
            }
        }
    }
    
    public static final AoC2022_19 create(final List<String> input) {
        return new AoC2022_19(input, false);
    }

    public static final AoC2022_19 createDebug(final List<String> input) {
        return new AoC2022_19(input, true);
    }
    
    private int solve(final int maxTime, final int time, int gbest, final int[] cost, final int o, final int c, final int ob, final int g, final int or, final int cr, final int obr, final int gr) {
        if (time == maxTime) {
            gbest = Math.max(gbest, g);
            return g;
        }
        
        final int timeLeft = maxTime - time;
        final int maxg = g + gr * timeLeft;
        if (maxg < gbest) {
            return 0;
        }
        
        final int no = o + or;
        final int nc = c + cr;
        final int nob = ob + obr;
        final int ng = g + gr;
        
        // always build g robot
        if (o >= cost[4] && ob >= cost[5]) {
            return solve(maxTime, time + 1, gbest, cost, no - cost[4], nc, nob - cost[5], ng, or, cr, obr, gr + 1);
        }
        // build ob robot ?
        // ** without this check: runs very fast and correct for main input, fast and incorrect part 1 for alt input
        // ** with this check: runs slower but correct for main input and alternate input
        if (cr >= cost[3] && obr < cost[5] && // **
                o >= cost[2] && c >= cost[3]) {
            return solve(maxTime, time + 1, gbest, cost, no - cost[2], nc - cost[3], nob, ng, or, cr, obr + 1, gr);
        }
        
        int best = 0;
        if (obr < cost[5] && o >= cost[2] && c >= cost[3]) {
            final int ans = solve(maxTime, time + 1, gbest, cost, no - cost[2], nc - cost[3], nob, ng, or, cr, obr + 1, gr);
            best = Math.max(best, ans);
        }
        if (cr < cost[3] && o >= cost[1]) {
            final int ans = solve(maxTime, time + 1, gbest, cost, no - cost[1], nc, nob, ng, or, cr + 1, obr, gr);
            best = Math.max(best, ans);
        }
        if (or < 4 && o >= cost[0]) {
            final int ans = solve(maxTime, time + 1, gbest, cost, no - cost[0], nc, nob, ng, or + 1, cr, obr, gr);
            best = Math.max(best, ans);
        }
        if (o <= 4) {
            final int ans = solve(maxTime, time + 1, gbest, cost, no, nc, nob, ng, or, cr, obr, gr);
            best = Math.max(best, ans);
        }
        return best;
    }
    
    @Override
    public Integer solvePart1() {
        int ans = 0;
        for (int i = 0; i < this.bp.length; i++) {
            log("Blueprint " + (i + 1));
            final int res = solve(24, 0, 0, this.bp[i], 0, 0, 0, 0, 1, 0, 0, 0);
            log(res);
            ans += (i + 1) * res;
        }
        return ans;
    }

    @Override
    public Integer solvePart2() {
        int ans = 1;
        for (int i = 0; i < Math.min(this.bp.length, 3); i++) {
            log("Blueprint " + (i + 1));
            final int res = solve(32, 0, 0, this.bp[i], 0, 0, 0, 0, 1, 0, 0, 0);
            log(res);
            ans *= res;
        }
        return ans;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_19.createDebug(TEST).solvePart1() == 33;
//        assert AoC2022_19.createDebug(TEST).solvePart2() == 56 * 62;

        final Puzzle puzzle = Aocd.puzzle(2022, 19);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_19.createDebug(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_19.createDebug(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines("""
        Blueprint 1:\
         Each ore robot costs 4 ore.\
         Each clay robot costs 2 ore.\
         Each obsidian robot costs 3 ore and 14 clay.\
         Each geode robot costs 2 ore and 7 obsidian.\
        
        Blueprint 2:\
         Each ore robot costs 2 ore.\
         Each clay robot costs 3 ore.\
         Each obsidian robot costs 3 ore and 8 clay.\
         Each geode robot costs 3 ore and 12 obsidian.\
        """);
}
