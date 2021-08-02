import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import com.github.pareronia.aocd.Aocd;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2015_09 extends AoCBase {
    
    private final transient Map<String, List<Distance>> inputs;

    private AoC2015_09(final List<String> input, final boolean debug) {
        super(debug);
        this.inputs = input.stream()
                .flatMap(s -> {
                    final String[] ss1 = s.split(" = ");
                    final String[] ss2 = ss1[0].split(" to ");
                    return Stream.of(
                            new Distance(ss2[0], ss2[1], Integer.valueOf(ss1[1])),
                            new Distance(ss2[1], ss2[0], Integer.valueOf(ss1[1]))
                    );
                })
                .collect(groupingBy(Distance::getFrom, toList()));
        log(this.inputs);
    }

    public static final AoC2015_09 create(final List<String> input) {
        return new AoC2015_09(input, false);
    }

    public static final AoC2015_09 createDebug(final List<String> input) {
        return new AoC2015_09(input, true);
    }
    
    private Stream<Route> allRoutesStartingFrom(final String start) {
        final Builder<Route> builder = Stream.builder();
        final Deque<Route> queue = new ArrayDeque<>();
        queue.add(new Route(List.of(start), 0));
        while (!queue.isEmpty()) {
            final Route route = queue.poll();
            builder.add(route);
            final String last = route.getLastLocation();
            for (final Distance d : this.inputs.getOrDefault(last, emptyList())) {
                if (!route.visited(d.getTo())) {
                    queue.add(route.add(d.getTo(), d.getAmount()));
                }
            }
        }
        return builder.build();
    }
    
    private Stream<Route> getAllCompleteRoutes() {
        final Set<String> allLocations = this.inputs.keySet();
        return allLocations.stream()
                .flatMap(this::allRoutesStartingFrom)
                .filter(route -> route.getLength() == allLocations.size());
    }

    @Override
    public Integer solvePart1() {
        return getAllCompleteRoutes()
                .mapToInt(Route::getTotalDistance)
                .min().getAsInt();
    }

    @Override
    public Integer solvePart2() {
        return getAllCompleteRoutes()
                .mapToInt(Route::getTotalDistance)
                .max().getAsInt();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2015_09.createDebug(TEST).solvePart1() == 605;
        assert AoC2015_09.createDebug(TEST).solvePart2() == 982;

        final List<String> input = Aocd.getData(2015, 9);

        lap("Part 1", () -> AoC2015_09.create(input).solvePart1());
        lap("Part 2", () -> AoC2015_09.create(input).solvePart2());
    }

    private static final List<String> TEST = splitLines(
            "London to Dublin = 464\r\n" +
            "London to Belfast = 518\r\n" +
            "Dublin to Belfast = 141"
    );
    
    @RequiredArgsConstructor
    @ToString
    @Getter
    private static final class Distance {
        private final String from;
        private final String to;
        private final int amount;
    }
    
    @RequiredArgsConstructor
    @ToString
    private static final class Route {
        private final List<String> locations;
        @Getter
        private final Integer totalDistance;
        
        public Route add(final String location, final int distance) {
            final List<String> newLocations = new ArrayList<String>(this.locations);
            newLocations.add(location);
            final int newTotal = this.totalDistance + distance;
            return new Route(newLocations, newTotal);
        }
        
        public int getLength() {
            return this.locations.size();
        }
        
        public String getLastLocation() {
            return this.locations.get(this.locations.size() - 1);
        }
        
        public boolean visited(final String location) {
            return this.locations.contains(location);
        }
    }
}
