import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import org.junit.Test;

public class AoC2021_18_PreOrdererTestCase {
    
    @Test
    public void test() {
        final List<AoC2021_18.Number> result = preOrder("[[[[[1,1],[2,2]],[3,3]],[4,4]],[5,5]]");
        System.out.println(result);
        assertThat(result, hasSize(9));
        assertThat(result.get(8).toString(), is("[5,5]"));
        assertThat(result.get(7).toString(), is("[4,4]"));
        assertThat(result.get(6).toString(), is("[3,3]"));
        assertThat(result.get(5).toString(), is("[2,2]"));
        assertThat(result.get(4).toString(), is("[1,1]"));
    }
    
    private List<AoC2021_18.Number> preOrder(final String string) {
        return AoC2021_18.Exploder.PreOrderer.preOrder(AoC2021_18.Parser.parse(string));
    }
}
