import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.apache.commons.lang3.ArrayUtils.indexOf;
import static org.apache.commons.lang3.ArrayUtils.reverse;

import java.util.Arrays;
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
    
    private String solve(String start, boolean encrypt) {
        char[] ch = start.toCharArray();
        for (int i = 0; i < this.operations.size(); i++) {
            final int idx = encrypt ? i : this.operations.size() - 1 - i;
            final String operation = operations.get(idx);
            log(operation);
            if (operation.startsWith("swap position ")) {
                final Integer[] params = StringOps.getDigits(operation, 2);
                final int first = params[encrypt ? 0 : 1];
                final int second = params[encrypt ? 1 : 0];
                ch = StringOps.swap(ch, first, second);
            } else if (operation.startsWith("swap letter ")) {
                final String[] s = operation.substring("swap letter ".length())
                                        .split(" with letter ");
                assert s[0].length() == 1 && s[1].length() == 1;
                final char first = s[encrypt ? 0 : 1].charAt(0);
                final char second = s[encrypt ? 1 : 0].charAt(0);
                ch = StringOps.swap(ch, first, second);
            } else if (operation.startsWith("reverse positions ")) {
                final Integer[] params = StringOps.getDigits(operation, 2);
                reverse(ch, params[0], params[1] + 1);
            } else if (operation.startsWith("rotate ") && !operation.startsWith("rotate based ")) {
                final Integer param = StringOps.getDigits(operation, 1)[0];
                int amount;
                if (operation.contains("left")) {
                    amount = ch.length - (encrypt ? param : ch.length - param);
                } else {
                    amount = encrypt ? param : ch.length - param;
                }
                ch = StringOps.rotateRight(ch, amount);
            } else if (operation.startsWith("move ")) {
                final Integer[] params = StringOps.getDigits(operation, 2);
                final Integer from = params[encrypt ? 0 : 1];
                final Integer to = params[encrypt ? 1 : 0];
                ch = StringOps.move(ch, from, to);
            } else if (operation.startsWith("rotate based ")) {
                final String param = operation.substring(operation.length() - 1,
                                                         operation.length());
                final char letter = param.charAt(0);
                if (encrypt) {
                    ch = rotateByLetter(ch, letter);
                } else {
                    char[] check1 = ch;
                    char [] check2;
                    do {
                        check1 = StringOps.rotateLeft(check1, 1);
                        check2 = rotateByLetter(check1, letter);
                    } while (!Arrays.equals(check2, ch));
                   ch = check1;
                }
            } else {
                throw new IllegalArgumentException("Invalid input");
            }
            log(String.valueOf(ch));
        }
        return String.valueOf(ch);
    }
    
    private char[] rotateByLetter(char[] ch, char c) {
        final int index = indexOf(ch, c);
        final int amount = index + 1 + (index >= 4 ? 1 : 0);
        return StringOps.rotateRight(ch, amount);
    }
    
    @Override
    public String solvePart1() {
        return solve("abcdefgh", TRUE);
    }
    
    @Override
    public String solvePart2() {
        return solve("fbgdceah", FALSE);
    }
    
    public static void main(String[] args) throws Exception {
        assert AoC2016_21.createDebug(TEST).solve("abcde", true).equals("decab");
        assert AoC2016_21.createDebug(TEST).solve("decab", false).equals("abcde");
    
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