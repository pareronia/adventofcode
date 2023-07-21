import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

public class AoC2021_18_ExploderTestCase {
    
    @Test
    public void ok() {
        assertThat(explode("[[[[[9,8],1],2],3],4]"), is("[[[[0,9],2],3],4]"));
        assertThat(explode("[7,[6,[5,[4,[3,2]]]]]"), is("[7,[6,[5,[7,0]]]]"));
        assertThat(explode("[[6,[5,[4,[3,2]]]],1]"), is("[[6,[5,[7,0]]],3]"));
        assertThat(explode("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]"),
                is("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"));
        assertThat(explode("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"),
                is("[[3,[2,[8,0]]],[9,[5,[7,0]]]]"));
        assertThat(explode("[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]"),
                is("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"));
    }

    @Test
    public void fail() {
    }

    private String explode(final String string) {
        final AoC2021_18.Number number = AoC2021_18.Parser.parse(string);
        AoC2021_18.Exploder.explode(number, 0);
        return number.toString();
    }
}
