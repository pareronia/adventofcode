import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class AoC2021_18_MagnitudeTestCase {
    
    @Test
    public void test() {
        assertThat(magnitude("[9,1]")).isEqualTo(29L);
        assertThat(magnitude("[1,9]")).isEqualTo(21L);
        assertThat(magnitude("[[9,1],[1,9]]")).isEqualTo(129L);
        assertThat(magnitude("[[1,2],[[3,4],5]]")).isEqualTo(143L);
        assertThat(magnitude("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]")).isEqualTo(1_384L);
        assertThat(magnitude("[[[[1,1],[2,2]],[3,3]],[4,4]]")).isEqualTo(445L);
        assertThat(magnitude("[[[[3,0],[5,3]],[4,4]],[5,5]]")).isEqualTo(791L);
        assertThat(magnitude("[[[[5,0],[7,4]],[5,5]],[6,6]]")).isEqualTo(1_137L);
        assertThat(magnitude("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]")).isEqualTo(3_488L);
    }
    
    private long magnitude(final String string) {
        return AoC2021_18.Magnitude.magnitude(AoC2021_18.Parser.parse(string));
    }

}
