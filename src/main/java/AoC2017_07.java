import static com.github.pareronia.aoc.AssertUtils.unreachable;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
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
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2017_07
            extends SolutionBase<AoC2017_07.Tower, String, Integer> {

    private AoC2017_07(final boolean debug) {
        super(debug);
    }

    public static AoC2017_07 create() {
        return new AoC2017_07(false);
    }

    public static AoC2017_07 createDebug() {
        return new AoC2017_07(true);
    }
 
    @Override
    protected Tower parseInput(final List<String> inputs) {
        return Tower.fromInput(inputs);
    }

    @Override
    public String solvePart1(final Tower tower) {
        return tower.findRoot().getName();
    }
    
    @Override
    public Integer solvePart2(final Tower tower) {
        final Node unbalanced = tower.findUnbalancedNode();
        log("Unbalanced: " + unbalanced.getName());
        final int combinedWeight = unbalanced.getChildren().stream()
                .mapToInt(Node::getFullWeight)
                .sum();
        log("Weight: " + unbalanced.getWeight() + " + " + combinedWeight);
        final int desiredWeigth = tower.getSiblings(unbalanced).stream()
                .filter(n -> !n.equals(unbalanced))
                .findFirst()
                .map(Node::getFullWeight)
                .orElseThrow();
        log("Desired weight: " + desiredWeigth);
        return desiredWeigth - combinedWeight;
    }
    
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "tknk"),
        @Sample(method = "part2", input = TEST, expected = "60"),
    })
    public static void main(final String[] args) throws Exception {
        AoC2017_07.create().run();
    }
    
    public static final String TEST = """
            pbga (66)
            xhth (57)
            ebii (61)
            havc (66)
            ktlj (57)
            fwft (72) -> ktlj, cntj, xhth
            qoyq (66)
            padx (45) -> pbga, havc, qoyq
            tknk (41) -> ugml, padx, fwft
            jptl (61)
            ugml (68) -> gyxo, ebii, jptl
            gyxo (61)
            cntj (57)
            """;
    
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
        
        public static Node fromInput(final String line) {
            final String[] sp = line.split(" -> ");
            final String[] spsp = sp[0].split(" ");
            final String name = spsp[0];
            final int weight = Integer.parseInt(
                    StringUtils.strip(spsp[1], "()"));
            if (sp.length == 1) {
                return new Node(name, weight, Set.of());
            } else {
                final Set<String> children =
                    Arrays.stream(sp[1].split(", ")).collect(toSet());
                return new Node(name, weight, children);
            }
        }
        
        private int findFullWeight() {
            if (getChildren().isEmpty()) {
                return getWeight();
            }
            return getWeight()
                    + getChildren().stream()
                        .mapToInt(Node::findFullWeight)
                        .sum();
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
    
    record Tower(Map<String, Node> nodes) {
        
        public static Tower fromInput(final List<String> inputs) {
            final Map<String, Node> nodes = inputs.stream()
                .map(Node::fromInput)
                .collect(toMap(Node::getName, n -> n));
            nodes.values().forEach(n ->
                    n.setChildren(n.getChildKeys().stream()
                            .map(nodes::get)
                            .collect(toSet())));
            nodes.values().forEach(n -> {
                    nodes.values().stream()
                            .filter(p -> p.getChildren().contains(n))
                            .findFirst()
                            .ifPresent(n::setParent);
                    n.setFullWeight(n.findFullWeight());
            });
            return new Tower(nodes);
        }

        private Node findRoot() {
            return this.nodes.values().stream()
                    .filter(n -> !n.getParent().isPresent())
                    .findFirst().orElseThrow();
        }

        public List<Node> getSiblings(final Node node) {
            final Optional<Node> parent = node.getParent();
            if (parent.isEmpty()) {
                return Collections.emptyList();
            }
            return nodes.values().stream()
                    .filter(e -> e.getParent()
                            .map(p -> p.equals(parent.get())).orElse(false))
                    .toList();
        }
    
        public Node findUnbalancedNode() {
            final Deque<Node> queue = new ArrayDeque<>();
            queue.add(findRoot().getChildren().iterator().next());
            while (!queue.isEmpty()) {
                final Node node = queue.poll();
                final List<Node> siblings = getSiblings(node);
                final Map<Integer, Long> weights = siblings.stream()
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
            throw unreachable();
        }
    }
}
