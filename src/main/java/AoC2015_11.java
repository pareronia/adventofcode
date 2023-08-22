import static com.github.pareronia.aoc.StringOps.nextLetter;

import java.util.List;

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
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2015_11.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2015_11.create(inputData)::solvePart2)
        );
    }
    
    private static final class PasswordChecker {
        
        public static boolean isOk(final char[] password) {
            final int[] pairs = new int[] { -1, -1 };
            boolean trio = false;
            for (int i = 0; i < password.length; i++) {
                if (password[i] == 'i' || password[i] == 'o' || password[i] == 'l') {
                    return false;
                }
                if (i > 0 && password[i] == password[i - 1]) {
                    pairs[pairs[0] == -1 ? 0 : 1] = i;
                }
                trio = trio
                        || i < password.length - 3
                        && password[i] == password[i + 1] - 1
                        && password[i + 1] == password[i + 2] - 1;
            }
            return trio && pairs[0] != -1 && pairs[1] != -1
                    && password[pairs[0]] != password[pairs[1]];
        }
    }
    
    private static final class PasswordGenerator {
        
        private static char[] increment(final char[] password, final int i) {
            password[i] = nextLetter(password[i], 1);
            if (password[i] == 'a') {
                increment(password, i - 1);
            }
            return password;
        }
        
        public static String generateFrom(final String password) {
            char [] chars = password.toCharArray();
            do {
                chars = increment(chars, chars.length - 1);
            } while (!PasswordChecker.isOk(chars));
            return String.valueOf(chars);
        }
    }
}
