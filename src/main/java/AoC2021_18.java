import static java.util.stream.Collectors.toList;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AoC2021_18 extends AoCBase {
    
    private final List<Number> numbers;
    private final List<String> inputs;
    
    private AoC2021_18(final List<String> input, final boolean debug) {
        super(debug);
        this.inputs = input;
        Reducer.debug = debug;
        this.numbers = input.stream()
            .map(Parser::parse)
            .collect(toList());
        numbers.forEach(this::log);
    }
    
    static final class Parser {
    
        public static Number parse(final String line) {
            final Deque<Number> stack = new ArrayDeque<>();
            for (int i = 0; i < line.length(); i++) {
                final Character ch = line.charAt(i);
                if (ch == '[') {
                    continue;
                } else if (Character.isDigit(ch)) {
                    stack.push(new Regular(Character.digit(ch, 10)));
                } else if (ch == ',') {
                    continue;
                } else if (ch == ']') {
                    final Number right = stack.pop();
                    final Number left = stack.pop();
                    final Pair pair = Pair.create(left, right);
                    stack.push(pair);
                } else {
                    throw new IllegalArgumentException();
                }
            }
            assert stack.size() == 1;
            return stack.pop();
        }
    }
    
    public static final AoC2021_18 create(final List<String> input) {
        return new AoC2021_18(input, false);
    }

    public static final AoC2021_18 createDebug(final List<String> input) {
        return new AoC2021_18(input, true);
    }
    
    static final class Exploder {
        
        public static boolean explode(final Number number, final int depth) {
            if (number instanceof Regular) {
                return false;
            }
            final Pair pair = (Pair) number;
            if (depth == 4) {
                final Regular leftAdjacent = pair.leftAdjacent();
                final Regular rightAdjacent = pair.rightAdjacent();
                if (leftAdjacent != null) {
                    leftAdjacent.value += ((Regular) pair.left).getValue();
                }
                if (rightAdjacent != null) {
                    rightAdjacent.value += ((Regular) pair.right).getValue();
                }
                final Pair parent = (Pair) pair.parent;
                assert parent != null;
                if (Objects.equals(pair, parent.left)) {
                    parent.left = new Regular(0);
                    parent.left.parent = parent;
                } else if (Objects.equals(pair, parent.right)) {
                    parent.right = new Regular(0);
                    parent.right.parent = parent;
                } else {
                    throw new IllegalStateException();
                }
                return true;
            }
            return explode(pair.left, depth + 1) || explode(pair.right, depth + 1);
        }
    }
    
    static final class Splitter {
        public static void split(final Number number) {
           doSplit(number, new HashSet<>(), new HashSet<>());
        }
        
        private static void doSplit(final Number number, final Set<Number> seen, final Set<Number> split) {
            if (!split.isEmpty()) {
                return;
            }
            if (number instanceof Pair) {
                final Pair pair = (Pair) number;
                doSplit(pair.left, seen, split);
                doSplit(pair.right, seen, split);
                return;
            }
            final Regular regular = (Regular) number;
            final int value = regular.value;
            if (value >= 10) {
                final Pair pair = Pair.create(new Regular(value / 2), new Regular(value - value / 2));
                pair.parent = regular.parent;
                final Pair parent = (Pair) regular.parent;
                if (regular.equals(parent.left)) {
                    parent.left = pair;
                } else if (regular.equals(parent.right)) {
                    parent.right = pair;
                } else {
                    throw new IllegalStateException();
                }
                split.add(pair);
            }
        }
    }
    
    static final class Reducer {
        static boolean debug;
        
        private static void log(final Supplier<Object> supplier) {
            if (!debug) {
                return;
            }
            System.out.println(supplier.get());
        }
        
        static void reduce(final Number number) {
            boolean changed = true;
            log(() -> "Reducing: " + number);
            while (changed) {
                final String beforeEx = number.toString();
                Exploder.explode(number, 0);
                final String afterEx = number.toString();
                changed = !beforeEx.equals(afterEx);
                if (changed) {
                    log(() -> "after explode: " + afterEx);
                    continue;
                }
                final String beforeSp = number.toString();
                Splitter.split(number);
                final String afterSp = number.toString();
                changed = !beforeSp.equals(afterSp);
                if (changed) {
                    log(() -> "after split: " + afterSp);
                }
            }
        }
    }
    
    static final class Adder {

        public static Number add(final List<Number> numbers) {
            return numbers.stream()
                .reduce(Pair::create).orElseThrow();
        }
    }
    
    static class Magnitude {
        
        public static long magnitude(final Number number) {
            if (number instanceof Regular) {
                return ((Regular) number).value;
            }
            final Pair pair = (Pair) number;
            return 3 * magnitude(pair.left) + 2 * magnitude(pair.right);
        }
    }
    private Number solve(final List<Number> numbers) {
        Number sum = numbers.get(0);
        for (int i = 1; i< numbers.size(); i++) {
            sum = Adder.add(List.of(sum, numbers.get(i)));
            Reducer.reduce(sum);
        }
        return sum;
    }
    
    private Number solve1() {
        return solve(this.numbers);
    }
    
    @Override
    public Long solvePart1() {
        final Number sum = solve1();
        log("final sum is: " + sum);
        return Magnitude.magnitude(sum);
    }
    
    @Override
    public Long solvePart2() {
        return this.inputs.stream()
            .flatMap(s1 -> this.inputs.stream()
                    .filter(s2 -> !s2.equals(s1))
                    .flatMap(s2 -> {
                        final Number n1 = Parser.parse(s1);
                        final Number n2 = Parser.parse(s2);
                        final Number n3 = Parser.parse(s2);
                        final Number n4 = Parser.parse(s1);
                        return Stream.of(List.of(n1, n2), List.of(n3, n4));
                    }))
            .peek(this::log)
            .map(this::solve)
            .peek(this::log)
            .map(Magnitude::magnitude)
            .peek(this::log)
            .mapToLong(Long::valueOf)
            .max().orElseThrow();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_18.create(List.of("[1,1]", "[2,2]", "[3,3]", "[4,4]", "[5,5]", "[6,6]")).solve1().toString().equals("[[[[5,0],[7,4]],[5,5]],[6,6]]");
        assert AoC2021_18.create(TEST1).solve1().toString().equals("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]");
        assert AoC2021_18.create(TEST2).solvePart1() == 4_140L;
        assert AoC2021_18.create(TEST2).solvePart2() == 3_993L;

        final Puzzle puzzle = Aocd.puzzle(2021, 18);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2021_18.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2021_18.create(puzzle.getInputData()).solvePart2())
        );
    }

    private static final List<String> TEST1 = splitLines(
        "[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]\r\n" +
        "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]\r\n" +
        "[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]\r\n" +
        "[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]\r\n" +
        "[7,[5,[[3,8],[1,4]]]]\r\n" +
        "[[2,[2,2]],[8,[8,1]]]\r\n" +
        "[2,9]\r\n" +
        "[1,[[[9,3],9],[[9,0],[0,7]]]]\r\n" +
        "[[[5,[7,4]],7],1]\r\n" +
        "[[[[4,2],2],6],[8,7]]"
    );
    private static final List<String> TEST2 = splitLines(
        "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]\r\n" +
        "[[[5,[2,8]],4],[5,[[9,9],0]]]\r\n" +
        "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]\r\n" +
        "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]\r\n" +
        "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]\r\n" +
        "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]\r\n" +
        "[[[[5,4],[7,7]],8],[[8,3],8]]\r\n" +
        "[[9,3],[[9,9],[6,[4,9]]]]\r\n" +
        "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]\r\n" +
        "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]"
    );
    
    static abstract class Number {
        protected Number parent = null;
    }
    
    @AllArgsConstructor
    @Getter
    static final class Regular extends Number {
        private int value = -1;
        
        @Override
        public String toString() {
            return String.valueOf(this.value);
        }
    }
    
    @AllArgsConstructor
    @Getter
    static final class Pair extends Number {
        private Number left = null;
        private Number right = null;
        
        public static Pair create(final Number left, final Number right) {
            final Pair pair = new Pair(left, right);
            left.parent = pair;
            right.parent = pair;
            return pair;
        }
        
        public Regular leftAdjacent() {
            final Pair parent = getParent();
            if (parent == null) {
                return null;
            }
            if (this == parent.right) {
                Number left = parent.left;
                while (left instanceof Pair) {
                    left = ((Pair) left).right;
                }
                return (Regular) left;
            }
            return parent.leftAdjacent();
        }

        public Regular rightAdjacent() {
            final Pair parent = getParent();
            if (parent == null) {
                return null;
            }
            if (this == parent.left) {
                Number right = parent.right;
                while (right instanceof Pair) {
                    right = ((Pair) right).left;
                }
                return (Regular) right;
            }
            return parent.rightAdjacent();
        }
        
        public Pair getParent() {
            return (Pair) this.parent;
        }
        
        @Override
        public String toString() {
            return String.format("[%s,%s]", this.left, this.right);
        }
    }
}