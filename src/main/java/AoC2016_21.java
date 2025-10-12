import static com.github.pareronia.aoc.CharArrayUtils.indexOf;
import static com.github.pareronia.aoc.CharArrayUtils.reverse;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.Arrays;
import java.util.List;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.solution.SolutionBase;

@SuppressWarnings({"PMD.NoPackage", "PMD.ClassNamingConventions"})
public final class AoC2016_21 extends SolutionBase<List<String>, String, String> {

    private AoC2016_21(final boolean debug) {
        super(debug);
    }

    public static AoC2016_21 create() {
        return new AoC2016_21(false);
    }

    public static AoC2016_21 createDebug() {
        return new AoC2016_21(true);
    }

    @Override
    protected List<String> parseInput(final List<String> inputs) {
        return inputs;
    }

    private String solve(final List<String> operations, final String start, final boolean encrypt) {
        char[] ch = start.toCharArray();
        for (int i = 0; i < operations.size(); i++) {
            final int idx = encrypt ? i : operations.size() - 1 - i;
            final String operation = operations.get(idx);
            if (operation.startsWith("swap position ")) {
                final Integer[] params = StringOps.getDigits(operation, 2);
                final int first = params[encrypt ? 0 : 1];
                final int second = params[encrypt ? 1 : 0];
                ch = StringOps.swap(ch, first, second);
            } else if (operation.startsWith("swap letter ")) {
                final String[] s =
                        operation.substring("swap letter ".length()).split(" with letter ");
                assert s[0].length() == 1 && s[1].length() == 1;
                final char first = s[encrypt ? 0 : 1].charAt(0);
                final char second = s[encrypt ? 1 : 0].charAt(0);
                ch = StringOps.swap(ch, first, second);
            } else if (operation.startsWith("reverse positions ")) {
                final Integer[] params = StringOps.getDigits(operation, 2);
                reverse(ch, params[0], params[1] + 1);
            } else if (operation.startsWith("rotate ") && !operation.startsWith("rotate based ")) {
                final Integer param = StringOps.getDigits(operation, 1)[0];
                final int amount;
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
            } else {
                final String param =
                        operation.substring(operation.length() - 1, operation.length());
                final char letter = param.charAt(0);
                if (encrypt) {
                    ch = rotateByLetter(ch, letter);
                } else {
                    char[] check1 = ch;
                    char[] check2;
                    do {
                        check1 = StringOps.rotateLeft(check1, 1);
                        check2 = rotateByLetter(check1, letter);
                    } while (!Arrays.equals(check2, ch));
                    ch = check1;
                }
            }
        }
        return String.valueOf(ch);
    }

    private char[] rotateByLetter(final char[] ch, final char c) {
        final int index = indexOf(ch, c);
        final int amount = index + 1 + (index >= 4 ? 1 : 0);
        return StringOps.rotateRight(ch, amount);
    }

    @Override
    public String solvePart1(final List<String> operations) {
        return solve(operations, "abcdefgh", TRUE);
    }

    @Override
    public String solvePart2(final List<String> operations) {
        return solve(operations, "fbgdceah", FALSE);
    }

    @Override
    @SuppressWarnings("PMD.LiteralsFirstInComparisons")
    protected void samples() {
        final AoC2016_21 test = createDebug();
        final List<String> input = test.parseInput(StringOps.splitLines(TEST));
        assert test.solve(input, "abcde", true).equals("decab");
        assert test.solve(input, "decab", false).equals("abcde");
    }

    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST =
            """
            swap position 4 with position 0
            swap letter d with letter b
            reverse positions 0 through 4
            rotate left 1 step
            move position 1 to position 4
            move position 3 to position 0
            rotate based on position of letter b
            rotate based on position of letter d
            """;
}
