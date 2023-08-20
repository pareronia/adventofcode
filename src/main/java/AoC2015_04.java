import static com.github.pareronia.aoc.IntegerSequence.Range.range;

import java.util.List;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.codec.MD5;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.RequiredArgsConstructor;

public final class AoC2015_04 extends AoCBase {

    private final AdventCoinsMiner miner;

    private AoC2015_04(final List<String> inputs, final boolean debug) {
        super(debug);
        assert inputs.size() == 1;
        this.miner = new AdventCoinsMiner(inputs.get(0));
    }

    public static AoC2015_04 create(final List<String> input) {
        return new AoC2015_04(input, false);
    }

    public static AoC2015_04 createDebug(final List<String> input) {
        return new AoC2015_04(input, true);
    }
    
    @Override
    public Integer solvePart1() {
        return this.miner.findMd5StartingWithZeroes(5);
    }

    @Override
    public Integer solvePart2() {
        return this.miner.findMd5StartingWithZeroes(6);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2015_04.createDebug(TEST1).solvePart1() == 609043;
        assert AoC2015_04.createDebug(TEST2).solvePart1() == 1048970;

        final Puzzle puzzle = Aocd.puzzle(2015, 4);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2015_04.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2015_04.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST1 = splitLines("abcdef");
    private static final List<String> TEST2 = splitLines("pqrstuv");
    
    @RequiredArgsConstructor
    private static final class AdventCoinsMiner {
        private final String secretKey;
    
        private boolean checkZeroes(final byte[] digest, final int zeroes) {
            int cnt = 0;
            for (final int j : range(zeroes / 2 + zeroes % 2)) {
                final byte c = digest[j];
                if ((c & 0xF0) != 0) {
                    break;
                }
                cnt++;
                if ((c & 0x0F) != 0) {
                    break;
                }
                cnt++;
            }
            return cnt == zeroes;
        }
        
        public int findMd5StartingWithZeroes(final int zeroes) {
            return IntStream.iterate(1, i -> i + 1)
                .dropWhile(i -> {
                    final String data = this.secretKey + String.valueOf(i);
                    return !checkZeroes(MD5.md5(data), zeroes);
                })
                .findFirst().getAsInt();
        }
    }
}