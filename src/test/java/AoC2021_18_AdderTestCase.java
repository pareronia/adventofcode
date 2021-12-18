import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;

public class AoC2021_18_AdderTestCase {
    
    @Test
    public void test() {
        assertThat(add(List.of("[1,2]", "[[3,4],5]")), is("[[1,2],[[3,4],5]]"));
        assertThat(add(List.of("[1,2]")), is("[1,2]"));
        assertThat(add(List.of("[1,1]", "[2,2]", "[3,3]", "[4,4]")),
                is("[[[[1,1],[2,2]],[3,3]],[4,4]]"));
        assertThat(add(List.of("[1,1]", "[2,2]", "[3,3]", "[4,4]", "[5,5]")),
                is("[[[[[1,1],[2,2]],[3,3]],[4,4]],[5,5]]"));
    }

    @Test(expected = NoSuchElementException.class)
    public void error() {
        AoC2021_18.Adder.add(List.of());
    }
    
    private String add(final List<String> strings) {
        final List<AoC2021_18.Number> numbers = strings.stream()
                .map(AoC2021_18.Parser::parse)
                .collect(toList());
        return AoC2021_18.Adder.add(numbers).toString();
    }
}
