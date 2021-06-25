import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.navigation.Headings;
import com.github.pareronia.aoc.navigation.NavigationWithHeading;
import com.github.pareronia.aoc.navigation.NavigationWithWaypoint;
import com.github.pareronia.aoc.navigation.WayPoint;
import com.github.pareronia.aocd.Aocd;

import lombok.Value;

public class AoC2020_12 extends AoCBase {
	
	private final List<NavigationInstruction> navs;
	
	private AoC2020_12(List<String> input, boolean debug) {
		super(debug);
		this.navs = input.stream()
		        .map(s -> NavigationInstruction.of(s.substring(0, 1), s.substring(1)))
		        .collect(toList());
		log(this.navs);
	}
	
	public static AoC2020_12 create(List<String> input) {
		return new AoC2020_12(input, false);
	}

	public static AoC2020_12 createDebug(List<String> input) {
		return new AoC2020_12(input, true);
	}
	
	@Override
	public Integer solvePart1() {
	    final Position start = Position.of(0, 0);
        final NavigationWithHeading nav
                = new NavigationWithHeading(start, Headings.EAST.get());
	    log(nav);
	    for (final NavigationInstruction ins : this.navs) {
	       if (ins.getAction() == Action.RIGHT) {
	           nav.right(ins.getValue());
	       } else if (ins.getAction() == Action.LEFT) {
	           nav.left(ins.getValue());
	       } else if (ins.getAction() == Action.FORWARD) {
	           nav.forward(ins.getValue());
	       } else if (Set.of(Action.NORTH, Action.SOUTH, Action.EAST, Action.WEST)
	               .contains(ins.getAction())) {
	           nav.drift(Headings.valueOf(ins.getAction().name()).get(), ins.getValue());
	       }
	       log(nav);
        }
	    return nav.getPosition().manhattanDistance(start);
	}
	
	@Override
	public Integer solvePart2() {
	    final Position start = Position.of(0, 0);
	    final WayPoint waypoint = new WayPoint(10, 1);
        final NavigationWithWaypoint nav
                = new NavigationWithWaypoint(start, waypoint);
	    log(nav);
	    for (final NavigationInstruction ins : this.navs) {
	       if (ins.getAction() == Action.RIGHT) {
	           nav.right(ins.getValue());
	       } else if (ins.getAction() == Action.LEFT) {
	           nav.left(ins.getValue());
	       } else if (ins.getAction() == Action.FORWARD) {
	           nav.forward(ins.getValue());
	       } else if (Set.of(Action.NORTH, Action.SOUTH, Action.EAST, Action.WEST)
	               .contains(ins.getAction())) {
	           nav.updateWaypoint(Headings.valueOf(ins.getAction().name()).get(),
	                              ins.getValue());
	       }
	       log(nav);
        }
	    return nav.getPosition().manhattanDistance(start);
	}

	public static void main(String[] args) throws Exception {
		assert AoC2020_12.createDebug(TEST).solvePart1() == 25;
		assert AoC2020_12.createDebug(TEST).solvePart2() == 286;

		final List<String> input = Aocd.getData(2020, 12);
		lap("Part 1", () -> AoC2020_12.create(input).solvePart1());
		lap("Part 2", () -> AoC2020_12.create(input).solvePart2());
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
	    RIGHT("R"), LEFT("L"),
	    FORWARD("F");
	    
	    private final String code;
	    
	    private Action(String code) {
	        this.code = code;
	    }
	    
	    public static Action of(String code) {
	        return Arrays.stream(Action.values())
	                .filter(a -> a.code.equals(code))
	                .findFirst()
	                .orElseThrow();
	    }
	}
	
	@Value
	private static final class NavigationInstruction {
	    private final Action action;
	    private final Integer value;
	    
	    public static NavigationInstruction of(String action, String value) {
	        return new NavigationInstruction(
	            Action.of(action), Integer.valueOf(value)
	        );
	    }
	}
}