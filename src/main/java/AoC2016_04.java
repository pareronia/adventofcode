import static com.github.pareronia.aoc.AssertUtils.assertTrue;
import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.Comparator.comparing;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.pareronia.aoc.Counter;
import com.github.pareronia.aoc.Counter.Entry;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2016_04
        extends SolutionBase<List<AoC2016_04.Room>, Integer, Integer> {
    
    private static final Pattern REGEXP = Pattern.compile("([-a-z]+)-([0-9]+)\\[([a-z]{5})\\]$");
    private static final Pattern MATCH = Pattern.compile("[a-z]{9}-[a-z]{6}-[a-z]{7}$");
    
	private AoC2016_04(final boolean debug) {
		super(debug);
	}
	
	public static final AoC2016_04 create() {
		return new AoC2016_04(false);
	}

	public static final AoC2016_04 createDebug() {
		return new AoC2016_04(true);
	}

	@Override
    protected List<Room> parseInput(final List<String> inputs) {
        return inputs.stream().map(Room::fromInput).toList();
    }

    @Override
	public Integer solvePart1(final List<Room> rooms) {
		return rooms.stream()
		        .filter(Room::isReal)
		        .mapToInt(Room::sectorId)
		        .sum();
	}

	@Override
	public Integer solvePart2(final List<Room> rooms) {
	    return rooms.stream()
	            .filter(r -> MATCH.matcher(r.name()).matches())
	            .filter(r -> r.decrypt().equals("northpole object storage"))
	            .map(Room::sectorId)
	            .findFirst().orElseThrow();
	}

	@Samples({
	    @Sample(method = "part1", input = TEST, expected = "1514")
	})
	public static void main(final String[] args) throws Exception {
	    AoC2016_04.create().run();
	}

	private static final String TEST = """
	        aaaaa-bbb-z-y-x-123[abxyz]
	        a-b-c-d-e-f-g-h-987[abcde]
	        not-a-real-room-404[oarel]
	        totally-real-room-200[decoy]
	        """;
	
	record Room(String name, int sectorId, String checkum) {
	    
	    public static Room fromInput(final String line) {
	        final Matcher m = REGEXP.matcher(line);
            assertTrue(m.matches(), () -> "Expected match");
            return new Room(m.group(1), Integer.parseInt(m.group(2)), m.group(3));
	    }
	    
	    public boolean isReal() {
	        return new Counter<>(
	                Utils.asCharacterStream(this.name.replace("-", "")))
	            .mostCommon().stream()
	            .sorted(comparing(e -> e.count() * -100 + e.value().charValue()))
	            .limit(5)
	            .map(Entry::value)
	            .collect(toAString())
	            .equals(this.checkum);
	    }
	    
	    private Character decrypt(final Character c, final int shift) {
	        if (c == '-') {
	            return ' ';
	        }
	        return StringOps.nextLetter(c, shift);
	    }
	    
	    public String decrypt() {
	        final int shift = this.sectorId % 26;
	        return Utils.asCharacterStream(this.name)
	                .map(c -> decrypt(c, shift))
	                .collect(toAString());
	    }
	}
}
