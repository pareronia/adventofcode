import static java.util.stream.Collectors.toList;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class AoC2021_18 extends AoCBase {
    
    private final List<Number> numbers;
    
    private AoC2021_18(final List<String> input, final boolean debug) {
        super(debug);
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
        
        static final class Leveler {
            public static Map<Integer, List<Number>> level(final Number number) {
                final Map<Integer, List<Number>> map = new HashMap<>();
                final Deque<State> q = new ArrayDeque<>();
                q.addFirst(new State(number, 0));
                while (!q.isEmpty()) {
                    final State s = q.pollFirst();
                    final Number n = s.number;
                    if (n instanceof Regular) {
                        continue;
                    }
                    map.merge(s.level, new ArrayList<>(List.of(n)), (l1, l2) -> {
                        l1.addAll(l2);
                        return l1;
                    });
                    if (n instanceof Pair) {
                        q.add(new State(((Pair) n).getLeft(), s.level + 1));
                        q.add(new State(((Pair) n).getRight(), s.level + 1));
                    }
                }
                return map;
            }
            
            @RequiredArgsConstructor
            private static final class State {
                private final AoC2021_18.Number number;
                private final int level;
            }
        }
        
        static final class PreOrderer {
        
            public static List<Number> preOrder(final Number number) {
               final ArrayList<Number> list = new ArrayList<>();
               doPreOrder(number, list);
               return list;
            }
            
            private static void doPreOrder(final Number number, final List<Number> list) {
                if (!(number instanceof Regular)) {
                    list.add(number);
                    doPreOrder(((Pair) number).getLeft(), list);
                    doPreOrder(((Pair) number).getRight(), list);
                }
            }
        }
        
        private static boolean isPairOfRegulars(final Number number) {
            return number instanceof Pair
                    && ((Pair) number).left instanceof Regular
                    && ((Pair) number).right instanceof Regular;
        }
        
        public static boolean explode3(final Number number, final int depth) {
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
            return explode3(pair.left, depth + 1) || explode3(pair.right, depth + 1);
        }
        
        public static void explode2(final Number number) {
            final Map<Integer, List<Number>> levels = Leveler.level(number);
            if (!levels.containsKey(4)) {
                return;
            }
            final List<Number> preOrder = PreOrderer.preOrder(number);
            final Pair toExplode = (Pair) levels.get(4).get(0);
            int idx = preOrder.indexOf(toExplode);
            Regular prev = null;
            Pair parent = null;
            while (--idx >= 0) {
                final Pair temp = (Pair) preOrder.get(idx);
                if (prev == null && levels.get(4).contains(temp)) {
                    if (temp.right instanceof Regular) {
                        prev = (Regular) temp.right;
                    } else if (temp.left instanceof Regular) {
                        prev = (Regular) temp.left;
                    }
                } else if (levels.get(3).contains(temp)) {
                    parent = temp;
                }
            }
            idx = preOrder.indexOf(toExplode);
            Regular next = null;
            while (++idx < preOrder.size()) {
                final Pair temp = (Pair) preOrder.get(idx);
                if (next == null && levels.get(4).contains(temp)) {
                    if (temp.left instanceof Regular) {
                        next = (Regular) temp.left;
                    } else if (temp.right instanceof Regular) {
                        next = (Regular) temp.right;
                    }
                }
            }
            assert parent != null;
//            assert !(prev == null && next == null); //?
            if (prev != null) {
                prev.value += ((Regular) toExplode.left).value;
            }
            if (next != null) {
                next.value += ((Regular) toExplode.right).value;
            }
            if (parent.left.equals(toExplode)) {
                parent.left = new Regular(0);
            } else if (parent.right.equals(toExplode)) {
                parent.right = new Regular(0);
            } else {
                throw new IllegalStateException();
            }
        }
        
        public static Pair explode(final Number number, final int level) {
            if (level == 3 && number instanceof Pair) {
                final Pair pair = (Pair) number;
                final Pair up;
                if (isPairOfRegulars(pair.left)) {
                    final int addLeft = ((Regular) ((Pair) pair.left).left).value;
                    final int addRight = ((Regular) ((Pair) pair.left).right).value;
                    up = new Pair(new Regular(addLeft), new Regular(addRight));
                    if (pair.right instanceof Regular) {
                        final Regular right = (Regular) pair.right;
                        pair.right = new Regular(right.value + addRight);
                        up.right = null;
                    }
                    pair.left = new Regular(0);
                } else if (isPairOfRegulars(pair.right)) {
                    final int addLeft = ((Regular) ((Pair) pair.right).left).value;
                    final int addRight = ((Regular) ((Pair) pair.right).right).value;
                    up = new Pair(new Regular(addLeft), new Regular(addRight));
                    if (pair.left instanceof Regular) {
                        final Regular left = (Regular) pair.left;
                        pair.left = new Regular(left.value + addLeft);
                        up.left = null;
                    }
                    pair.right = new Regular(0);
                } else {
                    throw new IllegalArgumentException();
                }
                
                return up;
            } else if (number instanceof Pair) {
                final Pair pair = ((Pair) number);
                Pair up = explode(pair.left, level + 1);
                if (up.left == null && up.right == null) {
                    up = explode(pair.right, level + 1);
                }
                final Regular addLeft = (Regular) up.left;
                final Regular addRight = (Regular) up.right;
                if (pair.left instanceof Regular && addLeft != null) {
                    final Regular left = (Regular) pair.left;
                    pair.left = new Regular(left.value + addLeft.value);
                    up.left = null;
                }
                if (pair.right instanceof Regular && addRight != null) {
                    final Regular right = (Regular) pair.right;
                    pair.right = new Regular(right.value + addRight.value);
                    up.right = null;
                }
                return up;
            } else {
                return new Pair(null, null);
            }
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
//            seen.add(number);
            if (number instanceof Pair) {
                final Pair pair = (Pair) number;
                doSplit(pair.left, seen, split);
                doSplit(pair.right, seen, split);
                return;
//            for (final Number child : List.of(pair.left, pair.right)) {
//                if (child instanceof Pair /*&& !seen.contains(child)*/) {
//                    doSplit(child, seen, split);
//                }
//            }
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
//
//            if (pair.left instanceof Regular) {
//                final int value = ((Regular) pair.left).value;
//                if (value >= 10) {
//                    pair.left = Pair.create(new Regular(value / 2), new Regular(value - value / 2));
//                    pair.left.parent = pair;
//                    split.add(number);
//                    return;
//                }
//            }
//            if (pair.right instanceof Regular) {
//                final int value = ((Regular) pair.right).value;
//                if (value >= 10) {
//                    pair.right = Pair.create(new Regular(value / 2), new Regular(value - value / 2));
//                    pair.right.parent = pair;
//                    split.add(number);
//                    return;
//                }
//            }
        }
    }
    
    static final class Reducer {
        private static Exploder exploder = new Exploder();
        private static Splitter splitter = new Splitter();
        static boolean debug;
        
        private static void log(final Object obj) {
            if (!debug) {
                return;
            }
            System.out.println(obj);
        }
        
        @SuppressWarnings("static-access")
        static void reduce(final Number number, final Exploder exploder, final Splitter splitter) {
            boolean changed = true;
            log("Reducing: " + number);
            while (changed) {
                final String beforeEx = number.toString();
                exploder.explode3(number, 0);
                final String afterEx = number.toString();
                changed = !beforeEx.equals(afterEx);
                if (changed) {
                    log("after explode: " + afterEx);
                    continue;
                }
                final String beforeSp = number.toString();
                splitter.split(number);
                final String afterSp = number.toString();
                changed = !beforeSp.equals(afterSp);
                if (changed) {
                    log("after split: " + afterSp);
                }
            }
        }
        
        public static void reduce(final Number number) {
            reduce(number, exploder, splitter);
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
    
    private Number solve1() {
        Number sum = this.numbers.get(0);
        for (int i = 1; i< this.numbers.size(); i ++) {
            sum = Adder.add(List.of(sum, this.numbers.get(i)));
            log("after addition:" + sum);
            Reducer.reduce(sum);
        }
        return sum;
    }
    
    @Override
    public Long solvePart1() {
        final Number sum = solve1();
        log("final sum is: " + sum);
        return Magnitude.magnitude(sum);
    }
    
    @Override
    public Integer solvePart2() {
        return null;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_18.create(List.of("[1,1]", "[2,2]", "[3,3]", "[4,4]", "[5,5]", "[6,6]")).solve1().toString().equals("[[[[5,0],[7,4]],[5,5]],[6,6]]");
        assert AoC2021_18.create(TEST1).solve1().toString().equals("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]");
        assert AoC2021_18.create(TEST2).solvePart1() == 4_140L;
//        assert AoC2021_18.create(TEST2).solvePart2() == null;

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