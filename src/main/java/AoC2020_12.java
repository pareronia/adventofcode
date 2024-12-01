import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;

import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.geometry.Turn;
import com.github.pareronia.aoc.navigation.Heading;
import com.github.pareronia.aoc.navigation.NavigationWithHeading;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2020_12 extends AoCBase {
	
	private final List<NavigationInstruction> instructions;
	
	private AoC2020_12(final List<String> input, final boolean debug) {
		super(debug);
		this.instructions = input.stream()
		        .map(s -> NavigationInstruction.of(s.substring(0, 1), s.substring(1)))
		        .collect(toList());
	}
	
	public static AoC2020_12 create(final List<String> input) {
		return new AoC2020_12(input, false);
	}

	public static AoC2020_12 createDebug(final List<String> input) {
		return new AoC2020_12(input, true);
	}
	
	@Override
	public Integer solvePart1() {
	    return new Navigation(
                this.instructions, Heading.EAST, ActionMeaning.ONE)
            .getDistanceTraveled();
	}
	
	@Override
	public Integer solvePart2() {
	    return new Navigation(
                this.instructions, Heading.of(10, 1), ActionMeaning.TWO)
            .getDistanceTraveled();
	}

	public static void main(final String[] args) throws Exception {
		assert AoC2020_12.createDebug(TEST).solvePart1() == 25;
		assert AoC2020_12.createDebug(TEST).solvePart2() == 286;

        final Puzzle puzzle = Aocd.puzzle(2020, 12);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2020_12.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2020_12.create(inputData)::solvePart2)
        );
	}
	
	private static final List<String> TEST = splitLines(
	        "F10\r\n" +
	        "N3\r\n" +
	        "F7\r\n" +
	        "R90\r\n" +
	        "F11"
	);
	
	public enum Action {
	    NORTH("N"), EAST("E"), SOUTH("S"), WEST("W"),
	    RIGHT("R"), LEFT("L"), FORWARD("F");
	    
	    private final String code;
	    
	    Action(final String code) {
	        this.code = code;
	    }
	    
	    public static Action of(final String code) {
	        return Arrays.stream(Action.values())
	                .filter(a -> a.code.equals(code))
	                .findFirst()
	                .orElseThrow();
	    }
	}
	
	record NavigationInstruction(Action action, int value) {
	    
	    public static NavigationInstruction of(final String action, final String value) {
	        return new NavigationInstruction(
	            Action.of(action), Integer.parseInt(value)
	        );
	    }
	}
	
	private enum ActionMeaning {
	    ONE, TWO;
	}
	
	private static final class Navigation {
	    private final List<NavigationInstruction> instructions;
	    private final ActionMeaning actionMeaning;
	    private final NavigationWithHeading nav;
	    private final Position start = Position.ORIGIN;
	    
	    public Navigation(
	            final List<NavigationInstruction> instructions,
	            final Heading initialHeading,
                final ActionMeaning actionMeaning
        ) {
            this.instructions = instructions;
            this.actionMeaning = actionMeaning;
            this.nav = new NavigationWithHeading(this.start, initialHeading);
        }

        public int getDistanceTraveled() {
            this.instructions.forEach(this::executeInstruction);
            return this.nav.getPosition().manhattanDistance(this.start);
	    }

        private void executeInstruction(final NavigationInstruction instuction) {
            final Action action = instuction.action();
            final int value = instuction.value();
            if (action == Action.RIGHT) {
                this.nav.turn(Turn.fromDegrees(value));
            } else if (action == Action.LEFT) {
                this.nav.turn(Turn.fromDegrees(360 - value));
            } else if (action == Action.FORWARD) {
                this.nav.forward(value);
            } else if (this.actionMeaning == ActionMeaning.ONE) {
                this.nav.drift(Heading.fromString(action.code), value);
            } else {
                final Direction dir = Direction.fromString(action.code);
                this.nav.setHeading(nav.getHeading().add(dir, value));
            }
        }
    }
}