import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

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
    
    private static final class Game {
        private int pos1;
        private int pos2;
        private int score1;
        private int score2;

        public Game(final int pos1, final int pos2, final int score1, final int score2) {
            this.pos1 = pos1;
            this.pos2 = pos2;
            this.score1 = score1;
            this.score2 = score2;
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

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("Game [pos1=").append(pos1).append(", pos2=")
                .append(pos2).append(", score1=").append(score1)
                .append(", score2=").append(score2).append("]");
            return builder.toString();
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
            final Game other = (Game) obj;
            return pos1 == other.pos1 && pos2 == other.pos2 && score1 == other.score1 && score2 == other.score2;
        }

        @Override
        public int hashCode() {
            return Objects.hash(pos1, pos2, score1, score2);
        }
    }
    
    @Override
    public Integer solvePart1() {
        final Game game = new Game(this.p1 - 1, this.p2 - 1, 0, 0);
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
    
    private final Map<Game, LongPair> winsCache = new HashMap<>();
    private static final Map<Integer, Integer> ROLLS = Map.of(
        3, 1,
        4, 3,
        5, 6,
        6, 7,
        7, 6,
        8, 3,
        9, 1
    );
    
    private LongPair solve2(final Game game) {
        if (winsCache.containsKey(game)) {
            return winsCache.get(game);
        }
        LongPair wins = new LongPair(0L, 0L);
        for (final Entry<Integer, Integer> roll : ROLLS.entrySet()) {
            final int nPos = (game.pos1 + roll.getKey()) % 10;
            final int nScore = game.score1 + nPos + 1;
            if (nScore >= 21 ) {
                wins = new LongPair(wins.one() + roll.getValue(), wins.two());
            } else {
                final Game newGame = new Game(game.pos2, nPos, game.score2, nScore);
                final LongPair nwins = solve2(newGame);
                wins = new LongPair(
                        wins.one() + roll.getValue() * nwins.two(),
                        wins.two() + roll.getValue() * nwins.one());
            }
        }
        winsCache.put(game, wins);
        return wins;
    }
    
    @Override
    public Long solvePart2() {
        final LongPair ans
            = solve2(new Game(this.p1 - 1, this.p2 - 1, 0, 0));
        log(ans);
        return Math.max(ans.one(), ans.two());
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_21.create(TEST).solvePart1() == 739_785;
        assert AoC2021_21.create(TEST).solvePart2() == 444_356_092_776_315L;

        final Puzzle puzzle = Aocd.puzzle(2021, 21);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2021_21.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2021_21.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines(
        "Player 1 starting position: 4\r\n" +
        "Player 2 starting position: 8"
    );
    
    record LongPair(Long one, Long two) {}
}