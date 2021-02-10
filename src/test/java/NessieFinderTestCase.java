import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

public class NessieFinderTestCase {
	
	private List<String> grid;

	@Before
	public void setUp() {
		grid = new ArrayList<>();
		grid.add(".#.#...#.###...#.##.##..");
		grid.add("#.#.##.###.#.##.##.#####");
		grid.add("..##.###.####..#.####.##");
	}
	
	@Test
	public void findNessies() {
		final List<Pair<Integer, Integer>> result = AoC2020_20.NessieFinder.findNessies(grid);
		
		assertThat(result.size(), is(1));
		assertThat(result.get(0).getLeft(), is(1));
		assertThat(result.get(0).getRight(), is(2));
	}
	
	@Test
	public void markNessies() {
		AoC2020_20.NessieFinder.markNessies(asList(Pair.of(1, 2)), grid);
		
		final List<String> expected = new ArrayList<>();
		expected.add("_~_~___~_~~~___~_~~_\u2592~__");
		expected.add("~_\u2592_~~_\u2592\u2592~_~_\u2592\u2592_~~_\u2592\u2592\u2592~~");
		expected.add("__~\u2592_~\u2592~_\u2592~~\u2592__\u2592_~\u2592~~_~~");
		assertThat(grid, equalTo(expected));
	}
}
