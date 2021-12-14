import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summarizingLong;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class AoC2021_14 extends AoCBase {
    
    private final String template;
    private final Map<String, String> rules;

    private AoC2021_14(final List<String> input, final boolean debug) {
        super(debug);
        this.template = input.get(0);
        this.rules = input.subList(2, input.size()).stream()
            .map(s -> s.split(" -> "))
            .collect(toMap(sp -> sp[0], sp -> sp[1]));
        log(template);
        log(rules);
    }
    
    public static final AoC2021_14 create(final List<String> input) {
        return new AoC2021_14(input, false);
    }

    public static final AoC2021_14 createDebug(final List<String> input) {
        return new AoC2021_14(input, true);
    }
    
    @Override
    public Long solvePart1() {
        final Polymer polymer = Polymer.create(this.template.toCharArray());
        polymer.elements().forEach(this::log);
        for (int i = 0; i < 10; i++) {
            polymer.grow(this.rules);
        }
        final HashMap<Character, Long> counters = polymer.elements()
            .collect(groupingBy(e -> e.getName(), HashMap::new, counting()));
        log(counters);
        final LongSummaryStatistics summary = counters.values().stream()
                .collect(summarizingLong(Long::valueOf));
        final long ans = summary.getMax() - summary.getMin();
        log(ans);
        return ans;
    }
    
    @Override
    public Integer solvePart2() {
        return 0;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_14.createDebug(TEST).solvePart1() == 1588;
        assert AoC2021_14.create(TEST).solvePart2() == 0;

        final Puzzle puzzle = Aocd.puzzle(2021, 14);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2021_14.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2021_14.create(puzzle.getInputData()).solvePart2())
        );
    }

    private static final List<String> TEST = splitLines(
        "NNCB\r\n" +
        "\r\n" +
        "CH -> B\r\n" +
        "HH -> N\r\n" +
        "CB -> H\r\n" +
        "NH -> C\r\n" +
        "HB -> C\r\n" +
        "HC -> B\r\n" +
        "HN -> C\r\n" +
        "NN -> C\r\n" +
        "BH -> H\r\n" +
        "NC -> B\r\n" +
        "NB -> B\r\n" +
        "BN -> B\r\n" +
        "BB -> N\r\n" +
        "BC -> B\r\n" +
        "CC -> N\r\n" +
        "CN -> C"
    );
    
    @RequiredArgsConstructor
    @Getter
    @ToString
    private static final class Element {
        private final char name;
        @ToString.Exclude
        private Element next;
    }
    
    @RequiredArgsConstructor
    @Setter
    @Getter
    private static final class Polymer implements Iterable<Element> {
        private Element head;
        
        public static Polymer create(final char[] elements) {
            final List<Element> list = Utils.asCharacterStream(String.valueOf(elements))
                .map(Element::new)
                .collect(toList());
            for (int i = 0; i < list.size() - 1; i++) {
                list.get(i).next = list.get(i + 1);
            }
            final Polymer polymer = new Polymer();
            polymer.setHead(list.get(0));
            return polymer;
        }
        
        public void grow(final Map<String, String> rules) {
            Element curr = this.head;
            while (curr != null && curr.next != null) {
                final char[] key = new char[2];
                key[0] = curr.name;
                key[1] = curr.next.name;
                final String keyString = String.valueOf(key);
                final String string = rules.get(keyString);
                final Element toAdd = new Element(string.charAt(0));
                toAdd.next = curr.next;
                curr.next = toAdd;
                curr = curr.next.next;
            }
        }
        
        @Override
        public Iterator<Element> iterator() {
            return new Iterator<>() {
                private Element curr = Polymer.this.getHead();
                
                @Override
                public Element next() {
                    final Element ans = curr;
                    curr = curr.next;
                    return ans;
                }
                
                @Override
                public boolean hasNext() {
                    return curr != null;
                }
            };
        }
        
        public Stream<Element> elements() {
            final Iterable<Element> iterable = () -> this.iterator();
            return StreamSupport.stream(iterable.spliterator(), false);
        }
    }
}