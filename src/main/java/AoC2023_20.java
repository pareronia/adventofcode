import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public final class AoC2023_20
        extends SolutionBase<Map<String, AoC2023_20.Module>, Integer, Long> {
    
    private AoC2023_20(final boolean debug) {
        super(debug);
    }
    
    public static AoC2023_20 create() {
        return new AoC2023_20(false);
    }
    
    public static AoC2023_20 createDebug() {
        return new AoC2023_20(true);
    }
    
    @Override
    protected Map<String, Module> parseInput(final List<String> inputs) {
        final Map<String, Module> modules = new HashMap<>();
        inputs.stream().forEach(line -> {
                final StringSplit split = StringOps.splitOnce(line, " -> ");
                final List<String> outputs = Arrays.asList(split.right().split(", "));
                if (split.left().equals("broadcaster")) {
                    modules.put("broadcaster", new Module("broadcaster", outputs));
                } else {
                    final char type = split.left().charAt(0);
                    modules.put(
                        split.left().substring(1),
                        new Module(String.valueOf(type), outputs));
                }
            });
        for (final String m : modules.keySet()) {
            final Module module = modules.get(m);
            for (final String output : module.getOutputs()) {
                final Module m1 = modules.get(output);
                if (m1 != null && m1.getType().equals("&")) {
                    m1.getState().put(m, 0);
                }
            }
            if (module.getType().equals("%")) {
                module.getState().put("self", 0);
            }
        }
        return modules;
    }
    
    record Pulse(String src, String dest, int value) {}
    
    @Override
    public Integer solvePart1(final Map<String, Module> modules) {
        log(modules);
        int hi = 0;
        int lo = 0;
        for (int i = 0; i < 1000; i++) {
            final Deque<Pulse> q = new ArrayDeque<>();
            modules.get("broadcaster").getOutputs().forEach(o -> {
                q.add(new Pulse("broadcaster", o, 0));
            });
            lo++;
            while (!q.isEmpty()) {
                final Pulse pulse = q.pop();
                if (pulse.value == 0) {
                    lo++;
                } else {
                    hi++;
                }
                if (!modules.containsKey(pulse.dest)) {
                    continue;
                }
                final Module target = modules.get(pulse.dest);
                if (target.getType().equals("%")) {
                    if (pulse.value == 0) {
                        final int state = target.getState().get("self");
                        final int newState = state == 1 ? 0 : 1;
                        target.getState().put("self", newState);
                        for (final String output : target.getOutputs()) {
                            q.add(new Pulse(pulse.dest, output, newState));
                        }
                    }
                } else {
                    target.getState().put(pulse.src, pulse.value);
                    final boolean allOn = target.getState().values().stream().allMatch(v -> v == 1);
                    for (final String output : target.getOutputs()) {
                        q.add(new Pulse(pulse.dest, output, allOn ? 0 : 1));
                    }
                }
            }
        }
        return hi * lo;
    }
    
    @Override
    public Long solvePart2(final Map<String, Module> modules) {
        final Map<String, List<Integer>> memo = new HashMap<>();
        for (int i = 1; i <= 10000; i++) {
            final Deque<Pulse> q = new ArrayDeque<>();
            modules.get("broadcaster").getOutputs().forEach(o -> {
                q.add(new Pulse("broadcaster", o, 0));
            });
            while (!q.isEmpty()) {
                final Pulse pulse = q.pop();
                if (!modules.containsKey(pulse.dest)) {
                    continue;
                }
                
                if (pulse.dest.equals("ql") && pulse.value == 1) {
                    memo.computeIfAbsent(pulse.src, k -> new ArrayList<>()).add(i);
                    if (memo.values().stream().allMatch(v -> v.size() > 1)) {
                        log(memo);
                        return memo.values().stream()
                             .map(v -> v.get(1) - v.get(0))
                             .map(BigInteger::valueOf)
                             .reduce((a, b) -> a.multiply(b).divide(a.gcd(b)))
                             .get().longValue();
                    }
                }
                
                final Module target = modules.get(pulse.dest);
                if (target.getType().equals("%")) {
                    if (pulse.value == 0) {
                        final int state = target.getState().get("self");
                        final int newState = state == 1 ? 0 : 1;
                        target.getState().put("self", newState);
                        for (final String output : target.getOutputs()) {
                            q.add(new Pulse(pulse.dest, output, newState));
                        }
                    }
                } else {
                    target.getState().put(pulse.src, pulse.value);
                    final boolean allOn = target.getState().values().stream().allMatch(v -> v == 1);
                    for (final String output : target.getOutputs()) {
                        q.add(new Pulse(pulse.dest, output, allOn ? 0 : 1));
                    }
                }
            }
        }
        throw new IllegalStateException("Unsolvable");
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "32000000"),
        @Sample(method = "part1", input = TEST2, expected = "11687500"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2023_20.createDebug().run();
    }
    
    @RequiredArgsConstructor
    @Getter
    @ToString
    static final class Module {
        private final String type;
        private final List<String> outputs;
        private final Map<String, Integer> state = new HashMap<>();
    }

    private static final String TEST1 = """
            broadcaster -> a, b, c
            %a -> b
            %b -> c
            %c -> inv
            &inv -> a
            """;
    private static final String TEST2 = """
            broadcaster -> a
            %a -> inv, con
            &inv -> b
            %b -> con
            &con -> output
            """;
}
