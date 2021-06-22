import static org.apache.commons.lang3.ArrayUtils.indexOf;
import static org.apache.commons.lang3.ArrayUtils.reverse;

import java.util.List;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aocd.Aocd;

public class AoC2016_21 extends AoCBase {
       
    private final List<String> operations;
    
    private AoC2016_21(List<String> input, boolean debug) {
    	super(debug);
    	this.operations = input;
    }
    
    public static AoC2016_21 create(List<String> input) {
    	return new AoC2016_21(input, false);
    }
    
    public static AoC2016_21 createDebug(List<String> input) {
    	return new AoC2016_21(input, true);
    }
    
    private String solve1(String start) {
        char[] ch = start.toCharArray();
        for (final String operation : this.operations) {
            if (operation.startsWith("swap position ")) {
                final List<Integer> params = StringOps.getDigits(operation, 2);
                final char temp = ch[params.get(0)];
                ch[params.get(0)] = ch[params.get(1)];
                ch[params.get(1)] = temp;
            } else if (operation.startsWith("swap letter ")) {
                final String[] s = operation.substring("swap letter ".length())
                                        .split(" with letter ");
                assert s[0].length() == 1 && s[1].length() == 1;
                final int first = indexOf(ch, s[0].charAt(0));
                final int second = indexOf(ch, s[1].charAt(0));
                final char temp = ch[first];
                ch[first] = ch[second];
                ch[second] = temp;
            } else if (operation.startsWith("reverse positions ")) {
                final List<Integer> params = StringOps.getDigits(operation, 2);
                reverse(ch, params.get(0), params.get(1) + 1);
            } else if (operation.startsWith("rotate left ")) {
                final Integer amount = StringOps.getDigits(operation, 1).get(0);
                ch = StringOps.rotateLeft(ch, amount);
            } else if (operation.startsWith("rotate right ")) {
                final Integer amount = StringOps.getDigits(operation, 1).get(0);
                ch = StringOps.rotateRight(ch, amount);
            } else if (operation.startsWith("move ")) {
                final List<Integer> params = StringOps.getDigits(operation, 2);
                ch = StringOps.move(ch, params.get(0), params.get(1));
            } else if (operation.startsWith("rotate based on position ")) {
                final String param = operation.substring(operation.length() - 1,
                                                         operation.length());
                final int index = indexOf(ch, param.charAt(0));
                final int amount = index + 1 + (index >= 4 ? 1 : 0);
                ch = StringOps.rotateRight(ch, amount);
            } else {
                throw new IllegalArgumentException("Invalid input");
            }
        }
        return String.valueOf(ch);
    }
    
    @Override
    public String solvePart1() {
        return solve1("abcdefgh");
    }
    
    @Override
    public String solvePart2() {
        return "";
    }
    
    public static void main(String[] args) throws Exception {
        assert AoC2016_21.createDebug(TEST).solve1("abcde").equals("decab");
    
        final List<String> input = Aocd.getData(2016, 21);
        lap("Part 1", () -> AoC2016_21.create(input).solvePart1());
        lap("Part 2", () -> AoC2016_21.create(input).solvePart2());
    }
    
    private static final List<String> TEST = splitLines(
            "swap position 4 with position 0\r\n" +
            "swap letter d with letter b\r\n" +
            "reverse positions 0 through 4\r\n" +
            "rotate left 1 step\r\n" +
            "move position 1 to position 4\r\n" +
            "move position 3 to position 0\r\n" +
            "rotate based on position of letter b\r\n" +
            "rotate based on position of letter d"
    );
}