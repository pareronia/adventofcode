import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

public class AoC2022_02 extends AoCBase {
    private static final String ROCK = "rock";
    private static final String PAPER = "paper";
    private static final String SCISSORS = "scissors";
    private static final String WIN = "win";
    private static final String DRAW = "draw";
    private static final String LOSS = "lose";
    private static final Map<String, String> SHAPES = Map.of(
            "A", ROCK,
            "B", PAPER,
            "C", SCISSORS,
            "X", ROCK,
            "Y", PAPER,
            "Z", SCISSORS);
    private static final Map<String, Integer> RESPONSE_POINTS
            = Map.of(ROCK, 1, PAPER, 2, SCISSORS, 3);
    private static final Map<String, Integer> OUTCOME_POINTS
            = Map.of(LOSS, 0, DRAW, 3, WIN, 6);
    
    private final List<String> input;
    
    private AoC2022_02(final List<String> input, final boolean debug) {
        super(debug);
        this.input = input;
    }
    
    public static final AoC2022_02 create(final List<String> input) {
        return new AoC2022_02(input, false);
    }

    public static final AoC2022_02 createDebug(final List<String> input) {
        return new AoC2022_02(input, true);
    }
    
    private int solve(final BiFunction<String, String, ResponseAndOutcome> f) {
        return this.input.stream()
            .mapToInt(line -> {
                final String[] splits = line.split(" ");
                final ResponseAndOutcome o = f.apply(splits[0], splits[1]);
                return RESPONSE_POINTS.get(o.response) + OUTCOME_POINTS.get(o.outcome);
            })
            .sum();
    }
    
    @Override
    public Integer solvePart1() {
        final Map<StringPair, String> outcomes = Map.of(
            new StringPair(ROCK, ROCK), DRAW,
            new StringPair(ROCK, SCISSORS), LOSS,
            new StringPair(ROCK, PAPER), WIN,
            new StringPair(SCISSORS, ROCK), WIN,
            new StringPair(SCISSORS, SCISSORS), DRAW,
            new StringPair(SCISSORS, PAPER), LOSS,
            new StringPair(PAPER, ROCK), LOSS,
            new StringPair(PAPER, SCISSORS), WIN,
            new StringPair(PAPER, PAPER), DRAW
        );

        return solve((col1, col2) -> {
            final String play = SHAPES.get(col1);
            final String response = SHAPES.get(col2);
            final String outcome = outcomes.get(new StringPair(play, response));
            return new ResponseAndOutcome(response, outcome);
        } );
    }

    @Override
    public Integer solvePart2() {
        final Map<String,String> outcomes = Map.of("X", LOSS, "Y", DRAW, "Z", WIN);
        final Map<StringPair, String> responses = Map.of(
                new StringPair(ROCK, LOSS), SCISSORS,
                new StringPair(ROCK, DRAW), ROCK,
                new StringPair(ROCK, WIN), PAPER,
                new StringPair(PAPER, LOSS), ROCK,
                new StringPair(PAPER, DRAW), PAPER,
                new StringPair(PAPER, WIN), SCISSORS,
                new StringPair(SCISSORS, LOSS), PAPER,
                new StringPair(SCISSORS, DRAW), SCISSORS,
                new StringPair(SCISSORS, WIN), ROCK);
       
        return solve((col1, col2) -> {
            final String play = SHAPES.get(col1);
            final String outcome = outcomes.get(col2);
            final String response = responses.get(new StringPair(play, outcome));
            return new ResponseAndOutcome(response, outcome);
        });
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_02.createDebug(TEST).solvePart1() == 15;
        assert AoC2022_02.createDebug(TEST).solvePart2() == 12;

        final Puzzle puzzle = Aocd.puzzle(2022, 2);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_02.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_02.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines(
        "A Y\r\n" +
        "B X\r\n" +
        "C Z"
    );
    
    @RequiredArgsConstructor
    @EqualsAndHashCode
    private static final class ResponseAndOutcome {
        private final String response;
        private final String outcome;
    }
    
    @RequiredArgsConstructor
    @EqualsAndHashCode
    private static final class StringPair {
        private final String one;
        private final String two;
    }
}
