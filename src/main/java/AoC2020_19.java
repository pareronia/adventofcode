import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2020_19
        extends SolutionBase<AoC2020_19.RulesAndMessages, Integer, Integer> {
    
    private AoC2020_19(final boolean debug) {
        super(debug);
    }
    
    public static AoC2020_19 create() {
        return new AoC2020_19(false);
    }
    
    public static AoC2020_19 createDebug() {
        return new AoC2020_19(true);
    }
    
    @Override
    protected RulesAndMessages parseInput(final List<String> inputs) {
        return RulesAndMessages.fromInput(inputs);
    }

    @Override
    public Integer solvePart1(final RulesAndMessages input) {
        final Pattern pattern
                = Pattern.compile(this.getRegexp(input.rules, "0"));
        return (int) input.messages.stream()
            .filter(m -> pattern.matcher(m).matches())
            .count();
    }
    
    @Override
    public Integer solvePart2(final RulesAndMessages input) {
        final Pattern pattern31
                = Pattern.compile(this.getRegexp(input.rules, "31"));
        log("31:" + pattern31.pattern());
        final Pattern pattern42
                = Pattern.compile(this.getRegexp(input.rules, "42"));
        log("42:" + pattern42.pattern());
        int ans = 0;
        for (final String message : input.messages) {
            log("%s (%d)".formatted(message, message.length()));
            int pos = 0;
            int count42 = 0;
            final Matcher m42 = pattern42.matcher(message);
            while (m42.find(pos) && m42.start() == pos) {
                log("42: (%d,%d) : %s".formatted(m42.start(), m42.end(), m42.group()));
                count42++;
                pos = m42.end();
            }
            int count31 = 0;
            final Matcher m31 = pattern31.matcher(message);
            while (m31.find(pos) && m31.start() == pos) {
                log("31: (%d,%d) : %s".formatted(m31.start(), m31.end(), m31.group()));
                count31++;
                pos = m31.end();
            }
            log(pos);
            if (0 < count31 && count31 < count42 && pos == message.length()) {
                log("OK");
                ans++;
            }
        }
        return ans;
    }
    
    private String getRegexp(final Map<String, String> rules, final String key) {
        if ("|".equals(key)) {
            return "|";
        }
        final String rule = rules.get(key);
        if (rule.startsWith("\"")) {
            return rule.substring(1, 2);
        }
        final String ans = Arrays.stream(rule.split(" "))
                .map(sp -> this.getRegexp(rules, sp))
                .collect(joining());
        return "(%s)".formatted(ans);
    }
    
    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "2"),
        @Sample(method = "part1", input = TEST2, expected = "3"),
        @Sample(method = "part2", input = TEST2, expected = "12"),
    })
    public static void main(final String[] args) throws Exception {
        AoC2020_19.create().run();
    }

    private static final String TEST1 = """
            0: 4 1 5
            1: 2 3 | 3 2
            2: 4 4 | 5 5
            3: 4 5 | 5 4
            4: "a"
            5: "b"

            ababbb
            bababa
            abbbab
            aaabbb
            aaaabbb
            """;
    private static final String TEST2 = """
            42: 9 14 | 10 1
            9: 14 27 | 1 26
            10: 23 14 | 28 1
            1: "a"
            11: 42 31
            5: 1 14 | 15 1
            19: 14 1 | 14 14
            12: 24 14 | 19 1
            16: 15 1 | 14 14
            31: 14 17 | 1 13
            6: 14 14 | 1 14
            2: 1 24 | 14 4
            0: 8 11
            13: 14 3 | 1 12
            15: 1 | 14
            17: 14 2 | 1 7
            23: 25 1 | 22 14
            28: 16 1
            4: 1 1
            20: 14 14 | 1 15
            3: 5 14 | 16 1
            27: 1 6 | 14 18
            14: "b"
            21: 14 1 | 1 14
            25: 1 1 | 1 14
            22: 14 14
            8: 42
            26: 14 22 | 1 20
            18: 15 15
            7: 14 5 | 1 21
            24: 14 1

            abbbbbabbbaaaababbaabbbbabababbbabbbbbbabaaaa
            bbabbbbaabaabba
            babbbbaabbbbbabbbbbbaabaaabaaa
            aaabbbbbbaaaabaababaabababbabaaabbababababaaa
            bbbbbbbaaaabbbbaaabbabaaa
            bbbababbbbaaaaaaaabbababaaababaabab
            ababaaaaaabaaab
            ababaaaaabbbaba
            baabbaaaabbaaaababbaababb
            abbbbabbbbaaaababbbbbbaaaababb
            aaaaabbaabaaaaababaa
            aaaabbaaaabbaaa
            aaaabbaabbaaaaaaabbbabbbaaabbaabaaa
            babaaabbbaaabaababbaabababaaab
            aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba
            """;

    record RulesAndMessages(Map<String, String> rules, List<String> messages) {

        public static RulesAndMessages fromInput(final List<String> inputs) {
            final List<List<String>> blocks = StringOps.toBlocks(inputs);
            final Map<String, String> rules = blocks.get(0).stream()
                    .map(line -> StringOps.splitOnce(line, ": "))
                    .collect(toMap(StringSplit::left, StringSplit::right));
            return new RulesAndMessages(rules, blocks.get(1));
        }
    }
}
