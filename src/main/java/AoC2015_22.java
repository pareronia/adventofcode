import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Supplier;

import com.github.pareronia.aocd.Aocd;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;
import lombok.With;

public class AoC2015_22 extends AoCBase {
	private final Fight fight;
	
	private AoC2015_22(List<String> input, boolean debug) {
		super(debug);
		final Boss boss = parse(input);
		final Player player = setUpPlayer();
		final Spells spells = setUpSpells();
		this.fight = setUpFight(spells, player, boss);
	}
	
	public static final AoC2015_22 createDebug(List<String> input) {
		return new AoC2015_22(input, true);
	}

	public static final AoC2015_22 create(List<String> input) {
		return new AoC2015_22(input, false);
	}

	private final Boss parse(List<String> inputs) {
		assert inputs.size() == 2;
		final Integer hitpoints
				= Integer.valueOf(inputs.get(0).substring("Hit Points: ".length()));
		final Integer damage
				= Integer.valueOf(inputs.get(1).substring("Damage: ".length()));
		return new Boss(hitpoints, damage);
	}
	
	private final Player setUpPlayer() {
		return new Player(50, 500, 0, 0, 0, 0);
	}
	
	static final Spells setUpSpells() {
		final Set<Spell> spells = new HashSet<>();
		spells.add(new Spell(
				"Missile",
				53,
				turn -> turn
					.withManaSpent(turn.getManaSpent() + 53)
					.withBoss(turn.getBoss()
							.withHitpoints(turn.getBoss().getHitpoints() - 4))
					.withPlayer(turn.getPlayer()
							.withMana(turn.getPlayer().getMana() - 53)),
				turn -> turn
		));
		spells.add(new Spell(
				"Drain",
				73,
				turn -> turn
					.withManaSpent(turn.getManaSpent() + 73)
					.withBoss(turn.getBoss()
							.withHitpoints(turn.getBoss().getHitpoints() - 2))
					.withPlayer(turn.getPlayer()
							.withMana(turn.getPlayer().getMana() - 73)
							.withHitpoints(turn.getPlayer().getHitpoints() + 2)),
				turn -> turn
		));
		spells.add(new Spell(
				"Shield",
				113,
				turn -> turn
					.withManaSpent(turn.getManaSpent() + 113)
					.withPlayer(turn.getPlayer()
							.withShieldTimer(6)
							.withArmor(7)
							.withMana(turn.getPlayer().getMana() - 113)),
				turn -> {
					final Integer shieldTimer = turn.getPlayer().getShieldTimer();
					assert shieldTimer >= 0 && shieldTimer <= 6;
					if (shieldTimer > 1) {
						return turn
							.withPlayer(turn.getPlayer()
									.withShieldTimer(shieldTimer - 1));
					} else {
						return turn
							.withPlayer(turn.getPlayer()
									.withArmor(0)
									.withShieldTimer(0));
					}
				}
		));
		spells.add(new Spell(
				"Poison",
				173,
				turn -> turn
					.withManaSpent(turn.getManaSpent() + 173)
					.withPlayer(turn.getPlayer()
							.withPoisonTimer(6)
							.withMana(turn.getPlayer().getMana() - 173)),
				turn -> {
					final Integer poisonTimer = turn.getPlayer().getPoisonTimer();
					assert poisonTimer >= 0 && poisonTimer <= 6;
					return turn
						.withBoss(turn.getBoss()
								.withHitpoints(turn.getBoss().getHitpoints() - 3))
						.withPlayer(turn.getPlayer()
								.withPoisonTimer(poisonTimer - 1));
				}
		));
		spells.add(new Spell(
				"Recharge",
				229,
				turn -> turn
					.withManaSpent(turn.getManaSpent() + 229)
					.withPlayer(turn.getPlayer()
							.withRechargeTimer(5)
							.withMana(turn.getPlayer().getMana() - 229)),
				turn -> {
					final Integer rechargeTimer = turn.getPlayer().getRechargeTimer();
					assert rechargeTimer >= 0 && rechargeTimer <= 5;
					return turn
						.withPlayer(turn.getPlayer()
								.withMana(turn.getPlayer().getMana() + 101)
								.withRechargeTimer(rechargeTimer - 1));
				}
		));
		return new Spells(spells);
	}

