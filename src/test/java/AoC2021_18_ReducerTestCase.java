import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AoC2021_18_ReducerTestCase {
    
    @BeforeAll
    public static void beforeClass() {
        if (!System.getProperties().containsKey("NDEBUG")) {
            AoC2021_18.Reducer.debug = true;
        }
    }
    
    @Test
    public void test() {
        assertThat(reduce("[[[[1,1],[2,2]],[3,3]],[4,4]]")).isEqualTo("[[[[1,1],[2,2]],[3,3]],[4,4]]");
        assertThat(reduce("[[[[[1,1],[2,2]],[3,3]],[4,4]],[5,5]]")).isEqualTo("[[[[3,0],[5,3]],[4,4]],[5,5]]");
    }

    @Test
    public void test2() {
        assertThat(reduce("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]"))
                .isEqualTo("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]");
    }
    
    private String reduce(final String string) {
        final AoC2021_18.Number number = AoC2021_18.Parser.parse(string);
        AoC2021_18.Reducer.reduce(number);
        return number.toString();
    }
}
