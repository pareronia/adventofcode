import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aocd.Puzzle;

public final class AoC2017_07 extends AoCBase {

    private final transient Map<String, Node> input;

    private AoC2017_07(final List<String> inputs, final boolean debug) {
        super(debug);
        this.input = parse(inputs);
        buildTree();
        log(this.input);
    }

    private Map<String, Node> parse(final List<String> inputs) {
        return inputs.stream()
                .map(s -> {
                    final String[] sp = s.split(" -> ");
                    final String[] spsp = sp[0].split(" ");
                    final String name = spsp[0];
                    final Integer weight = Integer.valueOf(
                            StringUtils.strip(spsp[1], "()"));
                    if (sp.length == 1) {
                        return new Node(name, weight, Set.of());
                    } else {
                        final Set<String> children =
                            Arrays.stream(sp[1].split(", ")).collect(toSet());
                        return new Node(name, weight, children);
                    }
                })
                .collect(toMap(AoC2017_07.Node::getName, n -> n));
    }

    private void buildTree() {
        this.input.values().forEach(n ->
                n.setChildren(n.getChildKeys().stream()
                        .map(this.input::get)
                        .collect(toSet())));
        this.input.values().forEach(n -> {
                this.input.values().stream()
                        .filter(p -> p.getChildren().contains(n))
                        .findFirst()
                        .ifPresent(n::setParent);
                n.setFullWeight(findFullWeight(n));
        });
    }
    
    private Integer findFullWeight(final Node node) {
        if (node.getChildren().isEmpty()) {
            return node.getWeight();
        }
        return node.getWeight()
                + node.getChildren().stream()
                    .map(this::findFullWeight)
                    .collect(summingInt(Integer::valueOf));
    }

    public static AoC2017_07 create(final List<String> input) {
        return new AoC2017_07(input, false);
    }

    public static AoC2017_07 createDebug(final List<String> input) {
        return new AoC2017_07(input, true);
    }
    
    private Node findRoot() {
        return this.input.values().stream()
                .filter(n -> !n.getParent().isPresent())
                .findFirst().orElseThrow();
    }
    
    private List<Node> getSiblings(final Node node) {
        final Optional<Node> parent = node.getParent();
        if (parent.isEmpty()) {
            return Collections.emptyList();
        }
        return this.input.values().stream()
                .filter(e -> e.getParent()
                        .map(p -> p.equals(parent.get())).orElse(false))
                .collect(toList());
    }
    
    private Node findUnbalancedNode() {
        final Deque<Node> queue = new ArrayDeque<>();
        queue.add(findRoot().getChildren().iterator().next());
        while (!queue.isEmpty()) {
            final Node node = queue.poll();
            final List<Node> siblings = getSiblings(node);
            final HashMap<Integer, Long> weights = siblings.stream()
                    .collect(groupingBy(Node::getFullWeight, HashMap::new, counting()));
            if (weights.size() == 1) {
                return node.getParent().get();
            }
            final int unbalancedWeight = weights.entrySet().stream()
                    .min(Comparator.comparing(Entry::getValue))
                    .map(Entry::getKey).orElseThrow();
            final Node unbalanced = siblings.stream()
                    .filter(c -> c.getFullWeight() == unbalancedWeight)
                    .findFirst().orElseThrow();
            unbalanced.getChildren().forEach(queue::add);
        }
        throw new IllegalStateException("Unsolvable");
    }

    @Override
    public String solvePart1() {
        return findRoot().getName();
    }
    
    @Override
    public Integer solvePart2() {
        final Node unbalanced = findUnbalancedNode();
        log("Unbalanced: " + unbalanced.getName());
        final Integer combinedWeight = unbalanced.getChildren().stream()
                .map(Node::getFullWeight)
                .collect(summingInt(Integer::valueOf));
        log("Weight: " + unbalanced.getWeight() + " + " + combinedWeight);
        final Integer desiredWeigth = getSiblings(unbalanced).stream()
                .filter(n -> !n.equals(unbalanced))
                .findFirst()
                .map(Node::getFullWeight)
                .orElseThrow();
        log("Desired weight: " + desiredWeigth);
        return desiredWeigth - combinedWeight;
    }
    
    public static void main(final String[] args) throws Exception {
        assert AoC2017_07.createDebug(TEST).solvePart1().equals("tknk");
        assert AoC2017_07.createDebug(TEST).solvePart2() == 60;

		final Puzzle puzzle = Puzzle.create(2017, 7);
		final List<String> input = puzzle.getInputData();
		puzzle.check(
		    () -> lap("Part 1", AoC2017_07.create(input)::solvePart1),
		    () -> lap("Part 2", AoC2017_07.create(input)::solvePart2)
		);
    }
    
    public static final List<String> TEST = splitLines(
            "pbga (66)\n" +
            "xhth (57)\n" +
            "ebii (61)\n" +
            "havc (66)\n" +
            "ktlj (57)\n" +
            "fwft (72) -> ktlj, cntj, xhth\n" +
            "qoyq (66)\n" +
            "padx (45) -> pbga, havc, qoyq\n" +
            "tknk (41) -> ugml, padx, fwft\n" +
            "jptl (61)\n" +
            "ugml (68) -> gyxo, ebii, jptl\n" +
            "gyxo (61)\n" +
            "cntj (57)"
    );
    
    private static final class Node {
        private final String name;
        private final int weight;
        private final Set<String> childKeys;
        private Node parent;
        private int fullWeight;
        private Set<Node> children;
        
        protected Node(final String name, final int weight, final Set<String> childKeys) {
            this.name = name;
            this.weight = weight;
            this.childKeys = childKeys;
        }

        public int getFullWeight() {
            return fullWeight;
        }

        public void setFullWeight(final int fullWeight) {
            this.fullWeight = fullWeight;
        }

        public String getName() {
            return name;
        }

        public int getWeight() {
            return weight;
        }

        public Set<String> getChildKeys() {
            return childKeys;
        }

        public Set<Node> getChildren() {
            return children;
        }

        public void setChildren(final Set<Node> children) {
            this.children = children;
        }

        public void setParent(final Node parent) {
            this.parent = parent;
        }

        public Optional<Node> getParent() {
            return Optional.ofNullable(this.parent);
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("Node [name=").append(name)
                .append(", weight=").append(weight)
                .append(", childKeys=").append(childKeys)
                .append(", fullWeight=").append(fullWeight)
                .append(", parent=").append(getParent())
                .append("]");
            return builder.toString();
        }
    }
}