	private Fight setUpFight(Spells spells, Player player, Boss boss) {
		final LeastCostlyFirstTurnStorage turnStorage = new LeastCostlyFirstTurnStorage();
		final AvailableSpellSelector spellSelector = new AvailableSpellSelector(spells);
		final Fight fight = new Fight(spells, turnStorage, spellSelector, player, boss);
		fight.setDebug(this.debug);
		return fight;
	}
	
	@Override
	public long solvePart1() {
		return this.fight.run();
	}

	@Override
	public long solvePart2() {
		this.fight.setDifficultyHard(true);
		return fight.run();
	}

	public static void main(String[] args) throws Exception {
		final List<String> input = Aocd.getData(2015, 22);
		lap("Part 1", () -> AoC2015_22.create(input).solvePart1());
		lap("Part 2", () -> AoC2015_22.create(input).solvePart2());
	}
	
	@RequiredArgsConstructor
	static final class Fight {
		private final Spells spells;
		private final TurnStorage turnStorage;
		private final SpellSelector spellSelector;
		private final Player player;
		private final Boss boss;
		private boolean difficultyHard = false;
		private boolean debug = false;
		
		public void setDebug(boolean debug) {
			this.debug = debug;
		}

		public void setDifficultyHard(boolean difficultyHard) {
			this.difficultyHard = difficultyHard;
		}
		
		long run() {
			this.turnStorage.push(new Turn(0, this.boss, this.player));
			while (true) {
				Turn oldTurn = this.turnStorage.pop();
				log(() -> "\n-- Player turn --");
				logTurn(oldTurn);
				if (this.difficultyHard) {
					oldTurn = oldTurn.withPlayer(oldTurn.getPlayer()
								.withHitpoints(oldTurn.getPlayer().getHitpoints() - 1));
					if (isGameOver(oldTurn)) {
						continue;
					}
				}
				oldTurn = applyActiveSpellsEffect(oldTurn);
				if (isGameOver(oldTurn)) {
					return oldTurn.getManaSpent();
				}
				final List<Spell> available = new ArrayList<>(this.spellSelector.select(oldTurn));
				available.sort(comparing(Spell::getCost));
				for (final Spell spell : available) {
					log(() -> String.format("Player casts %s.", spell.getName()));
					Turn newTurn = spell.cast.apply(oldTurn);
					if (isGameOver(newTurn)) {
						return newTurn.getManaSpent();
					}
					log(() -> "\n-- Boss turn --");
					logTurn(newTurn);
					newTurn = applyActiveSpellsEffect(newTurn);
					if (isGameOver(newTurn)) {
						return newTurn.getManaSpent();
					}
					newTurn = applyBossAttack(newTurn);
					if (newTurn.getPlayer().getHitpoints() > 0) {
						this.turnStorage.push(newTurn);
					}
				}
			}
		}

		private Turn applyActiveSpellsEffect(Turn turn) {
			final Integer shieldTimer = turn.getPlayer().getShieldTimer();
			if (shieldTimer > 0) {
				turn = this.spells.getByName("Shield").effect.apply(turn);
				log(() -> String.format("Shield's timer is now %d.", shieldTimer));
			}
			final Integer poisonTimer = turn.getPlayer().getPoisonTimer();
			if (poisonTimer > 0) {
				turn = this.spells.getByName("Poison").effect.apply(turn);
				log(() -> String.format("Poison deals 3 damage; its timer is now %d.",
						poisonTimer));
			}
			final Integer rechargeTimer = turn.getPlayer().getRechargeTimer();
			if (rechargeTimer > 0) {
				turn = this.spells.getByName("Recharge").effect.apply(turn);
				log(() -> String.format("Recharge provides 101 mana; its timer is now %d.",
										rechargeTimer));
			}
			return turn;
		}
		
		private Turn applyBossAttack(Turn turnIn) {
			final int bossDamage = Math.max(turnIn.getBoss().getDamage()
												- turnIn.getPlayer().getArmor(),
											1);
			log(() -> String.format("Boss attacks for %d damage.", bossDamage));
			return turnIn
					.withPlayer(turnIn.getPlayer()
							.withHitpoints(turnIn.getPlayer().getHitpoints() - bossDamage));
		}
		
