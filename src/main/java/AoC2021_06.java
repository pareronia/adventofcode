import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.pareronia.aocd.Aocd;

public class AoC2021_06 extends AoCBase {
    
    private final List<Integer> initial;
    
    private AoC2021_06(final List<String> input, final boolean debug) {
        super(debug);
        assert input.size() == 1;
        this.initial = Arrays.stream(input.get(0).split(","))
                .map(Integer::valueOf)
                .collect(toList());
    }

    public static final AoC2021_06 create(final List<String> input) {
        return new AoC2021_06(input, false);
    }

    public static final AoC2021_06 createDebug(final List<String> input) {
        return new AoC2021_06(input, true);
    }
    
    @Override
    public Integer solvePart1() {
        final List<Integer> list = new ArrayList<>(this.initial);
        for (int i = 0; i < 80; i++) {
            final List<Integer> add = new ArrayList<>();
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j) == 0) {
                    add.add(8);
                    list.set(j, 6);
                } else {
                    list.set(j, list.get(j) - 1);
                }
            }
            list.addAll(add);
        }
        return list.size();
    }

    @Override
    public Integer solvePart2() {
        return 0;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_06.create(TEST).solvePart1() == 5934;
        assert AoC2021_06.create(TEST).solvePart2() == 0;

        final List<String> input = Aocd.getData(2021, 6);
        lap("Part 1", () -> AoC2021_06.create(input).solvePart1());
        lap("Part 2", () -> AoC2021_06.create(input).solvePart2());
    }

    private static final List<String> TEST = splitLines(
        "3,4,3,1,2"
    );
}
