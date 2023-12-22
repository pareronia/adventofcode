import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pareronia.aoc.MutableBoolean;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.geometry.Draw;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.geometry3d.Cuboid;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import lombok.RequiredArgsConstructor;

public final class AoC2023_22
        extends SolutionBase<List<Cuboid>, Integer, Integer> {
    
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
    protected List<Cuboid> parseInput(final List<String> inputs) {
        final ArrayList<Cuboid> bricks = new ArrayList<>();
        for (final String line : inputs) {
            final StringSplit split = StringOps.splitOnce(line, "~");
            final String[] xyz1 = split.left().split(",");
            final String[] xyz2 = split.right().split(",");
            bricks.add(new Cuboid(
                Integer.parseInt(xyz1[0]),
                Integer.parseInt(xyz2[0]),
                Integer.parseInt(xyz1[1]),
                Integer.parseInt(xyz2[1]),
                Integer.parseInt(xyz1[2]),
                Integer.parseInt(xyz2[2])
            ));
        }
        return bricks;
    }

    @Override
    public Integer solvePart1(final List<Cuboid> bricks) {
        final Stack stack = new Stack(bricks);
        stack.sort();
        stack.display().forEach(this::log);
        log("");
        Draw.draw(stack.getViewY(), '#', '.').forEach(this::log);
        stack.stack();
        stack.sort();
        log("");
        Draw.draw(stack.getViewY(), '#', '.').forEach(this::log);
        int ans = 0;
        final Map<Integer, List<Cuboid>> bricksByZ1 = bricks.stream()
                .collect(groupingBy(Cuboid::getZ1));
        final Map<Integer, List<Cuboid>> bricksByZ2 = bricks.stream()
                .collect(groupingBy(Cuboid::getZ2));
        for (final int z : bricksByZ2.keySet()) {
            for (final Cuboid brick : bricksByZ2.get(z)) {
                final List<Cuboid> supportees = bricksByZ1.getOrDefault(brick.getZ2() + 1, List.of()).stream()
                    .filter(b -> Cuboid.overlapX(b, brick) && Cuboid.overlapY(b, brick))
                    .toList();
                if (supportees.isEmpty()) {
                    ans++;
                    continue;
                }
//                System.out.println("hey");
                boolean onlySupporter = false;
                for (final Cuboid supportee : supportees) {
                    if (bricksByZ2.get(supportee.getZ1() - 1).stream()
                            .filter(b -> !b.equals(brick))
                            .noneMatch(b -> Cuboid.overlapX(b, supportee) && Cuboid.overlapY(b, supportee))) {
                        onlySupporter = true;
                        break;
                    }
                }
                if (!onlySupporter) {
                    ans++;
                }
            }
            
        }
        return ans;
    }
    
    @Override
    public Integer solvePart2(final List<Cuboid> bricks) {
        return 0;
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "5"),
//        @Sample(method = "part2", input = TEST, expected = "TODO"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2023_22.create().run();
    }
    
    @RequiredArgsConstructor
    private final class Stack {
        private final List<Cuboid> bricks;

        public void sort() {
            Collections.sort(bricks,
                comparing(Cuboid::getZ1)
                .thenComparing(comparing(Cuboid::getY1))
                .thenComparing(comparing(Cuboid::getX1))
            );
        }

        public Set<Position> getViewY() {
          return bricks.stream()
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
        
        public boolean overlapsXY(final Collection<Cuboid> cuboids,final Cuboid brick) {
            return cuboids.stream()
                .anyMatch(c -> Cuboid.overlapX(c, brick) && Cuboid.overlapY(c, brick));
        }
        
        public void stack() {
            final Map<Integer, List<Cuboid>> bricksByZ1 = bricks.stream()
                .collect(groupingBy(Cuboid::getZ1));
            final Map<Integer, List<Cuboid>> bricksByZ2 = bricks.stream()
                .collect(groupingBy(Cuboid::getZ2));
            final MutableBoolean moved = new MutableBoolean(true);
            while (moved.isTrue()) {
                moved.setFalse();
                bricksByZ1.keySet().stream()
                    .sorted()
                    .filter(z -> z > 1)
                    .forEach(z -> {
//                        log("z: %d".formatted(z));
                        final List<Cuboid> list = new ArrayList<>(bricksByZ1.get(z));
                        for (final Cuboid brick : list) {
                            if (!overlapsXY(bricksByZ2.getOrDefault(z - 1, List.of()), brick)) {
                                bricksByZ1.getOrDefault(brick.getZ1(), new ArrayList<>()).remove(brick);
                                bricksByZ2.getOrDefault(brick.getZ2(), new ArrayList<>()).remove(brick);
                                final Cuboid m = moveToZ(brick, -1);
//                                log("move: %s -> %s".formatted(displayBrick(brick), displayBrick(m)));
                                bricksByZ1.computeIfAbsent(m.getZ1(), k -> new ArrayList<>()).add(m);
                                bricksByZ2.computeIfAbsent(m.getZ2(), k -> new ArrayList<>()).add(m);
                                moved.setTrue();
                            }
                        }
                    });
            }
            bricks.clear();
            bricksByZ2.values().stream().forEach(bricks::addAll);
        }
        
        public List<String> display() {
            return Stack.displayBricks(bricks);
        }
        
        private static List<String> displayBricks(final Collection<Cuboid> bricks) {
            return bricks.stream()
                .map(Stack::displayBrick)
                .toList();
        }
        
        private static String displayBrick(final Cuboid brick) {
            return "%d,%d,%d->%d,%d,%d".formatted(
                brick.getX1(), brick.getY1(), brick.getZ1(),
                brick.getX2(), brick.getY2(), brick.getZ2()
            );
        }
        
        @SuppressWarnings("unused")
        private void log(final Object obj) {
            AoC2023_22.this.log(obj);
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
