import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.collections4.SetUtils.unmodifiableSet;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;
import static org.apache.commons.math3.util.CombinatoricsUtils.combinationsIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class AoC2015_21 extends AoCBase {

	private final List<String> inputs;

	protected AoC2015_21(String input, boolean debug) {
		super(debug);
		this.inputs = asList((input + "\n").split("\\r?\\n"));
	}

	public static AoC2015_21 create(String input) {
		return new AoC2015_21(input, false);
	}
	
	private Shop setUpShop() {
		final Set<ShopItem> items = new HashSet<>();
		items.add(ShopItem.weapon("Dagger", 8, 4, 0));
		items.add(ShopItem.weapon("Shortsword", 10, 5, 0));
		items.add(ShopItem.weapon("Warhammer", 25, 6, 0));
		items.add(ShopItem.weapon("Longsword", 40, 7, 0));
		items.add(ShopItem.weapon("Greataxe", 74, 8, 0));
		items.add(ShopItem.armor("Leather", 13, 0, 1));
		items.add(ShopItem.armor("Chainmail", 31, 0, 2));
		items.add(ShopItem.armor("Splintmail", 53, 0, 3));
		items.add(ShopItem.armor("Bandedmail", 75, 0, 4));
		items.add(ShopItem.armor("Platemail", 102, 0, 5));
		items.add(ShopItem.ring("Damage +1", 25, 1, 0));
		items.add(ShopItem.ring("Damage +2", 50, 2, 0));
		items.add(ShopItem.ring("Damage +3", 100, 3, 0));
		items.add(ShopItem.ring("Defense +1", 20, 0, 1));
		items.add(ShopItem.ring("Defense +2", 40, 0, 2));
		items.add(ShopItem.ring("Defense +3", 80, 0, 3));
		assert items.size() == 16;
		return new Shop(items);
	}
	
	private List<PlayerConfig> collectAllPlayerConfigs(Shop shop,
													   Integer hitPoints) {
		final ArrayList<PlayerConfig> configs = new ArrayList<>();
		for (final ShopItem weapon : shop.getWeapons()) {
			for (final ShopItem armor : shop.getArmor()) {
				for (final Set<ShopItem> rings : shop.getRings()) {
					final Set<ShopItem> items = new HashSet<>();
					items.add(weapon);
					if (armor != ShopItem.NONE) {
						items.add(armor);
					}
					items.addAll(rings);
					configs.add(new PlayerConfig(hitPoints, items));
				}
			}
		}
		assert configs.size() == 5 * (5 + 1) * (15 + 6 + 1);
		return configs;
	}

	@Override
	public long solvePart1() {
		final Shop shop = setUpShop();
		log(shop);
		final List<PlayerConfig> playerConfigs = collectAllPlayerConfigs(shop, 100);
		playerConfigs.sort(comparing(PlayerConfig::getTotalCost));
		log(playerConfigs);
		for (final PlayerConfig playerConfig : playerConfigs) {
			if (playerConfig.getTotalDamage() - 2 >
					7 - playerConfig.getTotalArmor()) {
				return playerConfig.getTotalCost();
			}
			
		}
		throw new IllegalStateException("Unsolvable");
	}

	@Override
	public long solvePart2() {
		final Shop shop = setUpShop();
		log(shop);
		final List<PlayerConfig> playerConfigs = collectAllPlayerConfigs(shop, 100);
		playerConfigs.sort(comparing(PlayerConfig::getTotalCost).reversed());
		log(playerConfigs);
		for (final PlayerConfig playerConfig : playerConfigs) {
			if (!(playerConfig.getTotalDamage() - 2 >
					7 - playerConfig.getTotalArmor())) {
				return playerConfig.getTotalCost();
			}
			
		}
		throw new IllegalStateException("Unsolvable");
	}

	public static void main(String[] args) throws Exception {
		lap("Part 1", () -> AoC2015_21.create(INPUT).solvePart1());
		lap("Part 2", () -> AoC2015_21.create(INPUT).solvePart2());
	}
	
	private static final String INPUT =
			"Hit Points: 100\r\n" +
			"Damage: 8\r\n" +
			"Armor: 2";
	
	private static final class ShopItem {
		private enum Type { WEAPON, ARMOR, RING, NONE }
		
		private final Type type;
		private final String name;
		private final Integer cost;
		private final Integer damage;
		private final Integer armor;

		private ShopItem(Type type, String name, Integer cost,
						 Integer damage, Integer armor) {
			this.type = type;
			this.name = name;
			this.cost = cost;
			this.damage = damage;
			this.armor = armor;
		}
		
		public static ShopItem weapon(String name, Integer cost,
						 			  Integer damage, Integer armor) {
			return new ShopItem(Type.WEAPON, name, cost, damage, armor);
		}
		
		public static ShopItem armor(String name, Integer cost,
						 			 Integer damage, Integer armor) {
			return new ShopItem(Type.ARMOR, name, cost, damage, armor);
		}
		
		public static ShopItem ring(String name, Integer cost,
									Integer damage, Integer armor) {
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

		public String getName() {
			return name;
		}

		public Integer getCost() {
			return cost;
		}

		public Integer getDamage() {
			return damage;
		}

		public Integer getArmor() {
			return armor;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final ShopItem other = (ShopItem) obj;
			return new EqualsBuilder()
					.append(this.name, other.name)
					.append(this.type, other.type)
				.isEquals();
		}

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();
			builder.append("ShopItem [name=").append(name)
					.append(", type=").append(type)
					.append(", cost=").append(cost)
					.append(", damage=").append(damage)
					.append(", armor=").append(armor)
					.append("]");
			return builder.toString();
		}
	}
	
	private static final class Shop {
		private final Set<ShopItem> items;

		public Shop(Set<ShopItem> items) {
			this.items = items;
		}
		
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
			combinationsIterator(rings.size(), 2).forEachRemaining(indices -> {
				ringCombinations.add(	unmodifiableSet(rings.get(indices[0]),
														rings.get(indices[1])));
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


		@Override
		public String toString() {
			return "Shop [items=" + items + "]";
		}
	}
	
	private static final class PlayerConfig {
		private final Integer hitPoints;
		private final Set<ShopItem> items;

		public PlayerConfig(Integer hitPoints, Set<ShopItem> items) {
			this.hitPoints = hitPoints;
			this.items = items;
		}
		
		public int getHitPoints() {
			return this.hitPoints;
		}

		public int getTotalCost() {
			return this.items.stream().collect(summingInt(ShopItem::getCost));
		}
		
		public int getTotalDamage() {
			return this.items.stream().collect(summingInt(ShopItem::getDamage));
		}
		
		public int getTotalArmor() {
			return this.items.stream().collect(summingInt(ShopItem::getArmor));
		}

		@Override
		public String toString() {
			final ToStringBuilder tsb
					= new ToStringBuilder(this, SHORT_PREFIX_STYLE);
			this.items.stream()
					.forEach(i -> tsb.append(i.getName()));
			tsb.append("totalCost", this.getTotalCost());
			return tsb.toString();
		}
	}
}
