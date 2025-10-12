import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aoc.codec.MD5;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"PMD.NoPackage", "PMD.ClassNamingConventions"})
public final class AoC2016_14 extends SolutionBase<String, Integer, Integer> {

    private static final int NUMBER_OF_KEYS = 64;
    private static final Pattern TRIPLET = Pattern.compile("([a-f0-9])\\1\\1");

    private AoC2016_14(final boolean debug) {
        super(debug);
    }

    public static AoC2016_14 create() {
        return new AoC2016_14(false);
    }

    public static AoC2016_14 createDebug() {
        return new AoC2016_14(true);
    }

    @Override
    protected String parseInput(final List<String> inputs) {
        return inputs.get(0);
    }

    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    private int solve(final String salt, final int stretch) {
        final Function<Key, String> calculateMD5 =
                key -> {
                    String hash = String.format("%s%d", key.salt, key.index);
                    for (int i = 0; i < 1 + key.stretch; i++) {
                        hash = MD5.md5Hex(hash);
                    }
                    return hash;
                };
        final Map<Key, String> cache = new HashMap<>();
        int idx = -1;
        int cnt = 0;
        while (cnt < NUMBER_OF_KEYS) {
            idx++;
            String md5 = cache.computeIfAbsent(new Key(salt, idx, stretch), calculateMD5);
            final Matcher matcher = TRIPLET.matcher(md5);
            if (matcher.find()) {
                final String quintet = StringUtils.repeat(matcher.group(1).charAt(0), 5);
                for (int i = idx + 1; i < idx + 1001; i++) {
                    md5 = cache.computeIfAbsent(new Key(salt, i, stretch), calculateMD5);
                    if (md5.contains(quintet)) {
                        cnt++;
                        break;
                    }
                }
            }
        }
        return idx;
    }

    @Override
    public Integer solvePart1(final String salt) {
        return solve(salt, 0);
    }

    @Override
    public Integer solvePart2(final String salt) {
        return solve(salt, 2016);
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "22728"),
        @Sample(method = "part2", input = TEST, expected = "22551"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST = "abc";

    record Key(String salt, int index, int stretch) {}
}
