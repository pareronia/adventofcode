import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.pareronia.aoc.Grid;

public class NessieFinderTestCase {
	
	private Grid grid;

	@BeforeEach
	public void setUp() {
		final List<String> lines = new ArrayList<>();
		lines.add(".#.#...#.###...#.##.##..");
		lines.add("#.#.##.###.#.##.##.#####");
		lines.add("..##.###.####..#.####.##");
		grid = Grid.from(lines);
	}
	
	@Test
	public void findNessies() {
		final List<Pair<Integer, Integer>> result = AoC2020_20.NessieFinder.findNessies(grid);
		
		assertThat(result).hasSize(1);
		assertThat(result.get(0).getLeft()).isEqualTo(1);
		assertThat(result.get(0).getRight()).isEqualTo(2);
	}
	
	@Test
	public void markNessies() {
		final Grid result = AoC2020_20.NessieFinder.markNessies(asList(Pair.of(1, 2)), grid);
		
		final List<String> expected = new ArrayList<>();
		expected.add("_~_~___~_~~~___~_~~_\u2592~__");
		expected.add("~_\u2592_~~_\u2592\u2592~_~_\u2592\u2592_~~_\u2592\u2592\u2592~~");
		expected.add("__~\u2592_~\u2592~_\u2592~~\u2592__\u2592_~\u2592~~_~~");
		assertThat(result.getRowsAsStringList()).isEqualTo(expected);
	}
}
