import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class AoC2016_11TestCase {

    @Test
    public void isSafe() {
        final AoC2016_11.State state1 = new AoC2016_11.State(1, Map.of("L", 1, "H", 1), Map.of("L", 3, "H", 2));
        final AoC2016_11.State state2 = new AoC2016_11.State(1, Map.of("L", 1, "H", 3), Map.of("L", 3, "H", 2));
        
        assertThat(state1.isSafe(), is(TRUE));
        assertThat(state2.isSafe(), is(FALSE));
    }
    
    @Test
    public void moves() {
        final AoC2016_11.State state = new AoC2016_11.State(1, Map.of("L", 1, "H", 1), Map.of("L", 3, "H", 2));
        
        final List<AoC2016_11.State> moves = state.moves();
        
        assertThat(moves, containsInAnyOrder(
            new AoC2016_11.State(2, Map.of("L", 1, "H", 2), Map.of("L", 3, "H", 2))
        ));
    }
    
    @Test
    public void dontMoveTowardsEmptiedFloors() {
        final AoC2016_11.State state = new AoC2016_11.State(2, Map.of(), Map.of("A", 2));
        
        final List<AoC2016_11.State> moves = state.moves();
        
        assertThat(moves, contains(
            new AoC2016_11.State(3, Map.of(), Map.of("A", 3))
        ));
    }
    
    @Test
    public void countOfType() {
        final AoC2016_11.State state1 = new AoC2016_11.State(2, Map.of("A", 1, "B", 2), Map.of("A", 1, "B", 2));
        final AoC2016_11.State state2 = new AoC2016_11.State(3, Map.of("A", 1, "B", 3), Map.of("A", 1, "B", 2));
        
        assertThat(state1.equivalentState(), is(211110000));
        assertThat(state2.equivalentState(), is(311011000));
    }
}
