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

import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2015_22 extends SolutionBase<AoC2015_22.Game, Long, Long> {
	
	private AoC2015_22(final boolean debug) {
		super(debug);
	}
	
	public static final AoC2015_22 createDebug() {
		return new AoC2015_22(true);
	}

	public static final AoC2015_22 create() {
		return new AoC2015_22(false);
	}

    @Override
    protected Game parseInput(final List<String> inputs) {
        return Game.fromInput(inputs);
    }

    @Override
    protected Long solvePart1(final Game game) {
        return game.setUpEasyFight(this.debug).run();
    }

    @Override
    protected Long solvePart2(final Game game) {
        return game.setUpHardFight(this.debug).run();
    }

    public static void main(final String[] args) throws Exception {
        AoC2015_22.create().run();
    }

    record Game(Boss boss, Player player, Spells spells) {
        
        public static Game fromInput(final List<String> inputs) {
            return new Game(parse(inputs), setUpPlayer(), setUpSpells());
        }
        
        private static Boss parse(final List<String> inputs) {
            assert inputs.size() == 2;
            final Integer hitpoints
                    = Integer.valueOf(inputs.get(0).substring("Hit Points: ".length()));
            final Integer damage
                    = Integer.valueOf(inputs.get(1).substring("Damage: ".length()));
            return new Boss(hitpoints, damage);
        }
        
        private static Player setUpPlayer() {
            return new Player(50, 500, 0, 0, 0, 0);
        }
        
        static Spells setUpSpells() {
            final Set<Spell> spells = new HashSet<>();
            spells.add(new Spell(
                    "Missile",
                    53,
                    turn -> turn
                        .withManaSpent(turn.manaSpent() + 53)
                        .withBoss(turn.boss()
                                .withHitpoints(turn.boss().hitpoints() - 4))
                        .withPlayer(turn.player()
                                .withMana(turn.player().mana() - 53)),
                    turn -> turn
            ));
            spells.add(new Spell(
                    "Drain",
                    73,
                    turn -> turn
                        .withManaSpent(turn.manaSpent() + 73)
                        .withBoss(turn.boss()
                                .withHitpoints(turn.boss().hitpoints() - 2))
                        .withPlayer(turn.player()
                                .withMana(turn.player().mana() - 73)
                                .withHitpoints(turn.player().hitpoints() + 2)),
                    turn -> turn
            ));
            spells.add(new Spell(
                    "Shield",
                    113,
                    turn -> turn
                        .withManaSpent(turn.manaSpent() + 113)
                        .withPlayer(turn.player()
                                .withShieldTimer(6)
                                .withArmor(7)
                                .withMana(turn.player().mana() - 113)),
                    turn -> {
                        final Integer shieldTimer = turn.player().shieldTimer();
                        assert shieldTimer >= 0 && shieldTimer <= 6;
                        if (shieldTimer > 1) {
                            return turn
                                .withPlayer(turn.player()
                                        .withShieldTimer(shieldTimer - 1));
                        } else {
                            return turn
                                .withPlayer(turn.player()
                                        .withArmor(0)
                                        .withShieldTimer(0));
                        }
                    }
            ));
            spells.add(new Spell(
                    "Poison",
                    173,
                    turn -> turn
                        .withManaSpent(turn.manaSpent() + 173)
                        .withPlayer(turn.player()
                                .withPoisonTimer(6)
                                .withMana(turn.player().mana() - 173)),
                    turn -> {
                        final Integer poisonTimer = turn.player().poisonTimer();
                        assert poisonTimer >= 0 && poisonTimer <= 6;
                        return turn
                            .withBoss(turn.boss()
                                    .withHitpoints(turn.boss().hitpoints() - 3))
                            .withPlayer(turn.player()
                                    .withPoisonTimer(poisonTimer - 1));
                    }
            ));
            spells.add(new Spell(
                    "Recharge",
                    229,
                    turn -> turn
                        .withManaSpent(turn.manaSpent() + 229)
                        .withPlayer(turn.player()
                                .withRechargeTimer(5)
                                .withMana(turn.player().mana() - 229)),
                    turn -> {
                        final Integer rechargeTimer = turn.player().rechargeTimer();
                        assert rechargeTimer >= 0 && rechargeTimer <= 5;
                        return turn
                            .withPlayer(turn.player()
                                    .withMana(turn.player().mana() + 101)
                                    .withRechargeTimer(rechargeTimer - 1));
                    }
            ));
            return new Spells(spells);
        }

        public Fight setUpEasyFight(final boolean debug) {
            return setUpFight(false, debug);
        }
        
        public Fight setUpHardFight(final boolean debug) {
            return setUpFight(true, debug);
        }
        
        private Fight setUpFight(final boolean hard, final boolean debug) {
            return new Fight(
                    this.spells,
                    new Fight.LeastCostlyFirstTurnStorage(),
                    new Fight.AvailableSpellSelector(this.spells),
                    this.player,
                    this.boss,
                    hard,
                    debug);
        }
        
        record Fight(
            Spells spells,
            TurnStorage turnStorage,
            SpellSelector spellSelector,
            Player player,
            Boss boss,
            boolean difficultyHard,
            boolean debug
        ) {
            
            public long run() {
                this.turnStorage.push(new Turn(0, this.boss, this.player));
                while (true) {
                    Turn oldTurn = this.turnStorage.pop();
                    log(() -> "\n-- Player turn --");
                    logTurn(oldTurn);
                    if (this.difficultyHard) {
                        oldTurn = oldTurn.withPlayer(oldTurn.player()
                                    .withHitpoints(oldTurn.player().hitpoints() - 1));
                        if (isGameOver(oldTurn)) {
                            continue;
                        }
                    }
                    oldTurn = applyActiveSpellsEffect(oldTurn);
                    if (isGameOver(oldTurn)) {
                        return oldTurn.manaSpent();
                    }
                    final List<Spell> available = new ArrayList<>(this.spellSelector.select(oldTurn));
                    available.sort(comparing(Spell::cost));
                    for (final Spell spell : available) {
                        log(() -> String.format("Player casts %s.", spell.name()));
                        Turn newTurn = spell.cast.apply(oldTurn);
                        if (isGameOver(newTurn)) {
                            return newTurn.manaSpent();
                        }
                        log(() -> "\n-- Boss turn --");
                        logTurn(newTurn);
                        newTurn = applyActiveSpellsEffect(newTurn);
                        if (isGameOver(newTurn)) {
                            return newTurn.manaSpent();
                        }
                        newTurn = applyBossAttack(newTurn);
                        if (newTurn.player().hitpoints() > 0) {
                            this.turnStorage.push(newTurn);
                        }
                    }
                }
            }

            private Turn applyActiveSpellsEffect(Turn turn) {
                final Integer shieldTimer = turn.player().shieldTimer();
                if (shieldTimer > 0) {
                    turn = this.spells.getByName("Shield").effect.apply(turn);
                    log(() -> String.format("Shield's timer is now %d.", shieldTimer));
                }
                final Integer poisonTimer = turn.player().poisonTimer();
                if (poisonTimer > 0) {
                    turn = this.spells.getByName("Poison").effect().apply(turn);
                    log(() -> String.format("Poison deals 3 damage; its timer is now %d.",
                            poisonTimer));
                }
                final Integer rechargeTimer = turn.player().rechargeTimer();
                if (rechargeTimer > 0) {
                    turn = this.spells.getByName("Recharge").effect().apply(turn);
                    log(() -> String.format("Recharge provides 101 mana; its timer is now %d.",
                                            rechargeTimer));
                }
                return turn;
            }
            
            private Turn applyBossAttack(final Turn turnIn) {
                final int bossDamage = Math.max(turnIn.boss().damage()
                                                    - turnIn.player().armor(),
                                                1);
                log(() -> String.format("Boss attacks for %d damage.", bossDamage));
                return turnIn
                        .withPlayer(turnIn.player()
                                .withHitpoints(turnIn.player().hitpoints() - bossDamage));
            }
            
            private boolean isGameOver(final Turn turn) {
                if (turn.isBossDead()) {
                    log(() -> "Boss is dead. Player wins!!");
                    return TRUE;
                } else if (turn.isPlayerDead()) {
                    log(() -> "Player is dead!!");
                    return TRUE;
                }
                return FALSE;
            }

            protected void log(final Supplier<Object> supplier) {
                if (!debug) {
                    return;
                }
                System.out.println(supplier.get());
            }

            private void logTurn(final Turn playerTurn) {
                log(() -> String.format("- Player has %d hit points, %d armor, %d mana",
                            playerTurn.player().hitpoints(),
                            playerTurn.player().armor(),
                            playerTurn.player().mana()));
                log(() -> String.format("- Boss has %d hit points",
                            playerTurn.boss().hitpoints()));
            }
               
            interface TurnStorage {
                void push(Turn turn);
                Turn pop();
            }
            
            static final class LeastCostlyFirstTurnStorage implements TurnStorage {
                private final PriorityQueue<Turn> turns
                                = new PriorityQueue<>(comparing(Turn::manaSpent));

                @Override
                public void push(final Turn turn) {
                    this.turns.add(turn);
    //	          System.out.println(this.turns.size() + " Turns in storage");
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
            
            static final class AvailableSpellSelector implements SpellSelector {
                private final Spells spells;
                
                public AvailableSpellSelector(final Spells spells) {
                    this.spells = spells;
                }

                @Override
                public Set<Spell> select(final Turn turn) {
                    final Player player = turn.player();
                    return spells.getAll().stream()
                            .filter(s -> !(s.name().equals("Poison")
                                            && player.poisonTimer() > 0))
                            .filter(s -> !(s.name().equals("Recharge")
                                            && player.rechargeTimer() > 0))
                            .filter(s -> !(s.name().equals("Shield")
                                            && player.shieldTimer() > 0))
                            .filter(s -> s.cost() <= player.mana())
                            .collect(toSet());
                }
            }
        }
        
        record Turn(Integer manaSpent, Boss boss, Player player) {
            
            public boolean isBossDead() {
                return this.boss.isDead();
            }
            
            public boolean isPlayerDead() {
                return this.player.isDead();
            }
            
            public Turn withManaSpent(final int manaSpent) {
                return new Turn(manaSpent, this.boss, this.player);
            }
            
            public Turn withBoss(final Boss boss) {
                return new Turn(this.manaSpent, boss, this.player);
            }
            
            public Turn withPlayer(final Player player) {
                return new Turn(this.manaSpent, this.boss, player);
            }
        }
        
        @FunctionalInterface interface SpellEffect {
            Turn apply(Turn turn);
        }
            
        record Spell(
            String name, Integer cost, SpellEffect cast, SpellEffect effect
        ) {}
        
        static final class Spells {
            private final Map<String, Spell> spellsByName;
            
            public Spells(final Set<Spell> spells) {
                this.spellsByName = spells.stream()
                        .collect(toMap(Spell::name, identity()));
            }

            public Spell getByName(final String name) {
                return this.spellsByName.get(name);
            }
            
            public Set<Spell> getAll() {
                return this.spellsByName.values().stream().collect(toSet());
            }
        }
        
        record Player(
            Integer hitpoints,
            Integer mana,
            Integer armor,
            Integer shieldTimer,
            Integer poisonTimer,
            Integer rechargeTimer
        ) {
            public boolean isDead() {
                return this.hitpoints <= 0;
            }
            
            public Player withHitpoints(final int hitpoints) {
                return new Player(hitpoints, this.mana, this.armor,
                    this.shieldTimer, this.poisonTimer, this.rechargeTimer);
            }
        
            public Player withMana(final int mana) {
                return new Player(this.hitpoints, mana, this.armor,
                    this.shieldTimer, this.poisonTimer, this.rechargeTimer);
            }
        
            public Player withArmor(final int armor) {
                return new Player(this.hitpoints, this.mana, armor,
                    this.shieldTimer, this.poisonTimer, this.rechargeTimer);
            }
        
            public Player withShieldTimer(final int shieldTimer) {
                return new Player(this.hitpoints, this.mana, this.armor,
                    shieldTimer, this.poisonTimer, this.rechargeTimer);
            }
        
            public Player withPoisonTimer(final int poisonTimer) {
                return new Player(this.hitpoints, this.mana, this.armor,
                    this.shieldTimer, poisonTimer, this.rechargeTimer);
            }
        
            public Player withRechargeTimer(final int rechargeTimer) {
                return new Player(this.hitpoints, this.mana, this.armor,
                    this.shieldTimer, this.poisonTimer, rechargeTimer);
            }
        }
        
        record Boss(Integer hitpoints, Integer damage) {
            
            public boolean isDead() {
                return this.hitpoints <= 0;
            }
            
            public Boss withHitpoints(final int hitpoints) {
                return new Boss(hitpoints, this.damage);
            }
        }
    }
}