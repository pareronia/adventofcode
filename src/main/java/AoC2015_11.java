import static com.github.pareronia.aoc.StringOps.nextLetter;

import java.util.List;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2015_11 extends AoCBase {
    
    private final String input;
    
    private AoC2015_11(final List<String> inputs, final boolean debug) {
        super(debug);
        assert inputs.size() == 1;
        this.input = inputs.get(0);
    }

    public static final AoC2015_11 create(final List<String> input) {
        return new AoC2015_11(input, false);
    }

    public static final AoC2015_11 createDebug(final List<String> input) {
        return new AoC2015_11(input, true);
    }
    
    @Override
    public String solvePart1() {
        return PasswordGenerator.generateFrom(this.input);
    }

    @Override
    public String solvePart2() {
        return PasswordGenerator.generateFrom(PasswordGenerator.generateFrom(this.input));
    }

    public static void main(final String[] args) throws Exception {
        assert PasswordChecker.isOk("heqaabcc".toCharArray()) == true;
        assert PasswordGenerator.generateFrom("abcdefgh").equals("abcdffaa");
        assert PasswordGenerator.generateFrom("ghijklmn").equals("ghjaabcc");
        assert PasswordGenerator.generateFrom("heqaabcc").equals("heqbbcdd");

        final Puzzle puzzle = Aocd.puzzle(2015, 11);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2015_11.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2015_11.create(puzzle.getInputData()).solvePart2())
        );
    }
    
    private static final class PasswordChecker {
        
        private static int findPair(final char[] password, final int start) {
            for (int i = start + 1; i < password.length; i++) {
                if (password[i] == password[i - 1]) {
                    return i;
                }
            }
            return -1;
        }
        
        public static boolean isOk(final char[] password) {
            final int pair1 = findPair(password, 0);
            if (pair1 < 0) {
                return false;
            }
            final int pair2 = findPair(password, pair1 + 1);
            if (pair2 < 0) {
                return false;
            }
            if (password[pair1] == password[pair2]) {
                return false;
            }
            for (int i = 0; i < password.length - 3; i++) {
                if (password[i] == password[i + 1] - 1 && password[i + 1] == password[i + 2] - 1) {
                    return true;
                }
            }
            return false;
        }
    }
    
    private static final class PasswordGenerator {
        
        private static char[] increment(final char[] password) {
            char[] chars = new char[password.length];
            int i = password.length - 1;
            System.arraycopy(password, 0, chars, 0, i);
            chars[i] = nextLetter(password[i], 1);
            while (chars[i] == 'a' && i > 0) {
                i--;
                final char[] tmp = new char[chars.length];
                System.arraycopy(chars, 0, tmp, 0, i);
                tmp[i] = nextLetter(chars[i], 1);
                System.arraycopy(chars, i + 1, tmp, i + 1, chars.length - i - 1);
                chars = tmp;
            }
            return chars;
        }
        
        public static String generateFrom(String password) {
            char [] chars = password.toCharArray();
            final List<Integer> h = List.of(password.indexOf('i'), password.indexOf('o'), password.indexOf('l'));
            if (!h.equals(List.of(-1, -1, -1))) {
                final int min = h.stream().mapToInt(Integer::intValue).filter(i -> i != -1).min().orElseThrow();
                if (min < password.length() - 1) {
                    final int length = password.substring(min + 1).length();
                    password = password.substring(0, min) + StringOps.nextLetter(password.charAt(min), 1);
                    for (int i = 0; i < length; i++) {
                        password += 'a';
                    }
                    chars = password.toCharArray();
                }
            } else {
                chars = increment(chars);
            }
            while (!PasswordChecker.isOk(chars)) {
                chars = increment(chars);
            }
            return String.valueOf(chars);
        }
    }
}
