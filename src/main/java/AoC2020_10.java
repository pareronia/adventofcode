import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.github.pareronia.aocd.Aocd;

public class AoC2020_10 extends AoCBase {
	
	private final List<Integer> numbers;
	
	private AoC2020_10(List<String> input, boolean debug) {
		super(debug);
		this.numbers = input.stream().map(Integer::valueOf).collect(toList());
	    this.numbers.add(0);
	    this.numbers.add(this.numbers.stream().max(comparingInt(n -> n)).orElseThrow() + 3);
	    Collections.sort(this.numbers);
	}
	
	public static AoC2020_10 create(List<String> input) {
		return new AoC2020_10(input, false);
	}

	public static AoC2020_10 createDebug(List<String> input) {
		return new AoC2020_10(input, true);
	}
	
	@Override
	public Long solvePart1() {
	    final HashMap<Integer, Long> jumps = Stream.iterate(1, i -> i + 1)
	            .takeWhile(i -> i < this.numbers.size())
	            .map(i -> this.numbers.get(i) - this.numbers.get(i - 1))
	            .collect(groupingBy(jump -> jump, HashMap::new, counting()));
	    return jumps.get(1) * jumps.get(3);
	}
	
    // 0: 1, 1: 1, 4: 1, 5: 1, 6: 2, 7: 4, 10: 4, 11: 4, 12: 8, 15: 8, 16: 8, 19: 8
	@Override
	public Long solvePart2() {
	    log(this.numbers);
	    final Map<Integer, Long> map = new HashMap<>();
	    map.put(0, 1L);
	    this.numbers.stream().skip(1).forEach(i -> {
	        Stream.iterate(this.numbers.indexOf(i) - 1, j -> j - 1)
	                .takeWhile(j -> j >= 0)
	                .map(this.numbers::get)
	                .filter(j -> i - j <= 3)
                    .forEach(j -> map.merge(i, map.get(j), Long::sum));
	        log(map);
	    });
		return map.get(this.numbers.get(this.numbers.size() - 1));
	}

	public static void main(String[] args) throws Exception {
		assert AoC2020_10.createDebug(TEST1).solvePart1() == 35;
		assert AoC2020_10.createDebug(TEST2).solvePart1() == 220;
		assert AoC2020_10.createDebug(TEST1).solvePart2() == 8;
		assert AoC2020_10.createDebug(TEST2).solvePart2() == 19208;

		final List<String> input = Aocd.getData(2020, 10);
		lap("Part 1", () -> AoC2020_10.create(input).solvePart1());
		lap("Part 2", () -> AoC2020_10.create(input).solvePart2());
	}
	
	private static final List<String> TEST1 = splitLines(
	        "16\r\n" +
	        "10\r\n" +
	        "15\r\n" +
	        "5\r\n" +
	        "1\r\n" +
	        "11\r\n" +
	        "7\r\n" +
	        "19\r\n" +
	        "6\r\n" +
	        "12\r\n" +
	        "4"
	);
	private static final List<String> TEST2 = splitLines(
	        "28\r\n" +
	        "33\r\n" +
	        "18\r\n" +
	        "42\r\n" +
	        "31\r\n" +
	        "14\r\n" +
	        "46\r\n" +
	        "20\r\n" +
	        "48\r\n" +
	        "47\r\n" +
	        "24\r\n" +
	        "23\r\n" +
	        "49\r\n" +
	        "45\r\n" +
	        "19\r\n" +
	        "38\r\n" +
	        "39\r\n" +
	        "11\r\n" +
	        "1\r\n" +
	        "32\r\n" +
	        "25\r\n" +
	        "35\r\n" +
	        "8\r\n" +
	        "17\r\n" +
	        "7\r\n" +
	        "9\r\n" +
	        "4\r\n" +
	        "2\r\n" +
	        "34\r\n" +
	        "10\r\n" +
	        "3"
	);
}