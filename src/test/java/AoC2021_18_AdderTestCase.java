import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class AoC2021_18_AdderTestCase {
    
    @Test
    public void test() {
        assertThat(add("[1,2]", "[[3,4],5]")).isEqualTo("[[1,2],[[3,4],5]]");
        assertThat(add("[[1,1],[2,2]]", "[[3,3],[4,4]]"))
            .isEqualTo("[[[1,1],[2,2]],[[3,3],[4,4]]]");
    }

    private String add(final String left, final String right) {
        return AoC2021_18.Adder.add(AoC2021_18.Parser.parse(left), AoC2021_18.Parser.parse(right)).toString();
    }
}
