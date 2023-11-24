import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class AoC2021_18_SplitterTestCase {
    
    @Test
    public void test1() {
        // [[[[0,7],4],[15,[0,13]]],[1,1]]
        final AoC2021_18.Number number =
                AoC2021_18.Pair.create(
                        AoC2021_18.Pair.create(
                                AoC2021_18.Pair.create(
                                        AoC2021_18.Pair.create(
                                                new AoC2021_18.Regular(0),
                                                new AoC2021_18.Regular(7)),
                                        new AoC2021_18.Regular(4)),
                                AoC2021_18.Pair.create(
                                        new AoC2021_18.Regular(15),
                                        AoC2021_18.Pair.create(
                                                new AoC2021_18.Regular(0),
                                                new AoC2021_18.Regular(13)))),
                        AoC2021_18.Pair.create(new AoC2021_18.Regular(1),
                                            new AoC2021_18.Regular(1)));
        assert number.toString().equals("[[[[0,7],4],[15,[0,13]]],[1,1]]");
        assertThat(split(number)).isEqualTo("[[[[0,7],4],[[7,8],[0,13]]],[1,1]]");
        assertThat(split(number)).isEqualTo("[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]");
    }

    @Test
    public void test2() {
        // [[[[7,7],[7,8]],[[9,5],[8,0]]],[[[9,10],20],[8,[9,0]]]]
        final AoC2021_18.Number number =
                AoC2021_18.Pair.create(
                        AoC2021_18.Pair.create(
                                new AoC2021_18.Regular(9),
                                new AoC2021_18.Regular(10)),
                        new AoC2021_18.Regular(20));
        assert number.toString().equals("[[9,10],20]");
        assertThat(split(number)).isEqualTo("[[9,[5,5]],20]");
    }
    
    private String split(final AoC2021_18.Number number) {
        AoC2021_18.Splitter.split(number);
        return number.toString();
    }
}
