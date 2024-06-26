import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.github.pareronia.aoc.geometry3d.Position3D;
import com.github.pareronia.aoc.geometry3d.Transformations3D;
import com.github.pareronia.aoc.geometry3d.Vector3D;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2021_19 extends AoCBase {
    
    private final List<ScannerData> scannerData;
    
    private AoC2021_19(final List<String> input, final boolean debug) {
        super(debug);
        final List<ScannerData> scannerData = new ArrayList<>();
        final List<List<String>> blocks = toBlocks(input);
        for (final List<String> block : blocks) {
            final String id = block.get(0)
                    .substring("--- scanner ".length()).split(" ")[0];
            final List<Position3D> points = new ArrayList<>();
            for (int i = 1; i < block.size(); i++) {
                final List<Integer> coordinates
                        = Arrays.stream(block.get(i).split(","))
                            .map(Integer::valueOf).collect(toList());
                points.add(new Position3D(
                    coordinates.get(0), coordinates.get(1), coordinates.get(2)));
            }
            scannerData.add(new ScannerData(Integer.parseInt(id), points));
        }
        this.scannerData = Collections.unmodifiableList(scannerData);
    }
    
    public static final AoC2021_19 create(final List<String> input) {
        return new AoC2021_19(input, false);
    }

    public static final AoC2021_19 createDebug(final List<String> input) {
        return new AoC2021_19(input, true);
    }
    
    public Map<ScannerData, Vector3D> solve() {
        final Map<ScannerData, Vector3D> lockedIn = new HashMap<>();
        final Deque<ScannerData> q = new ArrayDeque<>(List.copyOf(this.scannerData));
        final ScannerData sc0 = q.pop();
        lockedIn.put(sc0, Vector3D.of(0, 0, 0));
        assert sc0.id() == 0;
        while (!q.isEmpty()) {
            final ScannerData sc = q.pop();
            final Optional<OverlappingScanner> overlapping
                    = locateOverlappingScanner(sc, lockedIn);
            if (overlapping.isPresent()) {
                lockedIn.put(
                        overlapping.get().scannerData(),
                        overlapping.get().vector());
            } else {
                q.add(sc);
            }
        }
        log(lockedIn.keySet().stream().map(ScannerData::id).collect(toList()));
        return lockedIn;
    }
    
    private
    Optional<OverlappingScanner>
    locateOverlappingScanner(final ScannerData sc,
                             final Map<ScannerData, Vector3D> lockedIn)
    {
        for (final List<Position3D> positions : Transformations.of(sc.beacons)) {
            for (final ScannerData li : lockedIn.keySet()) {
                final Optional<Vector3D> overlap
                        = findOverlap(sc, positions, li);
                if (overlap.isPresent()) {
                    final Vector3D vector = overlap.get();
                    log(vector);
                    final ScannerData overlapping = new ScannerData(
                            sc.id(),
                            Transformations3D.translate(positions, vector));
                    return Optional.of(new OverlappingScanner(overlapping, vector));
                }
            }
        }
        return Optional.empty();
    }

    private
    Optional<Vector3D>
    findOverlap(final ScannerData sc,
                final List<Position3D> positions,
                final ScannerData other)
    {
        final List<Vector3D> overlaptx
            = other.beacons.stream()
                .flatMap(b -> positions.stream().map(p -> new Position3DPair(b, p)))
                .collect(groupingBy(t -> Vector3D.from(t.two(), t.one()),
                                    counting()))
                .entrySet().stream()
                    .filter(e -> e.getValue() >= 12L)
                    .map(Entry::getKey)
                    .collect(toList());
        if (overlaptx.size() > 0) {
            log(String.format(
                    "overlaptx between sc%d and sc%d", sc.id(), other.id()));
            assert overlaptx.size() == 1;
            return Optional.of(overlaptx.get(0));
        } else {
            return Optional.empty();
        }
    }
    
    @Override
    public Long solvePart1() {
        final Map<ScannerData, Vector3D> scanners = solve();
        return scanners.keySet().stream()
                .flatMap(sc -> sc.beacons.stream())
                .distinct()
                .count();
    }
 
    @Override
    public Integer solvePart2() {
        final Map<ScannerData, Vector3D> scanners = solve();
        return scanners.values().stream()
            .flatMap(v1 -> scanners.values().stream()
                            .map(v2 -> new Vector3DPair(v1, v2)))
            .filter(t -> !t.one().equals(t.two()))
            .map(t -> Position3D.of(0, 0, 0).translate(t.one())
                        .manhattanDistance(
                                Position3D.of(0, 0, 0).translate(t.two())))
            .mapToInt(Integer::intValue)
            .max().orElseThrow();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_19.create(TEST).solvePart1() == 79;
        assert AoC2021_19.create(TEST).solvePart2() == 3621;

        final Puzzle puzzle = Aocd.puzzle(2021, 19);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2021_19.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2021_19.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines(
        "--- scanner 0 ---\r\n" +
        "404,-588,-901\r\n" +
        "528,-643,409\r\n" +
        "-838,591,734\r\n" +
        "390,-675,-793\r\n" +
        "-537,-823,-458\r\n" +
        "-485,-357,347\r\n" +
        "-345,-311,381\r\n" +
        "-661,-816,-575\r\n" +
        "-876,649,763\r\n" +
        "-618,-824,-621\r\n" +
        "553,345,-567\r\n" +
        "474,580,667\r\n" +
        "-447,-329,318\r\n" +
        "-584,868,-557\r\n" +
        "544,-627,-890\r\n" +
        "564,392,-477\r\n" +
        "455,729,728\r\n" +
        "-892,524,684\r\n" +
        "-689,845,-530\r\n" +
        "423,-701,434\r\n" +
        "7,-33,-71\r\n" +
        "630,319,-379\r\n" +
        "443,580,662\r\n" +
        "-789,900,-551\r\n" +
        "459,-707,401\r\n" +
        "\r\n" +
        "--- scanner 1 ---\r\n" +
        "686,422,578\r\n" +
        "605,423,415\r\n" +
        "515,917,-361\r\n" +
        "-336,658,858\r\n" +
        "95,138,22\r\n" +
        "-476,619,847\r\n" +
        "-340,-569,-846\r\n" +
        "567,-361,727\r\n" +
        "-460,603,-452\r\n" +
        "669,-402,600\r\n" +
        "729,430,532\r\n" +
        "-500,-761,534\r\n" +
        "-322,571,750\r\n" +
        "-466,-666,-811\r\n" +
        "-429,-592,574\r\n" +
        "-355,545,-477\r\n" +
        "703,-491,-529\r\n" +
        "-328,-685,520\r\n" +
        "413,935,-424\r\n" +
        "-391,539,-444\r\n" +
        "586,-435,557\r\n" +
        "-364,-763,-893\r\n" +
        "807,-499,-711\r\n" +
        "755,-354,-619\r\n" +
        "553,889,-390\r\n" +
        "\r\n" +
        "--- scanner 2 ---\r\n" +
        "649,640,665\r\n" +
        "682,-795,504\r\n" +
        "-784,533,-524\r\n" +
        "-644,584,-595\r\n" +
        "-588,-843,648\r\n" +
        "-30,6,44\r\n" +
        "-674,560,763\r\n" +
        "500,723,-460\r\n" +
        "609,671,-379\r\n" +
        "-555,-800,653\r\n" +
        "-675,-892,-343\r\n" +
        "697,-426,-610\r\n" +
        "578,704,681\r\n" +
        "493,664,-388\r\n" +
        "-671,-858,530\r\n" +
        "-667,343,800\r\n" +
        "571,-461,-707\r\n" +
        "-138,-166,112\r\n" +
        "-889,563,-600\r\n" +
        "646,-828,498\r\n" +
        "640,759,510\r\n" +
        "-630,509,768\r\n" +
        "-681,-892,-333\r\n" +
        "673,-379,-804\r\n" +
        "-742,-814,-386\r\n" +
        "577,-820,562\r\n" +
        "\r\n" +
        "--- scanner 3 ---\r\n" +
        "-589,542,597\r\n" +
        "605,-692,669\r\n" +
        "-500,565,-823\r\n" +
        "-660,373,557\r\n" +
        "-458,-679,-417\r\n" +
        "-488,449,543\r\n" +
        "-626,468,-788\r\n" +
        "338,-750,-386\r\n" +
        "528,-832,-391\r\n" +
        "562,-778,733\r\n" +
        "-938,-730,414\r\n" +
        "543,643,-506\r\n" +
        "-524,371,-870\r\n" +
        "407,773,750\r\n" +
        "-104,29,83\r\n" +
        "378,-903,-323\r\n" +
        "-778,-728,485\r\n" +
        "426,699,580\r\n" +
        "-438,-605,-362\r\n" +
        "-469,-447,-387\r\n" +
        "509,732,623\r\n" +
        "647,635,-688\r\n" +
        "-868,-804,481\r\n" +
        "614,-800,639\r\n" +
        "595,780,-596\r\n" +
        "\r\n" +
        "--- scanner 4 ---\r\n" +
        "727,592,562\r\n" +
        "-293,-554,779\r\n" +
        "441,611,-461\r\n" +
        "-714,465,-776\r\n" +
        "-743,427,-804\r\n" +
        "-660,-479,-426\r\n" +
        "832,-632,460\r\n" +
        "927,-485,-438\r\n" +
        "408,393,-506\r\n" +
        "466,436,-512\r\n" +
        "110,16,151\r\n" +
        "-258,-428,682\r\n" +
        "-393,719,612\r\n" +
        "-211,-452,876\r\n" +
        "808,-476,-593\r\n" +
        "-575,615,604\r\n" +
        "-485,667,467\r\n" +
        "-680,325,-822\r\n" +
        "-627,-443,-432\r\n" +
        "872,-547,-609\r\n" +
        "833,512,582\r\n" +
        "807,604,487\r\n" +
        "839,-516,451\r\n" +
        "891,-625,532\r\n" +
        "-652,-548,-490\r\n" +
        "30,-46,-14"
    );
    
    static class Transformations implements Iterable<List<Position3D>> {
        private final List<Position3D> positions;

        private Transformations(final List<Position3D> positions) {
            this.positions = positions;
        }
        
        public static Transformations of(final List<Position3D> positions) {
            return new Transformations(positions);
        }

        @Override
        public Iterator<List<Position3D>> iterator() {
            return new Iterator<>() {
                int i = 0;
                
                @Override
                public boolean hasNext() {
                    return i < 24;
                }

                @Override
                public List<Position3D> next() {
                    return Transformations3D.apply(
                            Transformations.this.positions,
                            Transformations3D.ALL_TRANSFORMATIONS.get(i++));
                }
            };
        }
    }
    
    record ScannerData(int id, List<Position3D> beacons) {}
    
    record OverlappingScanner(ScannerData scannerData, Vector3D vector) {}
    
    record Position3DPair(Position3D one, Position3D two) {}
    
    record Vector3DPair(Vector3D one, Vector3D two) {}
}