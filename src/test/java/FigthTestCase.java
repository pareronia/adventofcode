import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class FigthTestCase {

	@Test
	public void testSingle1() {
		final AoC2015_22.Spells spells = AoC2015_22.setUpSpells();
		final SingleTurnStorage turnStorage = new SingleTurnStorage();
		final Iterator<String> toCast = asList("Poison", "Missile").iterator();
		final AoC2015_22.SpellSelector spellSelector
				= turn -> Collections.singleton(spells.getByName(toCast.next()));
		final AoC2015_22.Boss boss = new AoC2015_22.Boss(13, 8);
		final AoC2015_22.Player player = new AoC2015_22.Player(10, 250, 0, 0, 0, 0);
		final AoC2015_22.Fight fight
				= new AoC2015_22.Fight(spells, turnStorage, spellSelector, player, boss);
		fight.setDebug(true);

		final long result = fight.run();
		
		assertThat(result, is(Long.valueOf(173 + 53)));
	}

	@Test
	public void testSingle2() {
		final AoC2015_22.Spells spells = AoC2015_22.setUpSpells();
		final SingleTurnStorage turnStorage = new SingleTurnStorage();
		final Iterator<String> toCast
				= asList("Recharge", "Shield", "Drain", "Poison", "Missile").iterator();
		final AoC2015_22.SpellSelector spellSelector
				= turn -> Collections.singleton(spells.getByName(toCast.next()));
		final AoC2015_22.Boss boss = new AoC2015_22.Boss(14, 8);
		final AoC2015_22.Player player = new AoC2015_22.Player(10, 250, 0, 0, 0, 0);
		final AoC2015_22.Fight fight
				= new AoC2015_22.Fight(spells, turnStorage, spellSelector, player, boss);
		fight.setDebug(true);
		
		final long result = fight.run();
		
		assertThat(result, is(Long.valueOf(229 + 113 + 73 + 173 + 53)));
	}
	
	@Test
	public void testLeastCostly1() {
		final AoC2015_22.Spells spells = AoC2015_22.setUpSpells();
		final AoC2015_22.LeastCostlyFirstTurnStorage turnStorage
				= new AoC2015_22.LeastCostlyFirstTurnStorage();
		final AoC2015_22.SpellSelector spellSelector
				= turn -> asList("Poison", "Missile").stream()
							.map(spells::getByName)
							.collect(toSet());
		final AoC2015_22.Boss boss = new AoC2015_22.Boss(13, 8);
		final AoC2015_22.Player player = new AoC2015_22.Player(10, 250, 0, 0, 0, 0);
		final AoC2015_22.Fight fight
				= new AoC2015_22.Fight(spells, turnStorage, spellSelector, player, boss);
		fight.setDebug(true);

		final long result = fight.run();
		
		assertThat(result, is(Long.valueOf(173 + 53)));
	}
	
	private static final class SingleTurnStorage implements AoC2015_22.TurnStorage {
		private List<AoC2015_22.Turn> turn;
		
		@Override
		public void push(AoC2015_22.Turn turn) {
			this.turn = Collections.singletonList(turn);
		}

		@Override
		public AoC2015_22.Turn pop() {
			final AoC2015_22.Turn returnTurn = this.turn.get(0);
			this.turn = Collections.emptyList();
			return returnTurn;
		}
	}
}
