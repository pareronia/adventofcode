import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2021_21 extends AoCBase {
    
    private final int p1;
    private final int p2;
    
    private AoC2021_21(final List<String> input, final boolean debug) {
        super(debug);
        assert input.size() == 2;
        this.p1 = Character.digit(input.get(0).charAt(input.get(0).length() - 1), 10);
        this.p2 = Character.digit(input.get(1).charAt(input.get(1).length() - 1), 10);
        log(p1);
        log(p2);
    }
    
    public static final AoC2021_21 create(final List<String> input) {
        return new AoC2021_21(input, false);
    }

    public static final AoC2021_21 createDebug(final List<String> input) {
        return new AoC2021_21(input, true);
    }
    
    @Override
    public Integer solvePart1() {
        int score1 = 0;
        int cnt = 0;
        int pos1 = this.p1 - 1;
        int score2 = 0;
        int pos2 = this.p2 - 1;
        int die = 0;
        while (true) {
            final List<Integer> throw1 = List.of(1 + (die++ % 100), 1 + (die++ % 100), 1 + (die++ % 100));
            String s1 = "Player 1 throws " + StringUtils.join(throw1, "+");
            cnt += 3;
            pos1 = (pos1 + throw1.get(0) + throw1.get(1) + throw1.get(2)) % 10;
            score1 += pos1 + 1;
            s1 += " and moves to space " + (pos1 + 1) + " for a total score of " + score1;
            log(s1);
            if (score1 >= 1000) {
                break;
            }
            final List<Integer> throw2 = List.of(1 + (die++ % 100), 1 + (die++ % 100), 1 + (die++ % 100));
            String s2 = "Player 2 throws " + StringUtils.join(throw2, "+");
            cnt += 3;
            pos2 = (pos2 + throw2.get(0) + throw2.get(1) + throw2.get(2)) % 10;
            score2 += pos2 + 1;
            s2 += " and moves to space " + (pos2 + 1) + " for a total score of " + score2;
            log(s2);
            if (score2 >= 1000) {
                break;
            }
        }
        if (score1 < score2) {
            return score1 * cnt;
        } else {
            return score2 * cnt;
        }
    }
    
    @Override
    public Integer solvePart2() {
        return null;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_21.createDebug(TEST).solvePart1() == 739785;
        assert AoC2021_21.create(TEST).solvePart2() == null;

        final Puzzle puzzle = Aocd.puzzle(2021, 21);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2021_21.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2021_21.create(puzzle.getInputData()).solvePart2())
        );
    }

    private static final List<String> TEST = splitLines(
        "Player 1 starting position: 4\r\n" +
        "Player 2 starting position: 8"
    );
}