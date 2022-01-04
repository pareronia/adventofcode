package com.github.pareronia.aoc.geometry3d;

import java.util.Optional;

import org.apache.commons.lang3.Range;

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
        return Range.between(cuboid1.x1, cuboid1.x2)
            .isOverlappedBy(Range.between(cuboid2.x1, cuboid2.x2));
    }

    private static boolean overlapY(final Cuboid cuboid1, final Cuboid cuboid2) {
        return Range.between(cuboid1.y1, cuboid1.y2)
                .isOverlappedBy(Range.between(cuboid2.y1, cuboid2.y2));
    }

    private static boolean overlapZ(final Cuboid cuboid1, final Cuboid cuboid2) {
        return Range.between(cuboid1.z1, cuboid1.z2)
                .isOverlappedBy(Range.between(cuboid2.z1, cuboid2.z2));
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
