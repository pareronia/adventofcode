import static com.github.pareronia.aoc.StringOps.nextLetter;

import java.util.List;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2015_11 extends SolutionBase<String, String, String> {
    
    private AoC2015_11(final boolean debug) {
        super(debug);
    }

    public static final AoC2015_11 create() {
        return new AoC2015_11(false);
    }

    public static final AoC2015_11 createDebug() {
        return new AoC2015_11(true);
    }
    
    @Override
    protected String parseInput(final List<String> inputs) {
        assert inputs.size() == 1;
        return inputs.get(0);
    }

    @Override
    public String solvePart1(final String input) {
        return PasswordGenerator.generateFrom(input);
    }

    @Override
    public String solvePart2(final String input) {
        return PasswordGenerator.generateFrom(PasswordGenerator.generateFrom(input));
    }

    @Override
    @Samples({
        @Sample(method = "part1", input = "abcdefgh", expected = "abcdffaa"),
        @Sample(method = "part1", input = "ghijklmn", expected = "ghjaabcc"),
        @Sample(method = "part1", input = "heqaabcc", expected = "heqbbcdd"),
    })
    public void samples() {
    }

    public static void main(final String[] args) throws Exception {
        assert PasswordChecker.isOk("heqaabcc".toCharArray()) == true;

        AoC2015_11.create().run();
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
