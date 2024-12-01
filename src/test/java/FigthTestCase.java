import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.pareronia.aoc.solution.Logger;

public class FigthTestCase {
    
    private Logger logger;
    
    @BeforeEach
    public void setUp() {
        logger = new Logger(!System.getProperties().containsKey("NDEBUG"));
    }

	@Test
	public void testSingle1() {
		final AoC2015_22.Game.Spells spells = AoC2015_22.Game.setUpSpells();
		final SingleTurnStorage turnStorage = new SingleTurnStorage();
		final Iterator<String> toCast = asList("Poison", "Missile").iterator();
		final AoC2015_22.Game.Fight.SpellSelector spellSelector
				= turn -> Collections.singleton(spells.getByName(toCast.next()));
		final AoC2015_22.Game.Boss boss = new AoC2015_22.Game.Boss(13, 8);
		final AoC2015_22.Game.Player player = new AoC2015_22.Game.Player(10, 250, 0, 0, 0, 0);
		final AoC2015_22.Game.Fight fight = new AoC2015_22.Game.Fight(
		        spells, turnStorage, spellSelector, player, boss, false,
		        logger);

		final long result = fight.run();
		
		assertThat(result).isEqualTo(Long.valueOf(173 + 53));
	}

	@Test
	public void testSingle2() {
		final AoC2015_22.Game.Spells spells = AoC2015_22.Game.setUpSpells();
		final SingleTurnStorage turnStorage = new SingleTurnStorage();
		final Iterator<String> toCast
				= asList("Recharge", "Shield", "Drain", "Poison", "Missile").iterator();
		final AoC2015_22.Game.Fight.SpellSelector spellSelector
				= turn -> Collections.singleton(spells.getByName(toCast.next()));
		final AoC2015_22.Game.Boss boss = new AoC2015_22.Game.Boss(14, 8);
		final AoC2015_22.Game.Player player = new AoC2015_22.Game.Player(10, 250, 0, 0, 0, 0);
		final AoC2015_22.Game.Fight fight = new AoC2015_22.Game.Fight(
		        spells, turnStorage, spellSelector, player, boss, false,
		        logger);
		
		final long result = fight.run();
		
		assertThat(result).isEqualTo(Long.valueOf(229 + 113 + 73 + 173 + 53));
	}
	
	@Test
	public void testLeastCostly1() {
		final AoC2015_22.Game.Spells spells = AoC2015_22.Game.setUpSpells();
		final AoC2015_22.Game.Fight.LeastCostlyFirstTurnStorage turnStorage
				= new AoC2015_22.Game.Fight.LeastCostlyFirstTurnStorage();
		final AoC2015_22.Game.Fight.SpellSelector spellSelector
				= turn -> asList("Poison", "Missile").stream()
							.map(spells::getByName)
							.collect(toSet());
		final AoC2015_22.Game.Boss boss = new AoC2015_22.Game.Boss(13, 8);
		final AoC2015_22.Game.Player player = new AoC2015_22.Game.Player(10, 250, 0, 0, 0, 0);
		final AoC2015_22.Game.Fight fight = new AoC2015_22.Game.Fight(
		        spells, turnStorage, spellSelector, player, boss, false,
		        logger);

		final long result = fight.run();
		
		assertThat(result).isEqualTo(Long.valueOf(173 + 53));
	}
	
	private static final class SingleTurnStorage implements AoC2015_22.Game.Fight.TurnStorage {
		private List<AoC2015_22.Game.Turn> turn;
		
		@Override
		public void push(final AoC2015_22.Game.Turn turn) {
			this.turn = Collections.singletonList(turn);
		}

		@Override
		public AoC2015_22.Game.Turn pop() {
			final AoC2015_22.Game.Turn returnTurn = this.turn.get(0);
			this.turn = Collections.emptyList();
			return returnTurn;
		}
	}
}
