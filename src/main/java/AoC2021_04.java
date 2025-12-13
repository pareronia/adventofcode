import static com.github.pareronia.aoc.AssertUtils.unreachable;
import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static com.github.pareronia.aoc.StringOps.toBlocks;

import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2021_04 extends SolutionBase<AoC2021_04.BingoGame, Integer, Integer> {

    private AoC2021_04(final boolean debug) {
        super(debug);
    }

    public static AoC2021_04 create() {
        return new AoC2021_04(false);
    }

    public static AoC2021_04 createDebug() {
        return new AoC2021_04(true);
    }

    @Override
    protected BingoGame parseInput(final List<String> inputs) {
        return BingoGame.fromInput(inputs);
    }

    private int solve(final BingoGame game, final int stopCount) {
        final BingoGame.Bingo bingo = game.play(stopCount);
        return bingo.draw() * bingo.board().value();
    }

    @Override
    public Integer solvePart1(final BingoGame game) {
        return solve(game, 1);
    }

    @Override
    public Integer solvePart2(final BingoGame game) {
        return solve(game, game.boards().size());
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "4512"),
        @Sample(method = "part2", input = TEST, expected = "1924"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST =
            """
            7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

            22 13 17 11  0
            8  2 23  4 24
            21  9 14 16  7
            6 10  3 18  5
            1 12 20 15 19

            3 15  0  2 22
            9 18 13 17  5
            19  8  7 25 23
            20 11 10 24  4
            14 21 16 12  6

            14 21 17 24  4
            10 16 15  9 19
            18  8 23 26 20
            22 11 13  6  5
            2  0 12  3  7
            """;

    private static class Board {
        private static final int MARKED = -1;

        private final int[][] numbers;
        private boolean completed;

        public boolean isCompleted() {
            return completed;
        }

        public Board(final List<String> numbers) {
            this.numbers =
                    range(numbers.size())
                            .intStream()
                            .mapToObj(
                                    i ->
                                            Arrays.stream(numbers.get(i).split("\\s+"))
                                                    .filter(StringUtils::isNotBlank)
                                                    .mapToInt(Integer::parseInt)
                                                    .toArray())
                            .toArray(int[][]::new);
        }

        @SuppressWarnings("PMD.AssignmentInOperand")
        public void mark(final int number) {
            range(getHeight())
                    .intStream()
                    .forEach(
                            r ->
                                    range(getWidth())
                                            .intStream()
                                            .filter(c -> this.numbers[r][c] == number)
                                            .forEach(c -> this.numbers[r][c] = MARKED));
        }

        public boolean win() {
            return Stream.concat(
                            range(getHeight()).intStream().mapToObj(row -> numbers[row]),
                            range(getWidth()).intStream().mapToObj(this::getColumn))
                    .anyMatch(rc -> Arrays.stream(rc).allMatch(n -> n == MARKED));
        }

        public void complete() {
            this.completed = true;
        }

        public int value() {
            return range(getHeight())
                    .intStream()
                    .flatMap(r -> range(getWidth()).intStream().map(c -> numbers[r][c]))
                    .filter(v -> v != MARKED)
                    .sum();
        }

        private int[] getColumn(final int col) {
            return range(getHeight()).intStream().map(row -> this.numbers[row][col]).toArray();
        }

        private int getWidth() {
            return this.numbers[0].length;
        }

        private int getHeight() {
            return this.numbers.length;
        }
    }

    record BingoGame(List<Integer> draws, List<List<String>> boards) {

        record Bingo(int draw, Board board) {}

        public static BingoGame fromInput(final List<String> input) {
            final List<List<String>> blocks = toBlocks(input);
            final List<Integer> draws =
                    Arrays.stream(blocks.get(0).get(0).split(",")).map(Integer::valueOf).toList();
            return new BingoGame(draws, blocks.subList(1, blocks.size()));
        }

        public Bingo play(final int stopCount) {
            final List<Board> boards = this.boards().stream().map(Board::new).toList();
            int bingoes = 0;
            for (final Integer draw : draws) {
                boards.forEach(b -> b.mark(draw));
                final List<Board> winners =
                        boards.stream().filter(b -> !b.isCompleted()).filter(Board::win).toList();
                for (final Board winner : winners) {
                    winner.complete();
                    bingoes++;
                    if (bingoes == stopCount) {
                        return new Bingo(draw, winner);
                    }
                }
            }
            throw unreachable();
        }
    }
}
