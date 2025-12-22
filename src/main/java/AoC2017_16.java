import static java.util.stream.Collectors.toList;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2017_16 extends SolutionBase<AoC2017_16.Dance, String, String> {

    private static final String PROGRAMS = "abcdefghijklmnop";

    private AoC2017_16(final boolean debug) {
        super(debug);
    }

    public static AoC2017_16 create() {
        return new AoC2017_16(false);
    }

    public static AoC2017_16 createDebug() {
        return new AoC2017_16(true);
    }

    @Override
    protected Dance parseInput(final List<String> inputs) {
        return Dance.fromInput(inputs.getFirst());
    }

    @Override
    public String solvePart1(final Dance dance) {
        return dance.execute(PROGRAMS, 1);
    }

    @Override
    public String solvePart2(final Dance dance) {
        int cnt = 0;
        String ans = PROGRAMS;
        while (true) {
            ans = dance.execute(ans, 1);
            cnt++;
            if (PROGRAMS.equals(ans)) {
                break;
            }
        }
        return dance.execute(PROGRAMS, 1_000_000_000 % cnt);
    }

    @Override
    protected void samples() {
        final String programs = "abcde";
        assert "cdeab".equals(String.valueOf(Spin.fromInput("3").execute(programs.toCharArray())));
        assert "abced"
                .equals(String.valueOf(Exchange.fromInput("3/4").execute(programs.toCharArray())));
        assert "cbade"
                .equals(String.valueOf(Partner.fromInput("a/c").execute(programs.toCharArray())));
        final AoC2017_16 test = createDebug();
        final Dance dance = test.parseInput(List.of("s1,x3/4,pe/b"));
        assert "baedc".equals(dance.execute(programs, 1));
        assert "ceadb".equals(dance.execute(programs, 2));
    }

    public static void main(final String[] args) throws Exception {
        create().run();
    }

    @FunctionalInterface
    private interface Move {

        @SuppressWarnings("PMD.UseVarargs")
        char[] execute(char[] programs);
    }

    private record Spin(int amount) implements Move {

        public static Spin fromInput(final String input) {
            return new Spin(Integer.parseInt(input));
        }

        @Override
        public char[] execute(final char[] programs) {
            final char[] ans = new char[programs.length];
            System.arraycopy(programs, programs.length - amount, ans, 0, amount);
            System.arraycopy(programs, 0, ans, amount, programs.length - amount);
            return ans;
        }
    }

    private record Exchange(int pos1, int pos2) implements Move {

        public static Exchange fromInput(final String input) {
            final StringSplit sp = StringOps.splitOnce(input, "/");
            return new Exchange(Integer.parseInt(sp.left()), Integer.parseInt(sp.right()));
        }

        @Override
        public char[] execute(final char[] programs) {
            final char tmp = programs[this.pos2];
            programs[this.pos2] = programs[this.pos1];
            programs[this.pos1] = tmp;
            return programs;
        }
    }

    private record Partner(Character program1, Character program2) implements Move {

        public static Partner fromInput(final String input) {
            final StringSplit sp = StringOps.splitOnce(input, "/");
            return new Partner(sp.left().charAt(0), sp.right().charAt(0));
        }

        @Override
        public char[] execute(final char[] programs) {
            int pos1 = -1;
            int pos2 = -1;
            for (int i = 0; i < programs.length; i++) {
                if (programs[i] == this.program1) {
                    pos1 = i;
                }
                if (programs[i] == this.program2) {
                    pos2 = i;
                }
                if (pos1 >= 0 && pos2 >= 0) {
                    break;
                }
            }
            final char tmp = programs[pos2];
            programs[pos2] = programs[pos1];
            programs[pos1] = tmp;
            return programs;
        }
    }

    record Dance(List<Move> moves) {

        public static Dance fromInput(final String input) {
            final List<Move> moves =
                    Arrays.stream(input.split(","))
                            .map(
                                    s ->
                                            switch (s.substring(0, 1)) {
                                                case "s" -> Spin.fromInput(s.substring(1));
                                                case "x" -> Exchange.fromInput(s.substring(1));
                                                default -> Partner.fromInput(s.substring(1));
                                            })
                            .collect(toList());
            return new Dance(moves);
        }

        public String execute(final String programsIn, final int reps) {
            char[] programs = programsIn.toCharArray();
            for (int i = 0; i < reps; i++) {
                for (final Move move : this.moves) {
                    programs = move.execute(programs);
                }
            }
            return String.valueOf(programs);
        }
    }
}
