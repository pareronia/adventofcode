import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.tuple.Tuples;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;

public final class AoC2017_10 extends AoCBase {

    private static final List<Integer> SEED = Collections.unmodifiableList(
            Stream.iterate(0, i -> i < 256, i -> i + 1).collect(toList()));

    private final transient String input;

    private AoC2017_10(final List<String> inputs, final boolean debug) {
        super(debug);
        assert inputs.size() == 1;
        this.input = inputs.get(0);
    }

    public static AoC2017_10 create(final List<String> input) {
        return new AoC2017_10(input, false);
    }

    public static AoC2017_10 createDebug(final List<String> input) {
        return new AoC2017_10(input, true);
    }
    
    private void reverse(final List<Integer> elements, final int start, final int length) {
        final int size = elements.size();
        for (int i = 0; i < length / 2; i++) {
            final int first = (start + i) % size;
            final int second = (start + length - 1 - i) % size;
            final Integer temp = elements.get(first);
            elements.set(first, elements.get(second));
            elements.set(second, temp);
        }
    }
    
    private Pair<Integer, Integer> round(
            final List<Integer> elements,
            final List<Integer> lengths,
            int cur,
            int skip) {
        final int size = elements.size();
        for (final int len : lengths) {
            reverse(elements, cur, len);
            cur = (cur + len + skip) % size;
            skip++;
        }
        return Tuples.pair(cur, skip);
    }
    
    private Integer solve1(final List<Integer> elements) {
        final List<Integer> lengths = Arrays.stream(this.input.split(","))
                .map(Integer::valueOf)
                .collect(toList());
        round(elements, lengths, 0, 0);
        return elements.get(0) * elements.get(1);
    }
    
    @Override
    public Integer solvePart1() {
        return solve1(new ArrayList<>(SEED));
    }
    
    @Override
    public String solvePart2() {
        final List<Integer> lengths = new ArrayList<>();
        Utils.asCharacterStream(this.input)
                .map(c -> (int) c.charValue())
                .collect(toCollection(() -> lengths));
        lengths.addAll(List.of(17, 31, 73, 47, 23));
        final List<Integer> elements = new ArrayList<>(SEED);
        Integer cur = 0;
        Integer skip = 0;
        for (int i = 0; i < 64; i++) {
            final Pair<Integer, Integer> result
                    = round(elements, lengths, cur, skip);
            cur = result.getOne();
            skip = result.getTwo();
        }
        final int[] dense = new int[16];
        for (int i = 0; i < 16; i++) {
            for (int j = i * 16; j < i * 16 + 16; j++) {
                dense[i] ^= elements.get(j);
            }
        }
        return Arrays.stream(dense)
                .boxed()
                .map(Integer::toHexString)
                .map(s -> StringUtils.leftPad(s, 2, '0'))
                .collect(joining());
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_10.createDebug(splitLines("3,4,1,5")).solve1(asList(0, 1, 2, 3, 4)) == 12;
        assert AoC2017_10.createDebug(splitLines("AoC 2017")).solvePart2().equals("33efeb34ea91902bb2f59c9920caa6cd");
        assert AoC2017_10.createDebug(splitLines("1,2,3")).solvePart2().equals("3efbe78a8d82f29979031a4aa0b16a9d");
        assert AoC2017_10.createDebug(splitLines("1,2,4")).solvePart2().equals("63960835bcdc130f0b66d7ff4f6a5a8e");

        final List<String> input = Aocd.getData(2017, 10);
        lap("Part 1", () -> AoC2017_10.create(input).solvePart1());
        lap("Part 2", () -> AoC2017_10.create(input).solvePart2());
    }
}
