package com.github.pareronia.aoc;

import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.github.pareronia.aoc.IterTools.ProductPair;
import com.github.pareronia.aoc.IterTools.WindowPair;
import com.github.pareronia.aoc.IterTools.ZippedPair;

public class IterToolsTestCase {

    @Test
    public void product() {
        final Iterator<ProductPair<Integer, Integer>> product1
                = IterTools.product(List.of(0, 1), Set.of(0, 1));
        final List<ProductPair<Integer, Integer>> ans = new ArrayList<>();
        while (product1.hasNext()) {
            ans.add(product1.next());
        }
        assertThat(ans).containsExactlyInAnyOrder(
            ProductPair.of(0, 0),
            ProductPair.of(0, 1),
            ProductPair.of(1, 0),
            ProductPair.of(1, 1)
        );

        final Iterator<ProductPair<String, String>> product2
                = IterTools.product(List.of("A", "B"), List.of("A", "B"));
        assertThat(product2.next()).isEqualTo(ProductPair.of("A", "A"));
        assertThat(product2.next()).isEqualTo(ProductPair.of("A", "B"));
        assertThat(product2.next()).isEqualTo(ProductPair.of("B", "A"));
        assertThat(product2.next()).isEqualTo(ProductPair.of("B", "B"));
        assertThat(product2.hasNext()).isFalse();

        final Iterator<ProductPair<Integer, Integer>> product3
                = IterTools.product(range(3), range(2));
        assertThat(product3.next()).isEqualTo( ProductPair.of(0, 0));
        assertThat(product3.next()).isEqualTo( ProductPair.of(0, 1));
        assertThat(product3.next()).isEqualTo( ProductPair.of(1, 0));
        assertThat(product3.next()).isEqualTo( ProductPair.of(1, 1));
        assertThat(product3.next()).isEqualTo( ProductPair.of(2, 0));
        assertThat(product3.next()).isEqualTo( ProductPair.of(2, 1));
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
        final Iterator<ZippedPair<Integer>> zip
                = IterTools.zip(List.of(1, 2, 3), List.of(4, 5, 6));
        assertThat(zip.next()).isEqualTo(new ZippedPair<>(1, 4));
        assertThat(zip.next()).isEqualTo(new ZippedPair<>(2, 5));
        assertThat(zip.next()).isEqualTo(new ZippedPair<>(3, 6));
        assertThat(zip.hasNext()).isFalse();
        
        final Iterator<ZippedPair<Long>> zip2
                = IterTools.zip(List.of(1L, 2L, 3L), List.of(4L, 5L, 6L));
        assertThat(zip2.next()).isEqualTo(new ZippedPair<>(1L, 4L));
        assertThat(zip2.next()).isEqualTo(new ZippedPair<>(2L, 5L));
        assertThat(zip2.next()).isEqualTo(new ZippedPair<>(3L, 6L));
        assertThat(zip2.hasNext()).isFalse();

        final Iterator<ZippedPair<String>> zip3
            = IterTools.zip(List.of("a", "b", "c"), List.of("d", "e", "f"));
        assertThat(zip3.next()).isEqualTo(new ZippedPair<>("a", "d"));
        assertThat(zip3.next()).isEqualTo(new ZippedPair<>("b", "e"));
        assertThat(zip3.next()).isEqualTo(new ZippedPair<>("c", "f"));
        assertThat(zip3.hasNext()).isFalse();

        final Iterator<ZippedPair<String>> zip4
                = IterTools.zip(List.of("a", "b"), List.of("d", "e", "f"));
        assertThat(zip4.next()).isEqualTo(new ZippedPair<>("a", "d"));
        assertThat(zip4.next()).isEqualTo(new ZippedPair<>("b", "e"));
        assertThat(zip4.hasNext()).isFalse();

        assertThat(IterTools.zip(List.of("a", "b"), List.of()).hasNext()).isFalse();
    }
    
    @Test
    public void cycle() {
        final Iterator<Integer> icycle = IterTools.cycle(List.of(1, 2, 3));
        for (int i = 0; i < 10; i++) {
            assertThat(icycle.next()).isEqualTo(1);
            assertThat(icycle.next()).isEqualTo(2);
            assertThat(icycle.next()).isEqualTo(3);
        }
        final Iterator<Character> ccycle = IterTools.cycle(Utils.asCharacterStream("abc").toList());
        for (int i = 0; i < 10; i++) {
            assertThat(ccycle.next()).isEqualTo('a');
            assertThat(ccycle.next()).isEqualTo('b');
            assertThat(ccycle.next()).isEqualTo('c');
        }
   }
    
    @Test
    public void windows() {
        final Iterator<WindowPair<Integer>> windows
                = IterTools.windows(List.of(1, 2, 3, 4));
        assertThat(windows.next()).isEqualTo(new WindowPair<>(1, 2));
        assertThat(windows.next()).isEqualTo(new WindowPair<>(2, 3));
        assertThat(windows.next()).isEqualTo(new WindowPair<>(3, 4));
        assertThat(windows.hasNext()).isFalse();
    }
    
    @Test
    public void chain() {
        final Iterator<Integer> chain = IterTools.chain(
                List.of(1, 2, 3).iterator(),
                List.of(4, 5).iterator());
        
        assertThat(chain.next()).isEqualTo(1);
        assertThat(chain.next()).isEqualTo(2);
        assertThat(chain.next()).isEqualTo(3);
        assertThat(chain.next()).isEqualTo(4);
        assertThat(chain.next()).isEqualTo(5);
        assertThat(chain.hasNext()).isFalse();
    }
}
