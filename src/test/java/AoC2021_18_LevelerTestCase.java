import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class AoC2021_18_LevelerTestCase {

    @Test
    public void test() {
        final Map<Integer, List<AoC2021_18.Number>> result = level("[[[[[1,1],[2,2]],[3,3]],[4,4]],[5,5]]");
        System.out.println(result);
        assertThat(result.get(0), hasSize(1));
        assertThat(result.get(0).get(0).toString(), is("[[[[[1,1],[2,2]],[3,3]],[4,4]],[5,5]]"));
        assertThat(result.get(1), hasSize(2));
        assertThat(result.get(1).get(1).toString(), is("[5,5]"));
        assertThat(result.get(2), hasSize(2));
        assertThat(result.get(2).get(1).toString(), is("[4,4]"));
        assertThat(result.get(3), hasSize(2));
        assertThat(result.get(3).get(1).toString(), is("[3,3]"));
        assertThat(result.get(4), hasSize(2));
        assertThat(result.get(4).get(0).toString(), is("[1,1]"));
        assertThat(result.get(4).get(1).toString(), is("[2,2]"));
    }
    
    private Map<Integer, List<AoC2021_18.Number>> level(final String string) {
        return AoC2021_18.Exploder.Leveler.level(AoC2021_18.Parser.parse(string));
    }
    
    
}
