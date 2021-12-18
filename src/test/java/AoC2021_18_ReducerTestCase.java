import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;

public class AoC2021_18_ReducerTestCase {
    
    @BeforeClass
    public static void beforeClass() {
        AoC2021_18.Reducer.debug = true;
    }
    
    @Test
    public void test() {
        assertThat(reduce("[[[[1,1],[2,2]],[3,3]],[4,4]]"), is("[[[[1,1],[2,2]],[3,3]],[4,4]]"));
        assertThat(reduce("[[[[[1,1],[2,2]],[3,3]],[4,4]],[5,5]]"), is("[[[[3,0],[5,3]],[4,4]],[5,5]]"));
    }

    @Test
    public void test2() {
        assertThat(reduce("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]"),
                is("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"));
    }
    
    private String reduce(final String string) {
        final AoC2021_18.Number number = AoC2021_18.Parser.parse(string);
        AoC2021_18.Reducer.reduce(number);
        return number.toString();
    }
}
