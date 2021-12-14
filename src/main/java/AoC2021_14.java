import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summarizingLong;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Set;
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

    private Long solve(final int cycles) {
        final Polymer polymer = Polymer.create(this.template.toCharArray());
//        polymer.elements().forEach(this::log);
        for (int i = 0; i < cycles; i++) {
            polymer.grow(this.rules);
        }
        final Map<Character, Long> counters = polymer.elements()
            .collect(groupingBy(e -> e.getName(), HashMap::new, counting()));
//        log(counters);
        final LongSummaryStatistics summary = counters.values().stream()
                .collect(summarizingLong(Long::valueOf));
        final long ans = summary.getMax() - summary.getMin();
        log(ans);
        return ans;
    }
    
    private Long solveBis(final int cycles) {
        Map<String, Long> pairCounters = new HashMap<>();
        for (int i = 1; i < this.template.length(); i++) {
            final String pair = String.valueOf(new char[] {
                    this.template.toCharArray()[i - 1],
                    this.template.toCharArray()[i]});
            pairCounters.merge(pair, 1L, Long::sum);
        }
        log(pairCounters);
        final Map<Character, Long> elemCounters = new HashMap<>();
        for (int i = 0; i < this.template.length(); i++) {
            elemCounters.merge(this.template.charAt(i), 1L, Long::sum);
        }
        log(elemCounters);
        for (int i = 0; i < cycles; i++) {
            final Set<String> keys = new HashSet<>(pairCounters.keySet());
            final Map<String, Long> pairCounters2 = new HashMap<>();
            for (final String k : keys) {
                final String elem = rules.get(k);
                final Long count = pairCounters.get(k);
                elemCounters.merge(elem.charAt(0), count, Long::sum);
                pairCounters2.merge(k.substring(0, 1) + elem, count, Long::sum);
                pairCounters2.merge(elem + k.substring(1), count, Long::sum);
            }
            pairCounters = pairCounters2;
            log(pairCounters);
            log(elemCounters);
        }
        final LongSummaryStatistics summary = elemCounters.values().stream()
                .collect(summarizingLong(Long::valueOf));
        final long ans = summary.getMax() - summary.getMin();
        log(ans);
        return ans;
    }
    
    @Override
    public Long solvePart1() {
        final long ans = solve(10);
        final long ansBis = solveBis(10);
        assert ans == ansBis;
        return ansBis;
    }
    
    @Override
    public Long solvePart2() {
        return solveBis(40);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_14.createDebug(TEST).solvePart1() == 1588;
        assert AoC2021_14.createDebug(TEST).solvePart2() == 2_188_189_693_529L;

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