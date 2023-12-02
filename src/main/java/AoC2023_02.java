import static com.github.pareronia.aoc.Utils.enumerate;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2023_02
        extends SolutionBase<List<AoC2023_02.Game>, Integer, Integer> {
    
    private AoC2023_02(final boolean debug) {
        super(debug);
    }
    
    public static AoC2023_02 create() {
        return new AoC2023_02(false);
    }
    
    public static AoC2023_02 createDebug() {
        return new AoC2023_02(true);
    }
    
    @Override
    protected List<Game> parseInput(final List<String> inputs) {
        final Function<String, Draw> parseDraw = s -> {
            int red = 0, green = 0, blue = 0;
            for (final String cc : s.split(",")) {
                final String[] splits = cc.strip().split(" ");
                final String color = splits[1];
                if ("red".equals(color)) {
                    red = Integer.parseInt(splits[0]);
                } else if ("green".equals(color)) {
                    green = Integer.parseInt(splits[0]);
                } else {
                    blue = Integer.parseInt(splits[0]);
                }
            }
            return new Draw(red, green, blue);
        };
        return enumerate(inputs.stream())
            .map(e -> new Game(
                e.getIndex() + 1,
                Arrays.stream(e.getValue().split(":")[1].split(";"))
                    .map(parseDraw).toList()))
            .toList();
    }

    @Override
    public Integer solvePart1(final List<Game> games) {
        final Predicate<Draw> possible = draw ->
            draw.red <= 12 && draw.green <= 13 && draw.blue <= 14;
        return games.stream()
            .filter(game -> game.draws.stream().allMatch(possible))
            .mapToInt(Game::id)
            .sum();
    }
    
    @Override
    public Integer solvePart2(final List<Game> games) {
        final Function<Game, Integer> power = game ->
                game.draws.stream().mapToInt(Draw::red).max().getAsInt()
              * game.draws.stream().mapToInt(Draw::green).max().getAsInt()
              * game.draws.stream().mapToInt(Draw::blue).max().getAsInt();
        return games.stream().mapToInt(power::apply).sum();
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "8"),
        @Sample(method = "part2", input = TEST, expected = "2286"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2023_02.create().run();
    }
    
    private record Draw(int red, int green, int blue) { }
    
    record Game(int id, List<Draw> draws) { }

    private static final String TEST = """
        Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
        Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
        Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
        Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
        """;
}
