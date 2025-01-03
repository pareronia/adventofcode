import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static com.github.pareronia.aoc.IterTools.enumerateFrom;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2024_09
        extends SolutionBase<int[], Long, Long> {

    public static final long[] TRIANGLE = {0, 0, 1, 3, 6, 10, 15, 21, 28, 36};
    
    private AoC2024_09(final boolean debug) {
        super(debug);
    }
    
    public static AoC2024_09 create() {
        return new AoC2024_09(false);
    }
    
    public static AoC2024_09 createDebug() {
        return new AoC2024_09(true);
    }
    
    @Override
    protected int[] parseInput(final List<String> inputs) {
        return Utils.asCharacterStream(inputs.get(0))
                .mapToInt(ch -> ch - '0')
                .toArray();
    }
    
    public long solve(final int[] input, final Mode mode) {
        final List<File> files = new ArrayList<>(input.length * 10);
        final List<PriorityQueue<Integer>> freeBySize = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            freeBySize.add(new PriorityQueue<>());
        }
        boolean isFree = false;
        int id = 0;
        int pos = 0;
        for (final int n : input) {
            if (isFree) {
                freeBySize.get(n).add(pos);
            } else {
                files.addAll(mode.createFiles(id, pos, n));
                id++;
            }
            pos += n;
            isFree = !isFree;
        }
        long ans = 0;
        for (int j = files.size() - 1; j >= 0; j--) {
            final File file = files.get(j);
            enumerateFrom(file.size, freeBySize.stream().skip(file.size))
                .filter(e -> !e.value().isEmpty())
                .min((e1, e2) -> Integer.compare(e1.value().peek(), e2.value().peek()))
                .filter(e -> e.value().peek() < file.pos)
                .ifPresent(e -> {
                    final int free_size = e.index();
                    final int free_pos = e.value().poll();
                    file.pos = free_pos;
                    if (file.size < free_size) {
                        freeBySize.get(free_size - file.size)
                            .add(file.pos + file.size);
                    }
                });
            ans += mode.fileChecksum(file);
        }
        return ans;
    }
    
    @Override
    public Long solvePart1(final int[] input) {
        return solve(input, Mode.MODE_1);
    }

    @Override
    public Long solvePart2(final int[] input) {
        return solve(input, Mode.MODE_2);
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "1928"),
        @Sample(method = "part2", input = TEST, expected = "2858"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2024_09.create().run();
    }

    private static final String TEST = "2333133121414131402";

    private static class File {
        int id;
        int pos;
        int size;
        
        public File(final int id, final int pos, final int size) {
            this.id = id;
            this.pos = pos;
            this.size = size;
        }
    }

    private enum Mode {
        MODE_1,
        MODE_2;

        public List<File> createFiles(
                final int id, final int pos, final int size
        ) {
            return switch (this) {
                case MODE_1 -> range(size).intStream()
                                    .mapToObj(i -> new File(id, pos + i, 1))
                                    .toList();
                case MODE_2 -> List.of(new File(id, pos, size));
            };
        }

        public long fileChecksum(final File f) {
            return switch (this) {
                case MODE_1 -> (long) f.id * f.pos;
                case MODE_2 -> f.id * (f.pos * f.size + TRIANGLE[f.size]);
            };
        }
    }
}