package com.github.pareronia.aoc.geometry3d;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aoc.RangeInclusive;
import com.github.pareronia.aoc.geometry.Position;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
public class Cuboid {
    final int x1;
    final int x2;
    final int y1;
    final int y2;
    final int z1;
    final int z2;

    public static Cuboid of(
            final int x1, final int x2,
            final int y1, final int y2,
            final int z1, final int z2) {
        assert x1 <= x2 && y1 <= y2 && z1 <= z2;
        return new Cuboid(x1, x2, y1, y2, z1, z2);
    }
    
    public Stream<Position3D> positions() {
        return IntStream.rangeClosed(x1, x2).boxed()
            .flatMap(x -> IntStream.rangeClosed(y1, y2)
                        .mapToObj(y -> Position.of(x, y)))
            .flatMap(p -> IntStream.rangeClosed(z1, z2)
                        .mapToObj(z -> Position3D.of(p.getX(), p.getY(), z)));
    }
    
    public boolean contains(final Position3D position) {
        return x1 <= position.getX() && position.getX() <= x2
            && y1 <= position.getY() && position.getY() <= y2
            && z1 <= position.getZ() && position.getZ() <= z2;
    }
    
    public long getVolume() {
        return (this.x2 - this.x1 + 1L)
             * (this.y2 - this.y1 + 1L)
             * (this.z2 - this.z1 + 1L);
    }
    
    public static boolean overlap(final Cuboid cuboid1, final Cuboid cuboid2) {
        return !(!overlapX(cuboid1, cuboid2)
            || !overlapY(cuboid1, cuboid2)
            || !overlapZ(cuboid1, cuboid2));
    }

    private static boolean overlapX(final Cuboid cuboid1, final Cuboid cuboid2) {
        return RangeInclusive.between(cuboid1.x1, cuboid1.x2)
            .isOverlappedBy(RangeInclusive.between(cuboid2.x1, cuboid2.x2));
    }

    private static boolean overlapY(final Cuboid cuboid1, final Cuboid cuboid2) {
        return RangeInclusive.between(cuboid1.y1, cuboid1.y2)
                .isOverlappedBy(RangeInclusive.between(cuboid2.y1, cuboid2.y2));
    }

    private static boolean overlapZ(final Cuboid cuboid1, final Cuboid cuboid2) {
        return RangeInclusive.between(cuboid1.z1, cuboid1.z2)
                .isOverlappedBy(RangeInclusive.between(cuboid2.z1, cuboid2.z2));
    }
    
    public static
    Optional<Cuboid>
    intersection(
        final Cuboid cuboid1,
        final Cuboid cuboid2
    ) {
        if (!overlap(cuboid1, cuboid2)) {
            return Optional.empty();
        }
        return Optional.of(Cuboid.of(
           Math.max(cuboid1.x1, cuboid2.x1), Math.min(cuboid1.x2, cuboid2.x2),
           Math.max(cuboid1.y1, cuboid2.y1), Math.min(cuboid1.y2, cuboid2.y2),
           Math.max(cuboid1.z1, cuboid2.z1), Math.min(cuboid1.z2, cuboid2.z2)));
    }
}
