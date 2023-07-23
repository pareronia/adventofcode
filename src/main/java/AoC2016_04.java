import static com.github.pareronia.aoc.StringUtils.countMatches;
import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Puzzle;

import lombok.Value;

public class AoC2016_04 extends AoCBase {
    
    private static final Pattern REGEXP = Pattern.compile("([-a-z]+)-([0-9]+)\\[([a-z]{5})\\]$");
    private static final Pattern MATCH = Pattern.compile("[a-z]{9}-[a-z]{6}-[a-z]{7}$");
    
    private final List<Room> rooms;

	private AoC2016_04(final List<String> inputs, final boolean debug) {
		super(debug);
		this.rooms = inputs.stream()
		        .map(REGEXP::matcher)
		        .filter(Matcher::matches)
		        .map(m -> new Room(m.group(1), Integer.valueOf(m.group(2)), m.group(3)))
		        .collect(toList());
		log(rooms);
	}
	
	public static final AoC2016_04 create(final List<String> input) {
		return new AoC2016_04(input, false);
	}

	public static final AoC2016_04 createDebug(final List<String> input) {
		return new AoC2016_04(input, true);
	}
	
	@Override
	public Integer solvePart1() {
		return this.rooms.stream()
		        .filter(Room::isReal)
		        .collect(summingInt(Room::getSectorId));
	}

	@Override
	public Integer solvePart2() {
	    final List<Integer> matches = this.rooms.stream()
	            .filter(r -> MATCH.matcher(r.getName()).matches())
	            .filter(r -> r.decrypt().equals("northpole object storage"))
	            .map(Room::getSectorId)
	            .collect(toList());
	    assert matches.size() == 1;
		return matches.get(0);
	}

	public static void main(final String[] args) throws Exception {
		assert AoC2016_04.createDebug(TEST).solvePart1() == 1514;
		
		final Puzzle puzzle = Puzzle.create(2016, 4);
		final List<String> input = puzzle.getInputData();
		puzzle.check(
		    () -> lap("Part 1", AoC2016_04.create(input)::solvePart1),
		    () -> lap("Part 2", AoC2016_04.create(input)::solvePart2)
		);
	}

	private static final List<String> TEST = splitLines(
			"aaaaa-bbb-z-y-x-123[abxyz]\r\n" +
			"a-b-c-d-e-f-g-h-987[abcde]\r\n" +
			"not-a-real-room-404[oarel]\r\n" +
			"totally-real-room-200[decoy]"
	);
	
	@Value
	private static final class Room {
	    private final String name;
	    private final Integer sectorId;
	    private final String checkum;
	    
	    public boolean isReal() {
	        return Utils.asCharacterStream(this.name.replace("-", ""))
                .collect(toSet()).stream()
                .collect(toMap(c -> c,
                               c -> Integer.valueOf(countMatches(this.name, c))))
                .entrySet().stream()
	            .sorted(comparing(e -> e.getValue() * -100 + e.getKey().charValue()))
	            .map(Entry::getKey)
	            .limit(5)
	            .collect(toAString())
	            .equals(this.checkum);
	    }
	    
	    private Character decrypt(final Character c, final int shift) {
	        if (c == '-') {
	            return ' ';
	        }
	        return (char) ((c + shift - 97) % 26 + 97);
	    }
	    
	    public String decrypt() {
	        final int shift = this.sectorId % 26;
	        return Utils.asCharacterStream(this.name)
	                .map(c -> decrypt(c, shift))
	                .collect(toAString());
	    }
	}
}
