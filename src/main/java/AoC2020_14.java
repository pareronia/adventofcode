import static com.github.pareronia.aoc.Utils.toAString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import lombok.ToString;

public class AoC2020_14 extends AoCBase {
    
    private final List<Group> groups;
	
	private AoC2020_14(final List<String> input, final boolean debug) {
		super(debug);
		this.groups = new ArrayList<>();
		Group.GroupBuilder groupBuilder = Group.builder();
		groupBuilder.mask(input.get(0).substring("mask = ".length()));
		for (int i = 1; i <= input.size(); i++) {
		    if (i == input.size()) {
		        this.groups.add(groupBuilder.build());
		    } else if (input.get(i).startsWith("mask = ")) {
		        this.groups.add(groupBuilder.build());
		        groupBuilder = Group.builder();
		        groupBuilder.mask(input.get(i).substring("mask = ".length()));
		    } else {
		        final String[] splits = input.get(i).split("] = ");
		        final int address = Integer.parseInt(splits[0].substring("mem[".length()));
		        final int value = Integer.parseInt(splits[1]);
		        groupBuilder.write(new Write(address, value));
		    }
		}
	}
	
	public static AoC2020_14 create(final List<String> input) {
		return new AoC2020_14(input, false);
	}

	public static AoC2020_14 createDebug(final List<String> input) {
		return new AoC2020_14(input, true);
	}
	
	private Result applyMask1(final long address, final long value, final String mask) {
	    final String bin = StringUtils.leftPad(Long.toBinaryString(value), mask.length(), '0');
	    final String newBin = IntStream.range(0, mask.length())
	            .mapToObj(i -> mask.charAt(i) == 'X' ? bin.charAt(i) : mask.charAt(i))
	            .collect(toAString());
	    return new Result(List.of(String.valueOf(address)), Long.parseLong(newBin, 2));
	}
    
    private List<String> permutateAddresses(final List<String> addresses, final int pos) {
        if (pos == -1) {
            return addresses;
        }
        final List<String> newAddresses = new ArrayList<>();
        for (final String address : addresses) {
            final String part1 = address.substring(0, pos);
            final String part2 = address.substring(pos + 1);
            newAddresses.add(part1 + "0" + part2);
            newAddresses.add(part1 + "1" + part2);
        }
        return permutateAddresses(newAddresses, newAddresses.get(0).indexOf('X'));
    }
    
	private Result applyMask2(final long address, final long value, final String mask) {
	    final String bin = StringUtils.leftPad(Long.toBinaryString(address), mask.length(), '0');
	    final String newBin = IntStream.range(0, mask.length())
	            .mapToObj(i -> {
                    if (mask.charAt(i) == '0') {
                        return bin.charAt(i);
                    } else if (mask.charAt(i) == '1') {
                        return '1';
                    } else {
                        return 'X';
                    }
                })
	            .collect(toAString());
	    final List<String> addresses = permutateAddresses(List.of(newBin), newBin.indexOf('X'));
	    return new Result(addresses, value);
	}
	
	private Long solve(final Strategy strategy) {
	    final Map<String, Long> memory = new HashMap<>();
	    for (final Group group : this.groups) {
            for (final Write write : group.writes) {
                final Result result = strategy.execute(write.address, write.value, group.mask);
                for (final String address : result.addresses) {
                    memory.put(address, result.value);
                }
            }
        }
	    return memory.values().stream().mapToLong(Long::valueOf).sum();
	}
	
	@Override
	public Long solvePart1() {
	    return solve(this::applyMask1);
	}
	
	@Override
	public Long solvePart2() {
	    return solve(this::applyMask2);
	}

	public static void main(final String[] args) throws Exception {
		assert AoC2020_14.createDebug(TEST1).solvePart1() == 165;
		assert AoC2020_14.createDebug(TEST2).solvePart2() == 208;

        final Puzzle puzzle = Aocd.puzzle(2020, 14);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", () -> AoC2020_14.create(inputData).solvePart1()),
            () -> lap("Part 2", () -> AoC2020_14.create(inputData).solvePart2())
        );
	}
	
	private static final List<String> TEST1 = splitLines(
	        "mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X\r\n" +
	        "mem[8] = 11\r\n" +
	        "mem[7] = 101\r\n" +
	        "mem[8] = 0"
	);
	private static final List<String> TEST2 = splitLines(
	        "mask = 000000000000000000000000000000X1001X\r\n" +
	        "mem[42] = 100\r\n" +
	        "mask = 00000000000000000000000000000000X0XX\r\n" +
	        "mem[26] = 1"
	);
	
	@RequiredArgsConstructor
	@ToString
	private static final class Write {
	    private final int address;
	    private final int value;
	}
	
	@RequiredArgsConstructor
	@Builder
	@ToString
	private static final class Group {
	    private final String mask;
	    @Singular
	    private final List<Write> writes;
	}
	
	@RequiredArgsConstructor
	private static final class Result {
	    private final List<String> addresses;
	    private final long value;
	}
    
    @FunctionalInterface
    private interface Strategy {
        Result execute(final long address, final long value, final String mask);
    }
}