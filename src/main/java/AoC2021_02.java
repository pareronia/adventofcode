import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Function;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2021_02 extends AoCBase {
    
    // private static final String FORWARD = "forward";
    private static final String UP = "up";
    private static final String DOWN = "down";

    private final List<Command> commands;
	
	private AoC2021_02(final List<String> input, final boolean debug) {
		super(debug);
		final Function<String, Command> createCommand = s -> {
            final String[] split = s.split(" ");
            final String direction = split[0];
            return Command.create(direction, split[1]);
        };
        this.commands = input.stream().map(createCommand).collect(toList());
	}
	
	public static final AoC2021_02 create(final List<String> input) {
		return new AoC2021_02(input, false);
	}

	public static final AoC2021_02 createDebug(final List<String> input) {
		return new AoC2021_02(input, true);
	}
	
	@Override
    public Long solvePart1() {
	    long hor = 0, ver = 0;
	    for (final Command command : this.commands) {
	        if (UP.equals(command.direction())) {
	            ver -= command.amount();
	        } else if (DOWN.equals(command.direction())) {
	            ver += command.amount();
	        } else {
	            hor += command.amount();
	        }
        }
	    return hor * ver;
	}
	
	@Override
	public Long solvePart2() {
	    long hor = 0, ver = 0, aim = 0;
	    for (final Command command : this.commands) {
	        if (UP.equals(command.direction())) {
	            aim -= command.amount();
	        } else if (DOWN.equals(command.direction())) {
	            aim += command.amount();
	        } else {
	            hor += command.amount();
	            ver += (aim * command.amount());
	        }
        }
	    return hor * ver;
	}
	
	public static void main(final String[] args) throws Exception {
		assert AoC2021_02.createDebug(TEST).solvePart1() == 150;
		assert AoC2021_02.createDebug(TEST).solvePart2() == 900;
		
        final Puzzle puzzle = Aocd.puzzle(2021, 2);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2021_02.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2021_02.create(inputData)::solvePart2)
        );
	}
	
	private static final List<String> TEST = splitLines(
	        "forward 5\r\n" +
            "down 5\r\n" +
            "forward 8\r\n" +
            "up 3\r\n" +
            "down 8\r\n" +
            "forward 2"
    );
	
	record Command(String direction, int amount) {
	    
	    public static Command create(final String direction, final String amount) {
	        return new Command(direction, Integer.parseInt(amount));
	    }
	}
}
