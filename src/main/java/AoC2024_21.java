import static com.github.pareronia.aoc.StringOps.zip;
import static java.util.Comparator.comparingLong;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToLongFunction;

import com.github.pareronia.aoc.IterTools;
import com.github.pareronia.aoc.IterTools.ZippedPair;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2024_21 extends SolutionBase<List<String>, Long, Long> {

    public static final KeyPad NUMERIC
                    = new KeyPad(new String[] {"789", "456", "123", " 0A"});
    public static final KeyPad DIRECTIONAL
                    = new KeyPad(new String[] {" ^A", "<v>"});
    
    private final Map<String, Long> cache = new HashMap<>();

	private AoC2024_21(final boolean debug) {
		super(debug);
	}
	
	public static final AoC2024_21 create() {
		return new AoC2024_21(false);
	}

	public static final AoC2024_21 createDebug() {
		return new AoC2024_21(true);
	}
	
	@Override
    protected List<String> parseInput(final List<String> inputs) {
		return inputs;
    }
	
	private Iterator<String> paths(
	        final KeyPad keypad,
	        final Position from,
	        final Position to,
	        final String s
	) {
	    if (from.equals(to)) {
	        return List.of(s + "A").iterator();
	    }
	    Iterator<String> ans = List.<String>of().iterator();
	    Position newFrom = new Position(from.x - 1, from.y);
	    if (to.x < from.x && keypad.getChar(newFrom) != ' ') {
            ans = IterTools.chain(ans, paths(keypad, newFrom, to, s + "<"));
	    }
	    newFrom = new Position(from.x, from.y - 1);
	    if (to.y < from.y && keypad.getChar(newFrom) != ' ') {
            ans = IterTools.chain(ans, paths(keypad, newFrom, to, s + "^"));
	    }
	    newFrom = new Position(from.x, from.y + 1);
	    if (to.y > from.y && keypad.getChar(newFrom) != ' ') {
            ans = IterTools.chain(ans, paths(keypad, newFrom, to, s + "v"));
	    }
	    newFrom = new Position(from.x + 1, from.y);
	    if (to.x > from.x && keypad.getChar(newFrom) != ' ') {
            ans = IterTools.chain(ans, paths(keypad, newFrom, to, s + ">"));
	    }
	    return ans;
	}

	private long count(
	        final String path, final int level, final int maxLevel
	) {
	    if (level > maxLevel) {
	        return path.length();
	    }
	    final KeyPad keypad = level > 0 ? DIRECTIONAL : NUMERIC;
	    final ToLongFunction<String> countConsecutiveSameChars = s ->
	            zip(s, s.substring(1)).stream()
	                .filter(zp -> zp.first() == zp.second())
	                .count();
	    final Function<ZippedPair<Character>, String> bestPath = zp -> {
	        final Position from = keypad.getPosition(zp.first());
	        final Position to = keypad.getPosition(zp.second());
	        return Utils.stream(paths(keypad, from, to, ""))
	                .max(comparingLong(countConsecutiveSameChars))
	                .orElseThrow();};
	    return zip("A" + path, path).stream()
	            .map(bestPath)
	            .mapToLong(best -> cachedCount(best, level + 1, maxLevel))
	            .sum();
	}
    
    private long cachedCount(
            final String path, final int level, final int maxLevel
    ) {
        final String key = "%s%d".formatted(path, level);
        if (cache.containsKey(key)) {
            return cache.get(key);
        } else {
            final long ans = count(path, level, maxLevel);
            cache.put(key, ans);
            return ans;
        }
    }
	
	private long solve(final List<String> input, final int levels) {
	    cache.clear();
	    return input.stream()
            .mapToLong(s -> cachedCount(s, 0, levels)
                        * Long.parseLong(s.substring(0, s.length() - 1)))
            .sum();
	}

	@Override
	public Long solvePart1(final List<String> input) {
	    return solve(input, 2);
	}
	
	@Override
	public Long solvePart2(final List<String> input) {
	    return solve(input, 25);
	}
	
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "126384"),
    })
    public void samples() {
    }

	public static void main(final String[] args) throws Exception {
		AoC2024_21.create().run();
	}

	private static final String TEST = """
	        029A
	        980A
	        179A
	        456A
	        379A
	        """;

	record Position(int x, int y) {}
	
	record KeyPad(String[] layout) {
	    public Position getPosition(final char ch) {
            for (int y = 0; y < layout.length; y++) {
                for (int x = 0; x < layout[y].length(); x++) {
                    if (layout[y].charAt(x) == ch) {
                        return new Position(x, y);
                    }
                }
            }
            throw new IllegalStateException("Unsolvable");
	    }

	    public char getChar(final Position pos) {
	        return layout[pos.y].charAt(pos.x);
	    }
	}
}