		private boolean isGameOver(AoC2015_22.Turn turn) {
			if (turn.isBossDead()) {
				log(() -> "Boss is dead. Player wins!!");
				return TRUE;
			} else if (turn.isPlayerDead()) {
				log(() -> "Player is dead!!");
				return TRUE;
			}
			return FALSE;
		}

		protected void log(Supplier<Object> supplier) {
			if (!debug) {
				return;
			}
			System.out.println(supplier.get());
		}

		private void logTurn(final Turn playerTurn) {
			log(() -> String.format("- Player has %d hit points, %d armor, %d mana",
						playerTurn.getPlayer().getHitpoints(),
						playerTurn.getPlayer().getArmor(),
						playerTurn.getPlayer().getMana()));
			log(() -> String.format("- Boss has %d hit points",
						playerTurn.getBoss().getHitpoints()));
		}
	}
	
	interface TurnStorage {
		void push(Turn turn);
		Turn pop();
	}
	
	final static class LeastCostlyFirstTurnStorage implements TurnStorage {
		private final PriorityQueue<Turn> turns
						= new PriorityQueue<>(comparing(Turn::getManaSpent));

		@Override
		public void push(Turn turn) {
			this.turns.add(turn);
//			System.out.println(this.turns.size() + " Turns in storage");
		}

		@Override
		public Turn pop() {
			return this.turns.poll();
		}
	}
	
	@FunctionalInterface
	interface SpellSelector {
		Set<Spell> select(Turn turn);
	}
	
	@RequiredArgsConstructor
	private final static class AvailableSpellSelector implements SpellSelector {
		private final Spells spells;
		
		@Override
		public Set<Spell> select(Turn turn) {
			final Player player = turn.getPlayer();
			return spells.getAll().stream()
					.filter(s -> !(s.getName().equals("Poison")
									&& player.getPoisonTimer() > 0))
					.filter(s -> !(s.getName().equals("Recharge")
									&& player.getRechargeTimer() > 0))
					.filter(s -> !(s.getName().equals("Shield")
									&& player.getShieldTimer() > 0))
					.filter(s -> s.getCost() <= player.getMana())
					.collect(toSet());
		}
	}
	
	@RequiredArgsConstructor
	@Getter
	@ToString(callSuper = true)
	static final class Turn {
		@With private final Integer manaSpent;
		@With private final Boss boss;
		@With private final Player player;
		
		public boolean isBossDead() {
			return this.boss.isDead();
		}
		
		public boolean isPlayerDead() {
			return this.player.isDead();
		}
	}
	
	@FunctionalInterface
	private interface SpellEffect {
		Turn apply(Turn turn);
	}
		
	@Value
	static final class Spell {
		private final String name;
		private final Integer cost;
		private final SpellEffect cast;
		private final SpellEffect effect;
	}
	
	static final class Spells {
		private final Map<String, Spell> spellsByName;
		
		public Spells(Set<Spell> spells) {
			this.spellsByName = spells.stream()
					.collect(toMap(Spell::getName, identity()));
		}

		public Spell getByName(@NonNull String name) {
			return this.spellsByName.get(name);
		}
		
		public Set<Spell> getAll() {
			return this.spellsByName.values().stream().collect(toSet());
		}
	}
	
	@RequiredArgsConstructor
	@Getter
	@ToString(callSuper = true)
	static final class Player {
		@With private final Integer hitpoints;
		@With private final Integer mana;
		@With private final Integer armor;
		@With private final Integer shieldTimer;
		@With private final Integer poisonTimer;
		@With private final Integer rechargeTimer;
		
		public boolean isDead() {
			return this.hitpoints <= 0;
		}
	}
	
	@RequiredArgsConstructor
	@Getter
	@ToString(callSuper = true)
	static final class Boss {
		@With private final Integer hitpoints;
		private final Integer damage;
		
		public boolean isDead() {
			return this.hitpoints <= 0;
		}
	}
}
