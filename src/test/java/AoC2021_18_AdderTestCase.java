import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class AoC2021_18_AdderTestCase {
    
    @Test
    public void test() {
        assertThat(add("[1,2]", "[[3,4],5]"), is("[[1,2],[[3,4],5]]"));
        assertThat(add("[[1,1],[2,2]]", "[[3,3],[4,4]]"),
                is("[[[1,1],[2,2]],[[3,3],[4,4]]]"));
    }

    private String add(final String left, final String right) {
        return AoC2021_18.Adder.add(AoC2021_18.Parser.parse(left), AoC2021_18.Parser.parse(right)).toString();
    }
}
