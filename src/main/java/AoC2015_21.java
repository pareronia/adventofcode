import static com.github.pareronia.aoc.IterTools.combinations;
import static java.util.Collections.singleton;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import com.github.pareronia.aoc.SetUtils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2015_21 extends AoCBase {
	
	private final Boss boss;
	private final List<PlayerConfig> playerConfigs;

	protected AoC2015_21(final List<String> input, final boolean debug) {
		super(debug);
		this.boss = parse(input);
		this.playerConfigs = setUpPlayerConfigs(setUpShop());
	}

	public static AoC2015_21 create(final List<String> input) {
		return new AoC2015_21(input, false);
	}
	
	public static AoC2015_21 createDebug(final List<String> input) {
	    return new AoC2015_21(input, true);
	}
	
	private final Boss parse(final List<String> inputs) {
		assert inputs.size() == 3;
		final Integer hitPoints
				= Integer.valueOf(inputs.get(0).substring("Hit Points: ".length()));
		final Integer damage
				= Integer.valueOf(inputs.get(1).substring("Damage: ".length()));
		final Integer armor
				= Integer.valueOf(inputs.get(2).substring("Armor: ".length()));
		return new Boss(hitPoints, damage, armor);
	}
	
	private Shop setUpShop() {
		final Set<ShopItem> items = new HashSet<>();
		items.add(ShopItem.weapon("Dagger", 8, 4));
		items.add(ShopItem.weapon("Shortsword", 10, 5));
		items.add(ShopItem.weapon("Warhammer", 25, 6));
		items.add(ShopItem.weapon("Longsword", 40, 7));
		items.add(ShopItem.weapon("Greataxe", 74, 8));
		items.add(ShopItem.armor("Leather", 13, 1));
		items.add(ShopItem.armor("Chainmail", 31, 2));
		items.add(ShopItem.armor("Splintmail", 53, 3));
		items.add(ShopItem.armor("Bandedmail", 75, 4));
		items.add(ShopItem.armor("Platemail", 102, 5));
		items.add(ShopItem.ring("Damage +1", 25, 1, 0));
		items.add(ShopItem.ring("Damage +2", 50, 2, 0));
		items.add(ShopItem.ring("Damage +3", 100, 3, 0));
		items.add(ShopItem.ring("Defense +1", 20, 0, 1));
		items.add(ShopItem.ring("Defense +2", 40, 0, 2));
		items.add(ShopItem.ring("Defense +3", 80, 0, 3));
		assert items.size() == 16;
		return new Shop(items);
	}
	
	private List<PlayerConfig> setUpPlayerConfigs(final Shop shop) {
		final List<PlayerConfig> configs = new ArrayList<>();
		for (final ShopItem weapon : shop.getWeapons()) {
			for (final ShopItem armor : shop.getArmor()) {
				for (final Set<ShopItem> rings : shop.getRings()) {
					final Set<ShopItem> items = new HashSet<>();
					items.add(weapon);
					if (armor != ShopItem.NONE) {
						items.add(armor);
					}
					items.addAll(rings);
					configs.add(new PlayerConfig(items));
				}
			}
		}
		assert configs.size() == 5 * (5 + 1) * (15 + 6 + 1);
		return configs;
	}

	private Predicate<PlayerConfig> winsFromBoss() {
		return playerConfig -> {
		    int playerHP = playerConfig.getHitPoints();
		    int bossHP = boss.getHitPoints();
		    while (true) {
		        bossHP -= Math.max(playerConfig.getTotalDamage() - boss.getArmor(), 1);
		        if (bossHP <= 0) {
		            return true;
		        }
		        playerHP -= Math.max(boss.getDamage() - playerConfig.getTotalArmor(), 1);
		        if (playerHP <= 0) {
		            return false;
		        }
		    }
		};
	}
	
	private int solve(	final Comparator<PlayerConfig> comparator,
						final Predicate<PlayerConfig> filter) {
		return this.playerConfigs.stream()
			.sorted(comparator)
			.filter(filter)
			.findFirst()
			.map(PlayerConfig::getTotalCost)
			.orElseThrow(() -> new IllegalStateException("Unsolvable"));
	}

	@Override
	public Integer solvePart1() {
		return solve(	comparing(PlayerConfig::getTotalCost),
						winsFromBoss());
	}

	@Override
	public Integer solvePart2() {
		return solve(	comparing(PlayerConfig::getTotalCost).reversed(),
						winsFromBoss().negate());
	}

	public static void main(final String[] args) throws Exception {
        final Puzzle puzzle = Aocd.puzzle(2015, 21);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2015_21.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2015_21.create(inputData)::solvePart2)
        );
	}
	
	@RequiredArgsConstructor
	@Getter
	@EqualsAndHashCode(onlyExplicitlyIncluded = true)
	@ToString
	private static final class ShopItem {
		private enum Type { WEAPON, ARMOR, RING, NONE }
		
		@EqualsAndHashCode.Include
		private final Type type;
		@EqualsAndHashCode.Include
		private final String name;
		private final Integer cost;
		private final Integer damage;
		private final Integer armor;

		public static ShopItem weapon(final String name, final Integer cost, final Integer damage) {
			return new ShopItem(Type.WEAPON, name, cost, damage, 0);
		}
		
		public static ShopItem armor(final String name, final Integer cost, final Integer armor) {
			return new ShopItem(Type.ARMOR, name, cost, 0, armor);
		}
		
		public static ShopItem ring(final String name, final Integer cost,
									final Integer damage, final Integer armor) {
			return new ShopItem(Type.RING, name, cost, damage, armor);
		}
		
		public static final ShopItem NONE = new ShopItem(Type.NONE, "", 0, 0, 0);
		
		public boolean isWeapon() {
			return this.type == Type.WEAPON;
		}

		public boolean isArmor() {
			return this.type == Type.ARMOR;
		}

		public boolean isRing() {
			return this.type == Type.RING;
		}
	}
	
	@RequiredArgsConstructor
	@ToString
	private static final class Shop {
		private final Set<ShopItem> items;

		public Set<ShopItem> getWeapons() {
			return this.items.stream()
					.filter(ShopItem::isWeapon)
					.collect(toSet());
		}
		
		public Set<ShopItem> getArmor() {
			return SetUtils.union(
					singleton(ShopItem.NONE),
					this.items.stream()
						.filter(ShopItem::isArmor)
						.collect(toSet()));
		}
		
		public Set<Set<ShopItem>> getRings() {
			final List<ShopItem> rings = this.items.stream()
					.filter(ShopItem::isRing)
					.collect(toList());
			final Set<Set<ShopItem>> ringCombinations = new HashSet<>();
			combinations(rings.size(), 2).forEach(indices -> {
				ringCombinations.add(
				    Set.of(rings.get(indices[0]), rings.get(indices[1])));
			});
			final Set<Set<ShopItem>> ringSingletons = rings.stream()
					.map(Collections::singleton)
					.collect(toSet());
			final Set<Set<ShopItem>> result = new HashSet<>();
			result.add(new HashSet<>());
			result.addAll(ringSingletons);
			result.addAll(ringCombinations);
			return result;
		}
	}
	
	@RequiredArgsConstructor
	@ToString
	private static final class PlayerConfig {
	    @Getter
	    private final int hitPoints = 100;
		private final Set<ShopItem> items;

		@ToString.Include
		public int getTotalCost() {
			return this.items.stream().collect(summingInt(ShopItem::getCost));
		}
		
		public int getTotalDamage() {
			return this.items.stream().collect(summingInt(ShopItem::getDamage));
		}
		
		public int getTotalArmor() {
			return this.items.stream().collect(summingInt(ShopItem::getArmor));
		}
	}
	
	@RequiredArgsConstructor
	@Getter
	private static final class Boss {
	    private final Integer hitPoints;
		private final Integer damage;
		private final Integer armor;
	}
}
