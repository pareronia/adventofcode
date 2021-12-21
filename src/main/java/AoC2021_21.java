import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.tuple.Tuples;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

// TODO: add iterative verson
public class AoC2021_21 extends AoCBase {
    
    private final int p1;
    private final int p2;
    
    private AoC2021_21(final List<String> input, final boolean debug) {
        super(debug);
        assert input.size() == 2;
        this.p1 = Character.digit(input.get(0).charAt(input.get(0).length() - 1), 10);
        this.p2 = Character.digit(input.get(1).charAt(input.get(1).length() - 1), 10);
    }
    
    public static final AoC2021_21 create(final List<String> input) {
        return new AoC2021_21(input, false);
    }

    public static final AoC2021_21 createDebug(final List<String> input) {
        return new AoC2021_21(input, true);
    }
    
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    private static final class Game {
        private int pos1;
        private int pos2;
        private int score1;
        private int score2;
        private final int player;
        
        public static Game copyForPlayer1(final Game game) {
            return new Game(game.pos1, game.pos2, game.score1, game.score2, 1);
        }

        public static Game copyForPlayer2(final Game game) {
            return new Game(game.pos1, game.pos2, game.score1, game.score2, 2);
        }
        
        public void turn1(final int[] rolls) {
            for (final int roll : rolls) {
                pos1 = (pos1 + roll) % 10;
            }
            score1 += pos1 + 1;
        }

        public void turn2(final int[] rolls) {
            for (final int roll : rolls) {
                pos2 = (pos2 + roll) % 10;
            }
            score2 += pos2 + 1;
        }
    }
    
    @Override
    public Integer solvePart1() {
        final Game game = new Game(this.p1 - 1, this.p2 - 1, 0, 0, -1);
        int die = 0;
        int cnt = 0;
        while (true) {
            game.turn1(new int[] { 1 + die++, 1 + die++, 1 + die++ });
            cnt += 3;
            if (game.score1 >= 1000) {
                log("p1 wins");
                return game.score2 * cnt;
            }
            game.turn2(new int[] { 1 + die++, 1 + die++, 1 + die++ });
            cnt += 3;
            if (game.score2 >= 1000) {
                log("p2 wins");
                return game.score1 * cnt;
            }
        }
    }
    
    private final Map<Game, Pair<Long, Long>> winsCache = new HashMap<>();
    
    private Pair<Long, Long> solve2(final Game game) {
        if (winsCache.containsKey(game)) {
            return winsCache.get(game);
        }
        if (game.score1 >= 21) {
            return Tuples.pair(1L,  0L);
        } else if (game.score2 >= 21) {
            return Tuples.pair(0L, 1L);
        }
        Pair<Long, Long> wins = Tuples.pair(0L,  0L);
        for (final int die1 : List.of(1, 2, 3)) {
            for (final int die2 : List.of(1, 2, 3)) {
                for (final int die3 : List.of(1, 2, 3)) {
                    final Game newGame;
                    if (game.player == 1) {
                        newGame = Game.copyForPlayer2(game);
                        newGame.turn1(new int[] { die1, die2, die3 });
                    } else {
                        newGame = Game.copyForPlayer1(game);
                        newGame.turn2(new int[] { die1, die2, die3 });
                    }
                    final Pair<Long, Long> nwins = solve2(newGame);
                    wins = Tuples.pair(wins.getOne() + nwins.getOne(),
                                       wins.getTwo() + nwins.getTwo());
                }
            }
        }
        winsCache.put(game, wins);
        return wins;
    }
    
    @Override
    public Long solvePart2() {
        final Pair<Long, Long> ans
            = solve2(new Game(this.p1 - 1, this.p2 - 1, 0, 0, 1));
        log(ans);
        return Math.max(ans.getOne(), ans.getTwo());
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_21.create(TEST).solvePart1() == 739_785;
        assert AoC2021_21.create(TEST).solvePart2() == 444_356_092_776_315L;

        final Puzzle puzzle = Aocd.puzzle(2021, 21);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2021_21.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2021_21.create(puzzle.getInputData()).solvePart2())
        );
    }

    private static final List<String> TEST = splitLines(
        "Player 1 starting position: 4\r\n" +
        "Player 2 starting position: 8"
    );
}