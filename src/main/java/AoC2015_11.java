import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2015_11 extends AoCBase {
    
    private static final String ALPH = "abcdefghijklmnopqrstuvwxyz";
    private static final String NEXT = "bcdefghjjkmmnppqrstuvwxyza";

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
    
    private char nextLetter(final char ch) {
        return NEXT.charAt(ALPH.indexOf(ch));
    }
    
    private String increment(String password) {
        int i = password.length() - 1;
        password = password.substring(0, i) + nextLetter(password.charAt(i));
        while (password.charAt(i) == 'a' && i > 0) {
            i--;
            password = password.substring(0, i) + nextLetter(password.charAt(i)) + password.substring(i + 1);
        }
        return password;
    }
    
    private boolean isOk(final String password) {
        final Matcher m = Pattern.compile("([a-z])\\1").matcher(password);
        if (!(m.find() && m.find())) {
            return false;
        }
        for (int i = 0; i < password.length() - 3; i++) {
            if (password.charAt(i) == password.charAt(i + 1) - 1
                    && password.charAt(i + 1) == password.charAt(i + 2) - 1) {
                return true;
            }
        }
        return false;
    }
    
    private String solve(String password) {
        final List<Integer> h = List.of(password.indexOf('i'), password.indexOf('o'), password.indexOf('l'));
        if (!h.equals(List.of(-1, -1, -1))) {
            final int min = h.stream().mapToInt(Integer::intValue).filter(i -> i != -1).min().orElseThrow();
            if (min < password.length() - 1) {
                final int length = password.substring(min + 1).length();
                password = password.substring(0, min) + nextLetter(password.charAt(min));
                for (int i = 0; i < length; i++) {
                    password += 'a';
                }
            }
        } else {
            password = increment(password);
        }
        while (!isOk(password)) {
            password = increment(password);
        }
        return password;
    }
    
    @Override
    public String solvePart1() {
        return solve(this.input);
    }

    @Override
    public String solvePart2() {
        return solve(solve(this.input));
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2015_11.createDebug(List.of("")).isOk("heqaabcc") == true;
        assert AoC2015_11.createDebug(List.of("")).solve(TEST1).equals("abcdffaa");
        assert AoC2015_11.createDebug(List.of("")).solve(TEST2).equals("ghjaabcc");

        final Puzzle puzzle = Aocd.puzzle(2015, 11);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2015_11.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2015_11.create(puzzle.getInputData()).solvePart2())
        );
    }
    
    private static final String TEST1 = "abcdefgh";
    private static final String TEST2 = "ghijklmn";
}
