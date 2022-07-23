import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.pareronia.aocd.Aocd;

import lombok.RequiredArgsConstructor;

public final class AoC2017_16 extends AoCBase {
    
    private static final String PROGRAMS = "abcdefghijklmnop";

    private final transient List<Move> moves;

    private AoC2017_16(final List<String> inputs, final boolean debug) {
        super(debug);
        assert inputs.size() == 1;
        this.moves = Arrays.stream(inputs.get(0).split(","))
            .map(s -> {
                if (s.startsWith("s")) {
                    final int c = Integer.parseInt(s.substring(1));
                    return new Spin(c);
                } else if (s.startsWith("x")) {
                    final int a = Integer.parseInt(s.substring(1).split("/")[0]);
                    final int b = Integer.parseInt(s.substring(1).split("/")[1]);
                    return new Exchange(a, b);
                } else if (s.startsWith("p")) {
                    final Character a = s.substring(1).split("/")[0].charAt(0);
                    final Character b = s.substring(1).split("/")[1].charAt(0);
                    return new Partner(a, b);
                } else  {
                    throw new IllegalArgumentException();
                }
            })
            .collect(toList());
    }

    public static AoC2017_16 create(final List<String> input) {
        return new AoC2017_16(input, false);
    }

    public static AoC2017_16 createDebug(final List<String> input) {
        return new AoC2017_16(input, true);
    }
    
    private Map<Character, Integer> fromString(final String string) {
        final Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < string.length(); i++) {
            map.put(string.charAt(i), i);
        }
        return map;
    }

    private String toString(final Map<Character, Integer> map) {
        return map.entrySet().stream()
            .sorted((e1, e2) -> Integer.compare(e1.getValue(), e2.getValue()))
            .map(Entry::getKey)
            .collect(toAString());
    }

    private void swap(final Map<Character, Integer> map, final Character prog1, final Character prog2) {
        final int tmp = map.get(prog1);
        map.put(prog1, map.get(prog2));
        map.put(prog2, tmp);
    }
    
    private Map<Character, Integer> dance(final Map<Character, Integer> map) {
        for (final Move move : this.moves) {
            if (move instanceof Spin) {
                final int amount = ((Spin) move).amount;
                for (final Character ch : map.keySet()) {
                    map.put(ch, (map.get(ch) + amount) % map.size());
                }
            } else if (move instanceof Exchange) {
                final int pos1 = ((Exchange) move).pos1;
                final int pos2 = ((Exchange) move).pos2;
                Character program1 = null;
                Character program2 = null;
                for (final Character ch : map.keySet()) {
                    if (map.get(ch) == pos1) {
                        program1 = ch;
                    } else if (map.get(ch) == pos2) {
                        program2 = ch;
                    }
                }
                swap(map, program1, program2);
            } else if (move instanceof Partner) {
                final Character program1 = ((Partner) move).program1;
                final Character program2 = ((Partner) move).program2;
                swap(map, program1, program2);
            }
        }
        return map;
    }
    
    private String solve(final String string, final int reps) {
        Map<Character, Integer> map = fromString(string);
        for (int i = 0; i < reps; i++) {
            map = dance(map);
        }
        return toString(map);
    }
    
    @Override
    public String solvePart1() {
        return solve(PROGRAMS, 1);
    }
    
    @Override
    public String solvePart2() {
        int cnt = 0;
        String ans = PROGRAMS;
        while (true) {
            ans = solve(ans, 1);
            cnt++;
            if (ans.equals(PROGRAMS)) {
                break;
            }
        }
        return solve(PROGRAMS, 1_000_000_000 % cnt);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_16.createDebug(TEST).solve("abcde", 1).equals("baedc");
        assert AoC2017_16.createDebug(TEST).solve("abcde", 2).equals("ceadb");

        final List<String> input = Aocd.getData(2017, 16);
        lap("Part 1", () -> AoC2017_16.create(input).solvePart1());
        lap("Part 2", () -> AoC2017_16.create(input).solvePart2());
    }
    
    private static final List<String> TEST = splitLines(
            "s1,x3/4,pe/b"
    );
    
    private static abstract class Move {
    }
    
    @RequiredArgsConstructor
    private static final class Spin extends Move {
        private final int amount;
    }

    @RequiredArgsConstructor
    private static final class Exchange extends Move {
        private final int pos1;
        private final int pos2;
    }
    
    @RequiredArgsConstructor
    private static final class Partner extends Move {
        private final Character program1;
        private final Character program2;
    }
}
