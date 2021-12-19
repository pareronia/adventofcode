import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.github.pareronia.aoc.geometry3d.Position3D;

public class AoC2021_19TestCase {

    @Test
    public void testPermutations() {
        final List<Position3D> positions = List.of(Position3D.of(-1, 2, -3));
        final Iterator<List<Position3D>> iterator = new AoC2021_19.Permutations(positions).iterator();
        final Set<Position3D> unique = new HashSet<>();
        int cnt = 0;
        while (iterator.hasNext()) {
            final List<Position3D> next = iterator.next();
            unique.addAll(next);
            assertThat(next, is(notNullValue()));
            System.out.println(next);
            cnt++;
        }
        assertThat(cnt, is(24));
        System.out.println("unique:");
        System.out.println(unique);
    }
}
