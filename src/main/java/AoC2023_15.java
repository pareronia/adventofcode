import static com.github.pareronia.aoc.IterTools.enumerateFrom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2023_15
        extends SolutionBase<List<String>, Integer, Integer> {
    
    private AoC2023_15(final boolean debug) {
        super(debug);
    }
    
    public static AoC2023_15 create() {
        return new AoC2023_15(false);
    }
    
    public static AoC2023_15 createDebug() {
        return new AoC2023_15(true);
    }
    
    @Override
    protected List<String> parseInput(final List<String> inputs) {
        return Arrays.asList(inputs.get(0).split(","));
    }
    
    private int hash(final String s) {
        return Utils.asCharacterStream(s)
            .mapToInt(ch -> ch)
            .reduce(0, (acc, ch) -> ((acc + ch) * 17) % 256);
    }

    @Override
    public Integer solvePart1(final List<String> steps) {
        return steps.stream()
            .mapToInt(this::hash)
            .sum();
    }
    
    @Override
    public Integer solvePart2(final List<String> steps) {
        final Boxes boxes = new Boxes();
        steps.forEach(step -> {
            if (step.contains("=")) {
                final StringSplit split = StringOps.splitOnce(step, "=");
                final String label = split.left();
                final int focalLength = Integer.parseInt(split.right());
                boxes.addLens(label, focalLength);
            } else {
                final String label = step.substring(0, step.length() - 1);
                boxes.removeLens(label);
            }
        });
        return boxes.getTotalFocusingPower();
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "1320"),
        @Sample(method = "part2", input = TEST, expected = "145"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2023_15.create().run();
    }
    
    
    private final class Boxes {
        @SuppressWarnings("unchecked")
        private final List<Lens>[] boxes = new List[256];
        
        protected Boxes() {
            for (int i = 0; i < this.boxes.length; i++) {
                this.boxes[i] = new ArrayList<>();
            }
        }

        public void addLens(final String label, final int focalLength) {
            final List<Lens> lenses = this.boxes[AoC2023_15.this.hash(label)];
            lenses.stream()
                .filter(item -> item.label.equals(label))
                .findFirst()
                .ifPresentOrElse(
                    lens -> lenses.set(lenses.indexOf(lens),
                                       new Lens(label, focalLength)),
                    () -> lenses.add(new Lens(label, focalLength)));
        }
        
        public void removeLens(final String label) {
            final List<Lens> lenses = this.boxes[AoC2023_15.this.hash(label)];
            lenses.stream()
                .filter(item -> item.label.equals(label))
                .findFirst()
                .ifPresent(lenses::remove);
        }
        
        public int getTotalFocusingPower() {
            return enumerateFrom(1, Arrays.stream(boxes))
                .flatMapToInt(box -> enumerateFrom(1, box.value().stream())
                .mapToInt(e -> box.index() * e.index() * e.value().focalLength()))
                .sum();
        }
        
        record Lens(String label, int focalLength) { }
    }

    private static final String TEST =
            "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7";
}
