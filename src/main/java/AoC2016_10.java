import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings({"PMD.NoPackage", "PMD.ClassNamingConventions"})
public final class AoC2016_10 extends SolutionBase<AoC2016_10.Factory, Integer, Integer> {

    private static final String TEST =
            """
            value 5 goes to bot 2
            bot 2 gives low to bot 1 and high to bot 0
            value 3 goes to bot 1
            bot 1 gives low to output 1 and high to bot 0
            bot 0 gives low to output 2 and high to output 0
            value 2 goes to bot 2
            """;

    private AoC2016_10(final boolean debug) {
        super(debug);
    }

    public static AoC2016_10 create() {
        return new AoC2016_10(false);
    }

    public static AoC2016_10 createDebug() {
        return new AoC2016_10(true);
    }

    @Override
    protected Factory parseInput(final List<String> inputs) {
        return Factory.fromInputs(inputs);
    }

    private Integer solvePart1(final Factory factory, final int first, final int second) {
        return factory.bots.values().stream()
                .filter(
                        b ->
                                b.comparisons().stream()
                                        .anyMatch(c -> c.loValue == first && c.hiValue == second))
                .findFirst()
                .map(Bot::number)
                .orElseThrow();
    }

    @Override
    public Integer solvePart1(final Factory factory) {
        return solvePart1(factory, 17, 61);
    }

    @Override
    public Integer solvePart2(final Factory factory) {
        return factory.outputs.entrySet().stream()
                .filter(e -> Set.of(0, 1, 2).contains(e.getKey()))
                .map(Entry::getValue)
                .reduce(1, (a, b) -> a * b);
    }

    @Override
    protected void samples() {
        final AoC2016_10 test = createDebug();
        final Factory input = test.parseInput(StringOps.splitLines(TEST));
        assert test.solvePart1(input, 2, 5) == 2;
        assert test.solvePart2(input) == 30;
    }

    public static void main(final String[] args) throws Exception {
        create().run();
    }

    record Bot(
            int number,
            BotActionType loAction,
            BotActionType hiAction,
            List<Integer> values,
            Set<Comparison> comparisons) {

        record Comparison(int loValue, int hiValue) {}

        private Bot(final Bot bot, final List<Integer> values, final Set<Comparison> comparisons) {
            this(bot.number, bot.loAction, bot.hiAction, values, comparisons);
        }

        public static Bot fromInput(final String string) {
            final String[] splits = string.substring("bot ".length()).split(" ");
            final int bot = Integer.parseInt(splits[0]);
            final BotActionType loAction =
                    new BotActionType(
                            "bot".equals(splits[4])
                                    ? BotActionType.Type.TO_BOT
                                    : BotActionType.Type.TO_OUT,
                            Integer.parseInt(splits[5]));
            final BotActionType hiAction =
                    new BotActionType(
                            "bot".equals(splits[9])
                                    ? BotActionType.Type.TO_BOT
                                    : BotActionType.Type.TO_OUT,
                            Integer.parseInt(splits[10]));
            return new Bot(bot, loAction, hiAction, new ArrayList<>(), new HashSet<>());
        }

        public BotResponse receive(final int value) {
            final List<Integer> values = new ArrayList<>(this.values);
            values.add(value);
            final Set<Comparison> comparisons = new HashSet<>(this.comparisons);
            if (values.size() == 2) {
                Collections.sort(values);
                final int loValue = values.get(0);
                final int hiValue = values.get(1);
                values.clear();
                comparisons.add(new Comparison(loValue, hiValue));
                final Bot newBot = new Bot(this, values, comparisons);
                final BotAction lo = new BotAction(this.loAction, loValue);
                final BotAction hi = new BotAction(this.hiAction, hiValue);
                return new BotResponse(newBot, Optional.of(lo), Optional.of(hi));
            } else {
                return new BotResponse(
                        new Bot(this, values, comparisons), Optional.empty(), Optional.empty());
            }
        }
    }

    record BotInput(int value, int toBot) {

        public static BotInput fromInput(final String string) {
            final String[] splits = string.substring("value ".length()).split(" goes to bot ");
            return new BotInput(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]));
        }
    }

    record Factory(Map<Integer, Bot> bots, Map<Integer, Integer> outputs) {

        public static Factory fromInputs(final List<String> inputs) {
            final Map<Integer, Bot> bots = new HashMap<>();
            final List<BotInput> botInputs = new ArrayList<>();
            for (final String string : inputs) {
                if (string.startsWith("value ")) {
                    botInputs.add(BotInput.fromInput(string));
                } else {
                    final Bot bot = Bot.fromInput(string);
                    bots.put(bot.number, bot);
                }
            }
            return new Factory(bots, doRun(bots, botInputs));
        }

        private static Map<Integer, Integer> doRun(
                final Map<Integer, Bot> bots, final List<BotInput> botInputs) {
            final Map<Integer, Integer> outputs = new HashMap<>();
            final Deque<BotInput> q = new ArrayDeque<>(botInputs);
            while (!q.isEmpty()) {
                final BotInput input = q.pollFirst();
                final BotResponse output = bots.get(input.toBot).receive(input.value);
                bots.put(output.bot.number, output.bot);
                for (final Optional<BotAction> action : List.of(output.lo, output.hi)) {
                    action.ifPresent(
                            a -> {
                                switch (a.type.type) {
                                    case TO_BOT -> q.addLast(new BotInput(a.value, a.type.target));
                                    case TO_OUT -> outputs.put(a.type.target, a.value);
                                    default -> {}
                                }
                            });
                }
            }
            return outputs;
        }
    }

    record BotActionType(Type type, int target) {

        enum Type {
            NONE,
            TO_BOT,
            TO_OUT;
        }
    }

    record BotAction(BotActionType type, int value) {}

    record BotResponse(Bot bot, Optional<BotAction> lo, Optional<BotAction> hi) {}
}
