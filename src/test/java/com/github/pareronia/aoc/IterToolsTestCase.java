package com.github.pareronia.aoc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

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
}
