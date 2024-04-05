import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.github.pareronia.aoc.MutableBoolean;
import com.github.pareronia.aoc.SetUtils;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.geometry3d.Cuboid;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2023_22
        extends SolutionBase<AoC2023_22.Stack, Integer, Integer> {
    
    private AoC2023_22(final boolean debug) {
        super(debug);
    }
    
    public static AoC2023_22 create() {
        return new AoC2023_22(false);
    }
    
    public static AoC2023_22 createDebug() {
        return new AoC2023_22(true);
    }
    
    @Override
    protected Stack parseInput(final List<String> inputs) {
        return Stack.fromInput(inputs);
    }

    @Override
    public Integer solvePart1(final Stack stack) {
        return stack.getDeletable().size();
    }
    
    @Override
    public Integer solvePart2(final Stack stack) {
        return stack.getNotDeletable().stream()
            .map(stack::delete)
            .mapToInt(Set::size)
            .sum();
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "5"),
        @Sample(method = "part2", input = TEST, expected = "7"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2023_22.create().run();
    }
    
    static final class Stack {
        private final Set<Cuboid> bricks;
        private final Map<Cuboid, Set<Cuboid>> supportees;
        private final Map<Cuboid, Set<Cuboid>> supporters;
        private final Map<Integer, List<Cuboid>> bricksByZ1;
        private final Map<Integer, List<Cuboid>> bricksByZ2;
        
        protected Stack(final Set<Cuboid> bricks) {
            this.bricks = bricks;
            this.bricksByZ1 = this.bricks.stream()
                    .collect(groupingBy(Cuboid::getZ1));
            this.bricksByZ2 = this.bricks.stream()
                    .collect(groupingBy(Cuboid::getZ2));
            this.stack();
            this.supportees = this.bricks.stream()
                .collect(toMap(
                    brick -> brick,
                    brick -> this.getBricksByZ1(brick.getZ2() + 1).stream()
                                .filter(b -> Stack.overlapsXY(b, brick))
                                .collect(toSet())));
            this.supporters = this.bricks.stream()
                .collect(toMap(
                    brick -> brick,
                    brick -> this.getBricksByZ2(brick.getZ1() - 1).stream()
                                .filter(b -> Stack.overlapsXY(b, brick))
                                .collect(toSet())));
        }

        public static Stack fromInput(final List<String> inputs) {
            final Set<Cuboid> bricks = new HashSet<>();
            for (final String line : inputs) {
                final StringSplit split = StringOps.splitOnce(line, "~");
                final String[] xyz1 = split.left().split(",");
                final String[] xyz2 = split.right().split(",");
                bricks.add(Cuboid.of(
                    Integer.parseInt(xyz1[0]),
                    Integer.parseInt(xyz2[0]),
                    Integer.parseInt(xyz1[1]),
                    Integer.parseInt(xyz2[1]),
                    Integer.parseInt(xyz1[2]),
                    Integer.parseInt(xyz2[2])
                ));
            }
            return new Stack(bricks);
        }

        public List<Cuboid> getBricksByZ1(final int z1) {
            return this.bricksByZ1.getOrDefault(z1, new ArrayList<>());
        }

        public List<Cuboid> getBricksByZ2(final int z2) {
            return this.bricksByZ2.getOrDefault(z2, new ArrayList<>());
        }

        public Set<Position> getViewY() {
          return this.bricks.stream()
              .flatMap(Cuboid::positions)
              .map(p -> Position.of(p.getX(), p.getZ()))
              .collect(toSet());
        }
        
        private static Cuboid moveToZ(final Cuboid block, final int dz) {
            return new Cuboid(
                block.getX1(), block.getX2(),
                block.getY1(), block.getY2(),
                block.getZ1() + dz, block.getZ2() + dz
            );
        }
        
        private static boolean overlapsXY(final Cuboid lhs, final Cuboid rhs) {
            return Cuboid.overlapX(lhs, rhs) && Cuboid.overlapY(lhs, rhs);
        }
        
        private static boolean overlapsXY(
                final Collection<Cuboid> cuboids,
                final Cuboid brick
        ) {
            return cuboids.stream().anyMatch(c -> Stack.overlapsXY(c, brick));
        }
        
        private void stack() {
            final MutableBoolean moved = new MutableBoolean(true);
            final Predicate<Cuboid> isNotSupported = brick -> !overlapsXY(
                this.bricksByZ2.getOrDefault(brick.getZ1() - 1, List.of()),
                brick);
            final Consumer<Cuboid> moveDown = brick -> {
                final Cuboid movedBrick = moveToZ(brick, -1);
                this.getBricksByZ1(brick.getZ1()).remove(brick);
                this.getBricksByZ2(brick.getZ2()).remove(brick);
                this.bricksByZ1.computeIfAbsent(
                        movedBrick.getZ1(), k -> new ArrayList<>())
                    .add(movedBrick);
                this.bricksByZ2.computeIfAbsent(
                        movedBrick.getZ2(), k -> new ArrayList<>())
                    .add(movedBrick);
                moved.setTrue();
            };
            while (moved.isTrue()) {
                moved.setFalse();
                this.bricksByZ1.keySet().stream()
                    .sorted()
                    .filter(z -> z > 1)
                    .forEach(z -> {
                        new ArrayList<>(this.getBricksByZ1(z)).stream()
                            .filter(isNotSupported)
                            .forEach(moveDown);
                    });
            }
            this.bricks.clear();
            bricksByZ2.values().stream().forEach(this.bricks::addAll);
        }
        
        public List<String> display() {
            return Stack.displayBricks(bricks);
        }
        
        public static List<String> displayBricks(final Collection<Cuboid> bricks) {
            return bricks.stream()
                .map(Stack::displayBrick)
                .toList();
        }
        
        public static String displayBrick(final Cuboid brick) {
            return "%d,%d,%d->%d,%d,%d".formatted(
                brick.getX1(), brick.getY1(), brick.getZ1(),
                brick.getX2(), brick.getY2(), brick.getZ2()
            );
        }
        
        public Set<Cuboid> getDeletable() {
            return this.bricks.stream()
                .filter(this::isNotSingleSupporter)
                .collect(toSet());
        }

        private boolean isNotSingleSupporter(final Cuboid brick) {
            return this.supportees.get(brick).stream()
                .map(this.supporters::get)
                .noneMatch(Set.of(brick)::equals);
        }
        
        public Set<Cuboid> getNotDeletable() {
            return SetUtils.disjunction(this.bricks, this.getDeletable());
        }
        
        public Set<Cuboid> delete(final Cuboid brick) {
            final Deque<Cuboid> q = new ArrayDeque<>();
            final Set<Cuboid> falling = new HashSet<>();
            falling.add(brick);
            q.add(brick);
            while (!q.isEmpty()) {
                final Cuboid b = q.poll();
                for (final Cuboid s : this.supportees.get(b)) {
                    if (this.supporters.get(s).stream()
                                .allMatch(falling::contains)) {
                        if (!falling.contains(s)) {
                            q.add(s);
                        }
                        falling.add(s);
                    }
                }
            }
            falling.remove(brick);
            return falling;
        }
    }

    private static final String TEST = """
            1,0,1~1,2,1
            0,0,2~2,0,2
            0,2,3~2,2,3
            0,0,4~0,2,4
            2,0,5~2,2,5
            0,1,6~2,1,6
            1,1,8~1,1,9
            """;
}
