import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.github.pareronia.aoc.geometry3d.Position3D;

public class AoC2021_19TestCase {

    @Test
    public void testTransformations() {
        final List<Position3D> positions = List.of(Position3D.of(-1, 2, -3));
        final Iterator<List<Position3D>> iterator = AoC2021_19.Transformations.of(positions).iterator();
        final Set<Position3D> unique = new HashSet<>();
        int cnt = 0;
        while (iterator.hasNext()) {
            final List<Position3D> next = iterator.next();
            unique.addAll(next);
            assertThat(next).isNotNull();
            log(next);
            cnt++;
        }
        assertThat(cnt).isEqualTo(24);
        log("unique:");
        log(unique);
    }
    
    private void log(final Object string) {
        if (!System.getProperties().containsKey("NDEBUG")) {
            System.out.println(string);
        }
    }
}
