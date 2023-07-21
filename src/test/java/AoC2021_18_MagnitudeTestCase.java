import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

public class AoC2021_18_MagnitudeTestCase {
    
    @Test
    public void test() {
        assertThat(magnitude("[9,1]"), is(29L));
        assertThat(magnitude("[1,9]"), is(21L));
        assertThat(magnitude("[[9,1],[1,9]]"), is(129L));
        assertThat(magnitude("[[1,2],[[3,4],5]]"), is(143L));
        assertThat(magnitude("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"), is(1_384L));
        assertThat(magnitude("[[[[1,1],[2,2]],[3,3]],[4,4]]"), is(445L));
        assertThat(magnitude("[[[[3,0],[5,3]],[4,4]],[5,5]]"), is(791L));
        assertThat(magnitude("[[[[5,0],[7,4]],[5,5]],[6,6]]"), is(1_137L));
        assertThat(magnitude("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]"), is(3_488L));
    }
    
    private long magnitude(final String string) {
        return AoC2021_18.Magnitude.magnitude(AoC2021_18.Parser.parse(string));
    }

}
