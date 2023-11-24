import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class AoC2021_18_ParserTestCase {

    @Test
    public void parse() {
        assertThat(parse("[1,2]")).isEqualTo("[1,2]");
        assertThat(parse("[[1,2],3]")).isEqualTo("[[1,2],3]");
        assertThat(parse("[9,[8,7]]")).isEqualTo("[9,[8,7]]");
        assertThat(parse("[[1,9],[8,5]]")).isEqualTo("[[1,9],[8,5]]");
        assertThat(parse("[[[[1,2],[3,4]],[[5,6],[7,8]]],9]"))
                    .isEqualTo("[[[[1,2],[3,4]],[[5,6],[7,8]]],9]");
        assertThat(parse("[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]"))
                    .isEqualTo("[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]");
        assertThat(parse("[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]"))
                    .isEqualTo("[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]");
        assertThat(parse("[[[[[9,8],1],2],3],4]")).isEqualTo("[[[[[9,8],1],2],3],4]");
        assertThat(parse("[7,[6,[5,[4,[3,2]]]]]")).isEqualTo("[7,[6,[5,[4,[3,2]]]]]");
        assertThat(parse("[[6,[5,[4,[3,2]]]],1]")).isEqualTo("[[6,[5,[4,[3,2]]]],1]");
        assertThat(parse("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]")).isEqualTo("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]");
        assertThat(parse("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")).isEqualTo("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]");
        assertThat(parse("[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]")).isEqualTo("[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]");
    }
    
    @Test
    public void adjacent1() {
        final AoC2021_18.Pair number = (AoC2021_18.Pair) AoC2021_18.Parser.parse("[[1,2],3]");
        assertThat(number.parent).isNull();
        assertThat(number.getLeft().parent).isEqualTo(number);
        assertThat(number.getRight().parent).isEqualTo(number);
        assertThat(((AoC2021_18.Pair) number.getLeft()).leftAdjacent()).isNull();
        assertThat(((AoC2021_18.Pair) number.getLeft()).rightAdjacent().getValue()).isEqualTo(3);
    }

    @Test
    public void adjacent2() {
        final AoC2021_18.Pair number = (AoC2021_18.Pair) AoC2021_18.Parser.parse("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]");
        assertThat(number.parent).isNull();
        assertThat(number.getLeft().parent).isEqualTo(number);
        assertThat(number.getRight().parent).isEqualTo(number);
        final AoC2021_18.Pair p = getRight(getRight(getRight((AoC2021_18.Pair) number.getLeft())));
        assertThat(((AoC2021_18.Regular) p.getLeft()).getValue()).isEqualTo(7);
        assertThat(((AoC2021_18.Regular) p.getRight()).getValue()).isEqualTo(3);
        assertThat(p.leftAdjacent().getValue()).isEqualTo(1);
        assertThat(p.rightAdjacent().getValue()).isEqualTo(6);
    }
    
    @Test
    public void adjacent3() {
        final AoC2021_18.Pair number = (AoC2021_18.Pair) AoC2021_18.Parser.parse("[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]");
        assertThat(number.parent).isNull();
        assertThat(number.getLeft().parent).isEqualTo(number);
        assertThat(number.getRight().parent).isEqualTo(number);
        final AoC2021_18.Pair p = getRight(getRight(getRight((AoC2021_18.Pair) number.getLeft())));
        assertThat(((AoC2021_18.Regular) p.getLeft()).getValue()).isEqualTo(6);
        assertThat(((AoC2021_18.Regular) p.getRight()).getValue()).isEqualTo(7);
        assertThat(p.leftAdjacent().getValue()).isEqualTo(0);
        assertThat(p.rightAdjacent().getValue()).isEqualTo(1);
    }
    
    private AoC2021_18.Pair getRight(final AoC2021_18.Pair pair) {
        return (AoC2021_18.Pair) pair.getRight();
    }
    
    private String parse(final String string) {
        return AoC2021_18.Parser.parse(string).toString();
    }
}
