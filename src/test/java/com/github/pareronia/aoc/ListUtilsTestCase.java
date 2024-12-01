package com.github.pareronia.aoc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;

class ListUtilsTestCase {

    @Test
    void reversed() {
        assertThat(ListUtils.reversed(List.of())).isEqualTo(List.of());
        assertThat(ListUtils.reversed(List.of(1, 2, 3))).isEqualTo(List.of(3, 2, 1));
    }

    @Test
    void indexesOfSubList() {
        assertThat(ListUtils.indexesOfSubList(List.of(), List.of())).isEmpty();
        assertThat(ListUtils.indexesOfSubList(List.of(1, 2, 3, 1, 2, 3), List.of(4))).isEmpty();
        assertThat(ListUtils.indexesOfSubList(List.of(1, 2, 3, 1, 2, 3), List.of(1))).isEqualTo(List.of(0, 3));
        assertThat(ListUtils.indexesOfSubList(List.of(1, 2, 3, 1, 2, 3), List.of(2, 3))).isEqualTo(List.of(1, 4));
        assertThat(ListUtils.indexesOfSubList(List.of(1, 2, 3, 1, 2, 3), List.of(1, 2, 3))).isEqualTo(List.of(0, 3));
        assertThat(ListUtils.indexesOfSubList(List.of(1, 2, 3, 1, 2, 3), List.of(1, 2, 3, 1))).isEqualTo(List.of(0));
        assertThat(ListUtils.indexesOfSubList(List.of(1, 2, 3, 1, 2, 3), List.of(1, 2, 3, 1, 2, 3))).isEqualTo(List.of(0));
        assertThat(ListUtils.indexesOfSubList(List.of(1, 2, 3, 1, 2), List.of(1, 2, 3))).isEqualTo(List.of(0));
    }

    @Test
    void subtractAll() {
        assertThat(ListUtils.subtractAll(
                List.of(1, 2, 3, 4, 1, 2, 3, 4, 1, 2), List.of()))
            .isEqualTo(List.of(1, 2, 3, 4, 1, 2, 3, 4, 1, 2));
        assertThat(ListUtils.subtractAll(
                List.of(1, 2, 3, 4, 1, 2, 3, 4, 1, 2), List.of(5)))
            .isEqualTo(List.of(1, 2, 3, 4, 1, 2, 3, 4, 1, 2));
        assertThat(ListUtils.subtractAll(
                List.of(1, 2, 3, 4, 1, 2, 3, 4, 1, 2), List.of(1, 2, 3)))
            .isEqualTo(List.of(4, 4, 1, 2));
        assertThat(ListUtils.subtractAll(
                List.of(1, 2, 3), List.of(1, 2, 3)))
            .isEqualTo(List.of());
    }
    
    @Test
    void transpose() {
        assertThatThrownBy(() -> ListUtils.transpose(
                List.of(List.of(), List.of())))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> ListUtils.transpose(
                List.of(List.of(1, 2, 3), List.of(4, 5))))
            .isInstanceOf(IllegalArgumentException.class);
        assertThat(ListUtils.transpose(List.of(
                List.of(1, 2, 3), List.of(4, 5, 6))))
            .isEqualTo(List.of(List.of(1, 4), List.of(2, 5), List.of(3, 6)));
        assertThat(ListUtils.transpose(List.of(
                List.of("a", "b"), List.of("c", "d"))))
            .isEqualTo(List.of(List.of("a", "c"), List.of("b", "d")));
    }
}
