import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.github.pareronia.aoc.MutableInt;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2023_20
        extends SolutionBase<AoC2023_20.Modules, Integer, Long> {
    
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
    protected Modules parseInput(final List<String> inputs) {
        return Modules.fromInput(inputs);
    }
    
    @Override
    public Integer solvePart1(final Modules modules) {
        final MutableInt hi = new MutableInt(0);
        final MutableInt lo = new MutableInt(0);
        final PulseListener listener = pulse -> {
            if (pulse.isLow()) {
                lo.increment();
            } else {
                hi.increment();
            }
        };
        for (int i = 0; i < 1000; i++) {
            modules.pushButton(listener);
        }
        return hi.intValue() * lo.intValue();
    }
    
    @Override
    public Long solvePart2(final Modules modules) {
        final Map<String, List<Integer>> memo = new HashMap<>();
        final String to_rx = modules.getModuleWithOutput_rx();
        final MutableInt pushes = new MutableInt();
        final PulseListener listener = pulse -> {
            if (pulse.dest.equals(to_rx) && pulse.isHigh()) {
                memo.computeIfAbsent(pulse.src, k -> new ArrayList<>())
                    .add(pushes.intValue());
            }
        };
        for (int i = 1; i <= 10000; i++) {
            pushes.increment();
            modules.pushButton(listener);
            if (!memo.isEmpty()
                    && memo.values().stream().allMatch(v -> v.size() > 1)) {
                return memo.values().stream()
                     .map(v -> v.get(1) - v.get(0))
                     .map(BigInteger::valueOf)
                     .reduce((a, b) -> a.multiply(b).divide(a.gcd(b)))
                     .get().longValue();
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

    record Pulse(String src, String dest, Pulse.Type value) {
        public enum Type {
            HIGH, LOW;
            
            public Type flipped() {
                return this == HIGH ? LOW : HIGH;
            }
        }
        
        public static Pulse high(final String src, final String dest) {
            return new Pulse(src, dest, Type.HIGH);
        }
    
        public static Pulse low(final String src, final String dest) {
            return new Pulse(src, dest, Type.LOW);
        }
        
        public boolean isHigh() {
            return value == Type.HIGH;
        }
        
        public boolean isLow() {
            return value == Type.LOW;
        }
    }
    
    private interface PulseListener {
        void onPulse(Pulse pulse);
    }

    private static final class Module {
        private static final String BROADCASTER = "broadcaster";
        private static final String BUTTON = "button";
        private static final String SELF = "self";
        
        private final String type;
        private final List<String> outputs;
        private final Map<String, Pulse.Type> state = new HashMap<>();
        
        public Module(final String type, final List<String> outputs) {
            this.type = type;
            this.outputs = outputs;
        }

        public List<String> getOutputs() {
            return outputs;
        }

        public Map<String, Pulse.Type> getState() {
            return state;
        }
        
        private boolean isConjunction() {
            return this.type.equals("&");
        }
        
        public boolean isFlipFlop() {
            return this.type.equals("%");
        }
        
        private boolean isBroadcaster() {
            return this.type.equals(BROADCASTER);
        }
        
        public List<Pulse> process(final Pulse pulse) {
            if (this.isBroadcaster()) {
                return this.getOutputs().stream()
                    .map(o -> new Pulse(BROADCASTER, o, pulse.value))
                    .toList();
            } else if (this.isFlipFlop()) {
                if (pulse.isLow()) {
                    final Pulse.Type newState
                            = this.getState().get(Module.SELF).flipped();
                    this.getState().put(Module.SELF, newState);
                    return this.getOutputs().stream()
                        .map(o -> newState == Pulse.Type.LOW
                                    ? Pulse.low(pulse.dest, o)
                                    : Pulse.high(pulse.dest, o))
                        .toList();
                } else {
                    return List.of();
                }
            } else {
                this.getState().put(pulse.src, pulse.value());
                final boolean allHigh = this.getState().values().stream()
                                        .allMatch(v -> v == Pulse.Type.HIGH);
                return this.getOutputs().stream()
                    .map(o -> allHigh
                                ? Pulse.low(pulse.dest, o)
                                : Pulse.high(pulse.dest, o))
                    .toList();
            }
        }
    }
    
    static final class Modules {
        private final Map<String, Module> modules;

        private Modules(final Map<String, AoC2023_20.Module> modules) {
            this.modules = modules;
        }
        
        public static Modules fromInput(final List<String> inputs) {
            final Map<String, Module> modules = new HashMap<>();
            inputs.stream().forEach(line -> {
                    final StringSplit split = StringOps.splitOnce(line, " -> ");
                    final List<String> outputs = Arrays.asList(split.right().split(", "));
                    if (split.left().equals(Module.BROADCASTER)) {
                        modules.put(
                            Module.BROADCASTER,
                            new Module(Module.BROADCASTER, outputs));
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
                    if (m1 != null && m1.isConjunction()) {
                        m1.getState().put(m, Pulse.Type.LOW);
                    }
                }
                if (module.isFlipFlop()) {
                    module.getState().put(Module.SELF, Pulse.Type.LOW);
                }
            }
            return new Modules(modules);
        }
        
        public String getModuleWithOutput_rx() {
            return modules.entrySet().stream()
                .filter(e -> e.getValue().getOutputs().stream()
                                .anyMatch(o -> o.equals("rx")))
                .map(Entry::getKey)
                .findFirst().orElseThrow();
        }
        
        public void pushButton(final PulseListener listener) {
            final Deque<Pulse> q = new ArrayDeque<>();
            q.add(Pulse.low(Module.BUTTON, Module.BROADCASTER));
            while (!q.isEmpty()) {
                final Pulse pulse = q.pop();
                listener.onPulse(pulse);
                Optional.ofNullable(this.modules.get(pulse.dest))
                    .ifPresent(target ->
                        target.process(pulse).forEach(q::add));
            }
        }
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
