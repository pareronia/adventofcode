import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.github.pareronia.aoc.MutableInt;
import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aocd.Puzzle;

public class AoC2020_22 extends AoCBase {
	
	private final List<String> inputs;
	
	private AoC2020_22(final List<String> input, final boolean debug) {
		super(debug);
		this.inputs = input;
	}
	
	public static final AoC2020_22 create(final List<String> input) {
		return new AoC2020_22(input, false);
	}

	public static final AoC2020_22 createDebug(final List<String> input) {
		return new AoC2020_22(input, true);
	}
	
	private Players parse() {
		final List<List<String>> blocks = new ArrayList<>();
		int i = 0;
		blocks.add(new ArrayList<String>());
		for (final String input: inputs) {
			if (input.isEmpty()) {
				blocks.add(new ArrayList<String>());
				i++;
			} else {
				blocks.get(i).add(input);
			}
		}
		assert blocks.size() == 2;
		
		final List<Integer> pl1 = new ArrayList<>();
		final List<Integer> pl2 = new ArrayList<>();
		
		for (final String string : blocks.get(0)) {
			if (string.startsWith("Player")) {
				continue;
			}
			pl1.add(Integer.valueOf(string));
		}
		for (final String string : blocks.get(1)) {
			if (string.startsWith("Player")) {
				continue;
			}
			pl2.add(Integer.valueOf(string));
		}
		
		return new Players(pl1, pl2);
	}

	private void playRegularCombat(final Players players) {
		playCombat(players, new MutableInt(), false);
	}
	
	private void playRecursiveCombat(final Players players) {
		playCombat(players, new MutableInt(), true);
	}
	
	private void playCombat(final Players players, final MutableInt _game, final boolean recursive) {
		_game.increment();
		final int game = _game.intValue();
		log(() ->String.format("=== Game %d ===", game));
		final Set<Round> seen = new HashSet<>();
		final MutableInt rnd = new MutableInt(1);
		final List<Integer> pl1 = players.player1;
		final List<Integer> pl2 = players.player2;
		while (!pl1.isEmpty() && !pl2.isEmpty()) {
			log(() ->"");
			log(() ->String.format("-- Round %d (Game %d) --", rnd.intValue(), game));
			log(() ->String.format("Player 1's deck: %s", StringUtils.join(pl1, ", ")));
			log(() ->String.format("Player 2's deck: %s", StringUtils.join(pl2, ", ")));
			final Round round = new Round(pl1, pl2);
			if (recursive && seen.contains(round)) {
				pl2.clear();
				break;
			}
			seen.add(round);
			final Integer n1 = pl1.remove(0);
			log(() ->String.format("Player 1 plays: %d", n1));
			final Integer n2 = pl2.remove(0);
			log(() ->String.format("Player 2 plays: %d", n2));
			final Integer winner;
			if (recursive && pl1.size() >= n1 && pl2.size() >= n2) {
				log(() ->"Playing a sub-game to determine the winner...");
				log(() ->"");
				final List<Integer> pl1_sub = new ArrayList<>(pl1.subList(0, n1));
				final List<Integer> pl2_sub = new ArrayList<>(pl2.subList(0, n2));
				playCombat(new Players(pl1_sub, pl2_sub), _game, true);
				log(() ->"");
				log(() ->String.format("...anyway, back to game %d.", game));
				winner = pl2_sub.isEmpty() ? 1 : 2;
			} else {
				winner = n1 > n2 ? 1 : 2;
			}
			if (winner == 1) {
				pl1.addAll(asList(n1, n2));
			} else {
				pl2.addAll(asList(n2, n1));
			}
			log(() ->String.format("Player %d wins round %d of game %d!", winner, rnd.intValue(), game));
			rnd.increment();
		}
		final Integer winner = pl2.isEmpty() ? 1 : 2;
		log(() ->String.format("The winner of game %d is player %d!", game, winner));
	}
	
	private int getScore(final Players players) {
		log("");
		log("");
		log("== Post-game results ==");
		log(String.format("Player 1's deck: %s", StringUtils.join(players.player1, ", ")));
		log(String.format("Player 2's deck: %s", StringUtils.join(players.player2, ", ")));
		log("");
		log("");
		final List<Integer> winner = players.player2.isEmpty() ? players.player1 : players.player2;
		int total = 0;
		for (int i = 0; i < winner.size(); i++) {
			total += (winner.size() - i) * winner.get(i);
		}
		return total;
	}
	
	@Override
	public Integer solvePart1() {
		final Players players = parse();
		playRegularCombat(players);
		return getScore(players);
	}
	
	@Override
	public Integer solvePart2() {
		final Players players = parse();
		playRecursiveCombat(players);
		return getScore(players);
	}
	
	public static void main(final String[] args) throws Exception {
		assert AoC2020_22.createDebug(splitLines(TEST)).solvePart1() == 306;
		assert AoC2020_22.createDebug(splitLines(TEST)).solvePart2() == 291;
		assert AoC2020_22.createDebug(splitLines(LOOP)).solvePart2() == 105;
		
		final Puzzle puzzle = Puzzle.create(2022, 22);
		final List<String> input = puzzle.getInputData();
		puzzle.check(
		    () -> lap("Part 1", AoC2022_22.create(input)::solvePart1),
		    () -> lap("Part 2", AoC2022_22.create(input)::solvePart2)
		);
	}
	
	private static final String TEST =
			"Player 1:\r\n" +
			"9\r\n" +
			"2\r\n" +
			"6\r\n" +
			"3\r\n" +
			"1\r\n" +
			"\r\n" +
			"Player 2:\r\n" +
			"5\r\n" +
			"8\r\n" +
			"4\r\n" +
			"7\r\n" +
			"10";
	
	private static final String LOOP =
			"Player 1:\r\n" +
			"43\r\n" +
			"19\r\n" +
			"\r\n" +
			"Player 2:\r\n" +
			"2\r\n" +
			"29\r\n" +
			"14";
	
	private static final class Players {
		private final List<Integer> player1;
		private final List<Integer> player2;
		
		public Players(final List<Integer> player1, final List<Integer> player2) {
			this.player1 = player1;
			this.player2 = player2;
		}
	}
	
	private static final class Round {
		private final List<Integer> pl1;
		private final List<Integer> pl2;
		
		public Round(final List<Integer> pl1, final List<Integer> pl2) {
			this.pl1 = new ArrayList<>(pl1);
			this.pl2 = new ArrayList<>(pl2);
		}

		@Override
		public int hashCode() {
			return Objects.hash(pl1, pl2);
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof Round)) {
				return false;
			}
			final Round other = (Round) obj;
			return Objects.equals(pl1, other.pl1) && Objects.equals(pl2, other.pl2);
		}
	}
}
