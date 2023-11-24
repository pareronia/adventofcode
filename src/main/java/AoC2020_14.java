import static com.github.pareronia.aoc.Utils.toAString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2020_14 extends AoCBase {
    
    private final List<Group> groups;
	
	private AoC2020_14(final List<String> input, final boolean debug) {
		super(debug);
		this.groups = new ArrayList<>();
		var groupBuilder = Group.builder();
		groupBuilder.mask(input.get(0).substring("mask = ".length()));
		for (int i = 1; i <= input.size(); i++) {
		    if (i == input.size()) {
		        this.groups.add(groupBuilder.build());
		    } else if (input.get(i).startsWith("mask = ")) {
		        this.groups.add(groupBuilder.build());
		        groupBuilder = Group.builder();
		        groupBuilder.mask(input.get(i).substring("mask = ".length()));
		    } else {
		        final var splits = input.get(i).split("] = ");
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
	    final var memory = new HashMap<String, Long>();
	    for (final var group : this.groups) {
            for (final var write : group.writes) {
                final var result = strategy.execute(write.address, write.value, group.mask);
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
	
	private static final List<String> TEST1 = splitLines("""
	        mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
	        mem[8] = 11
	        mem[7] = 101
	        mem[8] = 0
	        """);
	private static final List<String> TEST2 = splitLines("""
	        mask = 000000000000000000000000000000X1001X
	        mem[42] = 100
	        mask = 00000000000000000000000000000000X0XX
	        mem[26] = 1
	        """);
	
	private static final record Write(int address, int value) { }
	
	private static final record Group(String mask, List<Write> writes) {
	    public static GroupBuilder builder() {
	        return new GroupBuilder();
	    }
	    private static final class GroupBuilder {
	        private String mask;
	        private final List<Write> writes = new ArrayList<>();
	        
	        public Group build() {
	            return new Group(this.mask, this.writes);
	        }
	        
	        public GroupBuilder mask(final String mask) {
	            this.mask = mask;
	            return this;
	        }
	        
	        public GroupBuilder write(final Write write) {
	            this.writes.add(write);
	            return this;
	        }
	    }
	}
	
	private static final record Result(List<String> addresses, long value) { }
    
    @FunctionalInterface
    private interface Strategy {
        Result execute(final long address, final long value, final String mask);
    }
}