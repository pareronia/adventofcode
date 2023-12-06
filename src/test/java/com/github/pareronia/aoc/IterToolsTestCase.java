package com.github.pareronia.aoc;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.github.pareronia.aoc.IterTools.ZippedPair;

public class IterToolsTestCase {

    @Test
    public void product() {
        assertThat(
            IterTools.product(List.of(0, 1), Set.of(0, 1)))
            .containsExactlyInAnyOrder(List.of(0, 0), List.of(0, 1), List.of(1, 0), List.of(1, 1));
        assertThat(
            IterTools.product(List.of("A", "B"), List.of("A", "B")))
            .containsExactlyInAnyOrder(List.of("A", "A"), List.of("A", "B"), List.of("B", "A"), List.of("B", "B"));
    }
    
    @Test
    public void permutations() {
        assertThat(
            IterTools.permutations(List.of(1, 2, 3)).collect(toList()))
            .containsExactlyInAnyOrder(
                    List.of(1, 2, 3), List.of(1, 3, 2),
                    List.of(2, 1, 3), List.of(2, 3, 1),
                    List.of(3, 1, 2), List.of(3, 2, 1));
        assertThat(
            IterTools.permutations(new int[] { 1, 2, 3 }).collect(toList()))
            .containsExactlyInAnyOrder(
                    new int[] { 1, 2, 3}, new int[] { 1, 3, 2 },
                    new int[] { 2, 1, 3}, new int[] { 2, 3, 1 },
                    new int[] { 3, 1, 2}, new int[] { 3, 2, 1 });
    }
    
    @Test
    public void zip() {
        assertThat(
            IterTools.zip(List.of(1, 2, 3), List.of(4, 5, 6)))
            .containsExactly(new ZippedPair<>(1, 4), new ZippedPair<>(2, 5), new ZippedPair<>(3, 6));
        assertThat(
            IterTools.zip(List.of(1L, 2L, 3L), List.of(4L, 5L, 6L)))
            .containsExactly(new ZippedPair<>(1L, 4L), new ZippedPair<>(2L, 5L), new ZippedPair<>(3L, 6L));
        assertThat(
            IterTools.zip(List.of("a", "b", "c"), List.of("d", "e", "f")))
            .containsExactly(new ZippedPair<>("a", "d"), new ZippedPair<>("b", "e"), new ZippedPair<>("c", "f"));
        assertThat(
            IterTools.zip(List.of("a", "b"), List.of("d", "e", "f")))
            .containsExactly(new ZippedPair<>("a", "d"), new ZippedPair<>("b", "e"));
        assertThat(
            IterTools.zip(List.of("a", "b"), List.of()))
            .isEmpty();
    }
}
