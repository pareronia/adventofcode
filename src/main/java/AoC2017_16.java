import static com.github.pareronia.aoc.Utils.toAString;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.pareronia.aocd.Aocd;

public final class AoC2017_16 extends AoCBase {
    
    private final transient List<String> moves;

    private AoC2017_16(final List<String> inputs, final boolean debug) {
        super(debug);
        assert inputs.size() == 1;
        this.moves = Arrays.asList(inputs.get(0).split(","));
    }

    public static AoC2017_16 create(final List<String> input) {
        return new AoC2017_16(input, false);
    }

    public static AoC2017_16 createDebug(final List<String> input) {
        return new AoC2017_16(input, true);
    }
    
    private String solve(final String start) {
        final Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < start.length(); i++) {
            map.put(start.charAt(i), i);
        }
        
        for (final String move : this.moves) {
            if (move.startsWith("s")) {
                final int c = Integer.parseInt(move.substring(1));
                for (final Character ch : map.keySet()) {
                    map.put(ch, (map.get(ch) + c) % start.length());
                }
            } else if (move.startsWith("x")) {
                final int a = Integer.parseInt(move.substring(1).split("/")[0]);
                final int b = Integer.parseInt(move.substring(1).split("/")[1]);
                Character idx_a = null;
                Character idx_b = null;
                for (final Character ch : map.keySet()) {
                    if (map.get(ch) == a) {
                        idx_a = ch;
                    } else if (map.get(ch) == b) {
                        idx_b = ch;
                    }
                }
                final int tmp = map.get(idx_a);
                map.put(idx_a, map.get(idx_b));
                map.put(idx_b, tmp);
            } else if (move.startsWith("p")) {
                final Character a = move.substring(1).split("/")[0].charAt(0);
                final Character b = move.substring(1).split("/")[1].charAt(0);
                final int tmp = map.get(a);
                map.put(a, map.get(b));
                map.put(b, tmp);
            }
            log(asString(map));
        }
        return asString(map);
    }

    private String asString(final Map<Character, Integer> map) {
        return map.entrySet().stream()
            .sorted((e1, e2) -> Integer.compare(e1.getValue(), e2.getValue()))
            .map(Entry::getKey)
            .collect(toAString());
    }
    
    @Override
    public String solvePart1() {
        return solve("abcdefghijklmnop");
    }
    
    @Override
    public String solvePart2() {
        return "";
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_16.createDebug(TEST).solve("abcde").equals("baedc");

        final List<String> input = Aocd.getData(2017, 16);
        lap("Part 1", () -> AoC2017_16.create(input).solvePart1());
        lap("Part 2", () -> AoC2017_16.create(input).solvePart2());
    }
    
    private static final List<String> TEST = splitLines(
            "s1,x3/4,pe/b"
    );
}
