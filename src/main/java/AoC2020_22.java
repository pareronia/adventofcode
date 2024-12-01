import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.MutableInt;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aoc.solution.Logger;
import com.github.pareronia.aoc.solution.LoggerEnabled;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2020_22
            extends SolutionBase<List<String>, Integer, Integer> {
    
    private AoC2020_22(final boolean debug) {
        super(debug);
    }
    
    public static final AoC2020_22 create() {
        return new AoC2020_22(false);
    }

    public static final AoC2020_22 createDebug() {
        return new AoC2020_22(true);
    }
    
    @Override
    protected List<String> parseInput(final List<String> inputs) {
        return inputs;
    }
    
    @Override
    public Integer solvePart1(final List<String> inputs) {
        final CrabCombat combat = CrabCombat.fromInput(inputs, this.logger);
        combat.playRegular();
        return combat.getScore();
    }
    
    @Override
    public Integer solvePart2(final List<String> inputs) {
        final CrabCombat combat = CrabCombat.fromInput(inputs, this.logger);
        combat.playRecursive();
        return combat.getScore();
    }
    
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "306"),
        @Sample(method = "part2", input = TEST, expected = "291"),
        @Sample(method = "part2", input = LOOP, expected = "105"),
    })
    public static void main(final String[] args) throws Exception {
        AoC2020_22.create().run();
    }
    
    private static final String TEST = """
        Player 1:
        9
        2
        6
        3
        1
        
        Player 2:
        5
        8
        4
        7
        10
        """;
    
    private static final String LOOP = """
        Player 1:
        43
        19
        
        Player 2r
        2
        29
        14
        """;
    
    record CrabCombat(
            List<Integer> player1,
            List<Integer> player2,
            Logger logger
    ) implements LoggerEnabled {
        
        public static CrabCombat fromInput(
                final List<String> inputs, final Logger logger
        ) {
            final List<List<String>> blocks = StringOps.toBlocks(inputs);
            final List<Integer> player1 = new ArrayList<>();
            final List<Integer> player2 = new ArrayList<>();
            blocks.get(0).stream().skip(1)
                    .map(Integer::valueOf)
                    .collect(toCollection(() -> player1));
            blocks.get(1).stream().skip(1)
                    .map(Integer::valueOf)
                    .collect(toCollection(() -> player2));
            return new CrabCombat(player1, player2, logger);
        }

        @Override
        public Logger getLogger() {
            return logger;
        }

        public void playRegular() {
            play(this, new MutableInt(), false);
        }
        
        public void playRecursive() {
            play(this, new MutableInt(), true);
        }
        
        private void play(
                final CrabCombat combat,
                final MutableInt _game,
                final boolean recursive
        ) {
            _game.increment();
            final int game = _game.intValue();
            log(() -> String.format("=== Game %d ===", game));
            final Set<Round> seen = new HashSet<>();
            final MutableInt rnd = new MutableInt(1);
            final List<Integer> pl1 = combat.player1;
            final List<Integer> pl2 = combat.player2;
            while (!pl1.isEmpty() && !pl2.isEmpty()) {
                log(() -> "");
                log(() -> String.format("-- Round %d (Game %d) --", rnd.intValue(), game));
                log(() -> String.format("Player 1's deck: %s", StringUtils.join(pl1, ", ")));
                log(() -> String.format("Player 2's deck: %s", StringUtils.join(pl2, ", ")));
                final Round round = new Round(new ArrayList<>(pl1), new ArrayList<>(pl2));
                if (recursive && seen.contains(round)) {
                    pl2.clear();
                    break;
                }
                seen.add(round);
                final Integer n1 = pl1.remove(0);
                log(() -> String.format("Player 1 plays: %d", n1));
                final Integer n2 = pl2.remove(0);
                log(() -> String.format("Player 2 plays: %d", n2));
                final Integer winner;
                if (recursive && pl1.size() >= n1 && pl2.size() >= n2) {
                    log(() -> "Playing a sub-game to determine the winner...");
                    log(() -> "");
                    final List<Integer> pl1_sub = new ArrayList<>(pl1.subList(0, n1));
                    final List<Integer> pl2_sub = new ArrayList<>(pl2.subList(0, n2));
                    play(new CrabCombat(pl1_sub, pl2_sub, logger), _game, true);
                    log(() -> "");
                    log(() -> String.format("...anyway, back to game %d.", game));
                    winner = pl2_sub.isEmpty() ? 1 : 2;
                } else {
                    winner = n1 > n2 ? 1 : 2;
                }
                if (winner == 1) {
                    pl1.addAll(asList(n1, n2));
                } else {
                    pl2.addAll(asList(n2, n1));
                }
                log(() -> String.format("Player %d wins round %d of game %d!", winner, rnd.intValue(), game));
                rnd.increment();
            }
            final Integer winner = pl2.isEmpty() ? 1 : 2;
            log(() -> String.format("The winner of game %d is player %d!", game, winner));
        }
        
        public int getScore() {
            log("");
            log("");
            log("== Post-game results ==");
            log(String.format("Player 1's deck: %s", StringUtils.join(player1, ", ")));
            log(String.format("Player 2's deck: %s", StringUtils.join(player2, ", ")));
            log("");
            log("");
            final List<Integer> winner = player2.isEmpty() ? player1 : player2;
            int total = 0;
            for (int i = 0; i < winner.size(); i++) {
                total += (winner.size() - i) * winner.get(i);
            }
            return total;
        }
        
        record Round(List<Integer> pl1, List<Integer> pl2) {}
    }
}
