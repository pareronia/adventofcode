import static java.lang.Math.abs;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2022_20 extends AoCBase {
    
    private static final long ENCRYPTION_KEY = 811589153L;
    private final List<Integer> numbers;
    
    private AoC2022_20(final List<String> input, final boolean debug) {
        super(debug);
        this.numbers = input.stream().map(Integer::valueOf).collect(toList());
    }
    
    public static final AoC2022_20 create(final List<String> input) {
        return new AoC2022_20(input, false);
    }

    public static final AoC2022_20 createDebug(final List<String> input) {
        return new AoC2022_20(input, true);
    }
    
    private void print(final Num zero) {
        if (!this.debug) {
            return;
        }
        final var lst = new ArrayList<Long>();
        Num tmp = zero;
        do {
            lst.add(tmp.num);
            tmp = tmp.next;
        } while (tmp.num != 0);
        log(lst.stream().map(String::valueOf).collect(joining(", ")));
    }
    
    private long solve(final int rounds, final long factor) {
        final int size = this.numbers.size();
        final var nums = IntStream.range(0, size)
                .mapToLong(i -> factor * this.numbers.get(i))
                .mapToObj(Num::new)
                .collect(toList());
        Num zero = null;
        for (int i = 1; i <= nums.size(); i++) {
            if (nums.get(i - 1).num == 0) {
                zero = nums.get(i - 1);
            }
            nums.get(i % size).prev = nums.get((i - 1) % size);
            nums.get(i % size).next = nums.get((i + 1) % size);
        }
        print(zero);

        for (int i = 0; i < rounds; i++) {
            log("Round " + (i + 1));
            for (final Num to_move : nums) {
                if (to_move.num > 0) {
                    final int amount = (int) (to_move.num % (size - 1));
                    Num move_to = to_move.prev;
                    to_move.remove();
                    move_to = move_to.next(amount);
                    to_move.insertAfter(move_to);
                } else if (to_move.num < 0) {
                    final int amount = (int) (abs(to_move.num) % (size - 1) + 1);
                    Num move_to = to_move.next;
                    to_move.remove();
                    move_to = move_to.prev(amount);
                    to_move.insertAfter(move_to);
                }
            }
            print(zero);
        }

        long ans = 0;
        Num n = zero;
        for (int i = 0; i < 3; i++) {
            n = n.next(1000);
            ans += n.num;
        }
        return ans;
    }
    
    @Override
    public Long solvePart1() {
        return solve(1, 1L);
    }

    @Override
    public Long solvePart2() {
        return solve(10, ENCRYPTION_KEY);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_20.createDebug(TEST).solvePart1() == 3;
        assert AoC2022_20.createDebug(TEST).solvePart2() == 1_623_178_306;

        final Puzzle puzzle = Aocd.puzzle(2022, 20);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_20.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_20.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines("""
        1
        2
        -3
        3
        -2
        0
        4
        """);
    
    private static final class Num {
        private final long num;
        private Num next;
        private Num prev;

        public Num(final long num) {
            this.num = num;
        }
        
        public void remove() {
            this.prev.next = this.next;
            this.next.prev = this.prev;
            this.next = null;
            this.prev = null;
        }

        public void insertAfter(final Num move_to) {
            final Num before = move_to;
            final Num after = move_to.next;
            before.next = this;
            this.prev = before;
            after.prev = this;
            this.next = after;
        }
        
        public Num prev(int amount) {
            Num ans = this;
            while (amount-- > 0) {
                ans = ans.prev;
            }
            return ans;
        }
        
        public Num next(int amount) {
            Num ans = this;
            while (amount-- > 0) {
                ans = ans.next;
            }
            return ans;
        }
    }
}
