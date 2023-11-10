import static com.github.pareronia.aoc.IntegerSequence.Range.range;

import java.util.List;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.codec.MD5;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import lombok.RequiredArgsConstructor;

public final class AoC2015_04
        extends SolutionBase<AdventCoinsMiner, Integer, Integer> {

    private AoC2015_04(final boolean debug) {
        super(debug);
    }

    public static AoC2015_04 create() {
        return new AoC2015_04(false);
    }

    public static AoC2015_04 createDebug() {
        return new AoC2015_04(true);
    }

    @Override
    protected AdventCoinsMiner parseInput(final List<String> inputs) {
        assert inputs.size() == 1;
        return new AdventCoinsMiner(inputs.get(0));
    }

    @Override
    public Integer solvePart1(final AdventCoinsMiner miner) {
        return miner.findMd5StartingWithZeroes(5);
    }

    @Override
    public Integer solvePart2(final AdventCoinsMiner miner) {
        return miner.findMd5StartingWithZeroes(6);
    }

    @Override
    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "609043"),
        @Sample(method = "part1", input = TEST2, expected = "1048970"),
    })
    public void samples() {
        super.samples();
    }

    public static void main(final String[] args) throws Exception {

        AoC2015_04.create().run();
    }

    private static final String TEST1 = "abcdef";
    private static final String TEST2 = "pqrstuv";
}
    
@RequiredArgsConstructor
final class AdventCoinsMiner {
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